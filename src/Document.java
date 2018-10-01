import java.util.TreeSet;

class Document {
	
	TreeSet <String> dictionary; //use treeset for dictionary
	String pageName;
	private String contents;
	int docID;
	
	
	public Document(String pageName, int docID) {
		this.pageName = pageName;
		this.docID = docID;
		contents = "";
		dictionary = new TreeSet<>();
	}
	
	void addContent(String newContents) {
		contents += newContents;
	}
	
	String getContents() {
		return contents;
	}
}
