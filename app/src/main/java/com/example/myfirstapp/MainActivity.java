package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private TextView postId;
    private TextView postTitle;
    private TextView postText;
    ProgressDialog dialog;
    String title="Sample title";
    String body="Sample body";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog=new ProgressDialog(this);
        dialog.setTitle("Data Fetching..!");
        dialog.show();
        initViews();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostService postService = retrofit.create(PostService.class);

       // getAllPosts(postService);

        PostPojo newPost = new PostPojo(100,200,title,body);
        newPost.setId(100);
        newPost.setUserId(200);
        newPost.setTitle("Sample title");
        newPost.setBody("Sample data.");
        createPost(postService, newPost);

    }

    private void createPost(PostService postService, PostPojo newPost) {

        Call<PostPojo> call = postService.createPost(newPost);
        call.enqueue(new Callback<PostPojo>() {
            @Override
            public void onResponse(Call<PostPojo> call, Response<PostPojo> response) {
                dialog.dismiss();
                displayPost(response.body());
            }

            @Override
            public void onFailure(Call<PostPojo> call, Throwable t) {
                dialog.dismiss();
                Log.i("MainActivity",t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to create post" , Toast.LENGTH_LONG).show();
                Log.e(TAG,t.toString());
            }
        });
    }

    private void getAllPosts(PostService postService) {
        Call<List<PostPojo>> getAllPostsCall = postService.getAllPhotos();

        getAllPostsCall.enqueue(new Callback<List<PostPojo>>() {
            @Override
            public void onResponse(Call<List<PostPojo>> call, Response<List<PostPojo>> response) {
                dialog.dismiss();
                displayPost(response.body().get(0));
            }

            @Override
            public void onFailure(Call<List<PostPojo>> call, Throwable t) {
                dialog.dismiss();
                Log.e(TAG, "Error occured while fetching post.");
            }
        });
    }

    private void initViews() {
        this.postId = findViewById(R.id.postId);
        this.postTitle =  findViewById(R.id.postTitle);
        this.postText = findViewById(R.id.postText);
    }

    private void displayPost(PostPojo post) {
        postId.setText(post.getId().toString());
        postTitle.setText(post.getTitle());
        postText.setText(post.getBody());

    }
}