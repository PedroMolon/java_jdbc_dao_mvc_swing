package view.users;

import controllers.users.UserController;
import model.User;
import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.*;

public class FormGuiTest {
    private DialogFixture window;

    private User testUser;

    private String testLogin;

    private User findUserByLogin(String login) throws SQLException {
        for (User u : UserController.getInstance().allUsers()) {
            if (u.getLogin().equals(login)) return u;
        }
        return null;
    }

    @Before
    public void setUp() throws SQLException {
        testLogin = "teste" + System.currentTimeMillis();
        User existing = findUserByLogin(testLogin);
        if (existing != null) {
            UserController.getInstance().remove(existing.getId());
        }
        Form form = GuiActionRunner.execute(Form::new);
        window = new DialogFixture(form);
        window.show();
    }


    @After
    public void tearDown() throws SQLException {
        if (window != null) window.cleanUp();
        if (testUser != null) UserController.getInstance().remove(testUser.getId());
    }


    @Test
    @GUITest
    public void testSaveButton_NewUser() throws SQLException {
        window.textBox("nameField").setText("Pedro Teste");
        window.textBox("loginField").setText(testLogin);
        GuiActionRunner.execute(() -> window.button("saveButton").target().doClick());
        testUser = findUserByLogin(testLogin);
        assertNotNull(testUser);
        assertFalse(window.target().isVisible());
    }


    @Test
    @GUITest
    public void testCancelButtonClosesForm() {
        GuiActionRunner.execute(() -> window.button("cancelButton").target().doClick());
        assertFalse(window.target().isVisible());
    }

}