
/**
 * Clasa de teste unitare pentru {@link GoogleTTS}.
 * Testeaza functionalitatile metodei {@code cleanText} si comportamentul metodei {@code speak}.
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see GoogleTTS
 */
package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleTTSTest {

    /**
     * Testeaza daca metoda {@code cleanText} elimina marcajul de text ingrosat (**).
     *
     * @see GoogleTTS#cleanText(String)
     */
    @Test
    public void testCleanTextRemovesBoldMarkdown() {
        GoogleTTS tts = new GoogleTTS();
        String input = "Acesta este **important**!";
        String expected = "Acesta este important!";
        String result = tts.cleanText(input);
        assertEquals(expected, result);
    }

    /**
     * Testeaza eliminarea unor tag-uri personalizate, cum ar fi ###Intro###.
     *
     * @see GoogleTTS#cleanText(String)
     */
    @Test
    public void testCleanTextRemovesCustomTags() {
        GoogleTTS tts = new GoogleTTS();
        String input = "###Intro### Acesta este textul.";
        String expected = "Acesta este textul."; 
        String result = tts.cleanText(input);
        assertEquals(expected, result);
    }

    /**
     * Testeaza comportamentul metodei {@code cleanText} cand input-ul este {@code null}.
     *
     * @see GoogleTTS#cleanText(String)
     */
    @Test
    public void testCleanTextHandlesNull() {
        GoogleTTS tts = new GoogleTTS();
        String result = tts.cleanText(null);
        assertEquals("", result);
    }

    /**
     * Verifica daca un text simplu fara formatare ramane neschimbat.
     *
     * @see GoogleTTS#cleanText(String)
     */
    @Test
    public void testCleanTextReturnsUnchangedIfNoFormatting() {
        GoogleTTS tts = new GoogleTTS();
        String input = "Text simplu";
        String result = tts.cleanText(input);
        assertEquals("Text simplu", result);
    }

    /**
     * Testeaza daca metoda {@code speak} nu arunca exceptii la apel.
     * Atentie: acest test NU verifica efectiv redarea audio, ci doar absenta exceptiilor.
     *
     * @see GoogleTTS#speak(String)
     * @throws Exception in cazul in care implementarea metodei ar arunca o exceptie
     */
    @Test
    public void testSpeakDoesNotThrowException() {
        GoogleTTS tts = new GoogleTTS();
        assertDoesNotThrow(() -> tts.speak("Salut! Cum pot sa te ajut?"));
    }
}
