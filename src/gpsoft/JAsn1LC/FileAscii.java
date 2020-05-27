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

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAscii {

	private String sFileInput;
	private String sFileOutput;
	private OutputStream rafout;
	private Pattern rePatternTag;
	private Pattern rePatternValue;

	private Parameters params;
	private List<Tag> lTags;
	private boolean bError;
	
	private int currentLevel;
	private Tag currentTag;
	private int currentIdTag;
	private int currentClassTag;
    private String currentValueTag;
    private long currentLength;

	protected boolean isbError() {
		return bError;
	}

	protected void setbError(boolean bError) {
		this.bError = bError;
	}	
	
	protected void setParams(Parameters params) {
		this.params = params;
	}
	
	// Function to calculate level Tag counting . char
	private int getTagLevel(String strTag) {
		int retLevel = 0;
		
		for (int i = 0; i < strTag.length(); i++) {
		    if (strTag.charAt(i) == '.') {
		        retLevel++;
		    }
		}
		
		return retLevel;
	}
	
	private boolean getTagIdClass(String strTag) {
		boolean bRet = false;
		int sepTagIndex;
		int sepIdClassIndex;
		String strTagToElab = strTag;
		
		sepTagIndex = strTag.lastIndexOf('.');
		
		// Case of First Tag
		if ( sepTagIndex > 0 && strTag.length() != (sepTagIndex-1) ) {
          strTagToElab = strTag.substring(sepTagIndex+1);
		} else {
		  if ( sepTagIndex >= 0 || strTag.length() == (sepTagIndex-1) ) return false;
		}
		
		sepIdClassIndex = strTagToElab.indexOf('-');
		if ( sepIdClassIndex > 0 && strTagToElab.length() != (sepIdClassIndex-1) ) {
			
			if ( Utility.isNumeric(strTagToElab.substring(0,sepIdClassIndex-1)) ) {
			   this.currentIdTag = Integer.parseInt(strTagToElab.substring(0,sepIdClassIndex-1));
			   this.currentClassTag = Integer.parseInt(strTagToElab.substring(0,sepIdClassIndex+1));
			   bRet = true;
			}
			
		} else {
			if ( sepIdClassIndex >= 0 && strTagToElab.length() == (sepIdClassIndex-1) ) return false;
			this.currentIdTag = 1;
			if ( Utility.isNumeric(strTagToElab) ) {
				this.currentClassTag = Integer.parseInt(strTagToElab);
				bRet = true;
			}
		}
		return bRet;
	}

	public void displaySingleTag(Tag tag) {
		for(int i=0;i<tag.getiLevel();i++) System.out.print("\t");
 		if ( tag.isStruct() ) {
 	       System.out.println(tag.getTagId() + "-" + tag.getTagClass() + " isStruct : " + String.valueOf(tag.isStruct()) + " level : " + tag.getiLevel() + " length : " + tag.getLength() + " Childs : " + tag.getlTagSon().size());
 		} else {
 	       System.out.println(tag.getTagId() + "-" + tag.getTagClass() + " isStruct : " + String.valueOf(tag.isStruct()) + " level : " + tag.getiLevel() + " length : " + tag.getLength() + " Childs : " + tag.getlTagSon().size() + " Value : >" + tag.getValue() + "<");
 		}
 	}
	
	public void displaySingleTagToWrite(Tag tag) {
		for(int i=0;i<tag.getiLevel();i++) System.out.print("\t");
 		if ( tag.isStruct() ) {
 	       System.out.println(tag.getTagId() + "-" + tag.getTagClass() + " Binary <" + 
 		                      Utility.getBinaryTagIdClass(tag.getTagId(), tag.getTagClass(), tag.isStruct()) + ">" + " Hex <" +
 	    		              Utility.strBinToStrHex(Utility.getBinaryTagIdClass(tag.getTagId(), tag.getTagClass(), tag.isStruct())) + ">" +
 	    		              " isStruct : " + String.valueOf(tag.isStruct()) + 
 	    		              " level : " + tag.getiLevel() + 
 	    		              " length : " + tag.getLength() + " Binary <" +
 		                      Utility.getBinaryTagLength(tag.getLength()) + ">" + " Hex <" + 
 	    		              Utility.strBinToStrHex(Utility.getBinaryTagLength(tag.getLength())) + ">" +
 	    		              " Childs : " + tag.getlTagSon().size());
 		} else {
 	       System.out.println(tag.getTagId() + "-" + tag.getTagClass() + 
 	    		              " isStruct : " + String.valueOf(tag.isStruct()) + " Binary <" + 
 		                      Utility.getBinaryTagIdClass(tag.getTagId(), tag.getTagClass(), tag.isStruct()) + ">" + " Hex <" +
 	    		              Utility.strBinToStrHex(Utility.getBinaryTagIdClass(tag.getTagId(), tag.getTagClass(), tag.isStruct())) + ">" +
 	    		              " level : " + tag.getiLevel() + 
 	    		              " length : " + tag.getLength() + " Binary <" +
 		                      Utility.getBinaryTagLength(tag.getLength()) + ">" + " Hex <" + 
 	    		              Utility.strBinToStrHex(Utility.getBinaryTagLength(tag.getLength())) + ">" +
 	    		              " Childs : " + tag.getlTagSon().size() + 
 	    		              " Value : >" + tag.getValue() + "<");
 		}
	}
	
	public void displayTree(Tag tag) {
		displaySingleTag(tag);
		for (Tag tagSon : tag.getlTagSon()) {
			displayTree(tagSon);
		}
	}
	
	
	public int writeOnFile(String hexToWrite) {
	   int iRet = 0;
	   byte[] bytesToWrite = null;
	   
	   bytesToWrite = Utility.hexStringToByteArray(hexToWrite);
	   if ( bytesToWrite.length <= 0 ) {
			System.out.println("Error in hexStringToByteArray for String <" + hexToWrite + ">");
			return -1;
	   }
	   try {
		rafout.write(bytesToWrite,0,bytesToWrite.length);
	   } catch (IOException e) {
		 System.out.println("Error during write on " + this.sFileOutput + " for String <" + hexToWrite + ">");
		// TODO Auto-generated catch block
		  e.printStackTrace();
		  iRet = -1;
	   }
	   
	   return iRet;
	}
	
	public void displayTags() {
 	    System.out.println("\nDISPLAY TAGS ENCODED\n");
	 	for (Tag tag : lTags) {
	 		displayTree(tag);
	 	}
	}
	
	public int writeTag(Tag tag) {
		int iRet = 0;
		
		String tagHex = null;
		String tagLength = null;
		String tagValue = null;
		int iToFillHex;
		
		tagHex = Utility.getHexTagIdClass(tag.getTagId(), tag.getTagClass(), tag.isStruct());
		if ( tagHex == null ) {
			System.out.println("Error in getHexTagIdClass for Tag Id :" + tag.getTagId() + " and Class : " + tag.getTagClass());
			return -1;
		}
		
		if ( tagHex.length()%2 != 0 ) {
		   iToFillHex = tagHex.length()/2;
		   tagHex = Utility.lpad(tagHex,2*(iToFillHex+1),'0');
		}
		if ( writeOnFile(tagHex) != 0 ) return -1;
		
		tagLength = Utility.getHexTagLength(tag.getLength());
		if ( tagLength == null ) {
			System.out.println("Error in getHexTagLength for Tag Id :" + tag.getTagId() + " and Class : " + tag.getTagClass() + " and Length : " + tag.getLength());
			return -1;
		}
		if ( tagLength.length()%2 != 0 ) {
		   iToFillHex = tagLength.length()/2;
		   tagLength = Utility.lpad(tagLength,2*(iToFillHex+1),'0');
		}

		if ( writeOnFile(tagLength) != 0 ) return -1;
		
		if ( tag.isStruct() == false ) {
		   if ( tag.getValue().length() > 0 ) {
			   
		       if ( tag.getValue().length()%2 != 0 ) {
		          iToFillHex = tag.getValue().length()/2;
		          tagValue = Utility.lpad(tagHex,2*(iToFillHex+1),'0');
               } else {
            	  tagValue = tag.getValue(); 
               }
			   if ( writeOnFile(tagValue) != 0 ) return -1;   
		   }
		}
		
//		displaySingleTagToWrite(tag);
		for (Tag tagSon : tag.getlTagSon()) {
			writeTag(tagSon);
		}
		return iRet;
	}
	
	public void displayEncodeOk() {
	  System.out.println("\n\nJAsn1LC Encode Mode\n");
      System.out.println("\n Input File " + this.sFileInput + " was encoded succesfully in Asn1 BER Format on Output File " + this.sFileOutput + "\n\n");
	}
	
	public void writeOutFile() throws IOException {
//	    System.out.println("\n DEBUG WRITE TAGS ENCODED\n");	
	 	for (Tag tag : lTags) {
	 		if ( writeTag(tag) != 0 ) break;	
	 	}
	 	rafout.close();
	}
	
	public int elabFileAscii() throws IOException {
       String record;
       BufferedReader reader;
       Matcher m;
       Tag newTag;
	   int iRet = 0;	
	   int iInd = 0;
	   int iLevel = 0;
	   boolean bGetTag = false;
	   boolean bGetValue = false;
	   lTags = new ArrayList<Tag>();
	   this.currentTag = null;
	   currentLevel = -1;
	   
	   // Open File Output
       try
       {
	     rafout = new FileOutputStream(this.sFileOutput);
       }
       catch (Exception e)
       {
		 System.err.format("\nException occurred trying to open output encode file '%s'.", this.sFileOutput);
		 e.printStackTrace();
         setbError(true);
         return -1;
	   }	  	  
       
       try
       {
		 reader = new BufferedReader(new FileReader(this.sFileInput));
		 while ((record = reader.readLine()) != null)
		 {
			 // Record type to Elab :
			 // 
			 // - Primitive : 00000034:004         [1.4.108.16] {LocalTimeStamp} length : 14  "3230323030333031303130303432"h Value (20200301010042)A
			 // - Structure : 00000031:003     [1.4.108] {FileCreationTimeStamp} length : 25
			 //
			 
//			 System.out.println("DEBUG RECORD <"+record+"> current Level : "+currentLevel);

			 // If encountered ...[....]... get tag
			 if ( record.matches("^.*\\[.*\\].*$") == true ) {
                 iInd = 0;
				 m = rePatternTag.matcher(record);
				 while(m.find()) {
					bGetTag = getTagIdClass(m.group(iInd));
	                iLevel = getTagLevel(m.group(iInd));
	                if ( iLevel - currentLevel > 1 ) {
                      System.out.println("ERROR - Record <" + record + "> present a level greater than one compared to the previous one.");
                      iRet = -1;
                      break;
	                }
				    break;
				 }
			     if ( currentLevel < 0 ) {
			    	 if ( iLevel > 0 ) {
                        System.out.println("ERROR - First Record <" + record + "> present a level greater than 0.");
                        iRet = -1;
			    	 } else {
			    		currentLevel = 0; 
			    	 }
			     }
			     if ( iRet != 0 ) break;
			 } else {
				continue; 
			 }
			 // If encountered ...."....."..... get hexadecimal value
			 if ( bGetTag ) {
				bGetValue = false; 
				m = rePatternValue.matcher(record);
				while(m.find()) {
					if ( Utility.isHexadecimal(m.group(iInd)) ) {
                      currentValueTag = m.group(iInd);
					} else {
                      System.out.println("ERROR - Record <" + record + "> present value between \"..\" no in hexadecimal format.");
                      iRet = -1;
                      break;
					}
                    if ( currentValueTag.length()%2 == 0 ) {
	                   bGetValue = true;
                    } else {
                      System.out.println("ERROR - Record <" + record + "> present hexadecimal value with a number of odd digits.");
                      iRet = -1;
                    }
				    break;
				}
			}

			if ( iRet != 0 ) break;
			
			if ( iRet == 0 ) {
//			   System.out.println("DEBUG Create new TAG " + currentIdTag + "-" + currentClassTag + " Level : " + iLevel + " isStruct " + String.valueOf(!bGetValue));	
			   newTag = new Tag(currentIdTag,currentClassTag,iLevel,!bGetValue);
			   currentLength = Utility.getTagStructLen(currentIdTag, currentClassTag);
			   if ( bGetValue ) {
				 currentLength+=currentValueTag.length()/2;
				 currentLength+=Utility.getTagLenLength(currentLength);
			     newTag.setValue(currentValueTag);	   
			     newTag.setLength(currentValueTag.length()/2);
			   } else {
				 currentLength++;
			   }
			   
			   if ( currentTag == null ) currentTag = newTag;
			   
			   if ( currentLevel == iLevel ) {
				  if ( currentTag.getTagFather() != null ) {
					  currentTag = currentTag.getTagFather();
				  }
				  if ( currentLevel > 0 ) {
					  newTag.setTagFather(currentTag);
				      currentTag.addSon(newTag); 
				  } else {
				     lTags.add(newTag); 
				  }
				  currentTag = newTag;
				  currentLevel = iLevel;
			   } else {
				   if ( currentLevel > iLevel ) {
					   // First Level
					   if ( iLevel == 0 ) {
				          lTags.add(newTag); 
				          currentTag = newTag;
				          currentLevel = iLevel;
					   } else {
					     currentTag = currentTag.getLevelTagPrevious(iLevel-1); 
					     if ( currentTag == null ) {
			                  System.out.println("ERROR - Record <" + record + "> not encountered previous level.");
			                  iRet = -1;							   
			                  break;
					     }
				         newTag.setTagFather(currentTag);
                         currentTag.addSon(newTag); 
                         currentTag = newTag;						   
				         currentLevel = iLevel;
					   }
				   } else {
				     if ( !currentTag.isStruct() ) {
                        System.out.println("ERROR - Record <" + record + "> ecountered another level after a primitive Tag.");
			            iRet = -1;							   
			            break;
				     }
				     newTag.setTagFather(currentTag);
                     currentTag.addSon(newTag); 
				     currentTag = newTag;
	                 currentLevel = iLevel;
				   }
			   }
			   // Add Calculate Length to Parents
			   if ( currentTag.getTagFather() != null ) currentTag.addLengthFathers(currentLength);
			}
			 
		 }  
		 reader.close();
	   }
       catch (Exception e)
       {
		 System.err.format("\nException occurred trying to read '%s'.", this.sFileInput);
		 e.printStackTrace();
         setbError(true);
	   }	   
       
       if ( this.isbError() ) iRet = -1;
	   return iRet;
	}

	public FileAscii(String sFileInput, String sFileOutput) throws IOException {
		super();
		this.sFileInput = sFileInput;
		this.sFileOutput = sFileOutput;
	    rePatternTag = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])");
	    rePatternValue = Pattern.compile("(?<=\")([^\"]+)(?=\")");
	}
}
