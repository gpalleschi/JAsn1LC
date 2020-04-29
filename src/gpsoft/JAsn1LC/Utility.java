package gpsoft.JAsn1LC;

import java.io.File;

public class Utility {
	
	public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    int sz = str.length();
	    for (int i = 0; i < sz; i++) {
	        if (Character.isDigit(str.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static void clearScreen() {  
	    System.out.print("\033[H\033[2J");  
	    System.out.flush();  
	}  
	
	public static boolean isFile(String filePathString) {
	  File f = new File(filePathString);
	  if(f.exists() && !f.isDirectory()) { 
	    // do something
        return true;		 
	  } else {
        return false;		 
	  }
	}
}
