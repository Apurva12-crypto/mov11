package com.example.moviesfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private ImageView imageView;

    private Movie[] Movies = new Movie[0];
    private String sortType = "popular";
    private FetchData task;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String Sort_Popular = "http://api.themoviedb.org/3/movie/popular?api_key=338b39a38ed5065e52e0281a6aa38361";
        String Sort_Rating = "http://api.themoviedb.org/3/movie/top_rated?api_key=338b39a38ed5065e52e0281a6aa38361";

        task = new FetchData();
        task.execute("popular");

        recyclerView = (RecyclerView) findViewById(R.id.dear_RecyclerView);
        img = (ImageView) findViewById(R.id.myImage);

        // use a linear layout manager
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new RecyclerViewAdapter(context, Movies);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();

        Context context = MainActivity.this;


        switch (menuItemThatWasSelected) {
            case R.id.popular:
                sortType = "popular";
                Toast.makeText(getApplicationContext(), "Sort By Popular", Toast.LENGTH_LONG).show();
                // Network Call (popular)
                break;
//                return true;
            case R.id.topRated:
                sortType = "top_rated";
                Toast.makeText(getApplicationContext(), "Sort By Rating", Toast.LENGTH_LONG).show();
                break;

        }
        task.cancel(true);
        task = new FetchData();
        task.execute(sortType);
        return super.onOptionsItemSelected(item);
    }
    //implementing image library Picasso

    public void onClick(View view) {
        Picasso.get().load("http://image.tmdb.org/t/p/w185/").into(imageView);
    }


    public class FetchData extends AsyncTask<String, Void, String> {
        private static final String TAG = "error";

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: get response error");
            try {
                // build the proper URL
                URL url = NetworkUtils.buildUrl(strings[0]);
                // fetch movies from the API
                String string = NetworkUtils.getResponseFromHttpUrl(url);
                return string;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, "onPostExecute: error");
            super.onPostExecute(s);

            try {

                Movie[] Movies = JsonUtils.getMovieInformationsFromJson(MainActivity.this, s);
                new GridLayoutManager(MainActivity.this, 2);
                recyclerView.setLayoutManager(layoutManager);
                mAdapter = new RecyclerViewAdapter(MainActivity.this, Movies);
                recyclerView.setAdapter(mAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void setOnClickListeners() {
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String movie_id = getId();

                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);

                intent.putExtra("movieId", movie_id);

                startActivity(intent);
            }
            public String getId() {

                return movie_id;

            }
        });
    }
}





