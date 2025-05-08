/*
package ro.tuiasi.ac.ProiectPIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatGUI {
    private ChatService chatService;
    private GoogleTTS tts;
    private SpeakToText stt;
    private JTextArea conversationArea;

    public ChatGUI(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
        this.chatService = chatService;
        this.tts = tts;
        this.stt = stt;
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("DeepSeek Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

     
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(conversationArea);

        // === Butoane ===
        JButton voiceButton = new JButton("Vorbeste");
        JButton typeButton = new JButton("Tasteaza");
        JButton stopButton = new JButton("Opreste");

        voiceButton.addActionListener(e -> handleVoice());
        typeButton.addActionListener(e -> handleText());
        stopButton.addActionListener(e -> handleStop());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(voiceButton);
        buttonPanel.add(typeButton);
        buttonPanel.add(stopButton);


        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

    
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Se inchide aplicatia...");
                closeResources();
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void handleText() {
        String input = JOptionPane.showInputDialog(null, "Scrie intrebarea ta:");
        if (input != null && !input.trim().isEmpty()) {
            processInput(input);
        }
    }

    private void handleVoice() {
        new Thread(() -> {
            String input = stt.listen();
            if (input != null && !input.trim().isEmpty()) {
                SwingUtilities.invokeLater(() -> processInput(input));
            } else {
                JOptionPane.showMessageDialog(null, "Nu am inteles vocea.");
            }
        }).start();
    }

    private void processInput(String input) {
        appendToConversation("Tu: " + input);
        String response = chatService.sendMessage(input);
        appendToConversation("AI: " + response);

        try {
            String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
            tts.speak(cleaned); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Eroare la redare: " + e.getMessage());
        }
    }

    private void handleStop() {
        tts.stop();
    }

    private void appendToConversation(String text) {
        conversationArea.append(text + "\n\n");
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    private void closeResources() {
        System.out.println("Cleanup resurse...");

    }

    public static void main(String[] args) {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "API key-ul DEEPSEEK_API_KEY nu este setat.");
            return;
        }

        SwingUtilities.invokeLater(() -> new ChatGUI(
                new ChatService(apiKey),
                new GoogleTTS(),
                new SpeakToText()
        ));
    }
}
*/
/*
package ro.tuiasi.ac.ProiectPIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatGUI {
    private ChatService chatService;
    private GoogleTTS tts;
    private SpeakToText stt;
    private JTextArea conversationArea;
    private volatile boolean runningInteraction = false;


    private JButton voiceButton;
    private JButton typeButton;

    public ChatGUI(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
        this.chatService = chatService;
        this.tts = tts;
        this.stt = stt;
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("DeepSeek Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

    
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setLineWrap(true);
        conversationArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(conversationArea);

        // === Butoane ===
        voiceButton = new JButton("Vorbeste");
        typeButton = new JButton("Tasteaza");
        JButton stopButton = new JButton("Opreste");

        voiceButton.addActionListener(e -> handleVoice());
        typeButton.addActionListener(e -> handleText());
        stopButton.addActionListener(e -> handleStop());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(voiceButton);
        buttonPanel.add(typeButton);
        buttonPanel.add(stopButton);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Se inchide aplicatia...");
                closeResources();
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void handleText() {
        if (runningInteraction) return;
        runningInteraction = true;

        new Thread(() -> {
            while (runningInteraction) {
                String input = JOptionPane.showInputDialog(null, "Scrie intrebarea ta (sau 'exit' pentru a iesi):");
                if (input == null || input.trim().isEmpty()) continue;
                if (input.equalsIgnoreCase("exit")) break;

                String response = chatService.sendMessage(input);
                SwingUtilities.invokeLater(() -> {
                    appendToConversation("Tu: " + input);
                    appendToConversation("AI: " + response);
                });

                try {
                    String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
                    tts.speak(cleaned);
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Eroare la redare: " + e.getMessage()));
                }
            }
            runningInteraction = false;
            SwingUtilities.invokeLater(() -> setInputButtonsEnabled(true));
        }).start();

        setInputButtonsEnabled(false);
    }


    private void handleVoice() {
        if (runningInteraction) return;
        runningInteraction = true;

        new Thread(() -> {
            while (runningInteraction) {
                String input = stt.listen();
                if (input == null || input.trim().isEmpty()) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Nu am inteles vocea."));
                    continue;
                }

                if (input.equalsIgnoreCase("exit")) break;

                String response = chatService.sendMessage(input);
                SwingUtilities.invokeLater(() -> {
                    appendToConversation("Tu: " + input);
                    appendToConversation("AI: " + response);
                });

                try {
                    String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
                    tts.speak(cleaned);
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Eroare la redare: " + e.getMessage()));
                }
            }
            runningInteraction = false;
            SwingUtilities.invokeLater(() -> setInputButtonsEnabled(true));
        }).start();

        setInputButtonsEnabled(false);
    }


    private void processInput(String input) {
        appendToConversation("Tu: " + input);
        String response = chatService.sendMessage(input);
        appendToConversation("AI: " + response);

     
        setInputButtonsEnabled(false);

        new Thread(() -> {
            try {
                String cleaned = DeepSeekChatApp.cleanTextForSpeech(response);
                tts.speak(cleaned);
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(null, "Eroare la redare: " + e.getMessage()));
            } finally {
               
                SwingUtilities.invokeLater(() -> setInputButtonsEnabled(true));
            }
        }).start();
    }

    private void handleStop() {
        runningInteraction = false;
        tts.stop(); // opreste vocea
    }


    private void setInputButtonsEnabled(boolean enabled) {
        voiceButton.setEnabled(enabled);
        typeButton.setEnabled(enabled);
    }

    private void appendToConversation(String text) {
        conversationArea.append(text + "\n\n");
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }

    private void closeResources() {
        System.out.println("Cleanup resurse... (dacã e necesar)");
        // Dacã e nevoie de cleanup în viitor (TTS/STT persistent), se face aici
    }

    public static void main(String[] args) {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "API key-ul DEEPSEEK_API_KEY nu este setat.");
            return;
        }

        SwingUtilities.invokeLater(() -> new ChatGUI(
                new ChatService(apiKey),
                new GoogleTTS(),
                new SpeakToText()
        ));
    }
}
*/

package ro.tuiasi.ac.ProiectPIP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChatGUI extends JFrame {
    private final ChatService chatService;
    private final GoogleTTS tts;
    private final SpeakToText stt;

    private JTextArea conversationArea;
    private JButton btnText;
    private JButton btnVoice;
    private JButton btnStop;

    private volatile boolean runningInteraction = false;

    public ChatGUI(ChatService chatService, GoogleTTS tts, SpeakToText stt) {
        this.chatService = chatService;
        this.tts = tts;
        this.stt = stt;
        initializeUI();
    }

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

    private void handleStop() {
        runningInteraction = false;
        tts.stop();
    }

    private void setInputButtonsEnabled(boolean enabled) {
        btnText.setEnabled(enabled);
        btnVoice.setEnabled(enabled);
    }

    private void appendToConversation(String message) {
        SwingUtilities.invokeLater(() -> {
            conversationArea.append(message + "\n");
            conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
        });
    }


    public JButton getTextButton() {
        return btnText;
    }

    public JButton getStopButton() {
        return btnStop;
    }

    public JButton getVoiceButton() {
        return btnVoice;
    }

    public JTextArea getConversationArea() {
        return conversationArea;
    }

    protected String getUserTextInput() {
        return JOptionPane.showInputDialog(this, "Scrie intrebarea (sau 'exit'):");
    }
}

