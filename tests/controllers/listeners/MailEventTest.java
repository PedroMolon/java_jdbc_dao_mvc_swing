package controllers.listeners;

import static org.junit.Assert.*;

import org.junit.Test;

import model.User;
import controllers.users.listeners.*;

public class MailEventTest {

    @Test
    public void deveRetornarSourceTipado() {
        User user = new User("Pedro", "12345");
        user.setId(1L);
        user.setName("João");

        MailEvent<User> event = new MailEvent<>(user);

        assertNotNull(event);
        assertNotNull(event.getSource());
        assertEquals(user, event.getSource());
    }
}
