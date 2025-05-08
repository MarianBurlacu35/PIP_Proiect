package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoogleTTSTest {

    @Test
    public void testCleanTextRemovesBoldMarkdown() {
        GoogleTTS tts = new GoogleTTS();
        String input = "Acesta este **important**!";
        String expected = "Acesta este important!";
        String result = tts.cleanText(input);
        assertEquals(expected, result);
    }

    @Test
    public void testCleanTextRemovesCustomTags() {
        GoogleTTS tts = new GoogleTTS();
        String input = "###Intro### Acesta este textul.";
        String expected = "Acesta este textul."; 
        String result = tts.cleanText(input);
        assertEquals(expected, result);
    }


    @Test
    public void testCleanTextHandlesNull() {
        GoogleTTS tts = new GoogleTTS();
        String result = tts.cleanText(null);
        assertEquals("", result);
    }

    @Test
    public void testCleanTextReturnsUnchangedIfNoFormatting() {
        GoogleTTS tts = new GoogleTTS();
        String input = "Text simplu";
        String result = tts.cleanText(input);
        assertEquals("Text simplu", result);
    }
        
    @Test
    public void testSpeakDoesNotThrowException() {
        GoogleTTS tts = new GoogleTTS();
        assertDoesNotThrow(() -> tts.speak("Salut! Cum pot sã te ajut?"));
    }

}
//de adaugat JavaDOC la toate