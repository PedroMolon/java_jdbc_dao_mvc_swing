package view.users;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import controllers.users.UserController;
import model.User;

public class Form extends JDialog{

	private static final long serialVersionUID = 1L;

	private static Form form = new Form();

	private JTextField jtfName;
	private JTextField jtfLogin;
    private User userToUpdate = null;

	private JButton jbSave;
	private JButton jbCancel;

	public Form() {
		createForms();
		createButtons();
		registerListeners();
		configure();
	}

	private void configure(){
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(this.getRootPane());
	}

	private void createForms(){
		JPanel jpForm = new JPanel(new GridLayout(2, 1, 0, 5));

		jpForm.setBorder(BorderFactory.createTitledBorder("Dados Pessoais"));

		jpForm.add(fieldset(new JLabel("Nome: "),
                jtfName = new JTextField(30)));
        jtfName.setName("nameField");

		jpForm.add(fieldset(new JLabel("Login: "),
                jtfLogin = new JTextField(30)));
        jtfLogin.setName("loginField");

		this.add(jpForm, BorderLayout.CENTER);
	}

	private JPanel fieldset(JComponent...components){
		JPanel fieldset = new JPanel();
		for (JComponent component : components) {
			fieldset.add(component);
		}
		return fieldset;
	}

	private void createButtons(){
		JPanel jpButtons = new JPanel();

		jpButtons.add(jbSave = new JButton("Salvar"));
        jbSave.setName("saveButton");
		jpButtons.add(jbCancel = new JButton("Cancelar"));
        jbCancel.setName("cancelButton");

		this.add(jpButtons, BorderLayout.SOUTH);
	}

	private void registerListeners() {
		jbSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdSave();
			}
		});
		jbCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cmdCancel();
			}
		});
	}

	private void cmdSave(){
		try {
            if (userToUpdate == null) { // Modo de Criação
                User user = new User(jtfName.getText(), jtfLogin.getText());
                UserController.getInstance().save(user, true); // isNew = true
            } else { // Modo de Edição
                userToUpdate.setName(jtfName.getText());
                userToUpdate.setLogin(jtfLogin.getText());
                UserController.getInstance().save(userToUpdate, false); // isNew = false
            }
			JOptionPane.showMessageDialog(this, "Usuário Salvo Com Sucesso", "", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		}
	}

	private void cmdCancel(){
		dispose();
	}

	private void clearForm(JTextComponent... jtcomponets){
		for (JTextComponent component : jtcomponets) {
			component.setText("");
		}
	}

	@Override
	public void dispose(){
		super.dispose();
		clearForm(jtfName, jtfLogin);
		this.userToUpdate = null;
	}

	public static void toggle(){
		form.setVisible(!form.isVisible());
	}

    public void loadData(User user) {
        this.userToUpdate = user;
        jtfName.setText(user.getName());
        jtfLogin.setText(user.getLogin());
    }

    public static void toggle(User user) {
        form.loadData(user);
        toggle();
    }
}