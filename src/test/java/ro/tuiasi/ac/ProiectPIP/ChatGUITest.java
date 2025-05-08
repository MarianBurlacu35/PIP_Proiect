package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;

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

    private static class ChatGUIEmptyInputTest extends ChatGUI {
        private final String[] mockInputs = {"", "exit"};
        private int inputIndex = 0;

        public ChatGUIEmptyInputTest(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
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
            assertTrue(content.contains("AI: AI este inteligenta artificiala."), "Mesajul AI-ului nu a fost afisat.");
        });
    }


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
