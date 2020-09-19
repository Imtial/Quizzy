package com.example.quizzy.network;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.quizzy.domain.QuestionPaper;
import com.example.quizzy.domain.QuizFeed;
import com.example.quizzy.task.QuestionsForQuizTask;
import com.example.quizzy.task.ShowFeedTask;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkQuizUtil extends NetworkUtil {

    public NetworkQuizUtil() {
        super();
    }
//
//    public void postMyQuiz(String header, Quiz quiz, @Nullable final CreateQuizTask callBack){
//        Log.d("post quiz pre response", "let's see'");
//        Call<QuizResponse> call = retrofitInterface.executeCreateQuiz("Bearer "+header, quiz);
//        Log.d("post quiz pre response", "let's see again");
//        call.enqueue(new Callback<QuizResponse>() {
//            @Override
//            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
//                if(response.code() == 201){
//                    try{
//                        Log.d("post quiz pre response", "title");
//
//                        QuizResponse quizObject = (QuizResponse) response.body();//the posted quiz is returned from the server
////                        showDataTextView.setText("ekhaaneeeee");
//                        Log.d("post quiz response", quizObject.getTitle());
//                        callBack.createQuiz(quizObject);
//                        Log.d("post quiz response", "leaving callBack");
//
//                        //show whatever you want,you have the quizObject
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 400){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuizResponse> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//    }
//
//    public void deleteQuiz(String header, String id, @Nullable final DeleteQuizTask callBack){
//        Call<Void> call = retrofitInterface.executeDeleteQuiz("Bearer "+header, id);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.code() == 200){
//                    try{
//                        Log.d("delete Q ","deleted quiz successfully");
//                        callBack.deleteQuiz();
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 500){
//                    Log.d("response ","code => "+response.code());
//                }
//                else if(response.code() == 404){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//    }
//
//
//    public void showMyQuiz(String header, @Nullable final ShowQuizTask callBack){
//        Call<List<QuizResponse>> call = retrofitInterface.executeGetMyQuiz("Bearer "+header);
//        Log.d("after request => ","got response");
//        call.enqueue(new Callback<List<QuizResponse>>() {
//            @Override
//            public void onResponse(Call<List<QuizResponse>> call, Response<List<QuizResponse>> response) {
//                if(response.code() == 200){
//                    try{
//                        Log.d("in response => ","receiving quizzes");
//                        List<QuizResponse> quizzes = (List<QuizResponse>) response.body();//all the JSONObect quizzes are here
//                        Log.d("after response => ","received quizzes");
//                        //building quiz list,
//
//                        callBack.showMyQuizzes(quizzes);
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 500){
//                    Log.d("response ","code => "+response.code());
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<QuizResponse>> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//    }
//
//    public void editMyQuiz(String header, String _id, Quiz quiz, @Nullable final EditQuizTask callBack){
//        Call<QuizResponse> call = retrofitInterface.executeEditQuiz("Bearer "+header, _id, quiz);
//
//        call.enqueue(new Callback<QuizResponse>() {
//            @Override
//            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
//                if(response.code() == 200){
//                    try{
//                        QuizResponse quizResponse = (QuizResponse) response.body();//the posted quiz is returned from the server
//                        callBack.editQuiz(quizResponse);
////                            showDataTextView.setText(quizQuizResponse.getResponses().get(0).getMessage());//just showing the first response
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 400){
//                    Log.d("response ","code => "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuizResponse> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }

    public void showTopFeedQuizzes(String header, HashMap<String, String> hashMap, int skip, int limit,
                                   @Nullable final ShowFeedTask callBack){

        Call<List<QuizFeed>> call;
        if(hashMap.containsKey("title")) {
            if(hashMap.containsKey("tag")) {//tag title both ase
                String title = hashMap.get("title");
                String tag = hashMap.get("tag");
                call = retrofitInterface.executeRecentQuiz("Bearer "+header, title, tag, skip, limit);
            }
            else{
                String title = hashMap.get("title");
                call = retrofitInterface.executeRecentQuizByTitle("Bearer "+header, title, skip, limit);
            }
        }
        else if(hashMap.containsKey("tag")){
            String tag = hashMap.get("tag");
            call = retrofitInterface.executeRecentQuizByTag("Bearer "+header, tag, skip, limit);
        }
        else{
            call = retrofitInterface.executeRecentQuizSimple("Bearer "+header, skip, limit);
        }
        call.enqueue(new Callback<List<QuizFeed>>() {
            @Override
            public void onResponse(Call<List<QuizFeed>> call, Response<List<QuizFeed>> response) {
                if(response.code() == 200){
                    try{

                        List<QuizFeed> quizzes = (List<QuizFeed>) response.body();//all the JSONObect quizzes are her
                        //building quiz list,
                        Log.d("top feed => ", "before "+quizzes.size());

                        callBack.showTopFeedQuizzes(quizzes);

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                        callBack.onFailure(e.getMessage());
                    }
                }
                else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                    callBack.onFailure(response.message());
                }

            }

            @Override
            public void onFailure(Call<List<QuizFeed>> call, Throwable t) {
                Log.d("failure ",t.toString());
                callBack.onFailure(t.toString());
            }
        });

    }



    public void getQuestionsForAQuiz(String header, String quizId, String pwd,
                                     @Nullable final QuestionsForQuizTask callBack){

        Call<QuestionPaper> call;

        if(pwd.equalsIgnoreCase("NO_PASSWORD")) {
            call = retrofitInterface.executeQuizQuestionPublic("Bearer "+header, quizId);

        }
        else{
            call = retrofitInterface.executeQuizQuestionPrivate("Bearer "+header, quizId, pwd);
        }

        call.enqueue(new Callback<QuestionPaper>() {
            @Override
            public void onResponse(Call<QuestionPaper> call, Response<QuestionPaper> response) {
                if(response.code() == 200){
                    try{
                        QuestionPaper questionPaper = (QuestionPaper) response.body();//the posted quiz is returned from the server
                        callBack.getQuestionsForQuiz(questionPaper);


                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 400){
                    Log.d("response ","code => sth unknown went wrong "+response.code());
                }
                else if(response.code() == 401){
                    Log.d("response ","code => incorrect password "+response.code());
                }
                else if(response.code() == 404){
                    Log.d("response ","code => the id was not found "+response.code());
                }
            }

            @Override
            public void onFailure(Call<QuestionPaper> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }


//    public void getAnswerScript(String header, String _id, HashMap<String, List<Submission>> hashMap,
//                                @Nullable final GetAnswerScriptTask callBack){
//
//        Call<AnswerResponse> call = retrofitInterface.executeSubmitAnswer("Bearer "+header, _id,
//                hashMap);
//
//
//        call.enqueue(new Callback<AnswerResponse>() {
//            @Override
//            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
//                if(response.code() == 201){
//                    try{
//                        AnswerResponse answerResponse = (AnswerResponse) response.body();//the posted quiz is returned from the server
//                        callBack.getAnswerScript(answerResponse);
//
//
////                            showDataTextView.setText(quizAnswerResponse.getResponses().get(0).getMessage());//just showing the first response
//
//
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 500){
//                    Log.d("response ","code => sth unknown went wrong "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AnswerResponse> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }
//
//
//    public void postRating(String header, String _id, HashMap<String, Double> hashMap,
//                           @Nullable final PostReviewTask callBack){
//
//        Call<AnswerReview> call = retrofitInterface.executePostRating("Bearer "+header, _id,
//                hashMap);
//
//
//        call.enqueue(new Callback<AnswerReview>() {
//            @Override
//            public void onResponse(Call<AnswerReview> call, Response<AnswerReview> response) {
//                if(response.code() == 200){
//                    try{
//                        AnswerReview answerReview = (AnswerReview) response.body();//the posted quiz is returned from the server
//                        callBack.postRating(answerReview);
//
//
////                            showDataTextView.setText(quizAnswerReview.getResponses().get(0).getMessage());//just showing the first response
//
//
//                    }catch (Exception e){
//                        Log.d("exception ",e.getMessage());
//                    }
//                }
//                else if(response.code() == 400){
//                    Log.d("response ","code => bad request "+response.code());
//                }
//                else if(response.code() == 404){
//                    Log.d("response ","code => submission id not found "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AnswerReview> call, Throwable t) {
//                Log.d("failure ",t.toString());
//            }
//        });
//
//    }


}
