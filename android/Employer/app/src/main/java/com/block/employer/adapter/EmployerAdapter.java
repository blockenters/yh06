package com.block.employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.block.employer.R;
import com.block.employer.model.Employer;

import java.util.ArrayList;

import java.text.DecimalFormat;

// 3. 메소드 오버라이딩 할수 있도록 한다.
// 2. 상속받는다.
public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.ViewHolder> {

    // 4. 멤버변수 만든다.
    Context context;
    ArrayList<Employer> employerArrayList;

    // 5. 생성자 만든다.
    public EmployerAdapter(Context context, ArrayList<Employer> employerArrayList) {
        this.context = context;
        this.employerArrayList = employerArrayList;
    }

    // 6. 아래 3개 메소드를 모두 구현한다.
    @NonNull
    @Override
    public EmployerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employer_row, parent, false);
        return new EmployerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Employer employer = employerArrayList.get(position);

        holder.txtName.setText( employer.name );
        holder.txtAge.setText( "나이 : "+employer.age +"세" );

        DecimalFormat formatter = new DecimalFormat("#,###");
        String strSalary = formatter.format(employer.salary);

        holder.txtSalary.setText( "연봉 : $"+strSalary );

    }

    @Override
    public int getItemCount() {
        return employerArrayList.size();
    }

    // 1. 뷰홀더 클래스 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtAge;
        TextView txtSalary;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtSalary = itemView.findViewById(R.id.txtSalary);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

}




