package gpsoft.JAsn1LC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");
	
	public static boolean fileIsAscii(String filename) {
		 
        boolean result = false;
 
        FileReader inputStream = null;
 
        try {
            inputStream = new FileReader(filename);
 
            int c;
            while ((c = inputStream.read()) != -1) {
 
                Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
                 
                if (block == Character.UnicodeBlock.BASIC_LATIN || block == Character.UnicodeBlock.GREEK) {
                    if (c==9 || c == 10 || c == 11 || c == 13 || (c >= 32 && c <= 126)) {
                        result = true;
 
                    } else if (c == 153 || c >= 160 && c <= 255) {
                        result = true;
 
                    } else if (c == 884 || c == 885 || c == 890 || c == 894 || c >= 900 && c <= 1019) {
                        result = true;
 
                    } else {                        
                        result = false;
                    break;
                    }
                }                
            }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
 
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                }
            }
        }
        return result;
    }
	
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
	
	public static int getTagStructLen(int iTagId, int iTagClass) {
	    int iLength;
	    if ( iTagClass <= 30 )
	    {
	      iLength = 1;
	    }
	    else
	    {
	      if ( iTagClass < 128 )
	      {
	        iLength = 2;
	      }
	      else
	      {
	        iLength = 3;
	      }
	    }
	    return iLength;
	}
	
	public static int getTagLenLength(long lLength) {
	    int iLengthLen;
	    long N = lLength;
	    if ( lLength < 127 )
	    {
	      iLengthLen = 1;
	    }
	    else
	    {
	    /*  iLengthLen = iLength%255 + 2; */
	      iLengthLen = 0;
	      while (N != 0) {
	             N >>= 8;
	        ++iLengthLen;
	      }

	    }
	    return iLengthLen;
	}

	public static int calcDiffLength(long lOldLength, long lDeltaLength)
	{
	  int iByteBefore = 0;
	  int iByteAfter = 0;
	  long lNewLength = lOldLength + lDeltaLength;
	  
	  iByteBefore = getTagLenLength(lOldLength);
	  iByteAfter = getTagLenLength(lNewLength);;

	  if ( lOldLength < 127 && (lOldLength + lDeltaLength) >= 127 )
	  {
	    iByteAfter++;
	  }

	  return iByteAfter - iByteBefore;
	}

	public static String hexToDouble(String hexValue) {
        try {
			return String.valueOf(Long.parseLong(hexValue, 16));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return String.valueOf("NumberFormatException");
		}
	}
	

	public static boolean isHexadecimal(String input) {
	    final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
	    return matcher.matches();
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
	
	public static String int2binString(long lint)
	{
     String sRet = null;		
     String strOutput = null;
	 long n;
	 int i;
	 n = lint;

	 for(i=0;n>0;i++)
	 {
	   if ( n%2 == 0 )
	   {
		 sRet+="0";
	   }
	   else
	   {
		 sRet+="1";
	   }
	   n=n/2;
	 }
	 for(i=sRet.length()-1;i>=0;i--)
	 {
	   strOutput+=sRet.charAt(i);
	 }

	 return strOutput;
	}

	
	 public static String getBinaryIdClassTag(int iTagId, int iTagClass, boolean isStruct)
	 {
	   String sRet = null;
	   String classBinary;
	   String classBinaryAppo;
	   int iFill;

	   if ( iTagClass > 16383 )
	   {
	     System.out.println("getBinaryIdClassTag Class " + iTagClass + " xceeded max value tag class permitted of 16383.");
	     return null;
	   }

	   /* Id */
	   switch(iTagId) {
	     case 1  :
	    	 sRet += "01";
	     break;
	     case 2  :
	    	 sRet += "10";
	     break;
	     case 3  :
	    	 sRet += "11";
	     break;
	     default :
	    	 System.out.println("getBinaryIdClassTag Id " + iTagId + " not permitted.");
	         return null;
	   }

	   /* Type */
	   if ( isStruct == false )
	   {
         sRet += "0";
	   }
	   else
	   {
         sRet += "1";
	   }
	   
	   /* Class */
	   if ( iTagClass <= 30 )
	   {
		 classBinary = int2binString(iTagClass);
	     if ( classBinary == null )
	     {
	       System.out.println("Error in function int2binString for iTagClass " + iTagClass);
	       return null;
	     }
	     sRet+=String.format("%05d", Integer.parseInt(classBinary));
	   }
	   else
	   {
		 sRet+="11111";  
		 classBinaryAppo = int2binString(iTagClass);
	     if ( classBinaryAppo == null )
	     {
	       System.out.println("Error in function int2binString for iTagClass " + iTagClass);
	       return null;
	     }  
	     
	     if ( iTagClass >= 128 )
	     {
           classBinaryAppo=String.format("%016d", Integer.parseInt(classBinaryAppo));
	     }
	     else
	     {
           classBinaryAppo=String.format("%08d", Integer.parseInt(classBinaryAppo));
	     }
       }

	     // FINO A QUI .............................................................................
	     /* 
	     if ( classBinaryAppo.length() == 16 )
	       {
	         strncpy(cpAppoClassAppo,cpAppoClass,512);
	         strncpy(&cpAppoClass[1],&cpAppoClassAppo[2],7);
	         strncpy(&cpAppoClass[0],"1",1);
	         strncpy(&cpAppoClass[8],"0",1);
	       }

	       iLengthClass = strlen(cpClass);
	       cpClass = realloc(cpClass,strlen(cpClass)+strlen(cpAppoClass)+1);
	       if ( cpClass == NULL )
	       {
	         snprintf(acLogMsg,MAX_LOG_SIZE, "Error <%s> for realloc() cpClass.", strerror(errno));
	         v_002Log(E_Error, E_Internal, E_Generic, __FILE__, __LINE__, 1, acLogMsg);
	         return E_Generic;
	       }
	       strncpy(&cpClass[iLengthClass],cpAppoClass,strlen(cpAppoClass));
	       cpClass[iLengthClass+strlen(cpAppoClass)] = 0;
	     }

	     *cppIdClass = realloc(*cppIdClass,4+strlen(cpClass));
	     if ( *cppIdClass == NULL )
	     {
	       snprintf(acLogMsg,MAX_LOG_SIZE, "Error <%s> for calloc() cppIdClass.", strerror(errno));
	       v_002Log(E_Error, E_Internal, E_Generic, __FILE__, __LINE__, 1, acLogMsg);
	       return E_Generic;
	     }

	     strncpy(&(*cppIdClass)[POS_BINARY_CLASS],cpClass,strlen(cpClass));
	     (*cppIdClass)[3+strlen(cpClass)] = 0;
         */
	     return sRet;
	   }

}
