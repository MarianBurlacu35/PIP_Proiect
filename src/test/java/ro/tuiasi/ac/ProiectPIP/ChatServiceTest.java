
/**
 * Clasa de teste unitare pentru {@link ChatService}, care verifica raspunsurile
 * primite de la un server HTTP simulat folosind {@code MockWebServer}.
 * Aceste teste acopera raspunsuri valide, coduri de eroare si exceptii.
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see ChatService
 */
package ro.tuiasi.ac.ProiectPIP;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ChatServiceTest {

    private MockWebServer mockServer;

    /**
     * Initializeaza serverul HTTP simulat inainte de fiecare test.
     *
     * @throws IOException daca serverul nu poate fi pornit
     */
    @BeforeEach
    public void setup() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    /**
     * Opreste serverul HTTP simulat dupa fiecare test.
     *
     * @throws IOException daca serverul nu poate fi oprit
     */
    @AfterEach
    public void tearDown() throws IOException {
        mockServer.shutdown();
    }

    /**
     * Subclasa de test care permite specificarea unei adrese URL personalizate pentru API.
     *
     * @see ChatService
     */
    public static class TestableChatService extends ChatService {
        private final String url;

        /**
         * Constructor care seteaza cheia API si URL-ul de baza.
         *
         * @param apiKey cheia API folosita la autentificare
         * @param baseUrl adresa URL personalizata pentru testare
         */
        public TestableChatService(String apiKey, String baseUrl) {
            super(apiKey);
            this.url = baseUrl;
        }

        /**
         * Suprascrie metoda pentru a returna URL-ul personalizat.
         *
         * @return URL-ul catre care se trimite cererea
         */
        @Override
        protected String getApiUrl() {
            return url;
        }
    }

    /**
     * Testeaza daca metoda {@link ChatService#sendMessage(String)} returneaza un raspuns valid
     * cand serverul trimite un JSON corespunzator.
     *
     * @see ChatService#sendMessage(String)
     */
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

    /**
     * Testeaza daca metoda {@link ChatService#sendMessage(String)} gestioneaza corect un cod de eroare HTTP (ex. 401).
     *
     * @see ChatService#sendMessage(String)
     */
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

    /**
     * Testeaza daca metoda {@link ChatService#sendMessage(String)} gestioneaza exceptiile, cum ar fi cand serverul e oprit.
     *
     * @throws IOException daca serverul nu poate fi inchis
     * @see ChatService#sendMessage(String)
     */
    @Test
    public void testSendMessageHandlesException() throws IOException {
        mockServer.shutdown(); // Simulam o exceptie

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
