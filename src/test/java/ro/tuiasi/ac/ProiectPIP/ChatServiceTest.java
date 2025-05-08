package ro.tuiasi.ac.ProiectPIP;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServiceTest {

    private MockWebServer mockServer;

    @BeforeEach
    public void setup() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    
    public static class TestableChatService extends ChatService {
        private final String url;

        public TestableChatService(String apiKey, String baseUrl) {
            super(apiKey);
            this.url = baseUrl;
        }

        @Override
        protected String getApiUrl() {
            return url;
        }
    }

    @Test
    public void testSendMessageReturnsValidResponse() {
        
        String jsonResponse = """
            {
              "choices": [{
                "message": {
                  "content": "Salut! Cu ce te pot ajuta?"
                }
              }]
            }
        """;

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .addHeader("Content-Type", "application/json"));

        String baseUrl = mockServer.url("/chat/completions").toString();
        ChatService chatService = new ChatService("fake-key") {
            @Override
            protected String getApiUrl() {
                return baseUrl;
            }
        };

        String result = chatService.sendMessage("Buna!");
        assertEquals("Salut! Cu ce te pot ajuta?", result);
    }

    @Test
    public void testSendMessageHandlesErrorCode() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("Unauthorized"));

        ChatService chatService = new ChatService("fake-key") {
            @Override
            protected String getApiUrl() {
                return mockServer.url("/chat/completions").toString();
            }
        };

        String result = chatService.sendMessage("Test");
        assertTrue(result.contains("Eroare API: 401"));
    }

    @Test
    public void testSendMessageHandlesException() throws IOException {
        mockServer.shutdown(); 

        ChatService chatService = new ChatService("fake-key") {
            @Override
            protected String getApiUrl() {
                return mockServer.url("/chat/completions").toString();
            }
        };

        String result = chatService.sendMessage("Test");
        assertTrue(result.startsWith("Eroare in trimiterea cererii:"));
    }
}
