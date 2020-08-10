package com.yijun.this_is_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yijun.this_is_movie.adapter.RecyclerViewAdapter;
import com.yijun.this_is_movie.data.Movie;
import com.yijun.this_is_movie.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edit_title;
    ImageView img_search;
    Button btn_year_arr;
    Button btn_people_arr;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    RequestQueue requestQueue;
    // 페이ㅣ징 처리를 위한 변수
    int offset = 0;
   int cnt;
   int limit =  25;
   // 정렬을 위한 변수
    String order;
    String path = "/api/v1/movies/";
    String title ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_title = findViewById(R.id.edit_title);
        img_search = findViewById(R.id.img_search);
        btn_year_arr = findViewById(R.id.btn_year_arr);
        btn_people_arr = findViewById(R.id.btn_people_arr);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();
                if(lastPosition+1 == totalCount){
//                    if (cnt >= limit){
                   // 네트워크 통해서, 데이터를 더 불러오면 된다.
                    addNetworkData(path);
//                    }
                }
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        getNetworkData(path);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList.clear();
                 title = edit_title.getText().toString().trim();


                if (title.isEmpty() == true) {
                    Toast.makeText(MainActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                offset = 0;
                limit = 25;
                path = "/api/v1/movies/desc/";

                getNetworkData(path);

            }

        });

        btn_year_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList.clear();
                title = edit_title.getText().toString().trim();
                movieArrayList.clear();
                offset = 0;
                order = "year";
                limit = 25;
                path = "/api/v1/movies/desc/";

                getNetworkData(path);

            }
        });



    }



    private void getNetworkData(String path) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Utils.URL + path + "?offset="+offset+"&limit="+limit+"&order="+order+"&title="+edit_title.getText().toString().trim(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA",response.toString());
                        try {
                            boolean success = response.getBoolean("success");
                            if(success == false){
                                Toast.makeText(MainActivity.this,"에러에러",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            JSONArray movie = response.getJSONArray("movie");
                            for (int i = 0; i < movie.length(); i++){
                                JSONObject jsonObject = movie.getJSONObject(i);

                                String title = jsonObject.getString("title");
                                String genre = jsonObject.getString("genre");
                                Integer attendance = jsonObject.getInt("attendance");
                                String year = jsonObject.getString("year");

                                Movie movie1 = new Movie(title, genre, attendance, year);
                                movieArrayList.add(movie1);
                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(
                                    MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            // 페이징을 위해서, 오프셋을 증가시킨다. 그래야 리스트 끝에가서
                            // 네트워크 다시 호출할때, 해당 offset으로 서버에 요청이 가능하다.
                      offset = offset+ response.getInt("cnt");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.i("AAA",error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void addNetworkData(String path) {
      JsonObjectRequest request = new JsonObjectRequest(
              Request.Method.GET,
              Utils.URL + path + "?offset="+offset+"&limit="+limit+"&order="+order+"&title="+edit_title.getText().toString().trim(),
              null,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {
                      try {
                          boolean success = response.getBoolean("success");
                          if (success==false){
                              Toast.makeText(MainActivity.this,"에러에러",Toast.LENGTH_SHORT).show();
                              return;
                          }   JSONArray movie = response.getJSONArray("movie");
                          for (int i = 0; i < movie.length(); i++){
                              JSONObject jsonObject = movie.getJSONObject(i);

                              String title = jsonObject.getString("title");
                              String genre = jsonObject.getString("genre");
                              Integer attendance = jsonObject.getInt("attendance");
                              String year = jsonObject.getString("year");

                              Movie movie1 = new Movie(title, genre, attendance, year);
                              movieArrayList.add(movie1);
                          }
                          recyclerViewAdapter.notifyDataSetChanged();

                          offset = offset+ response.getInt("cnt");

                      } catch (JSONException e) {
                          e.printStackTrace();
                      }
                  }
              }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      }

      );
        requestQueue.add(request);
    }
}