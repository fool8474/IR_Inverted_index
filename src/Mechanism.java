import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Scanner;

class Mechanism {

	Iterator<?> docsItr = null;
	LinkedList<Document> docs = new LinkedList<>(); 
	LinkedList<Integer> tempList = null;
	HashMap<String, LinkedList<Integer>> posts = new HashMap<>();
	FileReaderProgram reader = new FileReaderProgram(docs);
	TreeSet<String> allVocas = new TreeSet<>();
	
	Scanner in = new Scanner(System.in);

	double avg = 0.0;
	int minDocID = 0;
	int maxDocID = 0;
	int minNum = Integer.MAX_VALUE;
	int maxNum = Integer.MIN_VALUE;

	void HCI() {
		while(true) {

			menu(0);
			int choice = in.nextInt(); in.nextLine();

			switch(choice) {	
			case 1 :
				menu(1);
				break;

			case 2 :
				String YN = "Y";
				while(YN.compareTo("Y") == 0 || YN.compareTo("y") == 0) {
					search();

					System.out.println("More Search?(Y/N) : ");
					YN = in.nextLine();
				}

				break;

			case 3 : 
				System.out.println("Quit Program");
				return;

			default :
				System.out.println("Wrong Input!!");
				break;

			}
		}
	}

	private void search() {
		menu(2);
		String searchWord = in.nextLine();

		if(posts.containsKey(searchWord)) {
			System.out.println("The Searching Result is...");

			tempList = posts.get(searchWord);

			System.out.print(tempList.size() + " found, " + searchWord + " : ");
			for(int i=0; i<tempList.size(); i++) {
				System.out.print(tempList.get(i) + " ");
				if(i%30 == 0) System.out.println();
			}
			System.out.println();

			while(true) {
				menu(3);
				int choice = in.nextInt(); in.nextLine();
				if(choice == -1)
					break;
				if(choice < docs.size() && choice >= 0) {
					showDoc(choice);
				}
				else {
					System.out.println("Wrong docID...");
				}
			}
		}

		else { System.out.println("Nothing found in Result"); }

	}

	private void menu(int menuCase) {

		switch(menuCase) {

		case 0 : 

			System.out.println("------------------------");
			System.out.println("     Choice Menu");
			System.out.println("     -----------");
			System.out.println("   1.get Statistic");
			System.out.println("      2.Search");
			System.out.println("       3.Exit");
			System.out.println("     -----------");
			System.out.println("------------------------");
			break;

		case 1 :
			System.out.println("Number of Docs : " + docs.size());
			System.out.println("Average Words of Docs : " + avg/docs.size());
			System.out.println("Max size of Doc Dictionary : " + maxDocID + ", Number of Words : " + maxNum);
			System.out.println("Min Size of Doc Dictionary : " + minDocID + ", Number of Words : " + minNum);
			break;

		case 2 :
			System.out.println("What are you searching for? ");
			break;

		case 3 :
			System.out.print("Choose number which want to open (Exit : -1) : ");
			break;
		}
	}

	public void programExecute() {
		
		reader.execute();
		makeDic();
		getDocStatistic();

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
		
		HCI();
	}

	private void showDoc(int docID) {
		System.out.println(docs.get(docID).pageName);
		System.out.println(docs.get(docID).getContents());
	}

	private void showAllDocs() {
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
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

			LinkedList<String> contentVocas = new LinkedList<>();

			for(int j=0; j<tempVocas.length; j++) {
				contentVocas.add(tempVocas[j]);
			}

			adjustVocas(contentVocas);


			for(int j=0; j<contentVocas.size(); j++) {

				String curString = contentVocas.get(j);
				curDoc.dictionary.add(curString);
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
			}
		}
	}

	private LinkedList<String> adjustVocas(LinkedList<String> contentVocas) {

		boolean digitPrivi = false;

		for(int i=0; i<contentVocas.size(); i++) {

			String curString = contentVocas.get(i);

			for(int j=0; j<curString.length(); j++) {

				char curChar = curString.charAt(j);

				if(Character.isUpperCase(curChar)) {
					curString = curString.replace(curChar, Character.toLowerCase(curChar));
					curChar = Character.toLowerCase(curChar);
				}
				
				if(curChar == '.' || curChar == '/') {
					if(j == curString.length()-1) {
						curString = curString.substring(0, j);
						j--; continue;
					}
					else if(j == 0 && curString.length() != 1) {
						curString = curString.substring(1, curString.length());
						j--; continue;
					}
				}

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
				}

				if(curChar == '/' && 
						j != curString.length()-1) {
					if(!digitPrivi || !Character.isDigit(curString.charAt(j+1))) {
						curString = curString.substring(0, j) + curString.substring(j+1, curString.length());
						j--; continue;
					}
					else {
						String[] slashDivided = curString.split("\\/");
						for(int k=0; k<slashDivided.length; k++)	contentVocas.add(slashDivided[k]);
					}
				}

				digitPrivi = Character.isDigit(curChar);
			}
			
			if(curString.compareTo("") == 0) {
				contentVocas.remove(i);
				i--;
				continue;
			}

			contentVocas.set(i, curString);
		}

		return contentVocas;
	}
}

