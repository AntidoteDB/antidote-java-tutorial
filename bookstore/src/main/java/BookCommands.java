import java.util.List;
import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.AntidoteStaticTransaction;
import eu.antidotedb.client.Bucket;
import eu.antidotedb.client.MapRef;


public class BookCommands {
		
	String userBucket = "userbucket";
	
	public void getUserInfo(AntidoteClient client, String username){
		System.out.println("User: " + username);
		System.out.println("Email: " + getEmail(client, username));
		System.out.println("Owned Books: " + getOwnedBooks(client, username));
		System.out.println("Borrowed Books: " + getBorrowedBooks(client, username));
	}
	
	public String addUser(AntidoteClient client, String username, String userEmail){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		userInfo.register("email").set(client.noTransaction(), userEmail);		
		return "ok";		
	}

	public String getEmail(AntidoteClient client, String username){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		return userInfo.register("email").read(client.noTransaction());
	}
	public String addOwnedBooks(AntidoteClient client, String username, String book){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		userInfo.set("ownbooks").add(client.noTransaction(), book);
		return "added " + book;
	}
	
	public List<String> getOwnedBooks(AntidoteClient client, String username){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		return userInfo.set("ownbooks").read(client.noTransaction());
	}
	
	public String removeOwnedBook(AntidoteClient client, String username, String book){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		userInfo.set("ownbooks").remove(client.noTransaction(), book);
		return "removed " + book;
	}
	
	public String borrowBook(AntidoteClient client, String fromUser, String byUser, String book){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> fromUserInfo = bucket.map_aw(fromUser);
		MapRef<String> byUserInfo = bucket.map_aw(byUser);
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		fromUserInfo.set("ownbooks").remove(tx, book);
		byUserInfo.set("borrowedbooks").add(tx, book);	
		tx.commitTransaction();		
		return "borrowed " + book;
	}
	
	public String returnBook(AntidoteClient client, String fromUser, String toUser, String book){
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> fromUserInfo = bucket.map_aw(fromUser);
		MapRef<String> toUserInfo = bucket.map_aw(toUser);
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		fromUserInfo.set("borrowedbooks").remove(tx, book);
		toUserInfo.set("ownbooks").add(tx, book);	
		tx.commitTransaction();		
		return "done";
	}
	
	public List<String> getBorrowedBooks(AntidoteClient client, String username) {
		Bucket<String> bucket = Bucket.create(userBucket);
		MapRef<String> userInfo = bucket.map_aw(username);
		return userInfo.set("borrowedbooks").read(client.noTransaction());
	}
	
	void not_implemented(){
		throw new RuntimeException("Not Implemented");
	}
}
