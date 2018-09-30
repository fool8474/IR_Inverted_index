class Document {
	
	String pageName;
	private String contents;
	int docID;
	
	public Document(String pageName, int docID) {
		this.pageName = pageName;
		this.docID = docID;
		contents = "";
	}
	
	void addContent(String newContents) {
		contents += newContents;
	}
	
	String getContents() {
		return contents;
	}
}
