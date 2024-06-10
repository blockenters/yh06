package com.block.simplecontacts.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.block.simplecontacts.MainActivity;
import com.block.simplecontacts.R;
import com.block.simplecontacts.UpdateActivity;
import com.block.simplecontacts.model.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;

// 2. RecyclerView.Adapter 상속받고, ViewHolder 를 셋팅한다.
// 3. 빨간줄 생기면,  구현할 메소드를 오버라이딩 해준다.
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    // 4. 이 어댑터 클래스의 멤버 변수 만든다.
    // 컨텍스트는, 어떤 액티비티에서 리스트를 보여줄지 알아야 하기 때문에 필요.
    Context context;
    // 리사이클러뷰는 여러개의 데이터를 리스트로 보여주는 것이니까 필요하다.
    ArrayList<Contact> contactArrayList;

    // 5. 생성자를 만들어준다.
    public ContactAdapter(Context context, ArrayList<Contact> contactArrayList) {
        this.context = context;
        this.contactArrayList = contactArrayList;
    }

    // 6. 아래 3개의 함수를 구현한다.

    // 화면과 뷰홀더를 연결시킨다.
    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    // 데이터를 화면에 표시한다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactArrayList.get(position);

        holder.txtName.setText( contact.name );
        holder.txtPhone.setText( contact.phone );

    }

    // 데이터의 갯수를 나타낸다.
    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    // 1. 뷰 홀더 클래스를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtPhone;
        ImageView imgDelete;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateActivity.class );

                    // 어댑터에서 몇번째를 유저가 눌렀는지
                    // 인덱스 정보를 알수 있는 함수!
                    int index = getAdapterPosition();

                    Contact contact = contactArrayList.get(index);

                    intent.putExtra("contact", contact);

                    context.startActivity(intent);
                }
            });

        }
    }
}
