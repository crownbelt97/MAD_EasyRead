package sg.edu.np.mad.easyread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sg.edu.np.mad.easyread.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final SelectListener selectListener;
    Context context;
    ArrayList<News> newsArrayList;

    public MyAdapter(Context context, ArrayList<News> newsArrayList, SelectListener selectListener) {
        this.context = context;
        this.newsArrayList = newsArrayList;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v , selectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        News news = newsArrayList.get(position);
        holder.tvHeading.setText(news.heading);
        Picasso.get().load(news.image).into(holder.titleImage);
        holder.tvAuthor.setText(news.author);
        holder.tvRank.setText(news.rank);
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView titleImage;
        TextView tvHeading;

        TextView tvAuthor;

        TextView tvRank;

        public MyViewHolder(@NonNull View itemView , SelectListener  selectListener) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.title_image);
            tvHeading = itemView.findViewById(R.id.tvHeading);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvRank = itemView.findViewById(R.id.tvRank);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectListener != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            selectListener.onItemClicked(pos);
                        }
                    }
                }
            });
        }
    }
}
