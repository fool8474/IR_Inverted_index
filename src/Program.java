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
		
		makeDic();
		
	}
	
	void makeDic() {
		for(int i=0; i<docs.size(); i++) {
			Document curDoc = docs.get(i);
			
			String[] contentVocas = curDoc.getContents().split
					("\\?|\"|\\.\\.|\\[|\\]|\\{|\\}|\\(|\\)|\\. |\\.\n| |\\, |\n|!");
			
			contentVocas = adjustVocas(contentVocas);
			
			for(int j=0; j<contentVocas.length; j++) {
				System.out.println(contentVocas[j] + " ");
			}
		}
	}
	
	String[] adjustVocas(String[] contentVocas) {
		
		boolean digitPrivi = false;
		
		for(int i=0; i<contentVocas.length; i++) {
			
			String curString = contentVocas[i];
			
			for(int j=0; j<curString.length(); j++) {
				
				char curChar = curString.charAt(j);
				
				if(curChar == ',' && 
						digitPrivi && 
						j != curString.length()-1 &&
						Character.isDigit(curString.charAt(j+1))) {
					curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
				}
				
				if(Character.isUpperCase(curChar)) {
					curString = curString.replace(curChar, Character.toLowerCase(curChar));
				}
				
				digitPrivi = Character.isDigit(curChar);
			}
			
			contentVocas[i] = curString;
		}
		
		return contentVocas;
	}
	
	void showAllDocs() {
		
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
	}	
}

