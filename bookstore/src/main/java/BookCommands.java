import java.util.List;


public class BookCommands {
		
	public String addUser(String username, String userEmail){
		
		return null;		
	}

	public String addOwnedBooks(String username, String book){
		not_implemented();
		return null;
	}
	
	public List<String> getOwnedBooks(String username){
		not_implemented();
		return null;
	}
	
	public String removeOwnedBook(String username, String book){
		not_implemented();
		return null;
	}
	
	public String borrowBook(String fromUser, String byUser, String book){
		not_implemented();
		return null;
	}
	
	void not_implemented(){
		throw new RuntimeException("Not Implemented");
	}
}
