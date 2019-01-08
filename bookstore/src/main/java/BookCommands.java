import java.util.List;

import eu.antidotedb.client.*;

import static eu.antidotedb.client.Key.register;


public class BookCommands {

	String userBucket = "userbucket";

	private static final RegisterKey<String> emailMapField = register("email");
	private static final SetKey<String> ownBooksMapField = Key.set("ownbooks");
	private static final SetKey<String> borrowedBooksMapField = Key.set("borrowedbooks");
			
	public void getUserInfo(AntidoteClient client, String username){
		System.out.println("User: " + username);
		System.out.println("Email: " + getEmail(client, username));
		System.out.println("Owned Books: " + getOwnedBooks(client, username));
		System.out.println("Borrowed Books: " + getBorrowedBooks(client, username));
	}
	
	public String addUser(AntidoteClient client, String username, String userEmail){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		bucket.update(client.noTransaction(),
				userInfo.update(emailMapField.assign(userEmail)));
		return "ok";
	}

	public String getEmail(AntidoteClient client, String username){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		MapKey.MapReadResult mapReadResult = bucket.read(client.noTransaction(),
				 userInfo);
		return mapReadResult.get(emailMapField);
	}

	public String addOwnedBooks(AntidoteClient client, String username, String book){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		bucket.update(client.noTransaction(),
				userInfo.update(ownBooksMapField.add(book)));
		return "added " + book;
	}
	
	public List<String> getOwnedBooks(AntidoteClient client, String username){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		MapKey.MapReadResult mapReadResult = bucket.read(client.noTransaction(),
				userInfo);
		return mapReadResult.get(ownBooksMapField);
	}
	
	public String removeOwnedBook(AntidoteClient client, String username, String book){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		bucket.update(client.noTransaction(),
				userInfo.update(ownBooksMapField.remove(book)));
		return "removed " + book;
	}
	
	public String borrowBook(AntidoteClient client, String fromUser, String byUser, String book){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey fromUserInfo = Key.map_rr(fromUser);
		MapKey byUserInfo = Key.map_rr(byUser);
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		bucket.update(tx,
				fromUserInfo.update(ownBooksMapField.remove(book)));
		bucket.update(tx,
				byUserInfo.update(borrowedBooksMapField.add(book)));
		tx.commitTransaction();
		return "done";
	}
	
	public String returnBook(AntidoteClient client, String fromUser, String toUser, String book){
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey fromUserInfo = Key.map_rr(fromUser);
		MapKey toUserInfo = Key.map_rr(toUser);
		AntidoteStaticTransaction tx = client.createStaticTransaction();
		bucket.update(tx,
				fromUserInfo.update(borrowedBooksMapField.remove(book)));
		bucket.update(tx,
				toUserInfo.update(ownBooksMapField.add(book)));
		tx.commitTransaction();
		return "done";
	}
	
	public List<String> getBorrowedBooks(AntidoteClient client, String username) {
		Bucket bucket = Bucket.bucket(userBucket);
		MapKey userInfo = Key.map_rr(username);
		MapKey.MapReadResult mapReadResult = bucket.read(client.noTransaction(),
				userInfo);
		return mapReadResult.get(borrowedBooksMapField);
	}
	
	private void not_implemented(){
		throw new RuntimeException("Not Implemented");
	}
}
