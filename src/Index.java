import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Index {

	public static void main(String[] args) {

		Program program = new Program();
	}
}

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

class Program {

	LinkedList<Document> docs = new LinkedList<>(); 
	
	public Program() { 

		readFiles();
		showAllDocs();
	}
	
	void showAllDocs() {
		
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
	}
	
	void readFiles() {
		File dirFile= new File("Dataset.");

		File [] fileList=dirFile.listFiles();
		for(File tempFile : fileList) {
			if(tempFile.isFile()) {
				String tempFileName=tempFile.getName();
				System.out.println(dirFile + tempFileName);

				File curFile = new File(dirFile + "/" + tempFileName);

				saveFileContents(curFile);
			}
		}
	}

	String saveFileContents(File curFile) {
		
		String fileLine = "";
		String curBulletinName = "";
		Document curNewDocument = null;
		
		try {

			FileReader filereader = new FileReader(curFile);
			BufferedReader buff = new BufferedReader(filereader);
			
			String line = "";
			
			while((line = buff.readLine()) != null) {
				
				if(line.length() > 6 && line.substring(0, 7).compareTo("<DOCNO>") == 0) {
					curBulletinName = line.substring(7,line.length()-8);
					continue;
				}
				
				if(line.compareTo("<DOC>") == 0) {
					curNewDocument = new Document(curBulletinName, docs.size());
					continue;
				}
				
				else if (line.compareTo("</DOC>") == 0) {
					docs.add(curNewDocument);
					continue;
				}
				
				line = adjustLine(line);
				line += "\n";
				curNewDocument.addContent(line);
			}
			
			buff.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileLine;
	}
	
	String adjustLine(String curline) {
		
		int start = 0;
		int end = 0;
		
		char curChar = ' ';
				
		for(int i=0; i<curline.length(); i++) {
			
			curChar = curline.charAt(i);
			
			if (curChar == '<') {
				start = i;
			}
			
			else if (curChar == '>') {
				end = i;
				
				String replaceString = curline.substring(0, start) + curline.substring(end+1, curline.length());
				curline = replaceString;
				
				i = start;
			}
			
		}
		
		return curline;
	}
}





