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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileConfAsn1 {
	
	private HashMap<String, InfoConvAsn1> ConvAsn1;
	private boolean bError;

	protected boolean isbError() {
		return bError;
	}

	protected void setbError(boolean bError) {
		this.bError = bError;
	}
	
	public InfoConvAsn1 getInfo(String tag) {
		return ConvAsn1.get(tag);
	}

	public FileConfAsn1(String fileConfAsn1) throws IOException {

  	      setbError(false);
  	      ConvAsn1 = new HashMap<String, InfoConvAsn1>();
 		  try
		  {
		    BufferedReader reader = new BufferedReader(new FileReader(fileConfAsn1));
		    String line;
		    while ((line = reader.readLine()) != null)
		    {
		      String[] parts = line.split("\\|");
		      if ( parts.length != 3 ) {
		    	  System.out.println("\n Error on file <" + fileConfAsn1 + "> record <" + line + "> not in correct format."); 
		    	  setbError(true);
		    	  break;
		      }
		      ConvAsn1.put(parts[0], new InfoConvAsn1(parts[1], parts[2]));
		    }
		    reader.close();
		  }
		  catch (Exception e)
		  {
		    System.err.format("\nException occurred trying to read '%s'.", fileConfAsn1);
		    e.printStackTrace();
            setbError(true);
		    return;
		  }
		
	}
	
	

}
