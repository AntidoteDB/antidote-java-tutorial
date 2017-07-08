import java.io.IOException;

import eu.antidotedb.client.AntidoteClient;
import eu.antidotedb.client.Host;

import asg.cliche.Command;
import asg.cliche.ShellFactory;

public class BookStore {
	
	AntidoteClient currentSession;
	
	/* *** Demo Commands *** */
	
	@Command // One,
	public String hello() {
	    return "Hello, World!";
	}
	
	@Command //test command
	public int inccounter(String bucket, String key){
		return DemoCommandsExecutor.incCounter(currentSession, bucket, key);
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
	// TODO
	
	public static void main(String[] args) throws IOException {
	    ShellFactory.createConsoleShell("antidotebookstore", "", new BookStore())
	        .commandLoop();
	}
}
