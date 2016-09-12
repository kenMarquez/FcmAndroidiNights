package com.ken.fcmandroidinights;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AndroidiNights";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, refreshedToken);

        Bundle extras = getIntent().getExtras();
        TextView tvData = (TextView) findViewById(R.id.tv_bundle);

        if (extras != null) {
            for (String key : extras.keySet()) {
                tvData.append(key + ":   " + extras.getString(key) + "\n");
            }
        }


        CardView cardView = (CardView) findViewById(R.id.card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPush();
            }
        });


    }

    private void sendPush() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging);

        OkHttpClient client = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com");

        Retrofit retrofit = builder.client(client).build();

        FcmService service = retrofit.create(FcmService.class);

        service.push("key=AIzaSyC7u7jtFFeIMBj9KJcRklsolCvElPUcqGI", "enHi5D93kwQ:APA91bF5p6BdD0MZJrvQ1damauvmhkZncnWeE_pPQXbBegRRrE2UPPytysZur1pACcbwt7oJ_YBVQ1WaXnwEOyYfvzmzYSAAQzf9UYhlhsYRfM4HApGxzuT3AkY90tVpgK8y8H_HyJt5").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("FCM", "messages" + response.message());
                Log.e("FCM", "code" + response.code());


                try {
                    if (response.body() != null) {
                        System.out.println(response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("FCM error", t.getMessage());
            }
        });


    }


    public interface FcmService {


        @FormUrlEncoded
        @POST("/fcm/send")
        Call<ResponseBody> push(@Header("Authorization") String key, @Field("to") String tokenId);
    }


}
