package com.yijun.this_is_movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.this_is_movie.MainActivity;
import com.yijun.this_is_movie.R;
import com.yijun.this_is_movie.data.Movie;
import com.yijun.this_is_movie.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

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
        Integer attendance = movie.getAttendance();
        String year = movie.getYear();

        holder.txtTitle.setText(title);
        holder.txtgenre.setText(genre);
        holder.txtpeople.setText("관객수 : "+attendance);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        df.setTimeZone(TimeZone.getTimeZone("UTC"));// 위의 시간을 UTC로 맞추는것, 서버에서 이미 맞춰놨으면 안해도 됨
        try {
            Date date = df.parse(year);
            df.setTimeZone(TimeZone.getDefault());// 내폰의 로컬 타임존으로 바꾼다.
            String strdate = df.format(date);
            holder.txtyear.setText("개봉 날짜 : "+strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (movie.getIs_favorite() == 1) {
            holder.btn_star.setImageResource(android.R.drawable.btn_star_big_on);
        }else{
            holder.btn_star.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtTitle;
        public TextView txtgenre;
        public TextView txtpeople;
        public TextView txtyear;
        public CardView cardView;
        public ImageView btn_star;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtgenre = itemView.findViewById(R.id.txtgenre);
            txtpeople = itemView.findViewById(R.id.txtpeople);
            txtyear = itemView.findViewById(R.id.txtyear);
            cardView = itemView.findViewById(R.id.cardView);
            btn_star = itemView.findViewById(R.id.btn_star);



            btn_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    SharedPreferences sp = context.getSharedPreferences(Utils.PREFERENCES_NAME,
                            MODE_PRIVATE);
                    final String token = sp.getString("token",null);
                    if (token == null){
                        Toast.makeText(context,"로그인이 필요합니다",Toast.LENGTH_SHORT).show();
                    }else{
                        // 정상적으로 별표 표시를 서버로 보냅니다.
                        // 즐겨찾기 구차가는 API를 호출한건데,
                        // 호출하는 코드는 메인엑티비티에 메소드로 만들고,
                        // 여기에서는 position 값만 넘겨주도록 한다.

                        // 별표가 이미 있으면, 즐겨찾기 삭제 함수 호출!

                        // 별표가 없으면, 즐겨찾기 추가 함수 호출!
                        int is_favorite = movieArrayList.get(position).getIs_favorite();
                        if (is_favorite==1) {
                            // 별표 색이 이미 있으면, 즐겨찾기 삭제함수 호출
                            ((MainActivity)context).delFavorite(position);
                        }else{
                            // 별표 색이 없으면, 즐겨찾기 추가 함수 호출
                            ((MainActivity)context).addFavorite(position);
                        }
                    }

                }
            });
        }
    }
}
