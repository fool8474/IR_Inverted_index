import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Index {

	public static void main(String[] args) {

		Program program = new Program();
	}
}

class Document {
	
	String pageName;
	String contents;
	int docID;
	
	
	public Document(String pageName, int docID) {
		this.pageName = pageName;
		this.docID = docID;
	}
}

class Program {

	Document[] fileContents;
	
	public Program() { 

		readFiles();

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
		
		try {

			FileReader filereader = new FileReader(curFile);
			BufferedReader buff = new BufferedReader(filereader);
			
			String line = "";
			
			while((line = buff.readLine()) != null) {
				
				line = adjustLine(line);
				fileLine += line;
				
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





