package com.block.postingtabbar.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.block.postingtabbar.PhotoActivity;
import com.block.postingtabbar.R;
import com.block.postingtabbar.api.NetworkClient;
import com.block.postingtabbar.api.PostingApi;
import com.block.postingtabbar.config.Config;
import com.block.postingtabbar.model.Posting;
import com.block.postingtabbar.model.Res;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        return new ViewHolder(view);
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

            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Posting posting = postingArrayList.get(index);
                    if( posting.isLike == 0 ){
                        setPostingLike( posting );
                    }else {
                        deletePostingLike(posting);
                    }
                }
            });

            imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Posting posting = postingArrayList.get(index);

                    Intent intent = new Intent(context, PhotoActivity.class);
                    intent.putExtra("imageUrl", posting.imageUrl);
                    context.startActivity(intent);

                }
            });
        }

        private void deletePostingLike(Posting posting) {

            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
            PostingApi api = retrofit.create(PostingApi.class);

            SharedPreferences sp = context.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE);
            String token = sp.getString("token", "");

            Call<Res> call = api.deletePostingLike("Bearer "+token, posting.id );
            call.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if(response.isSuccessful()){
                        posting.isLike = 0;
                        notifyDataSetChanged();
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Res> call, Throwable throwable) {

                }
            });


        }

        private void setPostingLike(Posting posting) {

            Retrofit retrofit = NetworkClient.getRetrofitClient(context);
            PostingApi api = retrofit.create(PostingApi.class);

            SharedPreferences sp = context.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE);
            String token = sp.getString("token", "");

            Call<Res> call = api.setPostingLike( "Bearer " + token,  posting.id );

            call.enqueue(new Callback<Res>() {
                @Override
                public void onResponse(Call<Res> call, Response<Res> response) {
                    if(response.isSuccessful()){

                        // 메모리 먼저 is_like 데이터를 1로 변경
                        posting.isLike = 1;
                        notifyDataSetChanged();

                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Res> call, Throwable throwable) {

                }
            });

        }
    }
}
