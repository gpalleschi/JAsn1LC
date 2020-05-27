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

public class InfoConvAsn1 {
	
	private String typeConv;
	private String labelTag;
	
	protected String getTypeConv() {
		return typeConv;
	}

	protected void setTypeConv(String typeConv) {
		this.typeConv = typeConv;
	}

	protected String getLabelTag() {
		return labelTag;
	}

	protected void setLabelTag(String labelTag) {
		this.labelTag = labelTag;
	}



	public InfoConvAsn1(String typeConv, String labelTag) {
		super();
		this.typeConv = typeConv;
		this.labelTag = labelTag;
	}

}
