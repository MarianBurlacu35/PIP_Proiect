
/**
 * Clasa de teste unitare pentru {@link DeepSeekChatApp}.
 * Verifica corectitudinea functionalitatilor de curatare a textului,
 * initializare a serviciului de chat si bucla de interactiune cu utilizatorul.
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see DeepSeekChatApp
 */
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

    /**
     * Redirectioneaza iesirea in consola pentru capturarea rezultatelor testelor.
     */
    @BeforeEach
    public void setupConsoleCapture() {
        consoleOutput = new ByteArrayOutputStream();
        PrintStream combinedStream = new PrintStream(consoleOutput);
        System.setOut(combinedStream);
        System.setErr(combinedStream);
    }

    /**
     * Restaureaza iesirea standard in consola dupa fiecare test.
     */
    @AfterEach
    public void restoreConsole() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // ============================
    // === cleanTextForSpeech() ===
    // ============================

    /**
     * Testeaza daca metoda {@code cleanTextForSpeech} returneaza sirul gol cand primeste {@code null}.
     *
     * @see DeepSeekChatApp#cleanTextForSpeech(String)
     */
    @Test
    public void testCleanTextHandlesNull() {
        assertEquals("", DeepSeekChatApp.cleanTextForSpeech(null));
    }

    /**
     * Testeaza eliminarea anteturilor markdown (#, ##) din text.
     */
    @Test
    public void testCleanTextHandlesHeaders() {
        String input = "# Titlu\n## Subtitlu";
        String expected = "Titlu\nSubtitlu";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    /**
     * Testeaza eliminarea formatarii **bold** din text.
     */
    @Test
    public void testCleanTextHandlesBoldText() {
        String input = "Acesta este **important**!";
        String expected = "Acesta este important!";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    /**
     * Testeaza eliminarea spatiilor albe de la inceput si sfarsit.
     */
    @Test
    public void testCleanTextTrimsWhitespace() {
        String input = "   Text   ";
        assertEquals("Text", DeepSeekChatApp.cleanTextForSpeech(input));
    }

    /**
     * Testeaza curatarea textului pe mai multe linii cu markdown.
     */
    @Test
    public void testCleanTextHandlesMultipleLines() {
        String input = "  ## Titlu  \n**Bold**  ";
        String expected = "Titlu\nBold";
        assertEquals(expected, DeepSeekChatApp.cleanTextForSpeech(input));
    }

    // ============================
    // === initChatService() ===
    // ============================

    /**
     * Testeaza daca metoda {@code initChatService} returneaza o instanta valida.
     *
     * @see DeepSeekChatApp#initChatService(String)
     * @return o instanta de tip {@link ChatService}
     */
    @Test
    public void testInitChatServiceReturnsInstance() {
        ChatService chatService = DeepSeekChatApp.initChatService("fake-key");
        assertNotNull(chatService);
        assertTrue(chatService instanceof ChatService);
    }

    // =======================================
    // === runInteractionLoop(): text input ===
    // =======================================

    /**
     * Testeaza bucla de interactiune pentru cazul in care utilizatorul alege input text.
     *
     * @throws Exception in caz de eroare la simularea interactiunii
     * @see DeepSeekChatApp#runInteractionLoop(Scanner, ChatService, GoogleTTS, SpeakToText)
     */
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

    // =======================================
    // === runInteractionLoop(): voice input ===
    // =======================================

    /**
     * Testeaza bucla de interactiune pentru cazul in care utilizatorul alege input vocal.
     *
     * @throws Exception daca interactiunea esueaza
     */
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

    // ================================================
    // === runInteractionLoop(): TTS throws exception ===
    // ================================================

    /**
     * Testeaza daca aplicatia gestioneaza exceptiile aruncate de TTS.
     *
     * @throws Exception daca apar probleme la executia testului
     * @see GoogleTTS#speak(String)
     */
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
