import java.io.*;
public class Tree {
    private TNode root = null;
    
    public Tree() {
	root = null;
    }
    
    public Tree(TNode r) {
	root = r;
    }
    
    public TNode getRoot() {
	return root;
    }
    
    public void setRoot(TNode r){
	root = r;
    }
    
    private void updateTreeHeights() {
	if (this.root != null)  {
	    int leftHeight = 0;
	    if(this.root.getLeft() != null){
		Tree lTree = new Tree(this.root.getLeft());
		lTree.updateTreeHeights();
		leftHeight = this.root.getLeft().getHeight(); 
	    }
	    int rightHeight = 0;
	    if(this.root.getRight() != null){
		Tree rTree = new Tree(this.root.getRight());
		rTree.updateTreeHeights();
		rightHeight = this.root.getRight().getHeight();
	    }
	    this.root.setHeight(Math.max(leftHeight, rightHeight) + 1);
	}    
    }    
	
    private void updateTreeHeightsAbove(TNode p)    {
	if (p != null)  {
	    for (TNode q = p.getParent(); q != null; q = q.getParent()) {
	    
	    }
	}
    }
    
    //calculate the height of the tree: one node is a height of 1
    
    public int height() {
	int treeHeight = 0;
	if (this.getRoot() != null) {
	    Tree lTree = new Tree(this.getRoot().getLeft());
	    int lHeight = 1 + lTree.height();
	    
	    Tree rTree = new Tree(this.getRoot().getRight());
	    int rHeight = 1 + rTree.height();
	    
	    treeHeight = lHeight > rHeight ? lHeight : rHeight;
	}
	return treeHeight;      
    }
    
    private int balanceFactor() {
	int leftHeight = 0;
	int rightHeight = 0;
	
	if (root.getLeft() != null){
	    leftHeight = root.getLeft().getHeight();
	}
	
	if(root.getRight() != null){
	    rightHeight = root.getRight().getHeight();
	}
	
	return leftHeight - rightHeight;
    }
    
    private void balance(TNode p) { 
	if (p != null) {
	    TNode ancestor = p;

	    while (ancestor != null) {
		Tree ancestorTree = new Tree(ancestor);            

		if (ancestorTree.balanceFactor() == -2) {
		    Tree rTree = new Tree(ancestorTree.root.getRight());                

		    int rTreeBalanceFactor = rTree.balanceFactor();
		    if (rTreeBalanceFactor == -1 || rTreeBalanceFactor == 0) { //0 happens in delete case 7a
			if (ancestor == root){
			    root = ancestorTree.leftRotate();
			    this.updateTreeHeights();
			}
			else { 

			    // determine if the ancestor is a left or a right child

			    if (ancestor.getParent().getLeft() == ancestor)
				ancestor.getParent().setLeft(ancestorTree.leftRotate()); 
			    else
				ancestor.getParent().setRight(ancestorTree.leftRotate());
			    ancestorTree.updateTreeHeights();
			    
			    updateTreeHeightsAbove(ancestor);  
			}
		    }
		    else if (rTreeBalanceFactor == 1 || rTreeBalanceFactor == 0) {
			ancestor.setRight(rTree.rightRotate());
			rTree.updateTreeHeights();
			if (ancestor == root){
			    root = ancestorTree.leftRotate();
			    this.updateTreeHeights();
			}
			else { // determine if the ancestor is a left or a right child

			    if (ancestor.getParent().getLeft() == ancestor)
				ancestor.getParent().setLeft(ancestorTree.leftRotate());
			    else
				ancestor.getParent().setRight(ancestorTree.leftRotate());
			    ancestorTree.updateTreeHeights();
			    
			    updateTreeHeightsAbove(ancestor);   
			}
		    }
		}
		else if (ancestorTree.balanceFactor() == 2) {
		    Tree lTree = new Tree(ancestorTree.root.getLeft());

		    int lTreeBalanceFactor = lTree.balanceFactor();
		    if (lTreeBalanceFactor == 1 || lTreeBalanceFactor == 0) { // 0 this symmetrical case of 7a does not happen. it's here to make the method symmetric and possible future optimization
		       if (ancestor == root){
			   root = ancestorTree.rightRotate();
			   this.updateTreeHeights();
		       }
		       else { // determine if the ancestor is a left or a right child

			   if (ancestor.getParent().getLeft() == ancestor)
			       ancestor.getParent().setLeft(ancestorTree.rightRotate());
			   else
			       ancestor.getParent().setRight(ancestorTree.rightRotate());
			   ancestorTree.updateTreeHeights();
			    
			   updateTreeHeightsAbove(ancestor);   
		       }
		    }
		    else if (lTreeBalanceFactor == -1 || lTreeBalanceFactor == 0) {
			ancestor.setLeft(lTree.leftRotate());
			lTree.updateTreeHeights();
			if (ancestor == root){
			   root = ancestorTree.rightRotate();
			   this.updateTreeHeights();
		       }
			else { // determine if the ancestor is a left or a right child
			    if (ancestor.getParent().getLeft() == ancestor)
				ancestor.getParent().setLeft(ancestorTree.rightRotate());
			    else
				ancestor.getParent().setRight(ancestorTree.rightRotate());
			   ancestorTree.updateTreeHeights();
			    
			   updateTreeHeightsAbove(ancestor);   
			}
		    }
		}
		ancestor = ancestor.getParent(); // ancestor may have changed in rotation. ancestor keeps moving up anyway and will reach the null of the root's parent
	    }
	}
    }
    
    public void insertNodeRec(TNode p) {
	if(root == null){
	    root = p;
	}
	else if (p.getIdentification().compareTo(root.getIdentification()) < 0){
	    if(root.getLeft() == null){
		root.setLeft(p);
		p.setParent(root);
	    }
	    else{
		Tree t = new Tree(root.getLeft());
		t.insertNodeRec(p);
	    }
	}
	else if (p.getIdentification().compareTo(root.getIdentification()) > 0){
	    if(root.getRight() == null){
		root.setRight(p);
		p.setParent(root);
	    }
	    else{
		Tree t = new Tree(root.getRight());
		t.insertNodeRec(p);
	    }
	}
	else{
	    System.out.println("Error inserting a node. Id already exists");
	}
	updateNodeHeight(root); 
    }
    
    public void insertNode(TNode p){
	insertNodeRec(p);
	balance(p);
    }
    
    private void updateNodeHeight(TNode p) {
	int leftHeight = p.getLeft() != null ? p.getLeft().getHeight() : 0;
	int rightHeight = p.getRight() != null ? p.getRight().getHeight() : 0;
	p.setHeight(Math.max(leftHeight, rightHeight) + 1);
    }
    
    public void printTree() {
	if(root != null){
	    Tree t = null;
	    
	    t = new Tree(root.getLeft());
	    t.printTree();
	    
	    System.out.println(root);
	    
	    t = new Tree(root.getRight());
	    t.printTree();
	}
    }
    
    public void prepareTransmissionString() {
	if(root != null){
	    Tree t = null;
	    
	    t = new Tree(root.getLeft());
	    t.prepareTransmissionString();
	    
	    NetIO.addToTransmissionString(root.getRecordNumber());
	    
	    t = new Tree(root.getRight());
	    t.prepareTransmissionString();
	}
    }
    
    public void printTree(TNode start, TNode end)   {
	if (start != null && end != null)   {
	    if (start.getIdentification().compareTo(end.getIdentification()) <= 0)    {
		if (start.getIdentification().compareTo(root.getIdentification()) < 0 && end.getIdentification().compareTo(root.getIdentification()) > 0)    {
		    TNode p = start;
		    while (p != null && p != root)  {
			System.out.println(p);
			Tree rTree = new Tree(p.getRight());
			rTree.printTree();
			
			do  {
			    p = p.getParent();
			} while (p != null && p.getIdentification().compareTo(start.getIdentification()) < 0);
		    }
		    System.out.println(root);
		    p = root.getRight();
		    while (p != null)   {
			if (p.getIdentification().compareTo(end.getIdentification()) <= 0)  {
			    Tree lTree = new Tree(p.getLeft());
			    lTree.printTree();
			    System.out.println(p);
			    p = p.getRight();
			}
			else    {
			    p = p.getLeft();
			}
		    }
		}
		else if (end.getIdentification().compareTo(root.getIdentification()) < 0)   {
		    Tree lTree = new Tree(root.getLeft());
		    lTree.printTree(start, end);
		}
		else if (start.getIdentification().compareTo(root.getIdentification()) > 0) {
		    Tree rTree = new Tree(root.getRight());
		    rTree.printTree(start, end);
		}
	    }
	}
    }
    
    public void prepareTransmissionString (TNode start, TNode end)   {
	if (start != null && end != null)   {
	    if (start.getIdentification().compareTo(end.getIdentification()) <= 0)    {
		if (start.getIdentification().compareTo(root.getIdentification()) < 0 && end.getIdentification().compareTo(root.getIdentification()) > 0)    {
		    TNode p = start;
		    while (p != null && p != root)  {
			NetIO.addToTransmissionString (p.getRecordNumber());
			Tree rTree = new Tree(p.getRight());
			rTree.prepareTransmissionString();//
			
			do  {
			    p = p.getParent();
			} while (p != null && p.getIdentification().compareTo(start.getIdentification()) < 0);
		    }
		    NetIO.addToTransmissionString (p.getRecordNumber());
		    p = root.getRight();
		    
		    while (p != null)   {
			if (p.getIdentification().compareTo(end.getIdentification()) <= 0)  {
			    Tree lTree = new Tree(p.getLeft());
			    lTree.prepareTransmissionString(); // 
			    NetIO.addToTransmissionString (p.getRecordNumber());
			    p = p.getRight();
			}
			else    {
			    p = p.getLeft();
			}
		    }
		}
		else if (end.getIdentification().compareTo(root.getIdentification()) < 0)   {
		    Tree lTree = new Tree(root.getLeft());
		    lTree.prepareTransmissionString(start, end);
		}
		else if (start.getIdentification().compareTo(root.getIdentification()) > 0) {
		    Tree rTree = new Tree(root.getRight());
		    rTree.prepareTransmissionString(start, end);
		}
	    }
	}
    }
    
    public void printTree(int level) {
	if(root != null){
	    Tree t = null;
	    
	    t = new Tree(root.getLeft());
	    t.printTree(level + 1);
	    
	    System.out.println(root + " in Level: " + level + " at Height: " + root.getHeight());
	    
	    t = new Tree(root.getRight());
	    t.printTree(level + 1);
	}
    }
    
    private TNode rightRotate() {
	TNode p = root.getLeft();
	p.setParent(root.getParent());
	
	root.setLeft(p.getRight());
	
	if(root.getLeft() != null){
	    root.getLeft().setParent(root);
	}
	p.setRight(root);
	p.getRight().setParent(p);
	return p;
    }
    
    private TNode leftRotate() {
	TNode p = root.getRight();
	p.setParent(root.getParent());
	
	root.setRight(p.getLeft());
	
	if(root.getRight() != null){
	    root.getRight().setParent(root);
	}
	p.setLeft(root);
	p.getLeft().setParent(p);
	
	return p;
    }
    
    public void breadthFirstRetrieve(String fileName)   {
	try {
	    RandomAccessFile f = new RandomAccessFile(fileName, "rw");
	    
	    int nodes = (int) (f.length() / (Globals.IDENTIFICATION_LEN + Globals.INT_LEN));
	    TNode p = null;
	    byte[] identification = new byte[Globals.IDENTIFICATION_LEN];
	    String identificationString = Globals.STR_NULL;
	    
	    for (int i = 0; i < nodes; i++) {
		identificationString = Globals.STR_NULL;
		f.read(identification);
		
		for (int j = 0; j < identification.length; j++) {
		    identificationString = identificationString + (char) identification[j];
		}
		
		p = new TNode(identificationString, f.readInt(), null, null, null);
		this.insertNodeRec(p);
	    }
	    f.close();
	}
	catch (IOException e)   {
	    System.out.println("***Error: Unable to retrieve tree file " + fileName);
	}
    }
    
    
    public TNode findNode(String identification){
	if(root == null){
	    return null;
	}
	else if (identification.equals(root.getIdentification())){
	    return root;
	}
	else if(identification.compareTo(root.getIdentification()) < 0){
	    Tree t = new Tree(root.getLeft());
	    return t.findNode(identification);
	}
	else if(identification.compareTo(root.getIdentification()) > 0){
	    Tree t = new Tree(root.getRight());
	    return t.findNode(identification);
	}
	else{
	    System.out.println("Fatal error");
	    return null;
	}
    }
    
    public void buildFromMessagesFile(int whatId){ // rebuild index depending on what index
	Record record = new Record();
	for(int recordNumber = 0; recordNumber < Globals.totalRecordsInMessagesFile; recordNumber++){
	    record.readFromMessagesFile(recordNumber);
	    if(record.getData().charAt(Globals.FIRST_RECORD_MARKER_POS) == Globals.FIRST_RECORD_MARKER){
		Message message = new Message();
		message.readFromMessagesFile(recordNumber);
		
		String key = Globals.STR_NULL;
		if(whatId == Globals.SENDER_ID){
		    key = message.getIdSenderFirst();
		}
		else if(whatId == Globals.RECEIVER_ID){
		    key = message.getIdReceiverFirst();
		}
		else{
		    System.out.println("***invalid whatId parameter in buildFromMessagesFile()");
		}
		TNode p = new TNode(key, recordNumber, null, null, null);
		insertNodeRec(p);
	    }
	}
    }
    
    public void breadthFirstSave(String fileName)   {
	try {
	    RandomAccessFile f = new RandomAccessFile (fileName, "rw");
	    f.setLength(0);
	    
	    for (int i = 0; i < height(); i++)  {
		writeLevel(i, f);
	    }
	    f.close();
	}
	catch (IOException e)   {
	    System.out.println(e);
	}
    }
    
    public void writeLevel(int level, RandomAccessFile f)   {
	if (level == 0) {
	    try {
		if (root != null)   {
		    f.write(root.getIdentification().getBytes());
		    f.writeInt(root.getRecordNumber());
		}
	    }
	    catch (IOException e)   {
		System.out.println(e);
	    }
	}
	else if (root != null)  {
	    Tree tree = new Tree(root.getLeft());
	    tree.writeLevel(level - 1, f);
	    tree = new Tree (root.getRight());
	    tree.writeLevel(level - 1, f);
	}
    }
    
    public TNode findNode(String partialKey, int where){
	if(partialKey.length() == Globals.IDENTIFICATION_LEN){
	    return findNode(partialKey);
	}
	else if(root == null){
	    return null;
	}
	else{
	    int n = partialKey.length();
	    
	    if(partialKey.compareTo(root.getIdentification().substring(0, n)) < 0){
		Tree t = new Tree(root.getLeft());
		return t.findNode(partialKey, where);
	    }
	    else if(partialKey.compareTo(root.getIdentification().substring(0, n)) > 0){
		Tree t = new Tree(root.getRight());
		return t.findNode(partialKey, where);
	    }
	    else{
		TNode p = new TNode(root.getIdentification(), root.getRecordNumber(), root.getLeft(), root.getRight(), root.getParent());
		if(where == 0){
		    TNode q = p.getLeft();
		    while(q != null){
			if(q.getIdentification().substring(0, n).equals(partialKey)){
			    p = q;
			    q = q.getLeft();
			}
			else{
			    q = q.getRight();
			}
		    } 
		}
		else{
		    TNode q = p.getRight();
		    while(q != null){
			if(q.getIdentification().substring(0, n).equals(partialKey)){
			    p = q;
			    q = q.getRight();
			}
			else{
			    q = q.getLeft();
			}
		    }
		}
	    return p;
	    }
	}
    }
    
    private void setParentsChildLink(TNode p, TNode q)  {
	if (p.getIdentification().compareTo(p.getParent().getIdentification()) < 0 ) p.getParent().setLeft(q);
	else if (p.getIdentification().compareTo(p.getParent().getIdentification()) > 0) p.getParent().setRight(q);
	else System.out.println("Error deleting root setting a parent's child link");
    }
    
    public void deleteNode(TNode p) {
	if (p != null)  {
	    TNode q = null;
	    TNode r = null;
	    if (p.getLeft() == null && p.getRight() == null)    {
		r = p.getParent();
		if (p == root) root = null;
		else setParentsChildLink(p, null);
	    }
	    else if (p.getLeft() != null && p.getRight() == null || p.getLeft() == null && p.getRight() != null)   {
		q = (p.getLeft() != null) ? p.getLeft() : p.getRight();
		
		if (p == root) root = q;
		else setParentsChildLink(p, q);
		
		q.setParent(p.getParent());
		r = q.getParent();
	    }
	    else    {
		q = p.getLeft();
		if (p.getLeft().getRight() == null) { //at this point it must have two children, thus it won't crash with p.getLeft (if null)
		    if (p == root) root = q;
		    else setParentsChildLink(p, q);
		    
		    q.setParent(p.getParent());
		    q.setRight(p.getRight());
		    q.getRight().setParent(q);
		    r = q;
		}
		else    {
		    q = p.getLeft();
		    while (q.getRight() != null)   {
			q = q.getRight();
		    }
		    r = q.getParent();
		    q.getParent().setRight(q.getLeft());
		    
		    if (q.getLeft() != null)
			q.getLeft().setParent(q.getParent());
		    
		    if (p == root) root = q;
		    else setParentsChildLink(p, q);
		    q.setParent(p.getParent());
		    q.setLeft(p.getLeft());
		    p.getLeft().setParent(q);
		    q.setRight(p.getRight());
		    p.getRight().setParent(q);
		}
	    }
	    Tree rTree = new Tree(r);
	    rTree.updateTreeHeights();

	    balance (r);
	    
	    p.setLeft(null); //all four lines are unnecssary -- when method ends, the variable is done
	    p.setRight(null);
	    p.setParent(null);
	    p = null;
	}
	
    }
    
    
    public String toString() {
	if(root == null){
	    return "Empty tree";
	}
	else{
	    return "Root Id: " + root.getIdentification();
	}    
    }
}
