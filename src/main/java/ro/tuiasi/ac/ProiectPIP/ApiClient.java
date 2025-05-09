/*

package ro.tuiasi.ac.ProiectPIP;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ApiClient {
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
*/
package ro.tuiasi.ac.ProiectPIP;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * {@code ApiClient} furnizeaza o instanta configurata de {@link OkHttpClient}
 * care este utilizata pentru trimiterea de cereri HTTP catre serverul DeepSeek.
 *
 * <p>
 * Instanta de client returnata este configurata cu timeout-uri pentru
 * conexiune, citire si scriere, fiecare setat la 60 de secunde.
 * </p>
 *
 * <p><b>Exemplu de utilizare:</b></p>
 * <pre>{@code
 * OkHttpClient client = ApiClient.getClient();
 * }</pre>
 *
 * @author Marian-Cosmin Burlacu
 * @version 09.05.2025
 * @see okhttp3.OkHttpClient
 * @see okhttp3.OkHttpClient.Builder
 * @see java.util.concurrent.TimeUnit
 */
public class ApiClient {

    /**
     * Creeaza si returneaza o instanta de {@link OkHttpClient} configurata cu urmatoarele timeout-uri:
     * <ul>
     *   <li><b>connectTimeout</b>: 60 secunde</li>
     *   <li><b>readTimeout</b>: 60 secunde</li>
     *   <li><b>writeTimeout</b>: 60 secunde</li>
     * </ul>
     *
     * <p>Acest client poate fi reutilizat in intreaga aplicatie pentru a trimite cereri HTTP.</p>
     *
     * @return instanta configurata de {@link OkHttpClient}
     * @throws RuntimeException in cazuri exceptionale (desi improbabile) daca configurarea clientului esueaza
     *
     * @see okhttp3.OkHttpClient
     * @see okhttp3.OkHttpClient.Builder#connectTimeout(long, TimeUnit)
     * @see okhttp3.OkHttpClient.Builder#readTimeout(long, TimeUnit)
     * @see okhttp3.OkHttpClient.Builder#writeTimeout(long, TimeUnit)
     */
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * @deprecated Aceasta metoda returneaza un client neconfigurat si nu trebuie folosita in productie.
     * Foloseste {@link #getClient()} in schimb.
     *
     * @return instanta neconfigurata de {@link OkHttpClient}
     */
    @Deprecated
    public static OkHttpClient oldClient() {
        return new OkHttpClient();
    }
}

