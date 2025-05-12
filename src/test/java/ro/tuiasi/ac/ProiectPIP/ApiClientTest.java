
package ro.tuiasi.ac.ProiectPIP;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clasa de test pentru clientul API, care verifica comportamentele si setarile clientului OkHttp.
 * 
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 */
public class ApiClientTest {

    /**
     * Testeaza daca instanta clientului OkHttp returnata nu este null.
     * 
     * @see ApiClient#getClient()
     * @throws AssertionError daca clientul returnat este null.
     */
    @Test
    public void testClientIsNotNull() {
        OkHttpClient client = ApiClient.getClient();
        assertNotNull(client, "Clientul OkHttp nu trebuie sa fie null");
    }

    /**
     * Testeaza daca timeout-urile de conexiune, citire si scriere sunt setate la 60 de secunde.
     * 
     * @see ApiClient#getClient()
     * @throws AssertionError daca vreunul dintre timeout-uri nu este setat corect.
     */
    @Test
    public void testTimeoutsAreSetTo60Seconds() {
        OkHttpClient client = ApiClient.getClient();

        assertEquals(60_000, client.connectTimeoutMillis(), "Timeoutul de conexiune nu este 60 secunde");
        assertEquals(60_000, client.readTimeoutMillis(), "Timeoutul de citire nu este 60 secunde");
        assertEquals(60_000, client.writeTimeoutMillis(), "Timeoutul de scriere nu este 60 secunde");
    }

    /**
     * Testeaza daca obiectul returnat este instanta a clasei OkHttpClient.
     * 
     * @see ApiClient#getClient()
     * @return true daca clientul este instanta de OkHttpClient.
     * @throws AssertionError daca clientul nu este instanta de OkHttpClient.
     */
    @Test
    public void testClientIsInstanceOfOkHttpClient() {
        OkHttpClient client = ApiClient.getClient();
        assertTrue(client instanceof OkHttpClient, "Obiectul returnat nu este un OkHttpClient");
    }
}
