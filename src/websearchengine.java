import java.util.Scanner;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.io.File;

public class websearchengine {
	// Used Hash table to store the count of the words in the URLS
	private static Hashtable<String, Integer> FrequencyList = new Hashtable<String, Integer>();
    public static final String CHARSET_NAME = "UTF-8"; // otherwise BoyerMoore search wont work
    public static final String HTML_PATH = "Files"+ File.separator+ "htmlFiles"; 
    public static final String TEXT_PATH = "Files"+ File.separator+ "TextFiles"; 
	public static boolean wordFound(File file, String word) {
		try (Scanner sc = new Scanner(file,CHARSET_NAME );){
			sc.useDelimiter("\\Z");
			if (sc.hasNext()) {
				String[] ar = sc.next().toLowerCase().split("::"); // "::" is used as a delimiter
				if(ar.length > 1) {
					String url = ar[0];
					int wordFreq = BoyerMoore.searchCount(ar[1], word);
					if(wordFreq > 0) {
						System.out.printf("[%d] url:-%s count:-%d\n",  FrequencyList.size(), url, wordFreq);
						FrequencyList.put(url, wordFreq);
						return true;
						
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void reset() {
		File file = new File(HTML_PATH.toString());
		System.out.println("HTML File:" + file.getAbsolutePath());
		file.mkdirs();
		for(File fl:file.listFiles())
			fl.delete();
		file = new File(TEXT_PATH.toString());
		file.mkdirs();
		for(File fl:file.listFiles())
			fl.delete();
	}
	public static void main(String[] args) {
		reset();
		System.out.printf("\nSearch Engine\n");
		try(Scanner sc= new Scanner(System.in);){
			while(true) {
				System.out.println("\nEnter a valid URL\n");
				String url = sc.next();
				if(!url.contains("http"))
					url = "https://" + url;
				System.out.println("\nStarting Crawler\n");
				boolean isCrawled = Crawler.start(url, 0);
				if(isCrawled)
					break;
				System.err.println("\nInvalid URL. Please try again!\n");
				
			}
			while(true) {
				System.out.println("\nEnter a word you would like to search or enter Quit to exit");
				String searchword = sc.next().toLowerCase();
				if(searchword.equals("quit")) {
					System.out.println("\nEnd of the program\n");
					break;
				}
				FrequencyList.clear();
				int totalcount = 0;
				for(File f: new File(TEXT_PATH).listFiles()) {
					if (wordFound(f, searchword))
						totalcount++;
				
				}
				System.out.printf("Word: \"%s\" found in %d files\n", searchword, totalcount);
				if(totalcount == 0) {
					if(WordSearch.autoSuggested(searchword))
						System.out.println("\nEntered word cannot be found\n");
					
				}else {
					WordSearch.rank(FrequencyList);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}