import java.io.*;
import java.util.Date;
public class EmailServer{
    public static String previousClientIPAddress = Globals.STR_NULL;
    public static long previousArrivalTime = 0;
    public static long currentArrivalTime = 0;
    
    public static void main(String args[]){
	int error = Init.initializeSystem();
	
	if (error == Globals.PROCESS_OK){
	    Message message = new Message();
	    int recordNumber = 0;
	    TNode p = null;
	    TNode q = null;
	    TNode r = null;
	    
	    String identification = Globals.STR_NULL;
	    char serverCommand = 0;
	    
	    do{
		System.out.println ("Waiting...");
		String request = NetIO.receiveRequest();
		serverCommand = request.charAt(0);
		switch(serverCommand){
		    case Globals.SEND_MESSAGE:
			request = Utils.setRecevingTime (request);
			message = new Message (request);
			recordNumber = message.writeToMessagesFile();
			//for sender tree
			identification = message.getIdSenderFirst();
			p = new TNode (identification, recordNumber, null,null,null);
			Globals.senderIndex.insertNode(p);
			//for receiver tree
			identification = message.getIdReceiverFirst();
			p = new TNode (identification, recordNumber, null,null,null);
			Globals.receiverIndex.insertNode(p);
			System.out.println (message);
			
			
			System.out.println(message.getIdSenderFirst());
			r = Globals.senderIndex.findNode(message.getIdSenderFirst());
			message.readFromMessagesFile(r.getRecordNumber());
			System.out.println (message);
			System.out.println();
			System.out.println(message.getIdReceiverFirst());
			r = Globals.receiverIndex.findNode(message.getIdReceiverFirst());
			message.readFromMessagesFile(r.getRecordNumber());
			System.out.println(message);
			
			break;
		    default :
			System.out.println ("Unknown request : " + request);
			break;
			
			case Globals.IN_BOX :
			    identification = request.substring(Globals.CLIENT_POS);
			    p = Globals.receiverIndex.findNode(identification, 0);
			    q = Globals.receiverIndex.findNode(identification, 1);
    
			    Globals.receiverIndex.prepareTransmissionString(p, q);
			    Globals.transmissionString = Globals.transmissionString + Utils.intToBytesStr(Globals.END_OF_MESSAGES_TRANSMISSION);
			    error = NetIO.sendRequest(Globals.transmissionString, Globals.clientIPAddress);
			    Globals.transmissionString = Globals.STR_NULL;
			    break;

			case Globals.OUT_BOX :
			    identification = request.substring(Globals.CLIENT_POS);
    
			    p = Globals.senderIndex.findNode(identification, 0);
			    break;

		}
	    } while (serverCommand != Globals.SERVER_SHUTDOWN);
	    
	    /*TNode p1 = Globals.senderIndex.findNode (id);
	    System.out.println(p);
	    recordNumber = p.getRecordNumber();
	    message.readFromMessagesFile(recordNumber);
	    System.out.println(message);*/
	}
	else
	    ErrorReport.report(0);
    }
}
