/*
 * Copyright 2020 The jAsn1LC Author
 *
 * Licensed under the GNU General Public License v3.0; you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

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
    // Max tag class supported is 2.097.151	
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
	    	if ( iTagClass < 16384 ) {
	          iLength = 3;
	    	} else {
	          iLength = 4;
	    	}
	      }
	    }
	    return iLength;
	}
	
	public static int getTagLenLength(long lLength) {
		
		int iLengthLen = getHexTagLength(lLength).length();
		
		if ( iLengthLen%2 != 0 ) iLengthLen++;
		
	    return iLengthLen/2;
	}

	/*****************************************************
	public static int getTagLenLength(long lLength) {
	    int iLengthLen;
	    long N = lLength;
	    if ( lLength < 127 )
	    {
	      iLengthLen = 1;
	    }
	    else
	    {
	    / *  iLengthLen = iLength%255 + 2; * /
	      iLengthLen = 0;
	      while (N != 0) {
	             N >>= 8;
	        ++iLengthLen;
	      }

	    }
	    return iLengthLen;
	}
	*****************************************************/

	public static int calcDiffLength(long lOldLength, long lDeltaLength)
	{
	  int iByteBefore = 0;
	  int iByteAfter = 0;
	  long lNewLength = lOldLength + lDeltaLength;
	  
	  iByteBefore = getTagLenLength(lOldLength);
	  iByteAfter = getTagLenLength(lNewLength);

//	  if ( lOldLength < 127 && (lOldLength + lDeltaLength) >= 127 )
//	  {
//	    iByteAfter++;
//	  }

	  return iByteAfter - iByteBefore;
	}

	public static String substrBinary(String valBin, String valBinToSub){
        // Use as radix 2 because it's binary    
		long lValBin = Long.parseLong(valBin, 2);
		long lValBinToSub = Long.parseLong(valBinToSub, 2);
		long sumLBin = lValBin-lValBinToSub;
		return Long.toBinaryString(sumLBin);
	}	
	
	public static String addBinary(String valBin, String valBinToAdd){
        // Use as radix 2 because it's binary    
		long lValBin = Long.parseLong(valBin, 2);
		long lValBinToAdd = Long.parseLong(valBinToAdd, 2);
		long sumLBin = lValBin+lValBinToAdd;
		return Long.toBinaryString(sumLBin);
	}	
	
	public static String binaryCompl1(String binaryValue) {
	  String binaryCompl1 = "";
	  if ( binaryValue.length() > 0 && binaryValue.length()%2 == 0 ) {
	    for(int i=0;i<binaryValue.length();i++) {
          if ( binaryValue.charAt(i) == '0' ) {
            binaryCompl1 += "1";  	
          } else {
            if ( binaryValue.charAt(i) == '1' ) {
              binaryCompl1 += "0";  	
            } else {
          	  binaryCompl1 = "";   
        	  break;
            }
          }
	    }
	  }
	  return binaryCompl1;
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
	
	public static String int2binString(int lint)
	{
     String sRet = "";		
     String strOutput = "";
	 int n;
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
	
	// Use only if in stringToFill there are no spaces
	public static String lpad(String stringToFill, int iLength, char charToFill) {
	    String sFrmt = "%" + iLength + "s";
		return String.format(sFrmt,stringToFill).replace(' ', charToFill);
	}

	// Use only if in stringToFill there are no spaces
	public static String rpad(String stringToFill, int iLength, char charToFill) {
	    String sFrmt = "%-" + iLength + "s";
		return String.format(sFrmt,stringToFill).replace(' ', charToFill);
	}
	
	public static String getHexTagIdClass(int iTagId, int iTagClass, boolean isStruct) {
		return strBinToStrHex(getBinaryTagIdClass(iTagId, iTagClass, isStruct));
	}
	
	public static String getBinaryTagIdClass(int iTagId, int iTagClass, boolean isStruct)
	{
	  String sRet = "";
	  String classBinary;
	  String classBinaryAppo = "";
	  String classExtr = "";
	  int valInt;
	  int i;
	  int iStart=0;
	  int iEnd=0;

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
	  if ( isStruct == false ) {
        sRet += "0";
	  } else {
        sRet += "1";
	  }
	   
	  /* Class */
	  if ( iTagClass <= 30 ) {
		 classBinary = int2binString(iTagClass);
	     if ( classBinary == null )
	     {
	       System.out.println("Error in function int2binString for iTagClass " + iTagClass);
	       return null;
	     }
	     sRet+=lpad(classBinary,5,'0');
	  } else {
		 sRet+="11111";  
		 classBinaryAppo = int2binString(iTagClass);
	     if ( classBinaryAppo == null )
	     {
	       System.out.println("Error in function int2binString for iTagClass " + iTagClass);
	       return null;
	     }  
	     valInt = classBinaryAppo.length()/8;
	     if ( classBinaryAppo.length()%8 != 0 ) valInt++;
	     classBinaryAppo = lpad(classBinaryAppo,valInt*8,'0');

	     valInt = classBinaryAppo.length()/6;
	     if ( classBinaryAppo.length()%6 != 0 ) valInt++;
	     
	     if ( iTagClass >= 128 ) {
	    	 String retValueGreater = "";
	    	 String retInd = "";
	    	 int iInd = 1;
	    	 for(i=valInt;i>0;i--) {
	    	    iStart = classBinaryAppo.length()-7*iInd;
	    	    if ( iStart < 0 ) iStart = 0;
	    	    iEnd = classBinaryAppo.length()-7*iInd+7;
	    		if ( iInd == 1 ) {
	                  retInd = "0";	
		    	} else {
		    		  retInd = "1";
		    	}	    	    
	            classExtr =  classBinaryAppo.substring(iStart,iEnd);
	            classExtr = Utility.lpad(classExtr, 7, '0');
	            if ( Integer.valueOf(classExtr).intValue() != 0 || i != 1 ) {
	    		  retValueGreater = retInd + classExtr + retValueGreater;
	            }
	    		iInd++;
	    	 }
	    	 sRet+=retValueGreater;
	     } else {
           classBinaryAppo=lpad(classBinaryAppo,8,'0');
           sRet+=classBinaryAppo;
	     }

	  }

	  return sRet;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String long2binString(long lValue ) {
		return Long.toString(lValue, 2);
	}

	public static String getHexTagLength(long lLengthTag) {
		return strBinToStrHex(getBinaryTagLength(lLengthTag));
	}
	
	public static String getBinaryTagLength(long lLengthTag)
	{
	   String sRet;
	   String sAppoLength;
	   int iLengthInBytes;
	   
	   sRet = long2binString(lLengthTag);
	   
	   if ( lLengthTag <= 127 )
	   {
		 sRet = lpad(sRet,8,'0');
	   }
	   else
	   {
	     iLengthInBytes = sRet.length()/8;
	     if ( sRet.length()%8 != 0 ) iLengthInBytes++;

	     sRet = lpad(sRet,8*iLengthInBytes,'0');
	     
	     sAppoLength =  int2binString(iLengthInBytes);
	     sAppoLength = lpad(sAppoLength,8,'0');
	     
	     sRet="1"+sAppoLength.substring(1)+sRet;
	     
	   }
	   return sRet;
	}
	
	public static String strBinToStrHex(String sBin ) {
		long value = 0L;
		if ( sBin == null || sBin.length() == 0 ) return null;
		value = Long.parseLong(sBin, 2);
		return Long.toString(value, 16);
	}

	public static String strHexToStrBin(String sHex ) {
		if ( sHex == null || sHex.length() == 0 ) return null;
		long value = Long.parseLong(sHex, 16);
		return Long.toString(value, 2);
	}
}
