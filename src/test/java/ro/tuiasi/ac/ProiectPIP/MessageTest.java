
/**
 * Clasa de teste unitare pentru clasa {@link Message}.
 * Verifica corectitudinea constructorului si a metodelor {@code getRole} si {@code getContent}.
 *
 * @author Marian-Cosmin Burlacu
 * @version 12.05.2025
 * @see Message
 */
package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    /**
     * Testeaza daca constructorul initializeaza corect campurile {@code role} si {@code content}.
     *
     * @see Message#Message(String, String)
     */
    @Test
    public void testConstructorInitializesFields() {
        Message message = new Message("user", "Salut!");

        assertEquals("user", message.getRole(), "Rolul ar trebui sa fie 'user'");
        assertEquals("Salut!", message.getContent(), "Continutul ar trebui sa fie 'Salut!'");
    }

    /**
     * Testeaza metoda {@code getRole} pentru un mesaj cu rolul 'assistant'.
     *
     * @see Message#getRole()
     */
    @Test
    public void testRoleGetter() {
        Message message = new Message("assistant", "Raspuns");
        assertEquals("assistant", message.getRole(), "getRole() nu returneaza valoarea corecta");
    }

    /**
     * Testeaza metoda {@code getContent} pentru un mesaj cu un anumit continut.
     *
     * @see Message#getContent()
     */
    @Test
    public void testContentGetter() {
        Message message = new Message("user", "Intrebare");
        assertEquals("Intrebare", message.getContent(), "getContent() nu returneaza valoarea corecta");
    }

    /**
     * Testeaza daca obiectul {@link Message} poate fi creat cu valori {@code null}.
     *
     * @see Message#Message(String, String)
     */
    @Test
    public void testNullValuesAllowed() {
        Message message = new Message(null, null);
        assertNull(message.getRole(), "Rolul ar trebui sa fie null");
        assertNull(message.getContent(), "Continutul ar trebui sa fie null");
    }

    /**
     * Testeaza comportamentul obiectului {@link Message} cand se folosesc siruri goale.
     *
     * @see Message#Message(String, String)
     */
    @Test
    public void testEmptyStrings() {
        Message message = new Message("", "");
        assertEquals("", message.getRole(), "Rolul ar trebui sa fie sirul gol");
        assertEquals("", message.getContent(), "Continutul ar trebui sa fie sirul gol");
    }
}
