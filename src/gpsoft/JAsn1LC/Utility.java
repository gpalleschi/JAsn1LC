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
//	    System.out.print("\033[H\033[2J");  
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
	
	public static String hexToBinary(String hexValue) {
        String part2 = new String("");
        for (int i = 0; i < hexValue.length(); i++)
        {
          
          switch (hexValue.charAt(i))
		  {
            case '0' :
            	part2 += "0000";
            	break;
            case '1':
            	part2 += "0001";
                break;
           case '2':
           	    part2 += "0010";
                break;
           case '3':
           	    part2 += "0011";
                break;
           case '4':
           	    part2 += "0100";
                break;
           case '5':
           	    part2 += "0101";
                break;
           case '6':
           	    part2 += "0110";
                break;
           case '7':
           	    part2 += "0111";
                break;
           case '8':
           	    part2 += "1000";
                break;
           case '9':
           	    part2 += "1001";
                break;
           case 'a': case 'A':
           	    part2 += "1010";
                break;
           case 'b': case 'B':
           	    part2 += "1011";
                break;
           case 'c': case 'C':
           	    part2 += "1100";
                break;
           case 'd': case 'D':
           	    part2 += "1101";
                break;
           case 'e': case 'E':
           	    part2 += "1110";
                break;
           case 'f': case 'F':
           	    part2 += "1111";
                break;
           default:
                part2 += "....";
		  }
        }
        return part2;		
	}
	

	public static String hexToDouble(String hexValue) {
        try {
			return String.valueOf(Long.parseLong(hexValue, 16));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return String.valueOf("NumberFormatException");
		}
	}
	
	public static int hexValue(char ch) {
        // Returns the hexadecimal value of ch, or returns
        // -1 if ch is not one of the hexadecimal digits.
        switch (ch) {
           case '0':
              return 0;
           case '1':
              return 1;
           case '2':
              return 2;
           case '3':
              return 3;
           case '4':
              return 4;
           case '5':
              return 5;
           case '6':
              return 6;
           case '7':
              return 7;
           case '8':
              return 8;
           case '9':
              return 9;
           case 'a':
           case 'A':
              return 10;
           case 'b':
           case 'B':
              return 11;
           case 'c':
           case 'C':
              return 12;
           case 'd':
           case 'D':
              return 13;
           case 'e':
           case 'E':
              return 14;
           case 'f':
           case 'F':
              return 15;
           default:
              return -1;
        }
     }  // end hexValue()
}
