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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edit_title;
    ImageView img_search;
    Button btn_year_arr;
    Button btn_people_arr;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    RequestQueue requestQueue;
    String nextPageToken;
    String pageToken = "";
    String searchUrl = "";

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
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_title.getText().toString().trim();
                if (movieArrayList.size() > 0) {
                    movieArrayList.clear();
                    return;
                }
                if (title.isEmpty() == true) {
                    Toast.makeText(MainActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject object = new JSONObject();
                try {
                    object.put("title", title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.URL + "/api/v1/movies/search/?offset=0",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray movie = response.getJSONArray("movie");
                                    for (int i = 0; i < movie.length(); i++) {
                                        JSONObject jsonObject = movie.getJSONObject(i);
                                        String title = jsonObject.getString("title");
                                        String genre = jsonObject.getString("genre");
                                        String attendance = jsonObject.getString("attendance");
                                        String year = jsonObject.getString("year");

                                        Movie movie1 = new Movie(title, genre, attendance, year);
                                        movieArrayList.add(movie1);
                                    }
                                    recyclerViewAdapter = new RecyclerViewAdapter(
                                            MainActivity.this, movieArrayList);
                                    recyclerView.setAdapter(recyclerViewAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("AAA", error.toString());
                            }
                        }
                );
                requestQueue.add(request);
            }

        });

        btn_year_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edit_title.getText().toString().trim();
                JSONObject object = new JSONObject();
                try {
                    object.put("title", title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                movieArrayList.clear();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        Utils.URL + "/api/v1/movies/desc/?offset=0&title=" + title,
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray movie = response.getJSONArray("movie");
                                    for (int i = 0; i < movie.length(); i++) {
                                        JSONObject jsonObject = movie.getJSONObject(i);
                                        String title = jsonObject.getString("title");
                                        String genre = jsonObject.getString("genre");
                                        String attendance = jsonObject.getString("attendance");
                                        String year = jsonObject.getString("year");

                                        Movie movie1 = new Movie(title, genre, attendance, year);
                                        movieArrayList.add(movie1);
                                    }
                                    recyclerViewAdapter = new RecyclerViewAdapter(
                                            MainActivity.this, movieArrayList);
                                    recyclerView.setAdapter(recyclerViewAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Utils.URL + "/api/v1/movies/?offset=0",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray movie = response.getJSONArray("movie");
                            for (int i = 0; i < movie.length(); i++) {
                                JSONObject jsonObject = movie.getJSONObject(i);
                                String title = jsonObject.getString("title");
                                String genre = jsonObject.getString("genre");
                                String attendance = jsonObject.getString("attendance");
                                String year = jsonObject.getString("year");

                                Movie movie1 = new Movie(title, genre, attendance, year);
                                movieArrayList.add(movie1);
                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(
                                    MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}