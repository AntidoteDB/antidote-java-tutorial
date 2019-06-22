
import java.util.List;

import java.util.Random;
import java.io.IOException;

import java.net.InetSocketAddress;

import eu.antidotedb.client.*;

public class Tester {

    final private String stateBucket = "tutorial";
    final private int taskNum = 11;
    private String saveObject;
    private AntidoteClient tutorialStateSession1;
    private AntidoteClient tutorialStateSession2;
    private AntidoteClient taskSession;

    final private String task4Host="antidote1";
    final private int task4Port = 8087;
    final private String tutBucket = "tutorial_buck";
    final private String task1Counter = "task1_counter";
    final private String task1Set = "task1_set";
    final private String task5Register = "task5_register";
    final private String task5Value = "task5_value";
    final private String task6Map = "task6_map";
    final private String task6EntryKey = "key";
    final private String task6Value = "value";
    final private String user1 = "Bob";
    final private String user1Mail = "bob@mail.com";
    final private String user2 = "Alice";
    final private String bookTitle = "book1";

    public void test() throws IOException {
        saveObject = "save_" + System.getenv("ANTIDOTE_TUTORIAL_SAVE");
        tutorialStateSession1 = new AntidoteClient(new InetSocketAddress("antidote1", 8087));
        tutorialStateSession2 = new AntidoteClient(new InetSocketAddress("antidote2", 8087));
        int state = 0; boolean passed;
        while (state < taskNum) {
            passed = false;
            state = getSavedState();
            switch (state) {
                case 0:
                    welcome();
                    advanceSavedState();
                    System.exit(0);
                    break;
                case 1:
                    printTask01();
                    passed = testTask01();
                    break;
                case 2:
                    printTask02();
                    passed = testTask02();
                    break;
                case 3:
                    printTask03();
                    passed = testTask03();
                    break;
                case 4:
                    printTask04();
                    passed = testTask04();
                    break;
                case 5:
                    printTask05();
                    passed = testTask05();
                    break;
                case 6:
                    printTask06();
                    passed = testTask06();
                    break;
                case 7:
                    printTask07();
                    passed = testTask07();
                    break;
                case 8:
                    printTask08();
                    passed = testTask08();
                    break;
                case 9:
                    printTask09();
                    passed = testTask09();
                    break;
                case 10:
                    printTask10();
                    passed = testTask10();
                    break;
                case taskNum:
                    finished();
                    break;
                default:
                    Random rand = new Random(System.currentTimeMillis());
                    saveObject = "save_" + Integer.toString(rand.nextInt());
            }
            if (passed) {
                if (state > 0 && state < taskNum)
                    printPass();
                advanceSavedState();
            } else {
                pringFail();
                System.exit(0);
            }
        }
    }

    private boolean testTask01() {
        int counter= DemoCommandsExecutor.getCounter(tutorialStateSession1, tutBucket, task1Counter);
        List<String> set = DemoCommandsExecutor.getSet(tutorialStateSession1, tutBucket, task1Set);
        if (counter > 3 && set.size() > 2)
            return true;
        return false;
    }

    private boolean testTask02() {
        int counter1 = DemoCommandsExecutor.getCounter(tutorialStateSession1, tutBucket, task1Counter);
        int counter2 = DemoCommandsExecutor.getCounter(tutorialStateSession2, tutBucket, task1Counter);
        List<String> set1 = DemoCommandsExecutor.getSet(tutorialStateSession1, tutBucket, task1Set);
        List<String> set2 = DemoCommandsExecutor.getSet(tutorialStateSession2, tutBucket, task1Set);
        if (counter1 > counter2 && set1.size() < set2.size())
            return true;
        return false;
    }

    private boolean testTask03() {
        int counter1 = DemoCommandsExecutor.getCounter(tutorialStateSession1, tutBucket, task1Counter);
        int counter2 = DemoCommandsExecutor.getCounter(tutorialStateSession2, tutBucket, task1Counter);
        List<String> set1 = DemoCommandsExecutor.getSet(tutorialStateSession1, tutBucket, task1Set);
        List<String> set2 = DemoCommandsExecutor.getSet(tutorialStateSession2, tutBucket, task1Set);
        if (counter1 == counter2 && set1.size() == set2.size())
            return true;
        return false;
    }

    private boolean testTask04() {
        try {
            taskSession = new BookCommands().connect(task4Host, task4Port);
        } catch (java.lang.RuntimeException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    private boolean testTask05() {
        BookCommands bookCmds = new BookCommands();
        try {
            bookCmds.assignToRegister(tutorialStateSession1, tutBucket, task5Register, task5Value);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        String regValue = Bucket.bucket(tutBucket).read(tutorialStateSession1.noTransaction(),  Key.register(task5Register));
        if (regValue.equals(new String(task5Value)))
            return true;
        return false;
    }

    private boolean testTask06() {
        BookCommands bookCmds = new BookCommands();
        try {
            bookCmds.updateMapRegister(tutorialStateSession1, tutBucket, task6Map, task6EntryKey, task6Value);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        MapKey.MapReadResult mapReadResult = Bucket.bucket(tutBucket).read(tutorialStateSession1.noTransaction(), Key.map_rr(task6Map));
        if (mapReadResult.get(Key.register(task6EntryKey)).equals(task6Value))
            return true;
        return false;
    }

    private boolean testTask07() {
        BookCommands bookCmds = new BookCommands();
        try {
            if (bookCmds.getEmail(tutorialStateSession1, user1).equals(user1Mail))
                return true;
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private boolean testTask08() {
      BookCommands bookCmds = new BookCommands();
        try {
          if (bookCmds.getOwnedBooks(tutorialStateSession1, user1).contains(new String(bookTitle)))
                return true;
            return false;
        } catch(Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private boolean testTask09() {
      BookCommands bookCmds = new BookCommands();
        try {
          if (!bookCmds.getOwnedBooks(tutorialStateSession1, user1).contains(new String(bookTitle)) && bookCmds.getBorrowedBooks(tutorialStateSession1, user2).contains(new String(bookTitle)))
                return true;
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private boolean testTask10() {
      BookCommands bookCmds = new BookCommands();
        try {
            if (bookCmds.getOwnedBooks(tutorialStateSession1, user1).contains(new String(bookTitle)) && !bookCmds.getBorrowedBooks(tutorialStateSession1, user2).contains(new String(bookTitle)))
                return true;
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private void welcome() {
        String welcomeMsg = "\nWelcome to the AntidoteDB Java Tutorial!\n\n" +
                "The aim of this tutorial is to familiarize the user with the basic " +
                "features, data model and API \nof AntidoteDB, and to demonstrate " +
                "the use of AntidoteDB as backend database.\n" +
                "Tasks 1 - 3 introduce the data model of AntidoteDB, and demonstrate that " +
                "the database remains \navailable under network partitions and the state o f" +
                "replicas converges when connectivity is restored.\n" +
                "Tasks 4 - 6 introduce the java AntidoteDB client interface.\n" +
                "Tasks 7 - 10 demonstrate how to use AntidoteDB as a backend database by " +
                "building a simple Bookstore \napplication.\n" +
                "============================\n\n" +
                "Run this executable again to proceed to the first task.\n";

        System.out.println(welcomeMsg);
    }


    private void printTask01() {
        String msg = "\nTask 01 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "Follow Step 3 in README.md to create two new shells with two instances of the shell application.\n" +
                "This demo application is a small shell that uses AntidoteDB as a backend database. Type:\n" +
                "bookstore@antidote> ?l \nto see the list of available commands, and:\n" +
                "bookstore@antidote> ?help <some_command>\nto see details about a command.\n\n" +
                "To pass this task, the counter \"" + task1Counter + "\" needs to have a value greater that 3, and the set\n" +
                "\"" + task1Set + "\" needs to have at least 3 elements, both in bucket \"" + tutBucket + "\".\n" +
                "You can try to update the same objects in both shells to see how AntidoteDB propagates updates \nbetween replicas.\n\n" +
                "Hint: this command increments a counter: inc <bucket_name> <counter_name>\n" +
                "Hint: this command adds an item to a set: additem <bucket_name> <set_name> <item_value>\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask02() {
        String msg = "\nTask 02 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "In this task we will simulate a network partition. We will see how users can perform updates\n" +
                "to AntidoteDB even in the presence of partitions.\n" +
                "To disrupt the communication between the two AntidoteDB replicas do:\n" +
                "in setup/\n./disconnect.sh\n" +
                "You can now continue to issue commands in bookstore@antidote1 and bookstore@antidote2.\n" +
                "Notice that the values at each replica diverge\n\n" +
                "To pass this task, the counter \"" + task1Counter + "\" needs to have a greater value in bookstore@antidote1 than\n" +
                "in bookstore@antidote2, and the set \"" + task1Set + "\" needs to have less elements in bookstore@antidote1 than\n" +
                "in bookstore@antidote2 (both in bucket \"" + tutBucket + "\").\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask03() {
        String msg = "\nTask 03 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "Let's now reconnect the two antidote replicas:\n" + "in setup/\n./connect.sh\n" +
                "Read the values of \"" + task1Counter + "\" and \"" + task1Set + "\". " +
                "Notice that the values of the CRDTs have converged to \nreflect updated committed to both replicas during the network partition.\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask04() {
        String msg = "\nTask 04 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "Let's now take a look at the interface of the AntidoteDB Java client.\n" +
                "In each task you will be asked to implement a class method using the Java client API.\n" +
                "All methods are located in bookstore/src/main/java/BookCommands.java .\n" +
                "In this task you will learn to open a connection to an AntidoteDB node.\n" +
                "Implement the connect() method. The method should receive as arguments a hostname and a port number,\n" +
                "initiates a connection to the corresponding AntidoteDB node, and return the connection object\n" +
                "To re-build the application after modifying the source code:\n" +
                "root@tutorial$ ./gradlew build\n" +
                "================================\n\n" +
                "Here is a useful reference for the rest of the tasks:\n" +
                "https://www.javadoc.io/doc/eu.antidotedb/antidote-java-client/0.3.0\n\n" +
                "Note: Remember to recompile the source code after making changes by invoking \"./gradlew build\" \nin bookstore/\n";
        System.out.println(msg);
    }

    private void printTask05() {
        String msg = "\nTask 05 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "You have so far familiarized with the Antidote data model by using the shell commands for updating\nCRDT counters and sets.\n" +
                "In this task you will learn how to start and commit a transaction, and how to update CRDT objets,\n" +
                "using the example of a last-writer-wins register.\n" +
                "Implement the assignToRegister() method. The method should receive a bucket name, an object key,\n" +
                "and a string value, and assign the given value to a CRDT last-writer-wins register with the given \nkey, in the given bucket.\n" +
                "================================\n"
                ;
        System.out.println(msg);
    }

    private void printTask06() {
        String msg = "\nTask 06 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "For this task, let's see how we can update a more complex CRDT datatype.\n" +
                "Implement the updateMapRegister() method. The method should receive a bucket name, an object key,\n" +
                "a map entry key, and a string value, and add a new entry to a CRDT map with the given object key, in the given bucket. " +
                "The new entry should be a last-writer-wins register with the given map entry key.\n" +
                "Finally, assign the given value to the register.\n" +
                "Use the remove-resets map datatype (map_rr).\n"+
                "================================\n";
        System.out.println(msg);
    }

    private void printTask07() {
        String msg = "\nTask 07 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "Now that you have seen the basics of using the AntidoteDB Java client, let's put this knowledge in use in \norder to build an application that uses AntidoteDB as a backend database.\n" +
                "As you may have noticed, the shell application has a few additional commands such as userinfo,\nadduser, and ownbook. " +
                "These commands implement a simple Bookstore application in which users can \ncreate accounts, register the books they own, and borrow books from other users.\n" +
                "For this task, implement the addUser() and getEmail() methods (also located in \nbookstore/src/main/java/BookCommands.java)\n" +
                "AddUser receives a username and an email as strings. It should store these information in \nthe database using the proper data structure.\n" +
                "GetEmail receives a username. It should return the email that corresponds to the given username.\n"+
                "Finally, use the adduser shell command to add a user with username \"" + user1 + "\" and email \"bob@mail.com\".\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask08() {
        String msg = "\nTask 08 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "For this task, implement the addOwnedBooks() and getOwnedBooks() methods.\n" +
                "AddOwnedBooks receives a username and a book title as strings. It should add the given book to the \nset of books owned by the given user.\n" +
                "GetOwnedBooks receives a username and should return the set of books owned by the given user.\n" +
                "Finally, use the ownbook shell command to add a book \"" + bookTitle + "\" to the user \"" + user1 + "\".\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask09() {
        String msg = "\nTask 09 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "Another Bookstore functionality implementation task.. But this is an interesting one.\n" +
                "Implement the borrowBook() and getBorrowedBooks() methods.\n" +
                "BorrowBook receives a book title, the username of the user who owns the book (userFrom) and the user \nwho borrows the book (userTo).\n" +
                "It should remove the given book from the set of books owned by userFrom, and add it to the set of \nbooks which userTo has borrowed.\n" +
                "getBorrowedBooks receives a username and should return the set of book that the given user has \nborrowed.\n" +
                "Finally, use the borrowbook shell command to indicate that user \"" + user2 + "\" borrows \"" + bookTitle + "\" from user \n\"" + user1 + "\".\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printTask10() {
        String msg = "\nTask 10 of the AntidoteDB Java Tutorial\n" +
                "--------------------------------\n" +
                "For the final task, implement the returnBook() method.\n" +
                "The method receives a book title, the username of the user who has borrowed the book (userFrom), and the username of the user to whom the book is returned to (userTo).\n" +
                "It should remove the given book from the set of books borrowed by userFrom, and add it to the set of books owned by userTo.\n" +
                "Finally, use the returnbook shell command to indicate that user \"" + user2 + "\" returns \"" + bookTitle + "\" to user \n\"" + user1 + "\".\n" +
                "================================\n";
        System.out.println(msg);
    }

    private void printPass() { System.out.println("SUCCESS\n"); }

    private void pringFail() {
        System.out.println("FAIL\nYou can retry by running this executable again.\n\n"); }

    private void finished() {
        System.out.println("\nYou have completed the AntidoteDB Java Tutorial. Well done!\n");
        System.exit(0);
    }

    private int getSavedState() {
        return DemoCommandsExecutor.getCounter(tutorialStateSession1, stateBucket, saveObject);
    }
    private void advanceSavedState() {
        DemoCommandsExecutor.incCounter(tutorialStateSession1, stateBucket, saveObject);
    }
}
