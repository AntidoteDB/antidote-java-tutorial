import java.io.IOException;
import java.util.List;

import eu.antidotedb.client.AntidoteClient;
import java.net.InetSocketAddress;

import asg.cliche.Command;
import asg.cliche.ShellFactory;

public class BookStore {
	
	private AntidoteClient currentSession;
	
	/* *** Demo Commands *** */
	
	@Command
	public String hello() {
	    return "Hello, World!";
	}
	
	@Command //test command
	public int inc(String bucket, String key){
		return DemoCommandsExecutor.incCounter(currentSession, bucket, key);
	}
	
	@Command
	public int getcounter(String bucket, String key){
		return DemoCommandsExecutor.getCounter(currentSession, bucket, key);
	}
	
	@Command 
	public List<String> additem(String bucket, String key, String item){
		return DemoCommandsExecutor.addToSet(currentSession, bucket, key, item);
	}
	
	@Command
	public List<String> getset(String bucket, String key){
		return DemoCommandsExecutor.getSet(currentSession, bucket, key);
	}
	
	@Command //connect antidote
	public String connect(String host, int port){
		currentSession = new AntidoteClient(new InetSocketAddress(host, port));
		return "Connected to " + host+":"+port;
	}
	
	@Command //exit shell
	public void quit(){
		System.out.println("Exiting app...");
		System.exit(0);
	}
	
	/* *** BookStore commands *** */
	
	@Command
	public void userinfo(String username){
		new BookCommands().getUserInfo(currentSession, username);
	}
	
	@Command
	public String adduser(String username, String userEmail){
		return (new BookCommands().addUser(currentSession, username, userEmail));
	}
	
	@Command
	public String ownbook(String username, String book){
		return (new BookCommands().addOwnedBooks(currentSession, username, book));
	}
	
	@Command
	public List<String> getownedbook(String username){
		return (new BookCommands().getOwnedBooks(currentSession, username));
	}
	
	@Command
	public String removeownedbook(String username, String book){
		return (new BookCommands().removeOwnedBook(currentSession, username, book));
	}
	
	@Command
	public String borrowbook(String fromUser, String byUser, String book){		
		return (new BookCommands().borrowBook(currentSession, fromUser, byUser, book));
	}
	
	@Command
	public List<String> getborrowedbook(String username){
		return (new BookCommands().getBorrowedBooks(currentSession, username));
	}
	
	@Command
	public String returnbook(String fromUser, String toUser, String book){		
		return (new BookCommands().returnBook(currentSession, fromUser, toUser, book));
	}
	
	public static void main(String[] args) throws IOException {
	    ShellFactory.createConsoleShell("bookstore@antidote"+args[0], "", new BookStore())
	        .commandLoop();
	}
}
