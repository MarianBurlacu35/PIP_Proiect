package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeepSeekChatAppTest {

    @Test
    public void testNullInputReturnsEmptyString() {
        String result = DeepSeekChatApp.cleanTextForSpeech(null);
        assertEquals("", result, "Inputul null ar trebui sa returneze sirul gol.");
    }

    @Test
    public void testEmptyStringReturnsEmptyString() {
        String result = DeepSeekChatApp.cleanTextForSpeech("");
        assertEquals("", result, "Inputul gol ar trebui sa ramana gol.");
    }

    @Test
    public void testRemovesMarkdownHeaders() {
        String input = "# Titlu\n## Subtitlu\nText";
        String expected = "Titlu\nSubtitlu\nText";
        String result = DeepSeekChatApp.cleanTextForSpeech(input);
        assertEquals(expected, result, "Titlurile Markdown nu au fost eliminate corect.");
    }

    @Test
    public void testRemovesBoldMarkdown() {
        String input = "Acesta este **important**.";
        String expected = "Acesta este important.";
        String result = DeepSeekChatApp.cleanTextForSpeech(input);
        assertEquals(expected, result, "Textul bold Markdown nu a fost eliminat corect.");
    }

    @Test
    public void testTrimsWhitespace() {
        String input = "   Text cu spatii   ";
        String expected = "Text cu spatii";
        String result = DeepSeekChatApp.cleanTextForSpeech(input);
        assertEquals(expected, result, "Spatiile in exces nu au fost eliminate corect.");
    }

    @Test
    public void testCombinedMarkdownAndWhitespace() {
        String input = "   ## Titlu   \n**Bold** text normal  ";
        String expected = "Titlu\nBold text normal";
        String result = DeepSeekChatApp.cleanTextForSpeech(input);
        assertEquals(expected, result, "Curatarea combinata de Markdown si spatii a esuat.");
    }

    @Test
    public void testNoMarkdownNoTrimReturnsSameText() {
        String input = "Text simplu fara Markdown.";
        String result = DeepSeekChatApp.cleanTextForSpeech(input);
        assertEquals(input, result, "Textul fara modificari ar trebui sa ramana neschimbat.");
    }
}
