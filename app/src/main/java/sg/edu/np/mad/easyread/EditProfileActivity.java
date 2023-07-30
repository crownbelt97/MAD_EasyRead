package sg.edu.np.mad.easyread;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    FirebaseUser currentUser;

    EditText updateUsername;

    EditText updateEmail;

    EditText joinDate;

    Button updateBtn;

    TextView passReset;

    CircleImageView uploadImage;
    Uri imagePath;
    StorageReference storageProfilePicsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        updateUsername = findViewById(R.id.updateUsernameEditText);
        updateEmail = findViewById(R.id.updateEmailEditText);
        joinDate = findViewById(R.id.joinDate);
        updateBtn = findViewById(R.id.updateBtn);
        passReset = findViewById(R.id.passReset);
        uploadImage = findViewById(R.id.uploadImage);


        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        currentUser = mAuth.getCurrentUser();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference();
        if (currentUser != null) {
            String targetEmail = currentUser.getEmail();

            reference.orderByChild("email").equalTo(targetEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String usernameDB = String.valueOf(userSnapshot.child("username").getValue(String.class));
                            String emailDB = String.valueOf(userSnapshot.child("email").getValue(String.class));
                            String creationDateDB = String.valueOf(userSnapshot.child("creationDate").getValue(String.class));
                            updateUsername.setText(usernameDB);
                            updateEmail.setText(emailDB);
                            joinDate.setText(creationDateDB);

                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error if necessary
                }
            });
        }


        passReset.setOnClickListener(v -> {
            String email = updateEmail.getText().toString();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Password reset email sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                }
            });
        });


        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 1);
            }
        });

        updateBtn.setOnClickListener(view -> {
            String newUsername = updateUsername.getText().toString().trim();
            uploadImage();
            if (!TextUtils.isEmpty(newUsername)) {
                DatabaseReference userRef = reference.child(currentUser.getUid());
                userRef.child("username").setValue(newUsername)
                        .addOnSuccessListener(aVoid -> {
                            // Username update successful
                            Toast.makeText(EditProfileActivity.this, "Username updated successfully!", Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(e -> {
                            // Failed to update the username
                            Toast.makeText(EditProfileActivity.this, "Failed to update username.", Toast.LENGTH_SHORT).show();
                        });
                Intent updateIntent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(updateIntent);

            } else {
                Toast.makeText(EditProfileActivity.this, "Please enter a new username.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imagePath = data.getData();
            getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        uploadImage.setImageBitmap(bitmap);
    }

    private void uploadImage() {
        if (imagePath == null) {
            // Handle the case where the user didn't choose an image to upload
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading.....");
        progressDialog.show();

        FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString()).putFile(imagePath)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Get the download URL of the uploaded image
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUri) {
                                    progressDialog.dismiss();
                                    String imageUrl = downloadUri.toString();
                                    // Now you can save the download URL to your Firebase Realtime Database
                                    DatabaseReference userRef = reference.child(currentUser.getUid());
                                    userRef.child("profileImageURL").setValue(imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(EditProfileActivity.this, "Profile image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EditProfileActivity.this, "Failed to upload profile image.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}