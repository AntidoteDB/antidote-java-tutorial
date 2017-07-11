import java.util.List;
import eu.antidotedb.client.AntidoteClient;


public class BookCommands {
			
	public void getUserInfo(AntidoteClient client, String username){
		System.out.println("User: " + username);
		System.out.println("Email: " + getEmail(client, username));
		System.out.println("Owned Books: " + getOwnedBooks(client, username));
		System.out.println("Borrowed Books: " + getBorrowedBooks(client, username));
	}
	
	public String addUser(AntidoteClient client, String username, String userEmail){
		not_implemented();
		return null;
	}

	public String getEmail(AntidoteClient client, String username){
		not_implemented();
		return null;
	}
	public String addOwnedBooks(AntidoteClient client, String username, String book){
		not_implemented();
		return null;
	}
	
	public List<String> getOwnedBooks(AntidoteClient client, String username){
		not_implemented();
		return null;
	}
	
	public String removeOwnedBook(AntidoteClient client, String username, String book){
		not_implemented();
		return null;
	}
	
	public String borrowBook(AntidoteClient client, String fromUser, String byUser, String book){
		not_implemented();
		return null;
	}
	
	public String returnBook(AntidoteClient client, String fromUser, String toUser, String book){
		not_implemented();
		return null;
	}
	
	public List<String> getBorrowedBooks(AntidoteClient client, String username) {
		not_implemented();
		return null;
	}
	
	void not_implemented(){
		throw new RuntimeException("Not Implemented");
	}
}
