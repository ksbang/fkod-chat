
package client;

public class ClientMain {
	public static void main(String[] args) {
		ClientServiceImpl client = new ClientServiceImpl();
		Thread user = new Thread(client);
		client.setThisThread(user);
		user.start();
	}
}
