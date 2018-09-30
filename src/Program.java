import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

class Program {

	LinkedList<Document> docs = new LinkedList<>(); 
	
	public Program() { 
		FileReaderProgram reader = new FileReaderProgram(docs);
		reader.execute();
		
		showAllDocs();
		
		
	}
	
	void showAllDocs() {
		
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
	}
	
	
}

