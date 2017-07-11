import java.io.IOException;
import java.util.List;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Host;

import asg.cliche.Command;
import asg.cliche.ShellFactory;

public class BookStore {
	
	AntidoteClient currentSession;
	
	/* *** Demo Commands *** */
	
	@Command
	public String hello() {
	    return "Hello, World!";
	}
	
	@Command //test command
	public int inccounter(String bucket, String key){
		return DemoCommandsExecutor.incCounter(currentSession, bucket, key);
	}
	
	@Command 
	public List<String> addtoset(String bucket, String key, String item){
		return DemoCommandsExecutor.addToSet(currentSession, bucket, key, item);
	}
	
	@Command //connect antidote
	public String connect(String host, int port){
		currentSession = new AntidoteClient(new Host(host, port));
		return "Connected to " + host+":"+port;
	}
	
	@Command //exit shell
	public void quit(){
		System.out.println("Exiting app...");
		System.exit(0);
	}
	
	/* *** BookStore commands *** */
	
	@Command
	public String adduser(String username, String userEmail){
		return (new BookCommands().addUser(username, userEmail));
	}
	
	@Command
	public String ownbook(String username, String book){
		return (new BookCommands().addOwnedBooks(username, book));
	}
	
	@Command
	public String[] getownedbook(String username){
		return (new BookCommands().getOwnedBooks(username));
	}
	
	@Command
	public String removeownedbook(String username, String book){
		return (new BookCommands().removeOwnedBook(username, book));
	}
	
	@Command
	public String borrowbook(String fromUser, String byUser, String book){		
		return (new BookCommands().borrowBook(fromUser, byUser, book));
	}
	
	public static void main(String[] args) throws IOException {
	    ShellFactory.createConsoleShell("antidotebookstore", "", new BookStore())
	        .commandLoop();
	}
}
