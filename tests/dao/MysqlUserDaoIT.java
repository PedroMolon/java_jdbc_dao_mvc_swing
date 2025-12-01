package dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import dao.concrete.MysqlUserDao;
import dao.interfaces.UserDao;
import daoFactory.DaoFactory;
import daoFactory.Mysql;
import model.User;

public class MysqlUserDaoIT {

    private MysqlUserDao userDao;

    @Before
    public void setUp() throws Exception {
        userDao = new MysqlUserDao();
        userDao.deleteAll(); // banco limpo antes de cada teste
    }

    @Test
    public void debugConexao() {
        Mysql mysql = new Mysql();
        Connection c = mysql.openConnection();
        System.out.println(c);
        assertNotNull(c);
    }
    @Test
    public void deveRetornarUserDao() {
        Mysql mysql = new Mysql();

        UserDao dao = mysql.getUserDao();

        assertNotNull(dao);
        assertEquals("MysqlUserDao", dao.getClass().getSimpleName());
    }

    @Test
    public void deveAbrirConexaoComBanco() throws Exception {
        Connection conn = DaoFactory.getDatabase().openConnection();
        assertNotNull("A conexão não deveria ser nula", conn);
        conn.close();
    }

    @Test
    public void deveInserirEConsultarUsuario() throws Exception {
        User user = new User("Joao", "joao_login");
        userDao.insert(user);

        assertNotNull("ID deveria ser gerado", user.getId());

        User found = userDao.findByLogin("joao_login");
        assertNotNull(found);
        assertEquals("Joao", found.getName());
    }

    @Test
    public void deveBuscarUsuarioPorId() throws Exception {
        User user = new User("Maria", "maria_login");
        userDao.insert(user);

        User found = userDao.findById(user.getId());

        assertNotNull(found);
        assertEquals("Maria", found.getName());
        assertEquals("maria_login", found.getLogin());
    }

    @Test
    public void deveListarTodosUsuarios() throws Exception {
        userDao.insert(new User("Ana", "ana_login"));
        userDao.insert(new User("Bia", "bia_login"));

        List<User> users = userDao.all();

        assertEquals(2, users.size());
    }

    @Test
    public void deveRemoverUsuario() throws Exception {
        User user = new User("Carlos", "carlos_login");
        userDao.insert(user);

        int rows = userDao.delete(user);
        assertEquals(1, rows);

        User deleted = userDao.findByLogin("carlos_login");
        assertNull(deleted);
    }

    @Test
    public void deveRemoverTodosUsuarios() throws Exception {
        userDao.insert(new User("X", "x_login"));
        userDao.insert(new User("Y", "y_login"));

        int rows = userDao.deleteAll();
        assertEquals(2, rows);

        assertTrue(userDao.all().isEmpty());
    }

    @Test
    public void deveFecharConexao() throws Exception {
        Connection conn = DaoFactory.getDatabase().openConnection();
        assertNotNull(conn);

        conn.close();
        assertTrue(conn.isClosed());
    }
}
