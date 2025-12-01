package controllers.users.listeners;

import java.util.EventListener;

import model.User;

public interface UserListener {

	public void useradd(MailEvent<User> event);

	

	public void userUpdated(MailEvent<User> event);

}
