import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

class Program {

	Iterator<?> docsItr = null;
	LinkedList<Document> docs = new LinkedList<>(); 
	LinkedList<Integer> tempList = null;
	HashMap<String, LinkedList<Integer>> posts = new HashMap<>();
	FileReaderProgram reader = new FileReaderProgram(docs);


	void programExecute() {
		reader.execute();

		makeDic();
		showDocInfor();
	}

	void showAllDocs() {
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
	}	

	void showDocInfor() {

		double avg = 0.0;

		for(int i=0; i<docs.size(); i++) {
			avg += docs.get(i).dictionary.size();
		}

		System.out.println("문서의 수 : " + docs.size());
		System.out.println("문서 당 평균 단어 수 : " + avg/docs.size());
		System.out.println("최대 단어의 문서 : " );
		System.out.println("최소 단어의 문서 : " );
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

				String curString = contentVocas.get(j);
				curDoc.dictionary.add(curString);
			}

			docsItr = curDoc.dictionary.iterator();

			while(docsItr.hasNext()) {
				String curString = (String)docsItr.next();

				if(posts.containsKey(curString)) {
					tempList = posts.get(curString);
					tempList.add(curDoc.docID);
				}

				else {
					tempList = new LinkedList<>();
					tempList.add(curDoc.docID);
					posts.put(curString, tempList);
				}
			}
		}
	}

	LinkedList<String> adjustVocas(LinkedList<String> contentVocas) {

		boolean digitPrivi = false;

		for(int i=0; i<contentVocas.size(); i++) {

			String curString = contentVocas.get(i);

			if(curString.compareTo("") == 0) {
				contentVocas.remove(i);
				i--;
				continue;
			}

			for(int j=0; j<curString.length(); j++) {

				char curChar = curString.charAt(j);

				if(curChar == ',') {
					if(digitPrivi && j != curString.length()-1 && Character.isDigit(curString.charAt(j+1))) {
						curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
					}

					if(!digitPrivi || j != curString.length()-1 && !Character.isDigit(curString.charAt(j+1))) {
						contentVocas.add(curString.substring(j+1, curString.length()));
						curString = curString.substring(0, j);
					}
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


}

