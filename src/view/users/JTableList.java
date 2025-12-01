package view.users;

import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.User;
import view.listeners.EventListerner;
import controllers.users.UserController;
import controllers.users.listeners.MailEvent;
import controllers.users.listeners.UserListener;

public class JTableList extends JTable implements UserListener, EventListerner {
	
	private static final long serialVersionUID = 1L;
	
	private TableModel model = new TableModel();

	public JTableList() {
		this.setModel(model);
		this.getTableHeader().setReorderingAllowed(false);
		UserController.getInstance().addUserListener(this);
		loadUsers();
	}
	
	public void loadUsers(){
		try {
			for (User user : UserController.getInstance().allUsers()) {
				model.insertRow(0, user.toArray());
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Erro", e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	
	private class TableModel extends DefaultTableModel{
		
		private static final long serialVersionUID = 1L;
		
		public TableModel() {
			super(new Object[][]{}, new String[] {"id", "Nome", "login"});	 
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
		
	}

	@Override
	public void useradd(MailEvent<User> event) {
		model.insertRow(0, event.getSource().toArray());
	}

	@Override
	public void userUpdated(MailEvent<User> event) {
		User updatedUser = event.getSource();
		for (int i = 0; i < model.getRowCount(); i++) {
			Long rowId = Long.parseLong((String) model.getValueAt(i, 0));
			if (rowId.equals(updatedUser.getId())) {
				model.setValueAt(updatedUser.getName(), i, 1);
				model.setValueAt(updatedUser.getLogin(), i, 2);
				break;
			}
		}
	}

	@Override
	public void cmdEdit() {
		if (this.getSelectedRow() != -1) {
            int row = this.getSelectedRow();
            Long userId = Long.parseLong((String) this.getValueAt(row, 0));
            try {
                User user = UserController.getInstance().findById(userId);
                    if (user != null) {
                        Form.toggle(user);
                    }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
	}

	@Override
	public void cmdRemove() {
		if (this.getSelectedRow() != -1) {
			int row = this.getSelectedRow();
			Long userId = Long.parseLong((String) this.getValueAt(row, 0));
			try {
				UserController.getInstance().remove(userId);
				model.removeRow(row);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void cmdDetails() {
		if (this.getSelectedRow() != -1) {
            int row = this.getSelectedRow();
            Long userId = Long.parseLong((String) this.getValueAt(row, 0));
            try {
                User user = UserController.getInstance().findById(userId);
                if (user != null) {
                    String details = "ID: " + user.getId() + "\n";
                    details += "Nome: " + user.getName() + "\n";
                    details += "Login: " + user.getLogin();
                    JOptionPane.showMessageDialog(this, details, "Detalhes do Usuário", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para ver os detalhes.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
	}
	
	@Override
	public void cmdAdd() {
		Form.toggle();
	}

}
