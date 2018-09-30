import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

class Program {

	LinkedList<Document> docs = new LinkedList<>(); 
	Iterator itr = null;

	public Program() { 
		FileReaderProgram reader = new FileReaderProgram(docs);
		reader.execute();

		makeDic();

	}

	void makeDic() {
		for(int i=0; i<docs.size(); i++) {
			Document curDoc = docs.get(i);

			String[] tempVocas = curDoc.getContents().split
					("\\'|\\?|\"|\\.\\.|\\[|\\]|\\{|\\}|\\(|\\)|\\. |\\.\n| |\\, |\n|!");

			LinkedList<String> contentVocas = new LinkedList<>();

			for(int j=0; j<tempVocas.length; j++) {
				contentVocas.add(tempVocas[j]);
			}

			adjustVocas(contentVocas);

			for(int j=0; j<contentVocas.size(); j++) {
				curDoc.dictionary.add(contentVocas.get(j));
			}

			itr = curDoc.dictionary.iterator();

			while(itr.hasNext()) {
				System.out.println(itr.next());
			}
		}
	}

	LinkedList adjustVocas(LinkedList<String> contentVocas) {

		boolean digitPrivi = false;

		for(int i=0; i<contentVocas.size(); i++) {

			String curString = contentVocas.get(i);


			if(curString.compareTo(" ") == 0) {

			}

			for(int j=0; j<curString.length(); j++) {

				char curChar = curString.charAt(j);

				if(curChar == ',' && 
						digitPrivi && 
						j != curString.length()-1 &&
						Character.isDigit(curString.charAt(j+1))) {
					curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
				}

				if(curChar == '/' && 
						j != curString.length()-1) {
					if(!digitPrivi || !Character.isDigit(curString.charAt(j+1))) {
						curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
					}
				}

				if(Character.isUpperCase(curChar)) {
					curString = curString.replace(curChar, Character.toLowerCase(curChar));
				}

				digitPrivi = Character.isDigit(curChar);
			}

			contentVocas.set(i, curString);
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

