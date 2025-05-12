package ro.tuiasi.ac.ProiectPIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * {@code ChatGUI} reprezinta interfata grafica a aplicatiei DeepSeek Chat.
 * Ofera utilizatorului posibilitatea de a interactiona cu AI-ul prin text sau voce
 * si afiseaza conversatia intr-un panou vizual.
 *
 * <p>
 * GUI-ul include 3 butoane:
 * <ul>
 *     <li>{@code Tasteaza} - pentru introducere manuala de text</li>
 *     <li>{@code Vorbeste} - pentru recunoasterea vocii prin microfon</li>
 *     <li>{@code Opreste} - opreste redarea audio</li>
 * </ul>
 * </p>
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see ChatService
 * @see GoogleTTS
 * @see SpeakToText
 */
public class ChatGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private final ChatService chatService;
    private final GoogleTTS tts;
    private final SpeakToText stt;

    private JTextArea conversationArea;
    private JButton btnText;
    private JButton btnVoice;
    private JButton btnStop;

    private volatile boolean runningInteraction = false;

    /**
     * Creeaza o fereastra GUI noua si initializeaza componentele.
     *
     * @param chatService instanta de {@link ChatService}
     * @param tts instanta de {@link GoogleTTS}
     * @param stt instanta de {@link SpeakToText}
     */
    public ChatGUI(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
        this.chatService = chatService;
        this.tts = tts;
        this.stt = stt;
        initializeUI();
    }

    /**
     * Initializeaza interfata grafica, layout-ul si butoanele.
     */
    private void initializeUI() {
        setTitle("DeepSeek Chat GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(conversationArea);

        btnText = new JButton("Tasteaza");
        btnVoice = new JButton("Vorbeste");
        btnStop = new JButton("Opreste");

        btnText.addActionListener(this::handleText);
        btnVoice.addActionListener(this::handleVoice);
        btnStop.addActionListener(e -> handleStop());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnText);
        buttonPanel.add(btnVoice);
        buttonPanel.add(btnStop);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Gestioneaza fluxul de conversatie text. Deschide un thread
     * care permite utilizatorului sa trimita intrebari consecutive prin text.
     *
     * @param e evenimentul de click pe butonul "Tasteaza"
     */
    private void handleText(ActionEvent e) {
        if (runningInteraction) return;
        runningInteraction = true;

        new Thread(() -> {
            while (runningInteraction) {
                String input = getUserTextInput();
                if (input == null || input.trim().isEmpty()) continue;
                if (input.equalsIgnoreCase("exit")) break;

                String response = chatService.sendMessage(input);
                appendToConversation("Tu: " + input);
                appendToConversation("AI: " + response);

                try {
                    String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
                    tts.speak(cleaned);
                } catch (Exception ex) {
                    appendToConversation("Eroare la redare: " + ex.getMessage());
                }
            }
            runningInteraction = false;
            setInputButtonsEnabled(true);
        }).start();

        setInputButtonsEnabled(false);
    }

    /**
     * Gestioneaza fluxul de conversatie prin voce. Inregistreaza vocea,
     * o transcrie si trimite continutul catre AI.
     *
     * @param e evenimentul de click pe butonul "Vorbeste"
     */
    private void handleVoice(ActionEvent e) {
        if (runningInteraction) return;
        runningInteraction = true;

        new Thread(() -> {
            while (runningInteraction) {
                String input = stt.listen();
                if (input == null || input.trim().isEmpty()) continue;
                if (input.equalsIgnoreCase("exit")) break;

                String response = chatService.sendMessage(input);
                appendToConversation("Tu (voce): " + input);
                appendToConversation("AI: " + response);

                try {
                    String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
                    tts.speak(cleaned);
                } catch (Exception ex) {
                    appendToConversation("Eroare la redare: " + ex.getMessage());
                }
            }
            runningInteraction = false;
            setInputButtonsEnabled(true);
        }).start();

        setInputButtonsEnabled(false);
    }

    /**
     * Opreste redarea vocala si orice thread de conversatie activ.
     */
    private void handleStop() {
        runningInteraction = false;
        tts.stop();
    }

    /**
     * Activeaza sau dezactiveaza butoanele de input (text si voce).
     *
     * @param enabled true pentru a activa, false pentru a dezactiva
     */
    private void setInputButtonsEnabled(boolean enabled) {
        btnText.setEnabled(enabled);
        btnVoice.setEnabled(enabled);
    }

    /**
     * Adauga un mesaj in zona de conversatie.
     *
     * @param message mesajul de afisat
     */
    private void appendToConversation(String message) {
        SwingUtilities.invokeLater(() -> {
            conversationArea.append(message + "\n");
            conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
        });
    }

    /**
     * Returneaza textul introdus de utilizator. Poate fi suprascrisa in teste.
     *
     * @return inputul text de la utilizator
     */
    protected String getUserTextInput() {
        return JOptionPane.showInputDialog(this, "Scrie intrebarea (sau 'exit'):");
    }

    // ==== Getteri publici pentru testare ====

    /**
     * @return butonul de text
     */
    public JButton getTextButton() {
        return btnText;
    }

    /**
     * @return butonul de oprire
     */
    public JButton getStopButton() {
        return btnStop;
    }

    /**
     * @return butonul de voce
     */
    public JButton getVoiceButton() {
        return btnVoice;
    }

    /**
     * @return zona de text in care este afisata conversatia
     */
    public JTextArea getConversationArea() {
        return conversationArea;
    }

    /**
     * Metoda main care initializeaza GUI-ul si componentele necesare.
     * Poate fi rulata direct.
     *
     * @param args argumente CLI (neutilizate)
     */
    public static void main(String[] args) {
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "API key-ul DEEPSEEK_API_KEY nu este setat.", "Eroare", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            ChatService chatService = new ChatService(apiKey);
            GoogleTTS tts = new GoogleTTS();
            SpeakToText stt = new SpeakToText();
            new ChatGUI(chatService, tts, stt);
        });
    }
}
