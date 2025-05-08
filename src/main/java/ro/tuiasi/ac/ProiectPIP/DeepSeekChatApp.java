/*package ro.tuiasi.ac.ProiectPIP;

import java.util.Scanner;

public class DeepSeekChatApp {
    public static void main(String[] args) throws Exception {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        ChatService chatService = new ChatService(apiKey);
        Scanner scanner = new Scanner(System.in);
       
        
        System.out.println("=== Chat cu DeepSeek ===");
        System.out.println("Scrie 'exit' pentru a inchide conversatia.");

        while (true) {

            System.out.print("Tu: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            String response = chatService.sendMessage(input);
            System.out.println("AI: " + response);

            try {
                GoogleTTS ttts = new GoogleTTS();
                ttts.speak(response); // redã rãspunsul AI cu voce
            } catch (Exception e) {
                System.err.println("Nu s-a putut reda vocea: " + e.getMessage());
            }
        }

        System.out.println("Conversatia s-a incheiat.");
        scanner.close();
    }
}
*/
/*
package ro.tuiasi.ac.ProiectPIP;

import java.util.Scanner;

public class DeepSeekChatApp {
    public static void main(String[] args) throws Exception {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println(" API key-ul DEEPSEEK_API_KEY nu este setat.");
            return;
        }

        ChatService chatService = new ChatService(apiKey);
        GoogleTTS tts = new GoogleTTS();
        SpeakToText stt = new SpeakToText();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chat cu DeepSeek  ===");
        System.out.println("Scrie 'exit' pentru a iesi.");
        System.out.println("Alege metoda de input:");
        System.out.println("1. Tastatura");
        System.out.println("2. Voce");

        int inputMode = 1;
        System.out.print("Optiune (1 sau 2): ");
        try {
            inputMode = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Mod invalid. Se va folosi tastatura.");
        }

        while (true) {
            String input = "";

            if (inputMode == 1) {
                System.out.print("Tu: ");
                input = scanner.nextLine();
            } else if (inputMode == 2) {
                System.out.println(" Vorbeste acum...");
                input = stt.listen();
                System.out.println("Tu (transcris): " + input);
            }

            if (input == null || input.trim().isEmpty()) {
                System.out.println(" Nu am inteles mesajul. Incearca din nou.");
                continue;
            }

            if (input.equalsIgnoreCase("exit")) break;

            String response = chatService.sendMessage(input);
            System.out.println("AI: " + response);

       
            String cleaned = cleanTextForSpeech(response);

           
            try {
                
                Thread ttsThread = new Thread(() -> {
                    try {
                    	
                        tts.speak(cleaned);
                    } catch (Exception e) {
                        System.err.println("Eroare la redarea vocii: " + e.getMessage());
                    }
                });
               
                	ttsThread.start();
                

               
                ttsThread.join(); 

            } catch (InterruptedException e) {
                System.err.println("Eroare de sincronizare a thread-urilor: " + e.getMessage());
            }
        }

        System.out.println(" Conversatia s-a incheiat.");
        scanner.close();
    }

    public static String cleanTextForSpeech(String text) {
        if (text == null) return "";
        text = text.replaceAll("(?m)^\\s*#+\\s*", "");        
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");    
        return text.trim();
    }
}
*/


package ro.tuiasi.ac.ProiectPIP;

import java.util.Scanner;

public class DeepSeekChatApp {
    public static void main(String[] args) throws Exception {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("API key-ul DEEPSEEK_API_KEY nu este setat.");
            return;
        }

        ChatService chatService = new ChatService(apiKey);
        GoogleTTS tts = new GoogleTTS();
        SpeakToText stt = new SpeakToText();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chat cu DeepSeek ===");
        System.out.println("Scrie 'exit' pentru a iesi.");
        System.out.println("Alege metoda de input:");
        System.out.println("1. Tastatura");
        System.out.println("2. Voce");

        int inputMode = 1;
        System.out.print("Optiune (1 sau 2): ");
        try {
            inputMode = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Mod invalid. Se va folosi tastatura.");
        }

        while (true) {
            String input = "";

            if (inputMode == 1) {
                System.out.print("Tu: ");
                input = scanner.nextLine();
            } else if (inputMode == 2) {
                System.out.println("Vorbeste acum...");
                input = stt.listen();
                System.out.println("Tu (transcris): " + input);
            }

            if (input == null || input.trim().isEmpty()) {
                System.out.println("Nu am inteles mesajul. Incearca din nou.");
                continue;
            }

            if (input.equalsIgnoreCase("exit")) break;


            String response = chatService.sendMessage(input);
            System.out.println("AI: " + response);

           
            String cleaned = cleanTextForSpeech(response);

            try {
                
                tts.speak(cleaned); 
            } catch (Exception e) {
                System.err.println("Eroare la redarea vocii: " + e.getMessage());
            }
        }

        System.out.println("Conversatia s-a incheiat.");
        scanner.close();
    }

    public static String cleanTextForSpeech(String text) {
        if (text == null) return "";

       
        text = text.replaceAll("(?m)^\\s*#+\\s*", "");
   
        text = text.replaceAll("\\*\\*(.*?)\\*\\*", "$1");

       
        String[] lines = text.split("\\r?\\n");
        StringBuilder cleaned = new StringBuilder();
        for (String line : lines) {
            cleaned.append(line.trim()).append("\n");
        }

        return cleaned.toString().trim(); 
    }

}



