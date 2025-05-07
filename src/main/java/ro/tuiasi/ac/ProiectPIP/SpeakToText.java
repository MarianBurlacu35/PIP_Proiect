/*package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.*;

public class SpeakToText {
    public static void main(String[] args) throws Exception {
        
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        System.out.println("* Spune ceva... (5 secunde)");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        long end = System.currentTimeMillis() + 5000;

        while (System.currentTimeMillis() < end) {
            int bytesRead = microphone.read(buffer, 0, buffer.length);
            out.write(buffer, 0, bytesRead);
        }

        microphone.stop();
        microphone.close();

        byte[] audioData = out.toByteArray();

       
        try (SpeechClient speechClient = SpeechClient.create()) {
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("ro-RO") 
                    .setSampleRateHertz(16000)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioData))
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            for (SpeechRecognitionResult result : response.getResultsList()) {
                System.out.println("* Ai spus: " + result.getAlternatives(0).getTranscript());
            }
        } catch (Exception e) {
            System.err.println("Eroare î=in recunoastere: " + e.getMessage());
        }
    }
}
*/
/*
package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SpeakToText {
    public String listen() {
        try (SpeechClient speechClient = SpeechClient.create()) {
            
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Microfonul nu este suportat.");
                return null;
            }

            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            System.out.println(" Ascult... (vorbeste acum)");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            long startTime = System.currentTimeMillis();

          
            while (System.currentTimeMillis() - startTime < 5000) {
                int numBytesRead = microphone.read(buffer, 0, buffer.length);
                out.write(buffer, 0, numBytesRead);
            }

            microphone.stop();
            microphone.close();

            byte[] audioData = out.toByteArray();

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("ro-RO")
                    .setSampleRateHertz(16000)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioData))
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder transcript = new StringBuilder();

            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcript.append(result.getAlternativesList().get(0).getTranscript());
            }

            return transcript.toString();

        } catch (Exception e) {
            System.err.println("Eroare in recunoasterea vocii: " + e.getMessage());
            return null;
        }
    }
}
*/

package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;

public class SpeakToText {
    public String listen() {
        try (SpeechClient speechClient = SpeechClient.create()) {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Microfonul nu este suportat.");
                return null;
            }

            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();

            System.out.println(" Ascult pana la 5 secunde...");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < 5000) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                out.write(buffer, 0, bytesRead);
            }

            microphone.stop();
            microphone.close();

            byte[] audioData = out.toByteArray();

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setLanguageCode("ro-RO")
                    .setSampleRateHertz(16000)
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioData))
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder result = new StringBuilder();

            for (SpeechRecognitionResult r : response.getResultsList()) {
                result.append(r.getAlternativesList().get(0).getTranscript());
            }

            return result.toString();

        } catch (Exception e) {
            System.err.println("Eroare in recunoasterea vocii: " + e.getMessage());
            return null;
        }
    }
}
