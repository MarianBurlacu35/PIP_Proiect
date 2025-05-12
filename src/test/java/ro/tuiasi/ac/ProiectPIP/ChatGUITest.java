
/**
 * Clasa de teste unitare pentru {@link ChatGUI}.
 * Testele verifica comportamentul interfetei grafice pentru chat
 * folosind mock-uri pentru serviciile externe: ChatService, GoogleTTS si SpeakToText.
 *
 * @author Marian-Cosmin Burlacu 
 * @version 12.05.2025
 * @see ChatGUI
 */
package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChatGUITest {

    private ChatService chatMock;
    private GoogleTTS ttsMock;
    private SpeakToText sttMock;
    private ChatGUI chatGUI;

    /**
     * Subclasa folosita pentru testare care simuleaza introducerea textului de la utilizator.
     *
     * @see ChatGUI
     */
    private static class ChatGUIForTest extends ChatGUI {
        private static final long serialVersionUID = 1L;
        private final String[] mockInputs = {"Ce este AI?", "exit"};
        private int inputIndex = 0;

        /**
         * Constructor pentru clasa de test.
         *
         * @param chatService instanta de ChatService simulata
         * @param tts instanta de GoogleTTS simulata
         * @param stt instanta de SpeakToText simulata
         */
        public ChatGUIForTest(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
            super(chatService, tts, stt);
        }

        /**
         * Returneaza input-ul utilizatorului simulat.
         *
         * @return String reprezentand textul introdus de utilizator
         */
        @Override
        protected String getUserTextInput() {
            return inputIndex < mockInputs.length ? mockInputs[inputIndex++] : "exit";
        }
    }

    /**
     * Subclasa folosita pentru testarea cazurilor in care input-ul este gol.
     *
     * @see ChatGUI
     */
    private static class ChatGUIEmptyInputTest extends ChatGUI {
        private static final long serialVersionUID = 1L;
        private final String[] mockInputs = {"", "exit"};
        private int inputIndex = 0;

        /**
         * Constructor pentru clasa de test.
         *
         * @param chatService instanta de ChatService simulata
         * @param tts instanta de GoogleTTS simulata
         * @param stt instanta de SpeakToText simulata
         */
        public ChatGUIEmptyInputTest(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
            super(chatService, tts, stt);
        }

        /**
         * Returneaza input-ul gol pentru testare.
         *
         * @return String gol sau "exit"
         */
        @Override
        protected String getUserTextInput() {
            return inputIndex < mockInputs.length ? mockInputs[inputIndex++] : "exit";
        }
    }

    /**
     * Initializeaza mock-urile si instanta ChatGUI inainte de fiecare test.
     *
     * @throws Exception in caz de eroare de initializare
     */
    @BeforeEach
    public void setup() throws Exception {
        chatMock = mock(ChatService.class);
        ttsMock = mock(GoogleTTS.class);
        sttMock = mock(SpeakToText.class);

        chatGUI = new ChatGUIForTest(chatMock, ttsMock, sttMock);
        chatGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Testeaza daca apasarea butonului Text trimite un mesaj si porneste TTS.
     *
     * @throws Exception daca apar probleme in GUI
     * @see ChatService#sendMessage(String)
     * @see GoogleTTS#speak(String)
     */
    @Test
    public void testTextButtonTriggersSendMessage() throws Exception {
        when(chatMock.sendMessage("Ce este AI?")).thenReturn("AI este inteligenta artificiala.");
        doNothing().when(ttsMock).speak(anyString());

        JButton btnText = chatGUI.getTextButton();
        assertNotNull(btnText);

        SwingUtilities.invokeAndWait(btnText::doClick);
        Thread.sleep(1500);

        verify(chatMock, atLeastOnce()).sendMessage("Ce este AI?");
        verify(ttsMock, atLeastOnce()).speak("AI este inteligenta artificiala.");
    }

    /**
     * Verifica daca butonul de oprire poate fi activat si ramane activ dupa click.
     *
     * @see ChatGUI#getStopButton()
     */
    @Test
    public void testStopButtonWorks() {
        JButton btnStop = chatGUI.getStopButton();
        assertNotNull(btnStop);
        SwingUtilities.invokeLater(btnStop::doClick);
        assertTrue(btnStop.isEnabled());
    }

    /**
     * Verifica daca mesajele utilizatorului si AI-ului apar in zona de conversatie.
     *
     * @throws Exception daca apar erori in GUI
     * @see JTextArea
     */
    @Test
    public void testMessageAppearsInConversationArea() throws Exception {
        when(chatMock.sendMessage("Ce este AI?")).thenReturn("AI este inteligenta artificiala.");
        doNothing().when(ttsMock).speak(anyString());

        chatGUI = new ChatGUIForTest(chatMock, ttsMock, sttMock);
        chatGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnText = chatGUI.getTextButton();
        assertNotNull(btnText);
        SwingUtilities.invokeAndWait(btnText::doClick);
        Thread.sleep(1500);

        SwingUtilities.invokeAndWait(() -> {
            JTextArea conversationArea = chatGUI.getConversationArea();
            String content = conversationArea.getText();

            assertTrue(content.contains("Tu: Ce este AI?"), "Mesajul utilizatorului nu a fost afisat.");
            assertTrue(content.contains("AI: AI este inteligenta artificiala."), "Raspunsul AI nu a fost afisat.");
        });
    }

    /**
     * Verifica ca nu se trimite niciun mesaj daca input-ul este gol.
     *
     * @throws Exception daca apar probleme de GUI
     */
    @Test
    public void testEmptyTextInput() throws Exception {
        chatGUI = new ChatGUIEmptyInputTest(chatMock, ttsMock, sttMock);
        chatGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnText = chatGUI.getTextButton();
        assertNotNull(btnText);
        SwingUtilities.invokeAndWait(btnText::doClick);
        Thread.sleep(1500);

        verify(chatMock, never()).sendMessage("");
        verify(ttsMock, never()).speak(anyString());
    }

    /**
     * Verifica ca butonul de oprire opreste interactiunea si TTS-ul.
     *
     * @throws Exception in caz de erori in GUI
     * @see GoogleTTS#stop()
     */
    @Test
    public void testStopButtonStopsInteraction() throws Exception {
        when(chatMock.sendMessage("Ce este AI?")).thenReturn("AI este inteligenta artificiala.");
        doNothing().when(ttsMock).speak(anyString());
        doNothing().when(ttsMock).stop();

        JButton btnText = chatGUI.getTextButton();
        SwingUtilities.invokeAndWait(btnText::doClick);

        JButton btnStop = chatGUI.getStopButton();
        SwingUtilities.invokeLater(btnStop::doClick);
        Thread.sleep(1000);

        verify(ttsMock, atLeastOnce()).stop();
    }

    /**
     * Verifica daca butonul Voice declanseaza recunoasterea vocala si trimiterea mesajului.
     *
     * @throws Exception daca apar erori la executarea testului
     * @see SpeakToText#listen()
     * @see ChatService#sendMessage(String)
     */
    @Test
    public void testVoiceButtonTriggersSendMessage() throws Exception {
        when(sttMock.listen()).thenReturn("Ce este AI?");
        when(chatMock.sendMessage("Ce este AI?")).thenReturn("AI este inteligenta artificiala.");
        doNothing().when(ttsMock).speak(anyString());

        JButton btnVoice = chatGUI.getVoiceButton();
        assertNotNull(btnVoice);

        SwingUtilities.invokeAndWait(btnVoice::doClick);
        Thread.sleep(1500);

        verify(chatMock, atLeastOnce()).sendMessage("Ce este AI?");
        verify(ttsMock, atLeastOnce()).speak("AI este inteligenta artificiala.");
    }
}
