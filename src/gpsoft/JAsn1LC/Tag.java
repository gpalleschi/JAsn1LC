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

import java.util.ArrayList;
import java.util.List;

public class Tag {
	
	private int iLevel;
	private int tagId;
	private int tagClass;
	private boolean isStruct;
	private boolean isDefiniteLength;
	private long length;
	private String value; // Hexadecimal Value for primitive Tags
	
	private Tag tagFather;
	private List<Tag> lTagSon;
	
	protected boolean isDefiniteLength() {
		return isDefiniteLength;
	}

	protected void setDefiniteLength(boolean isDefiniteLength) {
		this.isDefiniteLength = isDefiniteLength;
	}

	protected List<Tag> getlTagSon() {
		return lTagSon;
	}

	protected void setlTagSon(List<Tag> lTagSon) {
		this.lTagSon = lTagSon;
	}

	protected int getiLevel() {
		return iLevel;
	}

	protected void setiLevel(int iLevel) {
		this.iLevel = iLevel;
	}

	public void setInfinityLength() {
		this.length = -1;
	}
	
	public void addLength(long length) {
		if ( this.length >= 0 ) this.length+=length;
	}
	
	protected int getTagId() {
		return tagId;
	}

	protected void setTagId(int tagId) {
		this.tagId = tagId;
	}

	protected int getTagClass() {
		return tagClass;
	}

	protected void setTagClass(int tagClass) {
		this.tagClass = tagClass;
	}

	protected boolean isStruct() {
		return isStruct;
	}

	protected void setStruct(boolean isStruct) {
		this.isStruct = isStruct;
	}

	protected long getLength() {
		return length;
	}

	protected void setLength(long length) {
		this.length = length;
	}

	protected String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	protected Tag getTagFather() {
		return tagFather;
	}

	protected void setTagFather(Tag tagFather) {
		this.tagFather = tagFather;
	}

	public void addLengthFathers(long lLength) {
		long lDiffToAdd;
		Tag currentFatherTag = this.tagFather;
		if ( currentFatherTag.isDefiniteLength == true) {
		   lDiffToAdd = Utility.calcDiffLength(currentFatherTag.getLength(),lLength);
//	       System.out.println("DEBUG TO DELETE **** current Length : " + this.getLength() + " Length to add : " + lLength + " lDiffToAdd : " + lDiffToAdd);
           currentFatherTag.setLength(currentFatherTag.getLength()+lLength);
//		   System.out.println("DEBUG TO DELETE **** Tag " + currentFatherTag.getTagId() + "-" + currentFatherTag.getTagClass() + " Add Length " + lLength + " New Length is " + currentFatherTag.getLength());
           lLength+=lDiffToAdd;
		} else {
           // Add Length 2 bytes for end stream indefinitive length
           lLength+=2;	
		}
		if ( currentFatherTag.getTagFather() != null ) currentFatherTag.addLengthFathers(lLength);
	}
	
	public Tag getLevelTagPrevious(int iLevel) {
		Tag retTag = null;
		Tag currentFatherTag = this.getTagFather();
		while ( currentFatherTag != null ) {
			if ( currentFatherTag.getiLevel() == iLevel ) {
			   retTag = currentFatherTag;
			   break;
			}
		    currentFatherTag = currentFatherTag.getTagFather();
		}
		return retTag;
	}
	
	public void addSon(Tag sonTag) {
		lTagSon.add(sonTag); 
	}
	
	public Tag(int tagId, int tagClass, int iLevel, boolean isStruct, boolean isDefiniteLength) {
		super();
		this.tagId = tagId;
		this.tagClass = tagClass;
		this.isStruct = isStruct;
		this.isDefiniteLength = isDefiniteLength;
		this.iLevel = iLevel;
		this.length = 0;
		this.value = "";
		this.tagFather = null;
		lTagSon = new ArrayList<Tag>();
	}
}
