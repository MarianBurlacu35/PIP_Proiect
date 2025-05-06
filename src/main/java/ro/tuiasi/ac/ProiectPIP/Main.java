package ro.tuiasi.ac.ProiectPIP;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        DeepSeekChat chat = new DeepSeekChat(apiKey);

        Scanner scanner = new Scanner(System.in);
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a helpful assistant."));

        System.out.println("=== Chat cu DeepSeek ===");
        System.out.println("Scrie 'exit' pentru a închide conversația.");

        while (true) {
            System.out.print("Utilizaror: ");
            String userInput = scanner.nextLine();
            if (userInput.equalsIgnoreCase("exit")) break;

            messages.add(Map.of("role", "user", "content", userInput));

            try {
                String aiResponse = chat.sendMessage(messages);
                System.out.println("AI: " + aiResponse);
                messages.add(Map.of("role", "assistant", "content", aiResponse));
            } catch (Exception e) {
                System.out.println("A apărut o eroare: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Conversația s-a încheiat.");
    }
}
