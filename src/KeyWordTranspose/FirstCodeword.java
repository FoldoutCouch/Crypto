package KeyWordTranspose;

import java.util.SortedMap;
import java.util.TreeMap;

public class FirstCodeword {
	
	private int[] histogram;
	private SortedMap<String, Integer> digraphMap;
	private SortedMap<String, Integer> trigraphMap;
	
	public FirstCodeword(String cipher){
		histogram = parseLetterFreq(cipher);
		digraphMap = parseDigraphs(cipher);
		trigraphMap = parseTrigraphs(cipher);
	}
	
	/**
	 * Runs this class. Tries to help in finding the first key word.
	 * 
	 * @param cipher
	 */
	public static void parseLettersAndPrint(String cipher) {
		
		int[] lettersArray = parseLetterFreq(cipher);
		
		printHistogram(lettersArray, 0);

	}

	/**
	 * Bad behavior if cipher contains any characters except 'A' to
	 * 'Z' inclusive.
	 * 
	 * @param cipher
	 *            The cipher text, can only contain capital letters.
	 * 
	 */
	public static int[] parseLetterFreq(String cipher) {

		System.out.println("Parsing Letters...");

		// reset letters
		int[] charFreq = new int[26];
		for (int i = 0; i < 26; ++i) {
			charFreq[i] = 0;
		}

		// populate the letterMap //
		for (int i = 0; i < cipher.length(); ++i) {
			char curr = cipher.charAt(i);
			charFreq[curr - 'A']++;
		}

		System.out.println("Done parsing Letters!");
		return charFreq;
	}

	/**
	 * Print the letters in "tick" format.
	 */
	static void printHistogram(int[] lettersArray, int offset) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 26; ++i) {
			sb.append(lettersArray[(i+offset)%26]+"\t");
			sb.append((char) ((i+offset)%26 + 'A') + ": ");
			for (int x = 0; x < lettersArray[(i+offset)%26]; ++x) {
				sb.append("|");
			}
			sb.append("\n");
		}
		System.out.print(sb);
	}

	/**
	 * 
	 * @param cipher
	 * @return 
	 */
	private SortedMap<String, Integer> parseDigraphs(String cipher) {
		return digraphMap;
		
	}
	
	/**
	 * 
	 * @param cipher
	 * @return
	 */
	private SortedMap<String, Integer> parseTrigraphs(String cipher) {
		return digraphMap;

	}

}
