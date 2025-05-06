package ro.tuiasi.ac.ProiectPIP;

import okhttp3.*;
import com.google.gson.*;
import java.util.*;



public class DeepSeekChat {

    private OkHttpClient client;
    private Gson gson;
    private String apiKey;

    public DeepSeekChat(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public String sendMessage(List<Map<String, String>> messages) throws Exception {
        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "deepseek-chat");
        requestBodyMap.put("messages", messages);
        requestBodyMap.put("stream", false);

        String requestBody = gson.toJson(requestBodyMap);

        Request request = new Request.Builder()
        		.url("https://api.deepseek.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                return json.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
            } else {
                throw new RuntimeException("Eroare: " + response.code() + " - " + response.message());
            }
        }
    }
}



