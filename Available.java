public class Available{
    private int recordNumber =-1;
    private Available next = null;
    
    public Available (){
	this.recordNumber = 1;
	this.next = null;
    }
    
    public Available (int n){
	this.recordNumber = n;
	this.next = null;
    }
    
    public int getRecordNumber (){
	return this.recordNumber;
    }
    
    public Available getNext(){
	return this.next;
    }
    
    public void setRecordNumber (int n){
	this.recordNumber = n;
    }
    
    public void setNext(Available nxt){
	this.next = nxt;
    }
    
    public String toString(){
	return "" + this.recordNumber;
    }
}
