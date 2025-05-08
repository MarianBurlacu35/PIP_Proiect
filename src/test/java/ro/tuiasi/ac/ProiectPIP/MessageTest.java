package ro.tuiasi.ac.ProiectPIP;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testConstructorInitializesFields() {
        Message message = new Message("user", "Salut!");

        assertEquals("user", message.getRole(), "Rolul ar trebui sa fie 'user'");
        assertEquals("Salut!", message.getContent(), "Continutul ar trebui sa fie 'Salut!'");
    }

    @Test
    public void testRoleGetter() {
        Message message = new Message("assistant", "Raspuns");
        assertEquals("assistant", message.getRole(), "getRole() nu returneaza valoarea corecta");
    }

    @Test
    public void testContentGetter() {
        Message message = new Message("user", "Intrebare");
        assertEquals("Intrebare", message.getContent(), "getContent() nu returneaza valoarea corecta");
    }

    @Test
    public void testNullValuesAllowed() {
        Message message = new Message(null, null);
        assertNull(message.getRole(), "Rolul ar trebui sa fie null");
        assertNull(message.getContent(), "Continutul ar trebui sa fie null");
    }

    @Test
    public void testEmptyStrings() {
        Message message = new Message("", "");
        assertEquals("", message.getRole(), "Rolul ar trebui sa fie sirul gol");
        assertEquals("", message.getContent(), "Continutul ar trebui sa fie sirul gol");
    }
}
