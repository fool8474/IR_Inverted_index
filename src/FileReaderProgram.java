import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

class FileReaderProgram {

	LinkedList<Document> docs = null;
	
	public FileReaderProgram(LinkedList<Document> docs) {
		this.docs = docs;
	}
	
	void execute() {
		readFiles();
	}
	
	void readFiles() {
		File dirFile= new File("Dataset.");

		File [] fileList=dirFile.listFiles();
		for(File tempFile : fileList) {
			if(tempFile.isFile()) {
				String tempFileName=tempFile.getName();

				File curFile = new File(dirFile + "/" + tempFileName);

				saveFileContents(curFile);
			}
		}
	}

	String saveFileContents(File curFile) {
		
		/**
		 * Divide the file into documents according to <DOC>.
		 */
		
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
	
	String adjustLine(String curLine) {
		
		/**
		 * remove Tags
		 */
		
		int start = 0;
		int end = 0;
		
		char curChar = ' ';
				
		for(int i=0; i<curLine.length(); i++) {
			
			curChar = curLine.charAt(i);
			
			if (curChar == '<') {
				start = i;
			}
			
			else if (curChar == '>') {
				end = i;
				
				String replaceString = curLine.substring(0, start) + curLine.substring(end+1, curLine.length());
				curLine = replaceString;
				
				i = start;
			}
			
		}
		
		return curLine;
	}
}
