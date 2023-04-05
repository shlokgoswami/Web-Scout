import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WordSearch{
    private static String regex = "[a-z0-9]+";
    public static final Integer NumberOfSearch = 5;
    public static final String TEXT_PATH = "Files"+ File.separator+ "TextFiles"; 
    public static final Integer AltWordDistance = 3;  // edit distance alogrithm
	private static Pattern pattern = Pattern.compile(regex);
	private static Matcher matcher = pattern.matcher("");
	
	private static HashMap<String, Integer> nums = new HashMap<String, Integer>();
	
	// finds strings with similar pattern and calls edit distance() on those strings
	public static void find(File sourceFile, String str) {
		try(
				FileReader fr = new FileReader(sourceFile);
				BufferedReader obj = new BufferedReader(fr);)
		{
			for (String line = obj.readLine(); line !=null; line = obj.readLine()) {
				matcher.reset(line.toLowerCase());
				while (matcher.find()){
					String c = matcher.group().toLowerCase();
					nums.put(c, editDist(str, c));}
			}
		}
		catch(Exception e) {
			System.out.println("Exception:" + e);
		}
	}
	// Merge-sort for page sorting
	public static void rank(Hashtable<String, Integer>files) {
		// Sort after transferring as list
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(files.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
			public int compare(Map.Entry<String, Integer> obj, Map.Entry<String, Integer> obj1) {
				return obj.getValue().compareTo(obj1.getValue());
			}
		}
		);
		Collections.reverse(list);
		System.out.println("Top 5 results\n");
		for(int j = 1; list.size()> j && j<NumberOfSearch; j++) {
			if(list.get(j).getKey()!= null)
				System.out.printf("[%d] %s\n", j, list.get(j).getKey());
		}
	}
	public static boolean autoSuggested (String wordToSearch) {
		for (File file: new File(TEXT_PATH).listFiles())
			find(file, wordToSearch);
		int i=0;
		for(Map.Entry entry : nums.entrySet()){
			if(AltWordDistance > (Integer)entry.getValue()) {
				i++;
				if (i==1)
					System.out.println("\nDid you mean?\n ");
				else if(i >= NumberOfSearch)
					break;
				System.out.printf("[%d] %s\n", i, entry.getKey());
			}}
		return i!=0;
	}
	public static int editDist(String str, String str1) {
		int[][] arr = new int[str.length()+1][str1.length()+1];
		
		for (int i = 0; i<=str.length(); i++) {
			arr[i][0] = i;
		}
		for (int j = 0; j<=str1.length(); j++) {
			arr[0][j] = j;
		}
	    //checking last character
		for (int i = 0; i<str.length(); i++) {
			char c = str.charAt(i);
			for (int j = 0; j<str1.length(); j++) {
				char c1 = str1.charAt(j);
				
				
				if (c==c1) {
					arr[i+1][j+1] = arr[i][j];
				}else {
					int replace = arr[i][j] + 1;
					int insert = arr[i][j+1]+1;
					int delete = arr[i+1][j]+1;
					int min = replace>insert?insert:replace;
					min = delete > min ? min : delete;
					arr[i+1][j+1] = min;
				}
			}
		}
		return arr[str.length()][str1.length()];
	}}