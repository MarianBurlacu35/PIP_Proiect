package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpeakToTextTest {

    // ==============================
    //  Test: listen() cu mock audio
    // ==============================

    @Test
    public void testListenWithMockedMicrophone() {
       
        SpeakToText speakToText = new SpeakToText() {
            @Override
            protected byte[] recordAudioFromMicrophone(int durationMillis) {
         
                return new byte[32000]; 
            }
        };

        String result = speakToText.listen();
   
        assertNotNull(result, "Transcrierea nu trebuie sa fie null (chiar daca este goala)");
    }

    // ==============================
    //  Test: transcribeAudio() cu input vid
    // ==============================

    @Test
    public void testTranscribeSilentAudio() {
        SpeakToText stt = new SpeakToText();
        byte[] silentAudio = new byte[32000]; 

        String result = stt.transcribeAudio(silentAudio);
        assertNotNull(result);
        assertTrue(result.isEmpty() || result.isBlank(), "Transcrierea ar trebui sa fie goala pentru liniste.");
    }
}
