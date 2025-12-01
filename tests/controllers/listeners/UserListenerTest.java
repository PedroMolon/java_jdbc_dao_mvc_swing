package controllers.listeners;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import controllers.users.listeners.MailEvent;
import controllers.users.listeners.UserListener;
import model.User;

public class UserListenerTest {

    @Test
    public void shouldCreateUserListenerImplementation() {

        UserListener listener = new UserListener() {

            @Override
            public void useradd(MailEvent<User> event) {
                // implementação fake
            }

            @Override
            public void userUpdated(MailEvent<User> event) {
                // implementação fake
            }
        };

        assertNotNull(listener);
        assertTrue(listener instanceof UserListener);
    }
}
