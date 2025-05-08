/*
package ro.tuiasi.ac.ProiectPIP;

import com.google.gson.*;
import okhttp3.*;


import java.util.*;

public class ChatService {
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;
    private final List<Message> messages;

    public ChatService(String apiKey) {
        this.apiKey = apiKey;
        this.client = ApiClient.getClient();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.messages = new ArrayList<>();
        messages.add(new Message("system", "You are a helpful assistant."));
    }

    public String sendMessage(String userInput) {
        messages.add(new Message("user", userInput));

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "deepseek-chat");
        payload.put("messages", messages);
        payload.put("stream", false);

        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(gson.toJson(payload), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String aiResponse = json.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
                messages.add(new Message("assistant", aiResponse));
                return aiResponse;
            } else {
                return "Eroare API: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            return "Eroare în trimiterea cererii: " + e.getMessage();
        }
    }
}
*/


package ro.tuiasi.ac.ProiectPIP;

import com.google.gson.*;
import okhttp3.*;

import java.util.*;

public class ChatService {
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;
    private final List<Message> messages;

    public ChatService(String apiKey) {
        this.apiKey = apiKey;
        this.client = ApiClient.getClient();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.messages = new ArrayList<>();
        messages.add(new Message("system", "You are a helpful assistant."));
    }

    public String sendMessage(String userInput) {
        messages.add(new Message("user", userInput));

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "deepseek-chat");
        payload.put("messages", messages);
        payload.put("stream", false);

        Request request = new Request.Builder()
                .url(getApiUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(gson.toJson(payload), MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String aiResponse = json.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("message")
                        .get("content").getAsString();
                messages.add(new Message("assistant", aiResponse));
                return aiResponse;
            } else {
                return "Eroare API: " + response.code() + " - " + response.message();
            }
        } catch (Exception e) {
            return "Eroare in trimiterea cererii: " + e.getMessage();
        }
    }

    public List<Message> getMessageHistory() {
        return messages;
    }

    protected String getApiUrl() {
        return "https://api.deepseek.com/chat/completions";
    }
}
