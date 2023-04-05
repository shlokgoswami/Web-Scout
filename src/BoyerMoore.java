import java.lang.Math;
import java.util.HashMap;

public class BoyerMoore {
    private static HashMap<Character, Integer> badchar = new HashMap<Character, Integer>(); 

	private static void badCharHeuristic( char []str, int size) { 
		for (int i = 0; i < size; i++) 
			badchar.put(str[i], i);  // Put the each character and its index into the hash map
  	} 


	public static int searchCount( String data,  String pattern) {
		return searchCount(data.toCharArray(), pattern.toCharArray());

	}
	public static int searchCount( char txt[],  char pat[]) { 
		int pattern_length = pat.length;
		int text_length = txt.length;


		badCharHeuristic(pat, pattern_length);

		int s = 0; 
		int counter=0;   // This varible is for kepping the track of occurence of the word
		while(s <= (text_length - pattern_length)) // 
		{
			int j = pattern_length-1;

			while(j >= 0 && pat[j] == txt[s+j])
				j--;

			if (j < 0) {
				counter++;
				s += (s+pattern_length < text_length)? pattern_length-badchar.getOrDefault(txt[s+pattern_length], -1) : 1;
			} else
				s += Math.max(1, j - badchar.getOrDefault(txt[s+j], -1));
		}
		return counter;
	} 
}