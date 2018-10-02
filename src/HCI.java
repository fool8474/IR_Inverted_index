import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class HCI {
	
	private Mechanism tempProgram = new Mechanism();
	private Scanner in = new Scanner(System.in);
	private HashMap<String, LinkedList<Integer>> posts;
	private LinkedList<Integer> temp;
	private LinkedList<Document> docs;
	private int minNum;
	private int maxNum;
	private int minDocID;
	private int maxDocID;
	private double avg;
	
	HCI(HashMap<String, LinkedList<Integer>> posts, LinkedList<Document> docs) {
		this.posts = posts;
		this.docs = docs;
	}
	
	void setHCINums(int minNum, int maxNum, int minDocID, int maxDocID, double avg) {
		this.minNum = minNum;
		this.maxNum = maxNum;
		this.minDocID = minDocID;
		this.maxDocID = maxDocID;
		this.avg = avg;
	}
	
	void HCIMap() {
		
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
		
		LinkedList<Integer> tempList = new LinkedList<>();
		String beforeSearchWord = in.nextLine();
		String searchWord = tempProgram.toLowerCase(beforeSearchWord);
		temp = new LinkedList<>();
		
		if(posts.containsKey(searchWord)) {
			System.out.println("The Searching Result is...");

			tempList = posts.get(searchWord);

			System.out.print(tempList.size() + " found, " + beforeSearchWord + " : ");
			for(int i=0; i<tempList.size(); i++) {
				int curNum = tempList.get(i);
				System.out.print(curNum + " ");
				temp.add(curNum);
				if(i!=0 && i%30 == 0) System.out.println();
			}
			System.out.println();

			while(true) {
				menu(3);
				int choice = in.nextInt(); in.nextLine();
				if(choice == -1)
					break;
				
				boolean find = false;
				if(choice < docs.size() && choice >= 0) {
					for(int i=0; i<temp.size(); i++) {
						if(choice == temp.get(i)) {
							find = true;
							break;
						}
					}
					if(find) showDoc(choice);
					else System.out.println("It don't have '" + beforeSearchWord + "'!!");
				}
				
				else {
					System.out.println("Wrong docID...");
				}
			}
		}

		else { System.out.println("None"); }

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
	
	private void showDoc(int docID) {
		System.out.println("--------------------");
		System.out.println(docs.get(docID).pageName);
		System.out.println(docs.get(docID).getContents());
		System.out.println("--------------------");
	}

	private void showAllDocs() {
		System.out.println("--------------------");
		for(int i=0; i<docs.size(); i++) {
			System.out.println(docs.get(i).pageName);
			System.out.println(docs.get(i).getContents());
		}
		System.out.println("--------------------");
	}
}
