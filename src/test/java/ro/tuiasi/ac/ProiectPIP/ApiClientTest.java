package ro.tuiasi.ac.ProiectPIP;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ApiClientTest {

    @Test
    public void testClientIsNotNull() {
        OkHttpClient client = ApiClient.getClient();
        assertNotNull(client, "Clientul OkHttp nu trebuie sã fie null");
    }

    @Test
    public void testTimeoutsAreSetTo60Seconds() {
        OkHttpClient client = ApiClient.getClient();

        assertEquals(60_000, client.connectTimeoutMillis(), "Timeoutul de conexiune nu este 60 secunde");
        assertEquals(60_000, client.readTimeoutMillis(), "Timeoutul de citire nu este 60 secunde");
        assertEquals(60_000, client.writeTimeoutMillis(), "Timeoutul de scriere nu este 60 secunde");
    }

    @Test
    public void testClientIsInstanceOfOkHttpClient() {
        OkHttpClient client = ApiClient.getClient();
        assertTrue(client instanceof OkHttpClient, "Obiectul returnat nu este un OkHttpClient");
    }
}
