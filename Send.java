import hsa.*;
public class Send {
    public static void main(String[] args){
	int error = -1;
	do{
	    System.out.println("IP Address: ");
	    String ipAddress = Stdin.readLine();
	    System.out.print("Message: ");
	    String message = Stdin.readLine();
	    error = NetIO.sendRequest(message, ipAddress);
	}while (true);
    }
}
