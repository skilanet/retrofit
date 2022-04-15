package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView head, description;
    ImageView space_image;
    public final String api_nasa= "https://api.nasa.gov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        head = findViewById(R.id.head);
        description = findViewById(R.id.description);
        space_image = findViewById(R.id.space_image);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(api_nasa).
                addConverterFactory(GsonConverterFactory.create()).
                build();
        SpaceService spaceService = retrofit.create(SpaceService.class);
        Call<ResponseSpace> call = spaceService.getSpaceInfo("TAqdEkLZsdl34IAU2juxnh2iitH9VuzkYUMnN8WC");
        call.enqueue(new SpaceCallback());
    }
    class SpaceCallback implements Callback<ResponseSpace>{

        @Override
        public void onResponse(Call<ResponseSpace> call, Response<ResponseSpace> response) {
            if(response.isSuccessful()){
                String s = response.body().title + "\n\n" + response.body().explanation;
                description.setText(s);
                if(response.body().media_type.equals("image")){
                    Picasso.get().load(response.body().url)
                            .placeholder(R.drawable.apod)
                            .into(space_image);
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseSpace> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Ответ не получен", Toast.LENGTH_SHORT).show();
        }
    }
}