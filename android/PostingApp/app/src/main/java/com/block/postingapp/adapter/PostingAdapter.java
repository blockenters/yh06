package com.block.postingapp.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.block.postingapp.R;
import com.block.postingapp.model.Posting;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class PostingAdapter extends RecyclerView.Adapter<PostingAdapter.ViewHolder> {

    Context context;
    ArrayList<Posting> postingArrayList;

    SimpleDateFormat sf;
    SimpleDateFormat df;

    public PostingAdapter(Context context, ArrayList<Posting> postingArrayList) {
        this.context = context;
        this.postingArrayList = postingArrayList;

        //2024-05-30T07:23:52
        sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());

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

        Glide.with(context).load( posting.imageUrl ).into( holder.imgPhoto );

        holder.txtContent.setText( posting.content );
        holder.txtEmail.setText( posting.email );
        holder.txtCreatedAt.setText( posting.createdAt );

        if( posting.isLike == 0 ){
            holder.imgLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
        }else{
            holder.imgLike.setImageResource(R.drawable.baseline_thumb_up_alt_24);
        }

        try {
            Date date = sf.parse( posting.createdAt );
            String localTime = df.format(date);
            holder.txtCreatedAt.setText(localTime);
        } catch (ParseException e) {
            // 로그를 남겨서 디버깅한다.
        }

    }

    @Override
    public int getItemCount() {
        return postingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPhoto;
        TextView txtContent;
        TextView txtEmail;
        TextView txtCreatedAt;
        ImageView imgLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            imgLike = itemView.findViewById(R.id.imgLike);
        }
    }
}
