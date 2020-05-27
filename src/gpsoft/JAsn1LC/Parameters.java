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

public class Parameters {

	boolean bHex = false;
	boolean bOffSet = false;
	boolean bTag = false;
	boolean bNoLength = false;
	boolean bNoPrimValue = false;
	boolean bNoIndentation = false;
	boolean bEncode = false;
	long lStart = -1;
	long lEnd = -1;
	boolean bFileStruct = false;
	String sFileStruct = null;
	String sFileInput = null;
	String sFileOutput = null;
	
	protected String getsFileOutput() {
		return sFileOutput;
	}

	protected void setsFileOutput(String sFileOutput) {
		this.sFileOutput = sFileOutput;
	}

	protected boolean isbEncode() {
		return bEncode;
	}

	protected void setbEncode(boolean bEncode) {
		this.bEncode = bEncode;
	}

	public String getsFileStruct() {
		return sFileStruct;
	}
	
	public boolean getbFileStruct() {
		return bFileStruct;
	}
	
	public void setsFileStruct(String sFileStruct) {
		bFileStruct = true;
		this.sFileStruct = new String(sFileStruct);
	}
	public String getsFileInput() {
		return sFileInput;
	}
	public void setsFileInput(String sFileInput) {
		this.sFileInput = new String(sFileInput);
	}
	public boolean isbHex() {
		return bHex;
	}
	public void setbHex(boolean bHex) {
		this.bHex = bHex;
	}
	public boolean isbOffSet() {
		return bOffSet;
	}
	public void setbOffSet(boolean bOffSet) {
		this.bOffSet = bOffSet;
	}
	public boolean isbTag() {
		return bTag;
	}
	public void setbTag(boolean bTag) {
		this.bTag = bTag;
	}
	public long getlStart() {
		return lStart;
	}
	public boolean isbNoLength() {
		return bNoLength;
	}
	public void setbNoLength(boolean bNoLength) {
		this.bNoLength = bNoLength;
	}
	public boolean isbNoPrimValue() {
		return bNoPrimValue;
	}
	public void setbNoPrimValue(boolean bNoPrimValue) {
		this.bNoPrimValue = bNoPrimValue;
	}
	public void setlStart(long lStart) {
		this.lStart = lStart;
	}
	public long getlEnd() {
		return lEnd;
	}
	public void setlEnd(long lEnd) {
		this.lEnd = lEnd;
	}
	protected boolean isbNoIndentation() {
		return bNoIndentation;
	}
	protected void setbNoIndentation(boolean bNoIndentation) {
		this.bNoIndentation = bNoIndentation;
	}

}
