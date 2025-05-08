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

  
    private static class ChatGUIForTest extends ChatGUI {
        private final String[] mockInputs = {"Ce este AI?", "exit"};
        private int inputIndex = 0;

        public ChatGUIForTest(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
            super(chatService, tts, stt);
        }

        @Override
        protected String getUserTextInput() {
            return inputIndex < mockInputs.length ? mockInputs[inputIndex++] : "exit";
        }
    }

    @BeforeEach
    public void setup() {
        chatMock = mock(ChatService.class);
        ttsMock = mock(GoogleTTS.class);
        sttMock = mock(SpeakToText.class);

        chatGUI = new ChatGUIForTest(chatMock, ttsMock, sttMock);
        chatGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

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

    @Test
    public void testStopButtonWorks() {
        JButton btnStop = chatGUI.getStopButton();
        assertNotNull(btnStop);
        SwingUtilities.invokeLater(btnStop::doClick);
        assertTrue(btnStop.isEnabled());
    }
}
