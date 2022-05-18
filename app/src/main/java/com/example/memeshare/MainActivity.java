package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.JsonRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
//    String imageUrl;
//    Bitmap myImage;
    ImageView imageView;
    ProgressBar progressBar;
    String imageUrl;
//                      AsycnTask Method:
//    public class JsonDowloader extends AsyncTask<String,Void,String>{
//        @Override
//        protected String doInBackground(String... urls) {
//            String result ="";
//            try {
//                URL url = new URL(urls[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                InputStream inputStream = urlConnection.getInputStream();
//                InputStreamReader reader = new InputStreamReader(inputStream);
//                int data = reader.read();
//                while (data!=-1){
//                    char json = (char) data;
//                    result += json;
//                    data = reader.read();
//                }
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "failed!";
//            }
//        }
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                Log.i("json:" ,s);
//                JSONObject jsonObject = new JSONObject(s);
//                imageUrl = jsonObject.getString("url");
//                Log.i("image url:",imageUrl);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public class imageDownloader extends AsyncTask<String,Void, Bitmap>{
//        @Override
//        protected Bitmap doInBackground(String... urls) {
//            try {
//                URL url = new URL(urls[0]);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.connect();
//                InputStream in = urlConnection.getInputStream();
//                Bitmap myBitmap = BitmapFactory.decodeStream(in);
//                return  myBitmap;
//            }catch (Exception e){
//                e.printStackTrace();
//                return null;
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
//        JsonDowloader task = new JsonDowloader();
//        imageDownloader imageTask = new imageDownloader();                            // AsycnTask Method
//        try {
//            task.execute("https://meme-api.herokuapp.com/gimme").get();
//           myImage= imageTask.execute(imageUrl).get();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        imageView.setImageBitmap(myImage);
        loadMeme();
    }
                                // Volley and Glide Library method:
    private void loadMeme(){
        progressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);              // Volley is an efficient API calling framework
                                                                            // it is commented because for adding request queue singleton pattern is used
         String url ="https://meme-api.herokuapp.com/gimme";

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("json url:",response.toString());
                    imageUrl = response.getString("url");
                    Glide.with(MainActivity.this).load(imageUrl).listener(new RequestListener<Drawable>() {
                        @Override                                                                                                                          // this two methods are from setting listener in glide
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {          // onLoadFailed will execute when glide cannot load media from the url
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {  //onResourceReady will execute when glide successfully loads media from the url
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(imageView);           // Glide is an efficient open source management and image loading framework
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error:","That didn't work!");
            }
        });
// Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest);    commented because singleton pattern is used
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);  // Called using singleton pattern

    }
    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Look at this awesome meme i got from Reddit: "+imageUrl);
        Intent chooser =Intent.createChooser(intent,"Share this meme using: ");
        startActivity(chooser);
    }
    public void nextMeme(View view) {
        loadMeme();
    }
}