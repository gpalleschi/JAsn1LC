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
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * <p>Title: JAsn1LC</p>
 * <p>Description: Asn1 BER Decoder Line Command</p>
 * <p>Copyright: Copyright (c) 2020</p>
 * <p>Company: GPSoft.</p>
 * @author Giovanni Palleschi
 * @version 1.0
 */

public class FileAsn1 {

  private static File sFilein;
  private static long offset = 0;

  private static int[] intBuffer;
  private static int TotBuffer=0;
  private static int PosBuffer=0;

  private long lengthfilein;
  private String NomeFile;
  
  private FileConfAsn1 fileConfAsn1;

  private RandomAccessFile raf;

  private int level;            // Livello Tag
  private int id;               // Identificativo
  private int tag;              // Codice del Tag
  private boolean flag;         // Indica se Primitivo o Meno
  private long length;           // Lunghezza del Tag
  private boolean isInfinity;   // Lunghezza indefinita
  private long bytereaded;      // Byte Letti
  private long offSetFile;      // Off set sul file
  private String ValuehexLength; // Lunghezza in esadecimale

  private String TagCodehex;       // Codifica esadecimale del Tag
  private String TagCodehexToShow; // Codifica esadecimale del Tag
  private long lOffSetToShow; // OffSet To Show
  private String TagCode;          // Codifica ascii del Tag
  private String TagCodeCurrent;          // Codifica ascii del Tag
  private int    lengthTot;

  byte[] ValueBytes;

  private String errorMsg = null;       // Codice Tag nel Formato es. 2-1
  
  private Parameters params;
  
  protected FileConfAsn1 getFileConfAsn1() {
	return fileConfAsn1;
  }

  protected void setFileConfAsn1(FileConfAsn1 fileConfAsn1) {
 	this.fileConfAsn1 = fileConfAsn1;
  }

  
  protected long getlOffSetToShow() {
	return lOffSetToShow;
  }

  protected void setlOffSetToShow(long lOffSetToShow) {
	  this.lOffSetToShow = lOffSetToShow;
  }

  protected String getTagCode() {
	  return TagCode;
  }

  protected void setTagCode() {
	  if ( this.getLevel() > 1 ) {
		  TagCode = TagCode + ".";
		  TagCodehexToShow = TagCodehexToShow + ".";
	  }
	  if ( params.bTag ) {
		TagCode = TagCode + this.getTag();	
		TagCodeCurrent = String.valueOf(this.getTag());	
	  } else {
		TagCode = TagCode + this.getId() + "-" + this.getTag();
		TagCodeCurrent = this.getId() + "-" + this.getTag();
	  }
	  TagCodehexToShow = TagCodehexToShow + this.getTagCodehex();
  }


  protected String getTagCodehexToShow() {
  	return TagCodehexToShow;
  }

  public String getLengthHex() {
    return ValuehexLength;
  }

  protected int getId() {
	return id;
  }

  protected void setId(int id) {
  	this.id = id;
  }

  protected int getTag() {
	return tag;
  }

protected static File getsFilein() {
  	return sFilein;
  }

  protected static long getOffset() {
  	return offset;
  }

  protected static int[] getIntBuffer() {
    	return intBuffer;
  }

  protected static int getTotBuffer() {
  	return TotBuffer;
  }

  protected static int getPosBuffer() {
  	return PosBuffer;
  }

  protected long getLengthfilein() {
  	return lengthfilein;
  }

  protected boolean isFlag() {
	return flag;
  }

  protected boolean isInfinity() {
	return isInfinity;
  }

  protected long getBytereaded() {
	return bytereaded;
  }


  protected String getValuehexLength() {
	return ValuehexLength;
  }

  protected String getTagCodehex() {
	return TagCodehex;
  }

  protected byte[] getValueBytes() {
	return ValueBytes;
  }

  protected Parameters getParams() {
	 return params;
  }

  protected void setTag(int tag) {
  	 this.tag = tag;
  }

  public String getValuehex()
  {
    String sRet = "";

    if (ValueBytes == null ) return null;
    for(int iInd=0;iInd<ValueBytes.length;iInd++)
    {
      sRet = sRet + Integer.toString( ( ValueBytes[iInd] & 0xff ) + 0x100, 16 /* radix */ ) .substring( 1 );
    }
    return sRet;
  }

  public int getLengthTot() {
    return lengthTot;
  }

  public void setLength(int newvalue) {
    length = newvalue;
  }
  
  public void setParameters(Parameters params) {
	  this.params = params;
  }

  private void GetPrimitiveValue() throws IOException
  {
    int indice;
    byte[] bytectrl;
    int ctrlByte;

    if ( isInfinity == false )
    {
      ValueBytes = new byte[(int)length];
      ctrlByte = this.read(ValueBytes);
      if (ctrlByte == -1) return;
      if (ctrlByte < length) return; 
    }
    else
    {
      ValueBytes = new byte[2048];
      bytectrl = new byte[2];
      indice = 0;
      ctrlByte = this.read(bytectrl);

      while (ctrlByte != -1 &&
             bytectrl[indice] != 0x00 &&
             bytectrl[indice+1] != 0x00)
      {
        ValueBytes[indice] = bytectrl[indice];
        ValueBytes[indice+1] = bytectrl[indice+1];

        ctrlByte = this.read(bytectrl);
        length = indice;
        indice+=2;
      }
    }
    return;
  }

  public boolean getPrimitive()
  {
    return flag;
  }

  public void setLevel(int levelToSet)
  {
    level = levelToSet;
    return;
  }

  public int getLevel()
  {
    return level;
  }
  public long getLength()
  {
    return length;
  }
  

  public long getOffSet() throws IOException
  {
	return raf.getFilePointer();
//    return offset;
  }

  public long getEndOfFile() throws IOException {
    return raf.length();
  }

  public String getNomeFile()
  {
    return NomeFile;
  }

  public long getLengthFile()
  {
   return lengthfilein;
 }
  
  public String getErrorMsg() {
		return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
  }
  
 public void resetFile() {
 	  TotBuffer=0;
 	  PosBuffer=0;
 }
 

 public FileAsn1(String NomeFile) throws IOException {

    this.NomeFile = NomeFile;
    intBuffer = new int[1024];
    TotBuffer = 0;
    PosBuffer = 0;
    TagCodehex = "";
    TagCodehexToShow = "";
    TagCode = "";
    
    setLevel(0);

    raf = new RandomAccessFile(NomeFile,"rw");
    
    sFilein = new File(NomeFile);
    lengthfilein = sFilein.length();
    
    
  }
 
  public void initFileAsn1() throws IOException {
	    // Check Byte Start
	    if (params.getlStart() >= 0) {
	    	if ( sFilein.length() < params.getlStart() ) {
	    	   throw new IOException("Start Byte Specified greater than file length");	
	    	}
	    	raf.seek(params.getlStart());
	    }
	    // Check Byte End
	    if (params.getlEnd() >= 0) {
	    	if ( sFilein.length() < params.getlEnd() ) {
	    	   throw new IOException("End Byte Specified greater than file length");	
	    	}
	    }
  }

  public int read() throws IOException
  {
    int iret;

    if ( PosBuffer < TotBuffer )
    {
      iret = intBuffer[PosBuffer];
      PosBuffer++;
    }
    else
    {
      iret = raf.read();
    }
    offset++;
    
    if ( offset > lengthfilein) iret = -2;
    return iret;
  }

  public int read(byte[] readbyte) throws IOException
  {
    int Breaded;
    Breaded = raf.read(readbyte);
    offset+=Breaded;
    return Breaded;
  }

  /****************************************************
  public int CtrlFillerRec() throws IOException
  {
    int iret = 0;
    byte[] bytectrl;
    bytectrl = new byte[1];
    TotBuffer = 0;
    PosBuffer = 0;
    do
    {
      if ( raf.read(bytectrl) != 1 ) return -1;

      if ( bytectrl[0] != 0x00 )
      {
        intBuffer[TotBuffer] = bytectrl[0];
        TotBuffer++;
      }
      else
      {
        offset++;
      }
    } while (bytectrl[0] == 0x00 );

    return iret;
  }
  ****************************************************/

  public int CtrlInfinitiveEnd() throws IOException
  {
    int iret = 0;
    byte[] bytectrl;

    TotBuffer = 0;
    PosBuffer = 0;

//    System.out.println("Entro in CtrlInfinitiveEnd");
    bytectrl = new byte[2];
    if ( raf.read(bytectrl) != 2 ) return -1;
    if ( bytectrl[0] == 0x00 &&
         bytectrl[1] == 0x00)
    {
      iret = 1;
      intBuffer[TotBuffer] = bytectrl[0];
      intBuffer[++TotBuffer] = bytectrl[1];
      TotBuffer++;
      offset+=2;
    }
    else
    {
      intBuffer[TotBuffer] = bytectrl[0];
      intBuffer[++TotBuffer] = bytectrl[1];
      TotBuffer++;
    }
    return iret;
  }

  public void close() throws IOException {
    raf.close();
  }

  public void WriteNew(int towrite) throws IOException {
      raf.write(towrite);
  }

  public int MoveOnFileToOffset(long offsetToMove) throws IOException
  {
  	raf.seek(offsetToMove);
    offset = offsetToMove; 
    
//    System.out.println(" Offset real " + raf.getFilePointer());
    
    return 0;
  }
  
  public int UpdateTagValueonFile(long offset, int towrite) throws IOException
  {
    raf.seek(offset);
    raf.write(towrite);
    return 0;
  }

  //public void resetOffSet()
  //{
  //  offset = 0;
  //}
  
  public String getValue()
  {
    String valueToRet = "";
    int iInd;

    for(iInd=0;iInd<ValueBytes.length;iInd++)
    {
      valueToRet = valueToRet + (char)ValueBytes[iInd];
    }
    return valueToRet;
  }

  public long getByteReaded()
  {
    return bytereaded;
  }

  public void setOffSetFile(long iOffSet)
  {
    offSetFile = iOffSet;
  }

  public long getOffSetFile()
  {
    return offSetFile;
  }

  //public boolean getCtrlInfinitiveEndCtrlInfinitiveEndInfinity()
  //{
  //  return isInfinity;
  //}
  
  private void displayOffSet() {
  	  if ( params.isbOffSet() ) {
  		  System.out.printf("%08d:%03d ", this.getlOffSetToShow(), this.getLevel());
  	  }
  }
  
  private void displayTagCode() {
	  int iSpaces = 0;
	  int iInd=0;
	  int iStart=0;
	  String convType = null;
	  String convTypeToShow = null;
	  String valueConv = null;
	  
      InfoConvAsn1 info;
	  
	  if ( !params.isbNoIndentation() ) {
		  for( int i=0;i<getLevel()-1;i++) {
			iInd =  this.getTagCode().indexOf('.', iStart);
			if ( iInd > 0 ) {
				iSpaces=++iInd;
				iStart= iSpaces;
			} else {
				break;
			}
		  }
		  
		  for( int i=0;i<iSpaces;i++ ) {
			System.out.printf(" "); 
		  }
	  }
	  System.out.printf("[%s] ", this.getTagCode() );
	  
	  if ( params.isbHex() ) {
		  System.out.printf("\"%s\"h ", this.getTagCodehexToShow());
	  }
	  
	  if ( params.getbFileStruct() ) {
		 
		  info = this.fileConfAsn1.getInfo(String.valueOf(this.TagCodeCurrent));
		  if ( info == null ) {
			  System.out.printf("{Unknow} ");
		  } else {
			  System.out.printf("{%s} ", info.getLabelTag());
			  convType = info.getTypeConv();
		  }
	  }
	  
	  // Length Tag
	  if ( !params.bNoLength ) {
		  System.out.printf("length : %d ", this.getLength());  
	  }
	 
	  if ( !params.bNoPrimValue ) {
		  
  	    if ( this.getPrimitive() ) {
		    System.out.printf(" \"%s\"h ", this.getValuehex());
		  
		    if ( params.getbFileStruct() ) {
			    convTypeToShow = convType;
			    if ( convType.compareTo("A") == 0 ) {
				    valueConv = this.getValue();

			    } else {
				    if ( convType.compareTo("B") == 0 ) {
				  	    valueConv = Utility.hexToBinary(this.getValuehex()); 
				    } else {
					    if ( convType.compareTo("N") == 0 ) {
				  		    valueConv = Utility.hexToDouble(this.getValuehex());
					    } else {
					  	    valueConv = this.getValuehex();
						    convTypeToShow = "H";
					    }
				    }
			    }
		    }
		    System.out.printf("Value (%s)%s", valueConv, convTypeToShow);
	    }
	  }
	  
	  System.out.printf("\n");
  }
  
  private void displayTag() {
	 displayOffSet(); 
	 displayTagCode();
  }
  

  private void decreaseTagCode() {
	  int iInd = TagCode.lastIndexOf('.');
	  if ( iInd > 0 ) {
		  TagCode = TagCode.substring(0, iInd);
	  }

	  iInd = TagCodehexToShow.lastIndexOf('.');
	  if ( iInd > 0 ) TagCodehexToShow = TagCodehexToShow.substring(0, iInd);
  }
   
  public int readTags() throws IOException {

	    int nextbis;
	    int next;
	    int contabyte;
	    int nbyte;
	    int iRet=0;
	    long lCurrentLength = 0;
	    boolean currentInfinity = false;
	    int iLocalLevel;
	    long startOffSet = raf.getFilePointer();
	    
	    long nextEnd=0;

        
        this.setlOffSetToShow(raf.getFilePointer());

	    isInfinity = false;

	    bytereaded = this.getOffSet();
	    
	    
	    // End Offset setted
	    if ( params.getlEnd() > 0 && bytereaded > params.getlEnd() ) return -2;
	    
	    this.setOffSetFile(bytereaded);
	    
	    next = this.read();
	    if ( next == -1 )
	    {
	      setErrorMsg("ERROR TAG");
	      return -1;
	    }
	    if ( next == -2 )
	    {
	      setErrorMsg("FINE FILE");
	      return -2;
	    }
//	    System.out.println("Letto >" + next + "<" );
	    next = next & 0xff;
	    TagCodehex = Integer.toHexString(next);

	// Calcolo l'ID
	    id = (next & 192) >> 6;

	// Calcolo Flag Primitive
	    if ( ((next & 32) >> 5) == 1 )
	    flag = false;
	    else
	    flag = true;

	// Calcolo codice Tag
	    tag = (next & 31);
	    if ( tag == 31 )
	    {
	      nextbis = this.read();
	      if ( nextbis == -1 )
	      {
	        setErrorMsg("ERROR TAG");
	        return -1;
	      }
	      if ( nextbis == -2 )
	      {
	        setErrorMsg("FINE FILE");
	        return -1;
	      }
	      nextbis = nextbis & 0xff;
	      TagCodehex = TagCodehex + Integer.toHexString(nextbis);

	      tag = nextbis & 127;
	      while( 128 == ( nextbis & 128 ) )	/* CICLO WHILE-A */
	      {
	        nextbis = this.read();
	        if ( nextbis == -1 )
	        {
	          setErrorMsg("ERROR TAG");
	          return -1;
	        }
	        if ( next == -2 )
	        {
	          setErrorMsg("END FILE");
	          return -1;
	        }
	        nextbis = nextbis & 0xff;
	        TagCodehex = TagCodehex + Integer.toHexString(nextbis);
	        tag = tag << 7 | ( nextbis & 127 );
	      }
	    }

	    // Lettura Lunghezza
	    next = this.read();
	    if ( next == -1 )
	    {
	      setErrorMsg("ERROR LENGTH TAG");
	      return -1;
	    }
	    if ( next == -2 )
	    {
	      setErrorMsg("END FILE");
	      return -1;
	    }

	    nbyte = next & 127;

	    if ( (next & 128) == 0 )
	    {
	      length = nbyte;
	    }
	    else
	    {
	      if ( nbyte > 0 )
	      {
	        if ( nbyte > 4 )
	        {
	          setErrorMsg("ERROR TAG");
	          return -1;
	        }
	        else
	        {
	          next = this.read();
	          if ( next == -1 )
	          {
	            setErrorMsg("ERROR TAG");
	            return -1;
	          }
	          if ( next == -2 )
	          {
	            setErrorMsg("END FILE");
	            return -1;
	          }

	          length = next;
	          for(contabyte=1;contabyte<nbyte;contabyte++)
	          {
	            next = this.read();
	            if ( next == -1 )
	            {
	              setErrorMsg("ERROR TAG");
	              return -1;
	            }
	            if ( next == -2 )
	            {
	              setErrorMsg("END FILE");
	              return -1;
	            }

	            length = length << 8 | ( next );
	          }
	        }
	      }
	      else
	      {
	        length = -1;
	        isInfinity = true;
	      }
	    }     

	// Read Primitive Tag 
	    if ( flag == true ) {
	    	GetPrimitiveValue();
	    }
	    
	    lCurrentLength = length;
	    bytereaded = this.getOffSet() - bytereaded;

	    this.setLevel(this.getLevel()+1);
	    this.setTagCode();
	    displayTag();
	    
	    if ( flag == false ) {
	    	
	      currentInfinity = isInfinity;
//	      if ( raf.getFilePointer() <= 1000L ) System.out.printf("\nDEBUG BEFORE isInfinity %d raf.getFilePointer() <%d> - startOffSet <%d> <= lCurrentLength <%d>\n", (isInfinity?1:0), raf.getFilePointer(), startOffSet, lCurrentLength);
//	      if ( raf.getFilePointer() >= 3666000 ) System.out.printf("\nDEBUG BEFORE isInfinity %d raf.getFilePointer() <%d> - startOffSet <%d> <= lCurrentLength <%d>\n", (isInfinity?1:0), raf.getFilePointer(), startOffSet, lCurrentLength);
	      if ( !isInfinity ) nextEnd=raf.getFilePointer()+lCurrentLength;
	      while (
	    	 	  (!currentInfinity && ((raf.getFilePointer() - startOffSet ) <= lCurrentLength)) ||
	    		  (currentInfinity && CtrlInfinitiveEnd() == 0 )
	    	    ) {
	      // Recursive function
	    	  iRet = readTags();
	    	  if ( iRet < 0 ) break;
	      }
	      
	      // Check if end tag is correct
	      if ( iRet == 0 && !currentInfinity && raf.getFilePointer() != getLengthFile() ) {
	    	  if ( nextEnd != raf.getFilePointer() ) {
	    		 System.out.println("\n ERROR End of Tag expected is " + nextEnd + " instead of " + raf.getFilePointer());  
	    		 iRet=-1;
	    	  }
	      }
	    
//	    System.out.printf("\n DEBUG ESCO\n");
	    } 
	    
        iLocalLevel = this.getLevel()-1;
	    this.setLevel(iLocalLevel);
//	    System.out.printf("\nDEBUG After While Level <%d>\n", this.getLevel());
	    this.decreaseTagCode();
//        if ( raf.getFilePointer() >= 3666000 ) System.out.printf("\nDEBUG AFTER isInfinity %d raf.getFilePointer() <%d> - startOffSet <%d> <= lCurrentLength <%d>\n", (isInfinity?1:0), raf.getFilePointer(), startOffSet, lCurrentLength);
	    return iRet;
  }
}