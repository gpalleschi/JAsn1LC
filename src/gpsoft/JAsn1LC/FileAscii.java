package gpsoft.JAsn1LC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileAscii {

	private String sFileInput;
	private String sFileOutput;
	private RandomAccessFile rafout;
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
	
	public void displayTree(Tag tag) {
		displaySingleTag(tag);
		for (Tag tagSon : tag.getlTagSon()) {
			displayTree(tagSon);
		}
	}
	
	public void displayTags() {
 	    System.out.println("\nDISPLAY TAGS ENCODED\n");
	 	for (Tag tag : lTags) {
	 		displayTree(tag);
	 	}
	}
	
	public void writeTag(Tag tag) {
		
		for (Tag tagSon : tag.getlTagSon()) {
			writeTag(tagSon);
		}
	}
	
	public void writeOutFile() throws IOException {
	 	for (Tag tag : lTags) {
	 		writeTag(tag);	
	 	}
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
	     rafout = new RandomAccessFile(this.sFileOutput,"rw");
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
		this.sFileOutput = sFileInput;
	    rePatternTag = Pattern.compile("(?<=\\[)([^\\]]+)(?=\\])");
	    rePatternValue = Pattern.compile("(?<=\")([^\"]+)(?=\")");
	}
}
