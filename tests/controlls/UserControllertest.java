package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controllers.users.UserController;
import controllers.users.listeners.MailEvent;
import controllers.users.listeners.UserListener;
import model.User;

public class UserControllerTest {

    private UserController controller;
    private int notifyAddedCounter;
    private int notifyUpdatedCounter;

    @Before
    public void setUp() throws Exception {
        controller = UserController.getInstance();
        notifyAddedCounter = 0;
        notifyUpdatedCounter = 0;

        
        Field field = UserController.class.getDeclaredField("userListeners");
        field.setAccessible(true);
        ((List<?>) field.get(controller)).clear();
    }

    // Teste do Singleton
    @Test
    public void deveRetornarSempreAMesmaInstancia() {
        UserController c1 = UserController.getInstance();
        UserController c2 = UserController.getInstance();

        assertSame(c1, c2);
    }

    // Testa se adiciona listener apenas uma vez
    @Test
    public void deveAdicionarListenerApenasUmaVez() throws Exception {

        UserListener listener = new UserListener() {

            @Override
            public void useradd(MailEvent<User> event) {
            }

            @Override
            public void userUpdated(MailEvent<User> event) {
            }
        };

        controller.addUserListener(listener);
        controller.addUserListener(listener); 

        Field field = UserController.class.getDeclaredField("userListeners");
        field.setAccessible(true);
        List<?> listeners = (List<?>) field.get(controller);

        assertEquals(1, listeners.size());
    }

    // Testa notificação de usuário criado
    @Test
    public void deveNotificarListenersQuandoUsuarioForCriado() throws Exception {

        UserListener listener = new UserListener() {

            @Override
            public void useradd(MailEvent<User> event) {
                notifyAddedCounter++;
            }

            @Override
            public void userUpdated(MailEvent<User> event) {
            }
        };

        controller.addUserListener(listener);

        Method method = UserController.class
                .getDeclaredMethod("notifyUserAddedListener", User.class);
        method.setAccessible(true);

        User fakeUser = new User("Teste", "teste_login");
        method.invoke(controller, fakeUser);

        assertEquals(1, notifyAddedCounter);
    }

    // Testa notificação de usuário atualizado
    @Test
    public void deveNotificarListenersQuandoUsuarioForAtualizado() throws Exception {

        UserListener listener = new UserListener() {

            @Override
            public void useradd(MailEvent<User> event) {
            }

            @Override
            public void userUpdated(MailEvent<User> event) {
                notifyUpdatedCounter++;
            }
        };

        controller.addUserListener(listener);

        Method method = UserController.class
                .getDeclaredMethod("notifyUserUpdatedListener", User.class);
        method.setAccessible(true);

        User fakeUser = new User("Teste", "teste_login");
        method.invoke(controller, fakeUser);

        assertEquals(1, notifyUpdatedCounter);
    }
}
