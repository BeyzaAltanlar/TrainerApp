package com.example.trainerapp;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;


public class PerformActiviytReport {



    public static String performAction(SurveyResponse response) throws JSONException, ExecutionException, InterruptedException {
        final Map<String, Integer> processedData = processSurveyResponse(response);

        Gson gson = new Gson();
        String gsonString = gson.toJson(processedData);

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        JSONObject jsonObject = new JSONObject(gsonString);

        JSONObject newSampleData = new JSONObject();

        JSONArray keys = jsonObject.names();

        if (keys != null) {
            for (int i = 0; i < keys.length(); i++) {
                String key = keys.getString(i);

                JSONArray valueArray = new JSONArray();
                valueArray.put(jsonObject.getInt(key));

                newSampleData.put(key, valueArray);
            }
        }

        String transformedJsonData = newSampleData.toString();

        String[] keyOrder = {
                "weight", "height", "primary_goal_Healthy Habits", "primary_goal_Weight Loss",
                "body_type_Obese", "body_type_Slim", "body_type_Stocky",
                "past_injury_Chest region", "past_injury_Foot region", "past_injury_Head region",
                "past_injury_Leg region", "body_goal_Drop Sizes", "body_goal_Muscular",
                "body_goal_Toned", "fitness_experience_Gain Easily Struggle to Lose",
                "fitness_experience_Struggle to Build Muscle", "last_ideal_weight_time_Less than 1 year",
                "last_ideal_weight_time_More than 3 years", "last_ideal_weight_time_Never",
                "work_schedule_Flexible", "work_schedule_Night Shift", "work_schedule_Retired",
                "typical_day_Sitting Most of Day", "typical_day_Standing All Day"
        };

        JSONObject unorderedJson = new JSONObject(transformedJsonData);

        JSONObject orderedJson = new JSONObject();
        for (String key : keyOrder) {
            if (unorderedJson.has(key)) {
                orderedJson.put(key, unorderedJson.getJSONArray(key));
            }
        }
      String orderedJsonData = orderedJson.toString();


        RequestBody body = RequestBody.create(orderedJsonData, JSON);
        Request request = new Request.Builder()
                .url("https://trainerappp-5ad4d97b3c19.herokuapp.com/")
                .post(body)
                .build();

        String responseData = null;


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Response response = client.newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                return response.body().string();
            }
        });

        return future.get();

    }

//    public void connectFirebase(){
//        try{
//            FileInputStream serviceAccount =
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setProjectId("trainer-beyza-irmak")
//                    .build();
//
//            FirebaseApp.initializeApp(this, options);
//        }catch (Exception e){
//        }
//
//    }

    public static Map<String, Integer> processSurveyResponse(SurveyResponse response) {
        Map<String, Integer> processedData = new HashMap<>();

        // Process weight and height
        processedData.put("weight", response.getWeight());
        processedData.put("height", response.getHeight());

        // Process primary goals
        String primaryGoal = response.getPrimaryGoal();
        processedData.put("primary_goal_Healthy Habits", primaryGoal.equals("Forma girmek") || primaryGoal.equals("Sağlıklı alışkanlıklar edinmek") ? 1 : 0);
        processedData.put("primary_goal_Weight Loss", primaryGoal.equals("Kilo vermek") ? 1 : 0);

        // Process physical build
        String physicalBuild = response.getPhysicalBuild();
        processedData.put("body_type_Obese", physicalBuild.equals("Obez") ? 1 : 0);
        processedData.put("body_type_Slim", physicalBuild.equals("İnce") || physicalBuild.equals("Tıknaz")  ? 1 : 0);
        processedData.put("body_type_Stocky", physicalBuild.equals("Orta yapılı") ? 1 : 0);

        // Process past injuries
        String pastInjuries = response.getPastInjuries();
        processedData.put("past_injury_Chest region", pastInjuries.equals("Göğüs bölgesi") || pastInjuries.equals("Kol bölgesi") ? 1 : 0);
        processedData.put("past_injury_Foot region", pastInjuries.equals("Ayak Bölgesi") ? 1 : 0);
        processedData.put("past_injury_Head region", pastInjuries.equals("Kafa bölgesi") ? 1 : 0);
        processedData.put("past_injury_Leg region", pastInjuries.equals("Bacak Bölgesi") ? 1 : 0);

        // Process body goal
        String bodyGoal = response.getBodyGoal();
        processedData.put("body_goal_Drop Sizes", bodyGoal.equals("Birkaç beden küçülmek") ? 1 : 0);
        processedData.put("body_goal_Muscular", bodyGoal.equals("Kaslı") ? 1 : 0);
        processedData.put("body_goal_Toned", bodyGoal.equals("Parçalı kaslar") || bodyGoal.equals("Atletik") ? 1 : 0);


        // Process fitness experience
        String fitnessExperience = response.getFitnessExperience();
        processedData.put("fitness_experience_Gain Easily Struggle to Lose", fitnessExperience.equals("Kolay kilo alıyorum ama vermekte zorlanıyorum") ? 1 : 0);
        processedData.put("fitness_experience_Struggle to Build Muscle", fitnessExperience.equals("Kas geliştirmekte veya vücut yağı kazanımında sıkıntı çekiyorum")  || fitnessExperience.equals("Çaba sarf etmeden kilo alıp veriyorum")? 1 : 0);

        // Process last ideal weight time
        String lastIdealWeight = response.getLastIdealWeight();
        processedData.put("last_ideal_weight_time_Less than 1 year", lastIdealWeight.equals("1 yıldan az") ? 1 : 0);
        processedData.put("last_ideal_weight_time_More than 3 years", lastIdealWeight.equals("1-2 yıl") || lastIdealWeight.equals("3 yıldan fazla")? 1 : 0);
        processedData.put("last_ideal_weight_time_Never", lastIdealWeight.equals("Hiçbir zaman") ? 1 : 0);

        // Process work schedule
        String workSchedule = response.getWorkSchedule();
        processedData.put("work_schedule_Flexible", workSchedule.equals("Çalışma saatlerim esnek") || workSchedule.equals("9 - 5 arası") ? 1 : 0);
        processedData.put("work_schedule_Night Shift", workSchedule.equals("Gece mesaisi") ? 1 : 0);
        processedData.put("work_schedule_Retired", workSchedule.equals("Emekliyim") ? 1 : 0);

        // Process typical day
        String typicalDay = response.getTypicalDay();
        processedData.put("typical_day_Sitting Most of Day", typicalDay.equals("Günün çoğunu oturarak geçiriyorum") || typicalDay.equals("Aktif olarak mola veriyorum") ? 1 : 0);
        processedData.put("typical_day_Standing All Day", typicalDay.equals("Tüm gün ayaktayım") ? 1 : 0);

        return processedData;
    }


    public static void fetchDataFromFirestore(FirestoreDataCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("program-fittness").document("fitness-programs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Map<String, Object> data = document.getData();
                                callback.onDataReceived(data); // Adjust callback to handle Map instead of List
                            } else {
                                  }
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });

    }

    public static void fetchDataFromFirestoreFitness(FirestoreDataCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("fitness-program-weekly").document("fitness-program")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                Map<String, Object> data = document.getData();
                                callback.onDataReceived(data); // Adjust callback to handle Map instead of List
                            } else {
                             }
                        } else {
                            callback.onError(task.getException());
                        }
                    }
                });

    }

//    public static String sendDataFromFineTuning(String request){
//
//    }

    public static String sendDataToFineTuning(String request){
        String apiKey = "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"; // Replace with your API key
        String engine = "ft:gpt-3.5-turbo-0125:personal::9BgTBBSS"; // Replace with your fine-tuned model name if you have one

        try {
            URL url = new URL("https://api.openai.com/v1/engines/" + engine + "/completions");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setDoOutput(true);

            String jsonInputString = "{\"prompt\": \"" + request + "\", \"max_tokens\": 150}";

            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }



//    public static void getThePrograms() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("program-fitness").document("fitness-programs")
//                .collection("fitness-programs") // if 'fitness-programs' is a sub-collection
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                // Here you have each program as a Map and you can process it as needed
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//
//    }
}




