package com.block.actionbar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.block.actionbar.R;
import com.block.actionbar.model.Posting;

import java.util.ArrayList;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder> {

    Context context;
    ArrayList<Posting> postingArrayList;

    public PostingAdapter(Context context, ArrayList<Posting> postingArrayList) {
        this.context = context;
        this.postingArrayList = postingArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posting_row, parent, false);
        return new PostingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Posting posting = postingArrayList.get(position);

        holder.txtTitle.setText(   posting.getTitle()  );
        holder.txtBody.setText( posting.getBody() );

    }

    @Override
    public int getItemCount() {
        return postingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtBody;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}






