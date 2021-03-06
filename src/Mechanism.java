import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Scanner;

class Mechanism {

	Iterator<?> docsItr = null;
	LinkedList<Document> docs = new LinkedList<>(); 
	LinkedList<Integer> tempList = null;
	HashMap<String, LinkedList<Integer>> posts = new HashMap<>(); //use hashMap for postings list
	FileReaderProgram reader = new FileReaderProgram(docs);
	TreeSet<String> allVocas = new TreeSet<>();
	
	Scanner in = new Scanner(System.in);

	int minNum = Integer.MAX_VALUE;
	int maxNum = Integer.MIN_VALUE;
	double avg = 0.0;
	int minDocID = 0;
	int maxDocID = 0;
	
	public void programExecute() {
		
		reader.execute(); //read file
		makeDic(); //make dic and postings lists
		getDocStatistic();

		//showAllWords();
		HCI programHCI = new HCI(posts, docs); //doing search progress
		programHCI.setHCINums(minNum, maxNum, minDocID, maxDocID, avg);
		programHCI.HCIMap();
	}
	
	private void showAllWords() {
		
		/**
		 * Method showing words stored in the dic (test method)
		 */
		
		Iterator<String> itr = allVocas.iterator();
		int count = 0;
		
		while(itr.hasNext()) {
			System.out.print(itr.next() + " ");
			count++;
			if(count == 4) {
				System.out.println();
				count = 0;
			}
		}
		System.out.println();
	}

	private void getDocStatistic() {
		
		for(int i=0; i<docs.size(); i++) {
			int curSize = docs.get(i).dictionary.size();
			avg += curSize;

			if(curSize < minNum) {
				minDocID = i;
				minNum = curSize;
			}

			if(curSize > maxNum) {
				maxDocID = i;
				maxNum = curSize;
			}
		}
	}

	private void makeDic() {
		for(int i=0; i<docs.size(); i++) {
			Document curDoc = docs.get(i);

			String[] tempVocas = curDoc.getContents().split
					("\\*|\\-\\-|\\_|\\;|\\:|\\'|\\?|\"|\\.\\.|\\[|\\]|\\{|\\}|\\(|\\)|\\.\n| |\\, |\n|!");
			// I divided the words into [ * -- _ ; : ' ? " .. [ ] { } ( ) .\n , \n ! and ' '
			
			LinkedList<String> contentVocas = new LinkedList<>();

			for(int j=0; j<tempVocas.length; j++) {
				contentVocas.add(tempVocas[j]);
			}

			adjustVocas(contentVocas); // to adjust words


			for(int j=0; j<contentVocas.size(); j++) {

				String curString = contentVocas.get(j);
				curDoc.dictionary.add(curString); // save in dic (treeset)
				allVocas.add(curString);
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
				
				// make postings list (hashMap)
				
			}
		}
	}

	public String toLowerCase(String curString) {
		
		for(int i=0; i<curString.length(); i++) {
			char curChar = curString.charAt(i);
			
			if(Character.isUpperCase(curChar)) {
				curString = curString.replace(curChar, Character.toLowerCase(curChar));
				curChar = Character.toLowerCase(curChar);
			}
		}
		
		return curString;
	}
	
	private LinkedList<String> adjustVocas(LinkedList<String> contentVocas) {

		/**
		 * Adjust words through some rules.
		 */
		
		boolean digitPrivi = false;

		for(int i=0; i<contentVocas.size(); i++) {

			String curString = contentVocas.get(i);

			curString = toLowerCase(curString);
			
			for(int j=0; j<curString.length(); j++) {

				char curChar = curString.charAt(j);
				
				if(curChar == '.' || curChar == '/') {
					if(j == curString.length()-1) {
						curString = curString.substring(0, j);
						j--; continue;
					}
					else if(j == 0 && curString.length() != 1) {
						curString = curString.substring(1, curString.length());
						j--; continue;
					} 
				} // remove . or / in case ~~. .~~ ~~/ /~~

				if(curChar == ',') {
					if(digitPrivi && j != curString.length()-1 && Character.isDigit(curString.charAt(j+1))) {
						curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
						j--; continue;
					}

					if(!digitPrivi || j != curString.length()-1 && !Character.isDigit(curString.charAt(j+1))) {
						contentVocas.add(curString.substring(j+1, curString.length()));
						curString = curString.substring(0, j);
						j--; continue;
					}
				} // remove ',' in case digit [3,000 >> 3000], remove ',' and divide into two letters (not digit) [car,bike >> car bike]
				

				if((curChar == '/' || curChar == '-' || curChar == '.') && 
						j != curString.length()-1) {

						String[] dividedWord = new String[0];
					switch(curChar) {
					case('/') :
						dividedWord = curString.split("\\/"); break;
					case('-') :
						dividedWord = curString.split("\\-"); break;
					case('.') :
						dividedWord = curString.split("\\."); break;
					}
					
					for(int k=0; k<dividedWord.length; k++)	contentVocas.add(dividedWord[k]);
					
				} //remove word and divide into two letters and save the original [2007/08 >> 2007 08 2007/08, yukon/denali >> yukon denali yukon/denali]

				digitPrivi = Character.isDigit(curChar);
			}
			
			if(curString.compareTo("") == 0) {
				contentVocas.remove(i);
				i--;
				continue;
			} //remove ''

			contentVocas.set(i, curString);
		}

		return contentVocas;
	}
}

