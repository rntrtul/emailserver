import java.io.*;
public class Record {
    private byte[] data = new byte[Globals.RECORD_DATA_LEN];
    private int next = Globals.END_OF_MESSAGE;
    
    public Record (){
	for (int i=0; i < data.length;i++){
	    data [i] = (byte) Globals.BLANK;
	}
	
	next = Globals.END_OF_MESSAGE; 
    }
    
    public Record (String s, int nextRecord){
	setData(s,nextRecord);
    }
    
    public String getData(){
	String message = "";
	
	for (int i =0 ;i < data.length;i++){
	    message += (char) ((data[i] + 256) % 256);
	}
	return message;
    }
    
    public byte getByte(int index){
	return data[index];
    }
    
    public void setData (String txt, int nextRecord){
	for (int i=0; i < data.length;i++){
	    if (i < txt.length())
		data [i] = (byte) txt.charAt(i);
	    else
		data [i] = (byte) Globals.BLANK;
	}
	
	next = nextRecord;
    }
    
    public int getNext(){
	return next;
    }
    
    public int readFromMessagesFile(int recordNumber){
	try{
	    Globals.msg.seek(Globals.RECORD_LEN * recordNumber); // finds specific record 
	    int bytes = Globals.msg.read(data); //records the number of bytes
	    next = Globals.msg.readInt(); //records 4 bytes
	    return Globals.PROCESS_OK;
	}
	catch(IOException e){
	    return Globals.PROCESS_ERROR;
	}
	
    }
    
    public int writeToMessagesFile(int recordNumber, int mode){
	try{
	    Globals.msg.seek(Globals.RECORD_LEN * recordNumber);
	    Globals.msg.write(data);
	    Globals.msg.writeInt(next);
	    
	    if(mode == Globals.APPEND){
		Globals.totalRecordsInMessagesFile++; 
	    }
	    return Globals.PROCESS_OK;
	}
	catch(IOException e){
	    return Globals.PROCESS_ERROR;
	}
    }
    
    public int deleteFromMessagesFile(int recordNumber){
	int error = readFromMessagesFile (recordNumber);
	if (error == Globals.PROCESS_OK){
	    data[0] = (byte) Globals.DELETED;
	    error = writeToMessagesFile(recordNumber,Globals.MODIFY);
	    if (error == Globals.PROCESS_OK)
		Globals.availableList.addRecord(recordNumber);
	    else
		System.out.println ("Error in writing to message file. IN t he sprg");
	}
	else
	    System.out.println ("Error reading from tht esmsaage. In meathod");
	
       return error;
    }
    
    public String toString(){
	return getData() + next;
    }
}
