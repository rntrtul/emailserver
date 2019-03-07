public class Utils{
    public static void delay (int ms){
	try{
	    Thread.sleep (ms);
	}

	catch (InterruptedException ex){
	    Thread.currentThread().interrupt ();
	}
    }
    
    public static String setRecevingTime (String message){
	return message.substring (0, Globals.DATE_TIME_POS) + 
				  longToBytesStr (System.currentTimeMillis()) +
				  message.substring(message.indexOf (Globals.FIRST_RECORD_MARKER));
    }
    public static boolean isANumber (String s){
	boolean itIsANumber = true;
	for (int i =0; i < s.length() && itIsANumber;i++){
	    itIsANumber = Character.isDigit(s.charAt(i));        
	}
	
	return (itIsANumber && s.length() >0);
    }
    
    public static String leftPad(String text, int desiredLength, char paddingItem){
	String padding = Globals.STR_NULL;
	for (int i = 0; i < desiredLength - text.length(); i++){
	    padding = padding + paddingItem;
	}
	return padding + text;
    }
    
    public static String longToBytesStr (long num){
	return "" + (char)  (num >> 56)+
		    (char)  ((num & 0xFF000000000000L) >> 48) +
		    (char)  ((num & 0XFF0000000000L) >> 40) +
		    (char)  ((num & 0XFF00000000L) >> 32) +
		    (char)  ((num & 0XFF000000L) >> 24) +
		    (char)  ((num & 0XFF0000L) >> 16) +
		    (char)  ((num & 0XFF00L) >> 8) +
		    (char)  ((num & 0XFFL));
    }
    
    public static long bytesStrToLong (String str){
	return  ((long) (str.charAt(0)) << 56)|
		((long) (str.charAt(1)) << 48)|
		((long) (str.charAt(2)) << 40)|
		((long) (str.charAt(3)) << 32)|
		((long) (str.charAt(4)) << 24)|
		((long) (str.charAt(5)) << 16)|
		((long) (str.charAt(6)) << 8)|
		((long) (str.charAt(7)));
    }
    
    public static String intToBytesStr (int num){
	return "" + (char) (num >>24)+
		    (char) ((num & 0xFF0000) >> 16)+  
		    (char) ((num & 0xFF00) >> 8)+
		    (char) (num & 0xFF);
    }
    
    public static String removeChars (String text, char c){
	String result = new String (Globals.STR_NULL);
	for (int i= 0; i < text.length(); i ++){
	    if (text.charAt(i) != c)
		result =result + text.charAt(i);
	}
	return result;
    }
}
