
/**
 * Clasa de teste unitare pentru {@link SpeakToText}.
 * Verifica comportamentul metodelor de inregistrare si transcriere audio in conditii controlate.
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see SpeakToText
 */
package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpeakToTextTest {

    /**
     * Testeaza metoda {@code listen} folosind o inregistrare audio simulata (mock).
     * Verifica daca metoda returneaza un rezultat non-null.
     *
     * @see SpeakToText#listen()
     * @see SpeakToText#recordAudioFromMicrophone(int)
     */
    @Test
    public void testListenWithMockedMicrophone() {
        SpeakToText speakToText = new SpeakToText() {
            @Override
            protected byte[] recordAudioFromMicrophone(int durationMillis) {
                // Simuleaza o inregistrare de 1 secunda (32000 bytes la 16kHz, mono, 16-bit)
                return new byte[32000];
            }
        };

        String result = speakToText.listen();
        assertNotNull(result, "Transcrierea nu trebuie sa fie null (chiar daca este goala)");
    }

    /**
     * Testeaza metoda {@code transcribeAudio} pe o inregistrare de liniste (byte array gol).
     * Verifica daca rezultatul este null sau gol, dupa caz.
     *
     * @see SpeakToText#transcribeAudio(byte[])
     */
    @Test
    public void testTranscribeSilentAudio() {
        SpeakToText stt = new SpeakToText();
        byte[] silentAudio = new byte[32000]; // Simuleaza audio fara sunet

        String result = stt.transcribeAudio(silentAudio);
        assertNotNull(result);
        assertTrue(result.isEmpty() || result.isBlank(), "Transcrierea ar trebui sa fie goala pentru liniste.");
    }
}
