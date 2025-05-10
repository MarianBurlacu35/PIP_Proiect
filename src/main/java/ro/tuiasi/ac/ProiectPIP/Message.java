package ro.tuiasi.ac.ProiectPIP;

/**
 * Clasa {@code Message} reprezinta un mesaj individual dintr-o conversatie,
 * fie de la utilizator, fie de la asistent.
 *
 * <p>
 * Fiecare mesaj contine un rol (ex: "user", "assistant", "system") si un continut textual.
 * </p>
 *
 * <p><b>Exemplu:</b></p>
 * <pre>{@code
 * Message m = new Message("user", "Salut, AI!");
 * }</pre>
 *
 * @author Rares-Daniel Constantin
 * @version 1.0
 * @see ChatService
 */
public class Message {
    /**
     * Rolul mesajului (ex: "user", "assistant", "system").
     */
    private String role;

    /**
     * Continutul efectiv al mesajului.
     */
    private String content;

    /**
     * Creeaza o instanta noua de {@code Message}.
     *
     * @param role    rolul emitentului mesajului
     * @param content continutul text al mesajului
     */
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    /**
     * Returneaza rolul mesajului.
     *
     * @return un {@code String} care indica cine a emis mesajul
     */
    public String getRole() {
        return role;
    }

    /**
     * Returneaza continutul mesajului.
     *
     * @return continutul textual al mesajului
     */
    public String getContent() {
        return content;
    }
}
