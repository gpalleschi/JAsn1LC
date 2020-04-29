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
