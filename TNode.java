public class TNode {
    private String identification = "";
    private int recordNumber = -1;
    private TNode left = null;
    private TNode right = null;
    private TNode parent = null;
    private int height = 1;
    
    public TNode()  {
	identification = "";
	recordNumber = -1;
	left = null;
	right = null;
	parent = null;
    }
    
    public TNode(String i, int rn, TNode l, TNode r, TNode p)   {
	identification = i;
	recordNumber = rn;
	left = l;
	right = r;
	parent = p;
    }
    
    public String getIdentification()   {
	return identification;
    }
    
    public int getRecordNumber()    {
	return recordNumber;
    }
    
    public TNode getLeft()  {
	return left;
    }
    
    public TNode getRight() {
	return right;
    }
    
    public TNode getParent()    {
	return parent;
    }
    
    public int getHeight()  {
	return height;
    }
    
    public void setIdentification(String id)    {
	identification = id;
    }
    
    public void setRecordNumber(int rn) {
	recordNumber = rn;
    }
    
    public void setLeft(TNode l)   {
	left = l;
    }
    
    public void setRight(TNode r)   {
	right = r;
    }
    
    public void setParent(TNode p)  {
	parent = p;
    }
    
    public void setHeight(int h)    {
	height = h;
    }
    
    public String toString()    {
	return "ID: " + identification + " Record Number: " + recordNumber;
    }
}
