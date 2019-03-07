import java.io.*;
public class MainTester
{
    public static void main (String[] args)
    {
	Message messages = new Message();
	Globals.availableList = new AvailableList();
	int error = FileIO.openMessagesFile("_messages.dat");
	if (error == Globals.PROCESS_OK){
	    //messages.deleteFromMessagesFile(1);
	    //System.out.println(Globals.availableList);
	    messages.deleteFromMessagesFile(0);
	    System.out.println(Globals.availableList);
	    messages.deleteFromMessagesFile(5);
	    System.out.println(Globals.availableList);
	    FileIO.closeMessagesFile();
	}
	else
	    System.out.println("Error opening messaging file");
    }
}
