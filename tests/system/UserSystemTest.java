import controllers.users.UserController;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSystemTest {

    @Before
    public void cleanDatabase() throws Exception {
        Connection c = new daoFactory.Mysql().openConnection();
        Statement st = c.createStatement();
        st.execute("DELETE FROM users");
        c.close();
    }

    @Test
    public void shouldCreateUser() throws Exception {
        UserController controller = UserController.getInstance();

        User u = new User("Pedro", "pedro123");
        controller.save(u, true);

        assertThat(controller.allUsers())
                .extracting(User::getLogin)
                .contains("pedro123");
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        UserController controller = UserController.getInstance();

        User u = new User("Maria", "maria1");
        controller.save(u, true);

        u.setName("Maria Silva");
        controller.save(u, false);

        User updated = controller.findById(u.getId());

        assertThat(updated.getName()).isEqualTo("Maria Silva");
    }

    @Test
    public void shouldRemoveUser() throws Exception {
        UserController controller = UserController.getInstance();

        User u = new User("João", "joao1");
        controller.save(u, true);

        controller.remove(u.getId());

        assertThat(controller.allUsers()).isEmpty();
    }

    @Test
    public void shouldListUsers() throws Exception {
        UserController controller = UserController.getInstance();

        controller.save(new User("A", "a1"), true);
        controller.save(new User("B", "b1"), true);

        assertThat(controller.allUsers()).hasSize(2);
    }
}