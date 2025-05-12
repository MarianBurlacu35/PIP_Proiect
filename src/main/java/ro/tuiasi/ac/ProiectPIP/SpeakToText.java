
package ro.tuiasi.ac.ProiectPIP;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.ByteArrayOutputStream;

/**
 * {@code SpeakToText} ofera functionalitate de inregistrare audio de la microfon
 * si transcriere a vocii in text, folosind Google Cloud Speech-to-Text API.
 *
 * <p>
 * Este utilizata in aplicatie pentru a permite interactiunea vocala cu AI-ul.
 * </p>
 *
 * <p><b>Exemplu de utilizare:</b></p>
 * <pre>{@code
 * SpeakToText stt = new SpeakToText();
 * String rezultat = stt.listen();
 * }</pre>
 *
 * @author Raul Ghergheluc
 * @version 11.05.2025
 * @see com.google.cloud.speech.v1.SpeechClient
 */
public class SpeakToText {

    /**
     * Inregistreaza audio timp de 5 secunde de la microfon
     * si returneaza transcriptia ca text.
     *
     * @return textul transcris sau {@code null} daca a aparut o eroare
     * @throws RuntimeException daca configurarea microfonului sau API-ul esueaza
     */
    public String listen() {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioData = recordAudioFromMicrophone(5_000);

            if (audioData == null) return null;

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

    /**
     * Inregistreaza audio de la microfon pentru o durata specificata (in milisecunde).
     * Afiseaza un mesaj de informare in consola si o fereastra de tip pop-up.
     *
     * @param durationMillis durata in milisecunde a inregistrarii
     * @return un vector de bytes reprezentand datele audio sau {@code null} daca microfonul nu este suportat
     *
     * @throws LineUnavailableException daca microfonul nu poate fi accesat
     * @see AudioSystem
     */
    protected byte[] recordAudioFromMicrophone(int durationMillis) {
        try {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("[EROARE] Microfonul nu este suportat.");
                return null;
            }

            TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            System.out.println("[INFO] Ascult... Vorbeste acum!");
            JOptionPane.showMessageDialog(null, "Ascult... Vorbeste acum!", "Microfon activ", JOptionPane.INFORMATION_MESSAGE);

            microphone.start();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            long startTime = System.currentTimeMillis();

            while (System.currentTimeMillis() - startTime < durationMillis) {
                int bytesRead = microphone.read(buffer, 0, buffer.length);
                out.write(buffer, 0, bytesRead);
            }

            microphone.stop();
            microphone.close();

            return out.toByteArray();

        } catch (Exception e) {
            System.err.println("Eroare la inregistrare: " + e.getMessage());
            return null;
        }
    }

    /**
     * Transcrie audio deja existent sub forma de vector de bytes.
     * Util pentru testare si scenarii offline.
     *
     * @param audioData vector de bytes reprezentand date audio in format LINEAR16
     * @return textul transcris sau {@code null} daca a aparut o eroare
     *
     * @throws IllegalArgumentException daca formatul audio nu este suportat
     * @see com.google.cloud.speech.v1.RecognitionConfig
     */
    public String transcribeAudio(byte[] audioData) {
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

            StringBuilder result = new StringBuilder();
            for (SpeechRecognitionResult r : response.getResultsList()) {
                result.append(r.getAlternativesList().get(0).getTranscript());
            }

            return result.toString();

        } catch (Exception e) {
            System.err.println("Eroare la transcriere: " + e.getMessage());
            return null;
        }
    }
}
