public class AvailableList {
    private Available head = null;
    private Available tail = null;
    
    public AvailableList (){
	this.head = null;
	this.tail = null;
    }
    public AvailableList (Available h, Available t){
	this.head = h;
	this.tail = t;
    }
    
    public Available getHead(){
	return this.head;
    }
    
    public Available getTail(){
	return this.tail;
    }
    
    public void setHead(Available h){
	 this.head = h;
    }
    
    public void setTail(Available t){
	 this.tail = t;
    }
    
    public void addRecord (int recordNum){
	if (this.head == null){
	    this.head = new Available (recordNum);
	    this.tail = this.head;
	}
	else{
	    this.tail.setNext(new Available (recordNum));
	    this.tail = this.tail.getNext();
	}
    }
    
    public int getNextRecord(){
	if (this.head == null){
	    return Globals.EMPTY_AVAILABLE_LIST;
	}
	else{
	    int rec = this.head.getRecordNumber();
	    this.head = this.head.getNext();
	    this.tail = this.head == null ? null:this.tail ;
	
	    return rec;
	}
    }
    
    public String toString (){
	String s = "";
	for (Available p = head; p!=null; p = p.getNext()){
	    s += p.toString() + "\n";
	}
	return s;
    }
}
