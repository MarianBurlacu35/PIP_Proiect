package ro.tuiasi.ac.ProiectPIP;

import java.util.Scanner;

public class DeepSeekChatApp {
    public static void main(String[] args) {
        final String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Variabila de mediu DEEPSEEK_API_KEY nu este setată.");
            return;
        }

        ChatService chatService = new ChatService(apiKey);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Chat cu DeepSeek ===");
        System.out.println("Scrie 'exit' pentru a închide conversația.");

        while (true) {
            System.out.print("Tu: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            String response = chatService.sendMessage(input);
            System.out.println("AI: " + response);
        }

        System.out.println("Conversația s-a încheiat.");
        scanner.close();
    }
}
