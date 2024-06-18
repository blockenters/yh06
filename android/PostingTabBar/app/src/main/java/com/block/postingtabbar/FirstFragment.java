package com.block.postingtabbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.block.postingtabbar.adapter.PostingAdapter;
import com.block.postingtabbar.api.NetworkClient;
import com.block.postingtabbar.api.PostingApi;
import com.block.postingtabbar.config.Config;
import com.block.postingtabbar.model.Posting;
import com.block.postingtabbar.model.PostingList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<Posting> postingArrayList = new ArrayList<>();
    PostingAdapter adapter;

    String token;

    // 페이징 처리에 필요한 멤버 변수들!!
    int offset = 0;
    int limit = 5;
    int count;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_first, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE);
        token = sp.getString("token", "");

        progressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager( getActivity() ));

        getNetworkData();

        return rootView;
    }

    private void getNetworkData() {

        progressBar.setVisibility(View.VISIBLE);

        postingArrayList.clear();

        offset = 0;

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        PostingApi api = retrofit.create(PostingApi.class);

        Call<PostingList> call = api.getPostingList("Bearer "+token, offset, limit);

        call.enqueue(new Callback<PostingList>() {
            @Override
            public void onResponse(Call<PostingList> call, Response<PostingList> response) {
                progressBar.setVisibility(View.GONE);

                if(response.isSuccessful()){
                    PostingList postingList = response.body();

                    count = postingList.count;

                    offset = offset + count;

                    postingArrayList.addAll( postingList.items );

                    adapter = new PostingAdapter(getActivity(), postingArrayList);

                    recyclerView.setAdapter(adapter);

                }else{

                }

            }

            @Override
            public void onFailure(Call<PostingList> call, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}







