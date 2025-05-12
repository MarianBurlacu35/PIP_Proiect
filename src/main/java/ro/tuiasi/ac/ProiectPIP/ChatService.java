
package ro.tuiasi.ac.ProiectPIP;

import com.google.gson.*;
import okhttp3.*;

import java.util.*;

/**
 * {@code ChatService} gestioneaza comunicarea cu API-ul DeepSeek Chat.
 * Pastreaza istoricul conversatiei si trimite mesaje sub forma de JSON,
 * apoi preia si returneaza raspunsul AI.
 *
 * <p>
 * Foloseste {@link OkHttpClient} pentru trimiterea cererilor HTTP
 * si {@link Gson} pentru serializarea/deserializarea obiectelor.
 * </p>
 *
 * <p><b>Exemplu de utilizare:</b></p>
 * <pre>{@code
 * ChatService chat = new ChatService(apiKey);
 * String response = chat.sendMessage("Salut!");
 * }</pre>
 *
 * @author Rares-Daniel Constantin
 * @version 10.05.2025
 * @see ro.tuiasi.ac.ProiectPIP.ApiClient
 * @see ro.tuiasi.ac.ProiectPIP.Message
 */
public class ChatService {
    private final String apiKey;
    private final OkHttpClient client;
    private final Gson gson;
    private final List<Message> messages;

    /**
     * Creeaza o instanta noua de {@code ChatService}.
     *
     * @param apiKey cheia de autorizare pentru API-ul DeepSeek
     */
    public ChatService(String apiKey) {
        this.apiKey = apiKey;
        this.client = ApiClient.getClient();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.messages = new ArrayList<>();
        messages.add(new Message("system", "You are a helpful assistant."));
    }

    /**
     * Trimite un mesaj catre API si returneaza raspunsul AI.
     * Mesajul utilizatorului si raspunsul asistentului sunt adaugate la istoricul conversatiei.
     *
     * @param userInput mesajul introdus de utilizator
     * @return raspunsul AI sub forma de text
     *
     * @throws IllegalStateException daca formatul raspunsului JSON este invalid
     * @throws RuntimeException daca cererea HTTP esueaza sau corpul raspunsului este null
     *
     * @see #getMessageHistory()
     */
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

    /**
     * Returneaza istoricul complet al conversatiei (utilizator + asistent).
     *
     * @return lista de mesaje trimise si primite
     */
    public List<Message> getMessageHistory() {
        return messages;
    }

    /**
     * Returneaza URL-ul API-ului catre care sunt trimise cererile.
     * Poate fi suprascrisa in teste pentru a folosi un server local (mock).
     *
     * @return URL-ul endpoint-ului de chat completare
     *
     * @see okhttp3.Request.Builder#url(String)
     */
    protected String getApiUrl() {
        return "https://api.deepseek.com/chat/completions";
    }
}
