package com.example.quizzy.network;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.quizzy.domain.UserResponse;
import com.example.quizzy.task.LoginTask;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtil {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";

    public NetworkUtil(){
        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }


//    public void handleSignup(String name, String email, String pass, final @Nullable UiSingUpInterface callBack){
//        HashMap<String,String> hashMap = new HashMap<>();
//
//        hashMap.put("name",name);
//        hashMap.put("email",email);
//        hashMap.put("password",pass);
//
//        Call<UserInfo> call = retrofitInterface.executeSignup(hashMap);
//
//        call.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
//                if(response.code() == 201){
//                    try {
//                        UserInfo userInfo = (UserInfo) response.body();
//                        Log.d("email ",userInfo.getEmail());
//                        callBack.signUp(userInfo);
////                        Toast.makeText(SignupActivity.this, "saved info successfully " + name +"\n"+email+ "\n"+token, Toast.LENGTH_LONG).show();
////                        saveInfo(name, email, token);
////                        Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
////                        intent.putExtra("MyInfo",userInfo);
////                        startActivity(intent);
////                        finish();
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }else if(response.code() == 400){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }



    public void handleLogin(String email, final String pass, final @Nullable LoginTask callBack){
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("email",email);
        hashMap.put("password",pass);

        Call<UserResponse> call = retrofitInterface.executeLogin(hashMap);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        UserResponse userResponse = (UserResponse) response.body();
                        callBack.logIn(userResponse);
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }else if(response.code() == 400){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }


//    public void logOut(String header, @Nullable final UiLogOutTask callBack){
//        Call<Void> call = retrofitInterface.executeLogout("Bearer "+header);
////        Toast.makeText(HomeActivity.this,"inside this method",Toast.LENGTH_LONG).show();
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
//                if(response.code() == 200){
//                    try {
//                        callBack.logOut();
////                        Toast.makeText(HomeActivity.this, "logged out successfully ", Toast.LENGTH_LONG).show();
////                        removeInfo();
////                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }else if(response.code() == 400){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }

//    public void logoutAllDevice(String header, @Nullable final UiLogOutTask callBack){
//        //format of the header is : header-name "Authorization", header-value "Bearer <token>"
//        Call<Void> call = retrofitInterface.executeLogoutAll("Bearer "+header);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
//                if(response.code() == 200){
//                    try {
////                        Toast.makeText(HomeActivity.this, "logged out from all devices successfully ", Toast.LENGTH_LONG).show();
//                        callBack.logOut();
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }else if(response.code() == 500){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }

}
