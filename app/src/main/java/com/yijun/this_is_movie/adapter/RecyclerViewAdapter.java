package com.yijun.this_is_movie.adapter;

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


import com.yijun.this_is_movie.R;
import com.yijun.this_is_movie.data.Movie;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    Context context;
    ArrayList<Movie> movieArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Movie> movieArrayList){
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 첫번째 파라미터인, parent로 부터 뷰(화면 : 하나의 셀)를 생성한다.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent, false); //inflate=만들라는 뜻
        //리턴에, 위에서 생성한 뷰를, 뷰홀더에 담아서 리턴한다.
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        // 어레이리스트에 저장된 데이터를 화면과 연결 : bind
        Movie movie= movieArrayList.get(position);
        String title = movie.getTitle();
        String genre = movie.getGenre();
        String attendance = movie.getAttendance();
        String year = movie.getYear();
        holder.txtTitle.setText(title);
        holder.txtgenre.setText(genre);
        holder.txtpeople.setText(attendance);
        holder.txtyear.setText(year);


    }

    @Override
    public int getItemCount() {
        return movieArrayList .size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle;
        public TextView txtgenre;
        public TextView txtpeople;
        public TextView txtyear;
        public CardView cardView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtgenre = itemView.findViewById(R.id.txtgenre);
            txtpeople = itemView.findViewById(R.id.txtpeople);
            txtyear = itemView.findViewById(R.id.txtyear);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
