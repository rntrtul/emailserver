import java.util.Date;
public class Message {
    private String text = "";
    private char command = 0;
    private String sender, receiver, dateTime = "";
    private char marker = 0;
    private String subject = "";
    private char eosMarker = 0;
    
    public Message(){
	char command = 0 ;
	String sender, receiver, dateTime = "";
	char marker = 0;
	String subject = "";
	char eosMarker = 0;
	String text = "";
    }
    
    public Message (String s){
	setMessage (s);
    }
    
    public void setMessage (String s){
	this.command = s.charAt(Globals.COMMAND_POS);
	this.sender = s.substring (Globals.SENDER_POS, Globals.RECEIVER_POS);
	this.receiver = s.substring (Globals.RECEIVER_POS, Globals.DATE_TIME_POS);
	this.dateTime = s.substring (Globals.DATE_TIME_POS , Globals.FIRST_RECORD_MARKER_POS);
	this.marker = s.charAt (Globals.FIRST_RECORD_MARKER_POS);
	this.subject = s.substring (Globals.FIRST_RECORD_MARKER_POS +1, s.indexOf(Globals.END_OF_SUBJECT_MARKER));
	this.eosMarker = s.charAt (s.indexOf(Globals.END_OF_SUBJECT_MARKER));
	this.text = s.substring (s.indexOf(Globals.END_OF_SUBJECT_MARKER)+1);
    }
    
    public String getMessage(){
	return this.command + this.sender + this.receiver + this.dateTime + this.marker+ this.subject + this.eosMarker+ this.text; 
    }
    
    public char getCommand (){
	return this.command;
    }
    
    public String getSender (){
	return this.sender;
    }
    
    public String getReceiver (){
	return this.receiver;
    }
    
    public String getText (){
	return this.text;
    }
    
    public void setText(String txt){
	this.text = txt;
    }
    
    public String getID(){
	return this.sender + this.receiver + this.dateTime;
    }
    
    public String getIdSenderFirst(){
	return this.sender + this.receiver + this.dateTime;
    }
    
    public String getIdReceiverFirst(){
	return this.receiver + this.sender + this.dateTime;
    }
    
    public void readFromMessagesFile(int recordNumbers){
	String data = "";
	Record record = new Record();
	
	do{
	    record.readFromMessagesFile(recordNumbers);
	    data += record.getData();
	    recordNumbers = record.getNext();
	}while(recordNumbers != Globals.END_OF_MESSAGE);
	
	text = data;
	setMessage(data);
    }
    
    public int writeToMessagesFile(){
	String s = getMessage();
	int nextRecordNumber = -1;
	int recordNumber = -1;
	int startRecordNumber = -1;
	if(Globals.availableList.getHead() == null){
	    startRecordNumber = Globals.totalRecordsInMessagesFile;
	}
	else{
	    startRecordNumber = Globals.availableList.getHead().getRecordNumber();
	}
	Record record = new Record();
	
	while (s != ""){
	    if (Globals.availableList.getHead() == null){
		recordNumber = Globals.totalRecordsInMessagesFile;
		if (s.length() <= Globals.RECORD_LEN){
		    record.setData(s,Globals.END_OF_MESSAGE);
		    record.writeToMessagesFile (Globals.totalRecordsInMessagesFile,Globals.APPEND);
		    s =  "";
		}
		else{
		    nextRecordNumber = recordNumber+1;
		    record.setData(s.substring(0,Globals.RECORD_DATA_LEN),nextRecordNumber);
		    record.writeToMessagesFile(recordNumber, Globals.APPEND);
		    s = s.substring(Globals.RECORD_DATA_LEN);
	       }
	    }
	    else{
		 recordNumber = Globals.availableList.getHead().getRecordNumber();
		 if(s.length() <= Globals.RECORD_LEN){
		    record.setData(s, Globals.END_OF_MESSAGE);
		    record.writeToMessagesFile(recordNumber, Globals.MODIFY);
		    s = "";
		 }
		 else{
		    if(Globals.availableList.getHead() == null){
			nextRecordNumber = Globals.totalRecordsInMessagesFile;
		    }
		    else{
			nextRecordNumber = Globals.availableList.getHead().getRecordNumber();
		    }
		    
		    record.setData(s.substring(0, Globals.RECORD_DATA_LEN), nextRecordNumber);
		    record.writeToMessagesFile(recordNumber, Globals.MODIFY);
		    s = s.substring(Globals.RECORD_DATA_LEN);
		 }
	    }
	}
	return startRecordNumber;
    }
    
   public void deleteFromMessagesFile(int recordNumber){
	Record record = new Record();
	
	while (recordNumber != -1){
	    record.deleteFromMessagesFile(recordNumber);
	    recordNumber = record.getNext();
	}
    }
    
    public String toString(){
	//Date date = new Date (Utils.bytesStrToLong (dateTime));
	return "Command     : " + this.command + "\n" +
	       "Sender      : " + this.sender + "\n" +
	       "Receiver    : " + this.receiver + "\n" +
	       "Date/Time   : " + "00000000\n" + // this.dateTime
	       "Marker      : " + this.marker + "\n" + 
	       "Subject     : " + this.subject + "\n" + 
	       "EOS Marker  : " + this.eosMarker + "\n" + 
	       "Message text: " + this.text + "\n";
    }

}
