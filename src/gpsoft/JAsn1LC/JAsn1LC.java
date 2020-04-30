package gpsoft.JAsn1LC;

import java.io.IOException;

/**
 * <p>Title: JAsn1LC</p>
 * <p>Description: Asn1 BER Decoder Line Command</p>
 * <p>Copyright: Copyright (c) 2020</p>
 * <p>Company: GPSoft.</p>
 * @author Giovanni Palleschi
 * @version 1.0
 */

public class JAsn1LC {
	
	static String version = "1.0";
	static String years="2020";
	static String creator="GPSoft By GNNK71";
	 
	 
	
	public static String getVersion() {
	   return version;
	}

	public static String getYears() {
		return years;
	}

	public static String getCreator() {
		return creator;
	}
	
	private static void displayHelp() {
        System.out.println("\n\nJAsn1LC version " + getVersion() + "(" + getYears()   + ") " + getCreator() + "\n");
        System.out.println("Use: PrgAsn1.pl <File Asn1> [-s<File Name Conversion>] [-h] [-o] [-t] [-npv] [-nl] [-ni] [-b] [-e] [-help]\n");
        System.out.println("[...] are optional parameters\n\n");
        System.out.println("[-s<File Name Conversion>] : you can add a Conversion File. Each record has this format <Tag Name>|<Conversion Type>|<Desc Tag>\n");
        System.out.println("                             Values for <Conversion Type> : A for Hex to Ascii");
        System.out.println("                                                            B for Hex to Binary");
        System.out.println("                                                            N for Hex to Number\n");
        System.out.println("                             Example Record : 1.15.43|N|Total Records\n");
        System.out.println("[-h]                       : Display Hexadecimal Value for Tags\n");
        System.out.println("[-o]                       : Display Offset for each Tag\n");
        System.out.println("[-t]                       : Display Only value of Tag instead of Id-Tag (To use for TAP rappresentation)\n");
        System.out.println("[-npv]                     : No Display primitive Values\n");
        System.out.println("[-nl]                      : No Display Length for Tags\n");
        System.out.println("[-ni]                      : No Tag Indentation\n");
        System.out.println("[-b]                       : Specify Byte From \n");
        System.out.println("[-e]                       : Specify Byte To \n");      
        return;
	}

	public static void main(String args[]) throws Exception {
		
		    boolean bErr = true;
		    boolean bHelp = false;
		    Parameters params = new Parameters();
		    FileAsn1 fileAsn1;
		    FileConfAsn1 fileConfAsn1 = null;
		    
            if ( args.length == 0 )
            {
	            bHelp = true;
            	displayHelp();
            }
            
		    for(String arg : args) 
	    	{	    	  
	    	   // File Structure	
	    	   if ( arg.substring(0, 2).compareTo("-s") == 0 ) {
	    		  if ( arg.substring(2).length() > 0 && Utility.isFile(arg.substring(2)) ) { 
	    		     params.setsFileStruct(arg.substring(2)); 
	    		     continue;
	    		  } else {
	    			bErr = true;  
                    System.out.println("\n\nStruncture File specified <" +  arg.substring(2) + "> not exists or not is a correct file.\n");
                    break;
	    		  }
	    	   }
	    	   
	    	   if ( arg.length() > 4 && arg.substring(0, 5).compareTo("-help") == 0 ) {
	    		   displayHelp();
		           bHelp = true;
	    		   break;
	    	   }

	    	   // Notazione Hex
	    	   if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-h") == 0 ) {
                  params.setbHex(true);
                  continue;
	    	   }

	    	   // Notazione OffSet
	    	   if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-o") == 0 ) {
	    		   params.setbOffSet(true);
                  continue;
	    	   }	

	    	   // Notazione TAP
	    	   if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-t") == 0 ) {
                  params.setbTag(true);
                  continue;
	    	   }		    	   
    	   
	    	   // Notazione No Length
	    	   if ( arg.length() > 2 && arg.substring(0, 3).compareTo("-nl") == 0 ) {
                  params.setbNoLength(true);
                  continue;
	    	   }	
	    	   
	    	   // No Indentation
	    	   if ( arg.length() > 2 && arg.substring(0, 3).compareTo("-ni") == 0 ) {
                  params.setbNoIndentation(true);
                  continue;
	    	   }		    	   
	    	   
	    	   // Notazione No Primitive Value
	    	   if ( arg.length() > 3 && arg.substring(0, 4).compareTo("-npv") == 0 ) {
	    		  params.setbNoPrimValue(true);
                  continue;
	    	   }
	    	   
	    	   // From Byte - begin
	    	   if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-b") == 0 ) {
	    		  if ( Utility.isNumeric(arg.substring(2)) ) {
	    			params.setlStart(Long.parseLong(arg.substring(2)));  
                    continue;
	    		  } else {
		            bErr = true;
                    System.out.println("\n\nFor parameter -b (offset start) no numeric value specified <" + arg.substring(2) + "> .\n");
                    break;
	    		  }
	    	   }	
	    	   
	    	   // To Byte - End
	    	   if ( arg.length() > 1 && arg.substring(0, 2).compareTo("-e") == 0 ) {
	    		  if ( Utility.isNumeric(arg.substring(2)) ) {
	    			params.setlEnd(Long.parseLong(arg.substring(2)));  
                    continue;
	    		  } else {
		            bErr = true;
                    System.out.println("\n\nFor parameter -e (offset end) no numeric value specified <" + arg.substring(2) + "> .\n");
                    break;
	    		  }
	    	   }
   	   
	    	   if ( arg.substring(0, 1).compareTo("-") == 0 ) {
		            bErr = true;
                    System.out.println("\n\nParameter <" + arg + "> not recognized.\n");
                    break;	    		   
	    	   }
	    	   
	    	   // The last control is the input file 
    		  if ( arg.length() > 0 && Utility.isFile(arg) ) { 
    			 bErr = false; 
    		     params.setsFileInput(arg);
    		  } else {
    			bErr = true;  
                System.out.println("\n\nInput File specified <" +  arg + "> not exists or not is a correct file.\n");
                break;
              }
	    	}
		    
		    // If there are no errors
	        if ( !bErr ) {
	        	
	             // Load Conf File 
	             if ( params.getbFileStruct() == true ) {
	        	   try {
					fileConfAsn1 = new FileConfAsn1(params.getsFileStruct());
				   } catch (IOException e) {
					  // TODO Auto-generated catch block
					  e.printStackTrace();
				   }
	             }
	           
	             if ( !fileConfAsn1.isbError() ) {
	           
                 // Create File Class	        	
	             try {
				     fileAsn1 = new FileAsn1(params.getsFileInput());
				     fileAsn1.setParameters(params);
	                 if ( params.getbFileStruct() == true ) {
	                	 fileAsn1.setFileConfAsn1(fileConfAsn1);
    				 } 
	                 
				     fileAsn1.initFileAsn1(); 
				     
				     Utility.clearScreen();
				     System.out.println("\n\nASN1 FILE " + params.getsFileInput() + " SIZE : " + fileAsn1.getLengthFile() + "\n");
				     // function read asn1
			         fileAsn1.readTags();
	             } catch (IOException e) {
                     System.out.println("\n\nError in processing input file <" +  params.getsFileInput() + ">.\n");
				     e.printStackTrace();
			     }
			   }
	        } else {
	        	
	            if ( bHelp == false ) {
  	        	   if ( params.getsFileInput() == null ) {
                      System.out.println("\n\nInput Asn1 File no specified.\n\n");
	           	   }
	            }
	        }
	 }
}


