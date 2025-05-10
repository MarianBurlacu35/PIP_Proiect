/*package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;

public class GoogleTTS {
    public void speak(String text) throws Exception {
        try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {

            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ro-RO")  
                    .setName("ro-RO-Wavenet-A")  
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)  
                    .build();

       
            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();

           
            try (InputStream audioStream = new ByteArrayInputStream(audioContents.toByteArray())) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();

              
                while (clip.isRunning()) {
                    Thread.sleep(100);
                }
            }

        } catch (Exception e) {
            System.err.println("Eroare la sintetizarea sau redarea vocii: " + e.getMessage());
            throw e;
        }
    }
}

*/
/*
package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;
import java.util.regex.*;

public class GoogleTTS {
    public void speak(String text) throws Exception {
        try {
            
            text = cleanText(text);

            try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {

                SynthesisInput input = SynthesisInput.newBuilder()
                        .setText(text)  
                        .build();

                VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                        .setLanguageCode("ro-RO")  
                        .setName("ro-RO-Wavenet-A")  
                        .build();

                AudioConfig audioConfig = AudioConfig.newBuilder()
                        .setAudioEncoding(AudioEncoding.LINEAR16) 
                        .build();

             
                SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);

                ByteString audioContents = response.getAudioContent();

              
                try (InputStream audioStream = new ByteArrayInputStream(audioContents.toByteArray())) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();

                    
                    while (clip.isRunning()) {
                        Thread.sleep(100);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Eroare la sintetizarea sau redarea vocii: " + e.getMessage());
            throw e;
        }
    }

  
    private String cleanText(String text) {
  
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1"); 
   
        text = text.replaceAll("###.*?###", " ");

        
        return text;
    }


}

*/
/*

package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;

public class GoogleTTS {
    public void speak(String text) throws Exception {
        text = cleanText(text);

        try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ro-RO")
                    .setName("ro-RO-Wavenet-A")
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .setSpeakingRate(1.3f)
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            byte[] audioData = response.getAudioContent().toByteArray();

            // Redare sincronizata cu SourceDataLine
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format);
                line.start();

                line.write(audioData, 0, audioData.length);

                line.drain(); // asteapta finalul redarii
                line.stop();
            }

            System.out.println("Redarea audio s-a încheiat.");
        } catch (Exception e) {
            System.err.println("Eroare la sintetizare/redare: " + e.getMessage());
            throw e;
        }
    }

    private String cleanText(String text) {
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
        text = text.replaceAll("###.*?###", " ");
        return text;
    }
}
*/
/*
package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;

public class GoogleTTS {
    private volatile SourceDataLine currentLine;

    public void speak(String text) throws Exception {
        text = cleanText(text);

        try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ro-RO")
                    .setName("ro-RO-Wavenet-A")
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .setSpeakingRate(1.3f)
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            byte[] audioData = response.getAudioContent().toByteArray();

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format);
                line.start();
                this.currentLine = line;

                line.write(audioData, 0, audioData.length);

                line.drain();
                line.stop();
                this.currentLine = null;
            }

            System.out.println("Redarea audio s-a incheiat.");
        } catch (Exception e) {
            System.err.println("Eroare la redare: " + e.getMessage());
            throw e;
        }
    }

    public synchronized void stop() {
        try {
            if (currentLine != null) {
                if (currentLine.isRunning()) {
                    currentLine.stop();
                }
                currentLine.flush();
                currentLine.close();
                System.out.println("Redarea vocala a fost oprita de utilizator.");
            } else {
                System.out.println("Redarea era deja oprita sau nu fusese pornita.");
            }
        } catch (Exception e) {
            System.err.println("Eroare la oprirea redarii: " + e.getMessage());
        } finally {
            currentLine = null;
        }
    }



    protected String cleanText(String text) {
        if (text == null) return "";
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
        text = text.replaceAll("###.*?###\\s*", "");  
        return text;
    }

}
*/

package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;

/**
 * {@code GoogleTTS} este responsabil pentru sintetizarea textului in voce
 * folosind Google Cloud Text-to-Speech API si redarea acestuia prin difuzoare.
 *
 * <p>
 * Suporta limba romana si permite oprirea redarii in timpul functionarii.
 * </p>
 *
 * <p><b>Exemplu de utilizare:</b></p>
 * <pre>{@code
 * GoogleTTS tts = new GoogleTTS();
 * tts.speak("Salut, cum pot sa te ajut?");
 * }</pre>
 *
 * @author Marian
 * @version 1.0
 * @see com.google.cloud.texttospeech.v1.TextToSpeechClient
 * @see javax.sound.sampled.SourceDataLine
 */
public class GoogleTTS {

    private volatile SourceDataLine currentLine;

    /**
     * Converteste un text in voce folosind Google Cloud Text-to-Speech
     * si reda rezultatul audio prin difuzoare.
     *
     * @param text textul care va fi sintetizat si redat
     * @throws Exception daca sintetizarea sau redarea esueaza
     * @see #stop()
     */
    public void speak(String text) throws Exception {
        text = cleanText(text);

        try (TextToSpeechClient ttsClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("ro-RO")
                    .setName("ro-RO-Wavenet-A")
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.LINEAR16)
                    .setSpeakingRate(1.3f)
                    .build();

            SynthesizeSpeechResponse response = ttsClient.synthesizeSpeech(input, voice, audioConfig);
            byte[] audioData = response.getAudioContent().toByteArray();

            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format);
                line.start();
                this.currentLine = line;

                line.write(audioData, 0, audioData.length);

                line.drain();
                line.stop();
                this.currentLine = null;
            }

            System.out.println("Redarea audio s-a incheiat.");
        } catch (Exception e) {
            System.err.println("Eroare la redare: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Opreste redarea vocii daca aceasta este in curs.
     * Folosit pentru a permite utilizatorului sa intrerupa vocea instant.
     *
     * @see #speak(String)
     */
    public synchronized void stop() {
        try {
            if (currentLine != null) {
                if (currentLine.isRunning()) {
                    currentLine.stop();
                }
                currentLine.flush();
                currentLine.close();
                System.out.println("Redarea vocala a fost oprita de utilizator.");
            } else {
                System.out.println("Redarea era deja oprita sau nu fusese pornita.");
            }
        } catch (Exception e) {
            System.err.println("Eroare la oprirea redarii: " + e.getMessage());
        } finally {
            currentLine = null;
        }
    }

    /**
     * Elimina formatarea Markdown si tag-urile speciale din text,
     * pentru a-l pregati pentru sintetizare.
     *
     * @param text textul brut care poate contine formatare
     * @return textul curatat pentru a fi redat vocal
     */
    protected String cleanText(String text) {
        if (text == null) return "";
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");
        text = text.replaceAll("###.*?###\\s*", "");
        return text;
    }
}
