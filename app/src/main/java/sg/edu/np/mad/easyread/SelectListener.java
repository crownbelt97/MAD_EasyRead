package sg.edu.np.mad.easyread;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public interface SelectListener {
    void onViewCreated(@NonNull View view, LayoutInflater inflater, ViewGroup container,
                       Bundle savedInstanceState);

    void onItemClicked(int pos);
}
