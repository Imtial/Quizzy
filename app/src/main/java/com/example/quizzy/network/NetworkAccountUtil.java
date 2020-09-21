package com.example.quizzy.network;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.quizzy.task.UploadProfilePictureTask;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkAccountUtil extends NetworkUtil {

    public NetworkAccountUtil() {
        super();
    }

//    public void getProfile(String header, final @Nullable GetProfileTask callBack){
//        Log.d("before request ",header);
//        Call<UserInfo> call = retrofitInterface.executeGetMyProfile("Bearer "+header);
//
//        call.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                //Toast.makeText(FirstActivity.this,response.code()., Toast.LENGTH_LONG).show();
//                if(response.code() == 200){
//                    try {
//                        Log.d("before getting profile ","code => "+response.code());
//                        UserInfo userInfo = (UserInfo) response.body();
//                        Log.d("got profile ", userInfo.getEmail());
//                        callBack.getMyProfile(userInfo);
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                        callBack.onFailure(e.getMessage());
//                    }
//                }else if(response.code() == 400){
//                    Log.d("response ","code => "+response.code());
//                    callBack.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                Log.d("failure ",t.toString());
//                callBack.onFailure(t.toString());
//            }
//        });
//    }

//    public void deleteMyAccount(String header, @Nullable final DeleteUserTask callBack){
//        Call<UserInfo> call = retrofitInterface.executeDeleteMyAccount("Bearer "+header);
//
//        call.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                if(response.code() == 200){
//                    try {
////                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
//                        Log.d("call => ","before jsonUser");
//                        UserInfo userInfo = (UserInfo) response.body();
//                        Log.d("call => ","object Got");
//                        callBack.deleteUser(userInfo);
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                        callBack.onFailure(e.getMessage());
//                    }
//                }else if(response.code() == 500){
//                    Log.d("response ","code => "+response.code());
//                    callBack.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                Log.d("failure ",t.toString());
//                callBack.onFailure(t.toString());
//            }
//        });
//    }

//    public void editMyAccount(String header, String name, String password, @Nullable final EditProfileTask callBack){
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("name",name);
//        hashMap.put("password",password);
//        Call<UserInfo> call = retrofitInterface.executeEditMyAccount("Bearer "+header, hashMap);
//        call.enqueue(new Callback<UserInfo>() {
//            @Override
//            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
//                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
//                if(response.code() == 200){
//                    try {
//                        //the server sends back the name,email,_id etc of the updated userInfo,actually it can respond with other info too,
//                        // check the doc for that we can get any string in the same way as below so pera naai
//                        // new email and name has been assigned to the userInfo and then updated in the sqlite database
//                        Log.d("call => ","before jsonUser");
//                        UserInfo userInfo = ((UserInfo) response.body());
//                        Log.d("call => ","object Got");
//                        callBack.editProfile(userInfo);
//
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                        callBack.onFailure(e.getMessage());
//                    }
//                }else if(response.code() == 400){
////                    Toast.makeText(HomeActivity.this, "could not edit", Toast.LENGTH_LONG).show();
//                    Log.d("response ","code => "+response.code());
//                    callBack.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserInfo> call, Throwable t) {
//                Log.d("failure ",t.toString());
//                callBack.onFailure(t.toString());
//            }
//        });
//    }

    public void uploadProfilePicture(String header, MultipartBody.Part body, @Nullable final UploadProfilePictureTask callBack){
        Call<Void> call = retrofitInterface.executeUploadProfilePicture("Bearer "+header, body);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    try {
                        Log.d("call => ","after response");
                        callBack.uploadProfilePicture();
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                        callBack.onFailure(e.getMessage());
                    }
                }
                else if(response.code() == 400){
                    Log.d("response ","code => "+response.code());
                    callBack.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callBack.onFailure(t.toString());
                Log.d("failure ",t.toString());
            }
        });
    }

//    public void deleteProfilePicture(String header, @Nullable final DeleteProfilePictureTask callBack){
//        Call<Void> call = retrofitInterface.executeDeleteProfilePicture("Bearer "+header);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.code() == 200){
//                    try {
////                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
//                        Log.d("call => ","after response");
//                        callBack.deleteProfilePicture();
//                    }catch (Exception e){
//                        callBack.onFailure(e.getMessage());
//                        Log.d("exception ",e.getMessage());
//                    }
//                }else {
//                    Log.d("response ","code => "+response.code());
//                    callBack.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                callBack.onFailure(t.toString());
//                Log.d("failure ",t.toString());
//            }
//        });
//    }
//
//    public void getProfilePicture(String _id, @Nullable final FetchProfilePictureTask callBack){
//        Call<Object> call = retrofitInterface.executeGetProfilePicture(_id);
//
//        call.enqueue(new Callback<Object>() {
//            @Override
//            public void onResponse(Call<Object> call, Response<Object> response) {
//                if(response.code() == 200){
//                    try {
////                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
//                        String string = (String) response.body();
//
//                        byte[] bytes = string.getBytes();
//                        for(int i = 0 ; i < bytes.length ; i++) {
//                            Log.d("call => ", "byte => " + bytes[i]);
//                        }
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                        callBack.fetchProfilePicture(bitmap);
//                    }catch (Exception e){
//                        callBack.onFailure(e.getMessage());
//                        Log.d("exception ",e.getMessage());
//                    }
//                }else {
//                    Log.d("response ","code => "+response.code());
//                    callBack.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
//                callBack.onFailure(t.toString());
//                Log.d("failure ",t.toString());
//            }
//        });
//    }

}
