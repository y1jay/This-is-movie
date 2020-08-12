package com.yijun.this_is_movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edit_title;
    ImageView img_search;
    Button btn_year_arr;
    Button btn_people_arr;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Movie> movieArrayList = new ArrayList<>();
    RequestQueue requestQueue;
    SharedPreferences sp;

    private AlertDialog dialog;
    EditText edit_email;
    EditText edit_passwd;
    EditText edit_passwd1;
    TextView txtNO ;
    TextView txtYES;
    EditText log_email;
    EditText log_passwd;
    // 페이ㅣ징 처리를 위한 변수
    int offset = 0;
   int cnt;
   int limit =  25;
   // 정렬을 위한 변수
    String order = "";
    String path = "/api/v1/movies";
    String title = "" ;
    String token;
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

                   // 네트워크 통해서, 데이터를 더 불러오면 된다.
                    addNetworkData(path);

                }
            }
        });
        requestQueue = Volley.newRequestQueue(MainActivity.this);
       SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
       token = sp.getString("token",null);

       if (token !=null){
           path = "/api/v1/favorite/auth";

       }else{
           path = "/api/v1/movies";
       }

        getNetworkData(path);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList.clear();


                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                token = sp.getString("token",null);

                if (token !=null){
                    path = "/api/v1/movies/search";

                }else{
                    path = "/api/v1/movies/search";
                }
                title = edit_title.getText().toString().trim();
                offset = 0;
                limit = 25;

                getNetworkData(path);

            }

        });

        btn_year_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList.clear();


                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                token = sp.getString("token",null);

                if (token !=null){
                    path = "/api/v1/movies/desc";

                }else{
                    path = "/api/v1/movies/desc";

                }
                title = edit_title.getText().toString().trim();
                offset = 0;
                order = "year";
                limit = 25;

                getNetworkData(path);

            }
        });
        btn_people_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieArrayList.clear();

                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                token = sp.getString("token",null);

                if (token !=null){
                    path = "/api/v1/movies/people/";

                }else{
                    path = "/api/v1/movies/people/";

                }
                title = edit_title.getText().toString().trim();
                offset = 0;
                order = "attendance";
                limit = 25;


                getNetworkData(path);
            }
        });


    }



    private void getNetworkData(String path) {
        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Utils.URL + path + "?offset="+offset+"&limit="+limit+"&order="+order+"&title="+title,
                body,
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
                                int attendance = jsonObject.getInt("attendance");
                                String year = jsonObject.getString("year");
                                int id = jsonObject.getInt("id");
                                int is_favorite;
                                if (movie.getJSONObject(i).isNull("is_favorite")){
                                    is_favorite = 0;
                                }else{
                                    is_favorite = movie.getJSONObject(i).getInt("is_favorite");
                                }

                                Movie movie1 = new Movie(id,title, genre, attendance, year,is_favorite);
                                movieArrayList.add(movie1);
                            }
                            recyclerViewAdapter = new RecyclerViewAdapter(
                                    MainActivity.this, movieArrayList);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            // 페이징을 위해서, 오프셋을 증가시킨다. 그래야 리스트 끝에가서
                            // 네트워크 다시 호출할때, 해당 offset으로 서버에 요청이 가능하다.
                      offset = offset + response.getInt("cnt");


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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>params = new HashMap<>();
                params.put("Authorization","Bearer "+token);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void addNetworkData(String path) {

        JSONObject body = new JSONObject();
        try {
            body.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

      JsonObjectRequest request = new JsonObjectRequest(
              Request.Method.GET,
              Utils.URL + path + "?offset="+offset+"&limit="+limit+"&order="+order+"&title="+title,
              body,
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
                              int attendance = jsonObject.getInt("attendance");
                              String year = jsonObject.getString("year");
                             int id = jsonObject.getInt("id");
                              int is_favorite;
                              if (movie.getJSONObject(i).isNull("is_favorite")){
                                  is_favorite = 0;
                              }else{
                                  is_favorite = movie.getJSONObject(i).getInt("is_favorite");
                              }

                              Movie movie1 = new Movie(id,title, genre, attendance, year,is_favorite);
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

      ){
          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
              Map<String, String>params = new HashMap<>();
              params.put("Authorization","Bearer "+token);
              return params;
          }
      };
        requestQueue.add(request);
    }
    public void createPopupDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder
                (MainActivity.this);
        View alertView = getLayoutInflater().inflate(R.layout.sign_up,null);
        edit_email = alertView.findViewById(R.id.edit_email);
        edit_passwd = alertView.findViewById(R.id.edit_passwd);
        edit_passwd1 = alertView.findViewById(R.id.edit_passwd1);
        txtNO = alertView.findViewById(R.id.txtNO);
        txtYES = alertView.findViewById(R.id.txtYES);

        txtYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        final String email = edit_email.getText().toString().trim();
                        final String passwd = edit_passwd.getText().toString().trim();
                        String check_passwd = edit_passwd1.getText().toString().trim();
                        // 클라이언트에서 1차적으로 체크, 서버에서 2차적으로 체크한다 보안을 위해
                        if(email.contains("@")==false){
                            Toast.makeText(MainActivity.this,
                                    "이메일 아니자나",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(passwd.length()<4 || passwd.length()>12){
                            Toast.makeText(MainActivity.this,
                                    "비밀번호 길이는 4이상 12 이하로 하셈",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(passwd.equalsIgnoreCase(check_passwd)==false){
                            Toast.makeText(MainActivity.this,
                                    "ㅄ",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject  object = new JSONObject();
                        try {
                            object.put("email",email);
                            object.put("passwd",passwd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // 서버로 이메일과 비밀번호를 전송한다.
                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.POST,
                                Utils.URL + "/api/v1/user/signUp",
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.i("회원가입", response.toString());

                                        try {
                                            String token = response.getString("token");

                                            sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                                            SharedPreferences.Editor editor= sp.edit();
                                            editor.putString("token", token);
                                            editor.apply();

                                           dialog.cancel();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.i("회원가입",error.toString());
                                    }
                                }
                        );


                        // 네트워크 타고 DB로보내는 것
                        requestQueue.add(request);


                dialog.cancel();
                Toast.makeText(MainActivity.this,"회원가입이 완료되었습니다."
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        txtNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        alert.setView(alertView);

        dialog=alert.create();
//                alert.setCancelable(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    public void createPopupDialog1(){
        AlertDialog.Builder alert = new AlertDialog.Builder
                (MainActivity.this);
        View alertView = getLayoutInflater().inflate(R.layout.log_in,null);
        log_email = alertView.findViewById(R.id.log_email);
        log_passwd = alertView.findViewById(R.id.log_passwd);
        txtNO = alertView.findViewById(R.id.txtNO);
        txtYES = alertView.findViewById(R.id.txtYES);

        txtYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = log_email.getText().toString().trim();
                String passwd = log_passwd.getText().toString().trim();
                if (email.contains("@")==false){
                    Toast.makeText(MainActivity.this,"이메일이 아닙니다",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(passwd.isEmpty()||passwd.length()<4 || passwd.length()>12){
                    Toast.makeText(MainActivity.this,
                            "비밀번호 길이는 4이상 12 이하로 하셈",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject  object = new JSONObject();
                try {
                    object.put("email",email);
                    object.put("passwd",passwd);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        Utils.URL + "/api/v1/user/login",
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("로그인", response.toString());

                                try {
                                    String token = response.getString("token");
                                    sp = getSharedPreferences(Utils.PREFERENCES_NAME,MODE_PRIVATE);
                                    SharedPreferences.Editor editor= sp.edit();
                                    editor.putString("token", token);
                                    editor.apply();

                                    dialog.cancel();

                                    getNetworkData("/api/v1/favorite/auth");
                                    Toast.makeText(MainActivity.this,"로그인이 완료되었습니다."
                                            ,Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                                // 요기요
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("로그인", error.toString());
                                Toast.makeText(MainActivity.this,"없는아이디입니다."
                                        ,Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                );

                Volley.newRequestQueue(MainActivity.this).add(request);


            }

        });

        txtNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        alert.setView(alertView);

        dialog=alert.create();
//                alert.setCancelable(false);
        dialog.setCancelable(false);
        dialog.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_signup) {
           createPopupDialog();
        }else if(id ==R.id.menu_login){
            createPopupDialog1();

        }

        return super.onOptionsItemSelected(item);

    }
    public void addFavorite(final int position) {
        // position을 통해서, 즐겨찾기 추가할 movie_id 값을 가져올 수 있습니다.
        Movie movie = movieArrayList.get(position);
        Integer movie_id = movie.getId();
        Log.i("AAA", "position : " + position);

        JSONObject body = new JSONObject();
        try {
            body.put("movie_id", movie_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Utils.URL + "/api/v1/favorite",
                body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", "add favorite : " + response.toString());
                        // 어레이 리스트의 값을 변경시켜줘야 한다.
                        Movie movie1 = movieArrayList.get(position);
                        movie1.setIs_favorite(1);
                        path = "/api/v1/favorite/auth";
                        getNetworkData(path);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("AAA", "add favorite error : " + error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        requestQueue.add(request);

    }
    public void delFavorite(final int position) {
        // position을 통해서, 즐겨찾기 추가할 movie_id 값을 가져올 수 있습니다.
        Movie movie = movieArrayList.get(position);
        Integer movie_id = movie.getId();
        Log.i("AAA", "position : " + position);

//        JSONObject body = new JSONObject();
//        try {
//            body.put("movie_id", movie_id);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                Utils.URL + "/api/v1/favorite/"+movie_id+"/del",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("AAA", "add favorite : " + response.toString());
                        // 어레이 리스트의 값을 변경시켜줘야 한다.
                        Movie movie1 = movieArrayList.get(position);
                        movie1.setIs_favorite(0);

                        path = "/api/v1/favorite/auth";
                        getNetworkData(path);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("AAA", "add favorite error : " + error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sp = getSharedPreferences(Utils.PREFERENCES_NAME, MODE_PRIVATE);
                String token = sp.getString("token", null);

                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };

        requestQueue.add(request);

    }

}
