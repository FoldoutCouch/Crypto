package KeyWordTranspose;

import java.util.SortedMap;
import java.util.TreeMap;

public class FirstCodeword {
	
	private static SortedMap<String, Integer> digraphMap = new TreeMap<String, Integer>();
	private static SortedMap<String, Integer> trigraphMap = new TreeMap<String, Integer>();

	/**
	 * Runs this class. Tries to help in finding the first key word.
	 * 
	 * @param cipher
	 */
	public static void parseLettersAndPrint(String cipher) {
		
		int[] lettersArray = parseLetterFreq(cipher);
		
		printLetters(lettersArray, 0);

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
	static void printLetters(int[] lettersArray, int offset) {

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
	 */
	private static void parseDigraphs(String cipher) {
		// TODO Auto-generated method stub

	}

}
