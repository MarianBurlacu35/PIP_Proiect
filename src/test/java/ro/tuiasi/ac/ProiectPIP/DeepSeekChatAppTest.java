package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeepSeekChatAppTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream consoleOutput;

    @BeforeEach
    public void setupConsoleCapture() {
        consoleOutput = new ByteArrayOutputStream();
        PrintStream combinedStream = new PrintStream(consoleOutput);
        System.setOut(combinedStream);
        System.setErr(combinedStream); 
    }

    @AfterEach
    public void restoreConsole() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // === cleanTextForSpeech() ===

    @Test
    public void testCleanTextHandlesNull() {
        assertEquals("", DeepSeekChatApp.cleanTextForSpeech(null));
    }

    @Test
    public void testCleanTextHandlesHeaders() {
        String input = "# Titlu\n## Subtitlu";
        String expected = "Titlu\nSubtitlu";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    @Test
    public void testCleanTextHandlesBoldText() {
        String input = "Acesta este **important**!";
        String expected = "Acesta este important!";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    @Test
    public void testCleanTextTrimsWhitespace() {
        String input = "   Text   ";
        assertEquals("Text", DeepSeekChatApp.cleanTextForSpeech(input));
    }

    @Test
    public void testCleanTextHandlesMultipleLines() {
        String input = "  ## Titlu  \n**Bold**  ";
        String expected = "Titlu\nBold";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    // === initChatService() ===

    @Test
    public void testInitChatServiceReturnsInstance() {
        ChatService chatService = DeepSeekChatApp.initChatService("fake-key");
        assertNotNull(chatService);
        assertTrue(chatService instanceof ChatService);
    }

    // === runInteractionLoop(): input text ===

    @Test
    public void testRunInteractionLoopWithTextInput() throws Exception {
        String input = "1\nSalut\nexit\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ChatService chatMock = mock(ChatService.class);
        when(chatMock.sendMessage("Salut")).thenReturn("Salutare!");

        GoogleTTS ttsMock = mock(GoogleTTS.class);
        SpeakToText sttMock = mock(SpeakToText.class);

        DeepSeekChatApp.runInteractionLoop(scanner, chatMock, ttsMock, sttMock);

        String output = consoleOutput.toString();
        assertTrue(output.contains("AI: Salutare!"));
        verify(ttsMock).speak("Salutare!");
    }

    // === runInteractionLoop(): input voice ===

    @Test
    public void testRunInteractionLoopWithVoiceInput() throws Exception {
        String input = "2\nexit\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ChatService chatMock = mock(ChatService.class);
        when(chatMock.sendMessage("intrebare vocala")).thenReturn("Raspuns vocal");

        GoogleTTS ttsMock = mock(GoogleTTS.class);
        SpeakToText sttMock = mock(SpeakToText.class);
        when(sttMock.listen()).thenReturn("intrebare vocala").thenReturn("exit");

        DeepSeekChatApp.runInteractionLoop(scanner, chatMock, ttsMock, sttMock);

        String output = consoleOutput.toString();
        assertTrue(output.contains("AI: Raspuns vocal"));
        verify(ttsMock).speak("Raspuns vocal");
    }

    // === runInteractionLoop(): TTS throws exception ===

    @Test
    public void testRunInteractionLoopHandlesTtsException() throws Exception {
        String input = "1\nSalut\nexit\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        ChatService chatMock = mock(ChatService.class);
        when(chatMock.sendMessage("Salut")).thenReturn("Salut!");

        GoogleTTS ttsMock = mock(GoogleTTS.class);
        doThrow(new RuntimeException("TTS error")).when(ttsMock).speak(anyString());

        SpeakToText sttMock = mock(SpeakToText.class);

        DeepSeekChatApp.runInteractionLoop(scanner, chatMock, ttsMock, sttMock);

        String output = consoleOutput.toString();

        
        if (!output.contains("Eroare la redarea vocii: TTS error")) {
            System.out.println("---- OUTPUT COMPLET ----");
            System.out.println(output);
            fail("Mesajul de eroare TTS nu a fost detectat.");
        }

   
        verify(ttsMock).speak(anyString());
    }
}

