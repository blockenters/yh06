package com.block.memo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.block.memo.MainActivity;
import com.block.memo.R;
import com.block.memo.UpdateActivity;
import com.block.memo.api.MemoApi;
import com.block.memo.api.NetworkClient;
import com.block.memo.config.Config;
import com.block.memo.model.Memo;
import com.block.memo.model.Res;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    Context context;
    ArrayList<Memo> memoArrayList;

    SimpleDateFormat sf;
    SimpleDateFormat df;

    public MemoAdapter(Context context, ArrayList<Memo> memoArrayList) {
        this.context = context;
        this.memoArrayList = memoArrayList;

        // UTC 를 로컬타임으로 변환
        sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_row, parent, false);
        return new MemoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Memo memo = memoArrayList.get(position);
        holder.txtTitle.setText( memo.getTitle() );

        try {
            Date date = sf.parse( memo.getDate() );
            String localDate = df.format(date);
            holder.txtDate.setText( localDate   );

        } catch (ParseException e) {
            // 로그 남기고 리턴.
            return;
        }

        holder.txtContent.setText(memo.getContent() );
    }

    @Override
    public int getItemCount() {
        return memoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtDate;
        TextView txtContent;
        ImageView imgDelete;
        CardView cardView;

        // 몇번째 눌렀는지의 인덱스 정보
        int index;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtContent = itemView.findViewById(R.id.txtContent);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Memo memo = memoArrayList.get(index);
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("memo", memo);
                    context.startActivity(intent);
                }
            });

            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertDialog();
                }
            });

        }

        private void showAlertDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setTitle("삭제");
            builder.setMessage("정말 삭제하시겠습니까?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Retrofit retrofit = NetworkClient.getRetrofitClient(context);
                    MemoApi api = retrofit.create(MemoApi.class);

                    index = getAdapterPosition();
                    Memo memo = memoArrayList.get(index);

                    SharedPreferences sp = context.getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE);
                    String token = sp.getString("token", "");

                    Call<Res> call = api.deleteMemo(memo.getId(),  "Bearer " + token );
                    call.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {

                            if(response.isSuccessful()){

                                // ((MainActivity)context).getNetworkData();
                                memoArrayList.remove(index);
                                notifyDataSetChanged();

                            }else{

                            }

                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable throwable) {

                        }
                    });

                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }


    }
}
