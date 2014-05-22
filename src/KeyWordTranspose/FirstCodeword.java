package KeyWordTranspose;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Cipher;

public class FirstCodeword {

	// this cipher is only encoded by one keyword
	private String cipher;

	// the frequency of the letters
	private int[] histogram;
	// frequency of pairs
	private SortedMap<String, Integer> digraphMap;
	private SortedMap<String, Integer> trigraphMap;
	// THE_p is read as (THE)p, as in the plain version of THE in cipher text
	private String THE_p;
	private String AIO_p;
	private String N_p;
	
	private int[] translation = new int[26];

	public FirstCodeword(String cipher) {
		this.cipher = cipher;
		histogram = parseLetterFreq(cipher);
		digraphMap = new TreeMap<String, Integer>();
		populateDigraphs();
		trigraphMap = new TreeMap<String, Integer>();
		populateTrigraphs();

		printDigraphs();
		printTrigraphs();
		System.out.println("Looking for words: ");

		find_THE();
		System.out.println("THE is encoded as: " + THE_p);
		System.out.println("E is encoded as: " + THE_p.charAt(2));
		// L is definitely a vowel
		// potential vowels:
		// W I F S
		SortedMap<String, Integer> vowels = new TreeMap<String, Integer>();
		vowels.put(""+THE_p.charAt(2), histogram[THE_p.charAt(2)-'A']);
		vowels.put("W", histogram['W'-'A']);
		vowels.put("C", histogram['C'-'A']);
		vowels.put("F", histogram['F'-'A']);
		vowels.put("N", histogram['N'-'A']);
		// We know that consonants TH: OX
		SortedMap<String, Integer> consonants = new TreeMap<String, Integer>();
		consonants.put(""+THE_p.charAt(0), histogram[THE_p.charAt(0)-'A']);
		consonants.put(""+THE_p.charAt(1), histogram[THE_p.charAt(1)-'A']);
		find_AIO(vowels, consonants);
		
		
		find_N(vowels);
		SortedMap<String, Integer> Ns = new TreeMap<String, Integer>();
		Ns.put("K", histogram['K'-'A']);
		Ns.put("Q", histogram['Q'-'A']);
		Ns.put("S", histogram['S'-'A']);
		System.out.println("Now try messing around.");
		
		vowels.put("A", histogram['A'-'A']);
		checkIfN(Ns, vowels);
		
		translate();
	}

	private void translate() {
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
	 * Bad behavior if cipher contains any characters except 'A' to 'Z'
	 * inclusive.
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
	 * 
	 * @return
	 */
	private void populateDigraphs() {

		int N = cipher.length();
		// the first
		String digraph = "." + cipher.charAt(0);
		digraphMap.put(digraph, 1);
		// the last
		digraph = cipher.charAt(N - 1) + ".";
		digraphMap.put(digraph, 1);

		// and everything in between
		for (int i = 0; i < N - 1; ++i) {
			digraph = cipher.substring(i, i + 2);
			Integer value = digraphMap.remove(digraph);
			if (null == value) {
				digraphMap.put(digraph, 1);
			} else {
				digraphMap.put(digraph, 1 + value.intValue());
			}
		}
	}

	/**
	 * 
	 * @param cipher
	 * @return
	 */
	private void populateTrigraphs() {
		String trigraph;
		for (int i = 0, len = cipher.length() - 2; i < len; ++i) {
			trigraph = cipher.substring(i, i + 3);
			Integer value = trigraphMap.remove(trigraph);
			if (null == value) {
				trigraphMap.put(trigraph, 1);
			} else {
				trigraphMap.put(trigraph, 1 + value.intValue());
			}
		}
	}

	/**
	 * Print the letters in "tick" format.
	 */
	static void printHistogram(int[] lettersArray, int offset) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 26; ++i) {
			sb.append(lettersArray[(i + offset) % 26] + "\t");
			sb.append((char) ((i + offset) % 26 + 'A') + ": ");
			for (int x = 0; x < lettersArray[(i + offset) % 26]; ++x) {
				sb.append("|");
			}
			sb.append("\n");
		}
		System.out.print(sb);
	}

	void printDigraphs() {
		digraphMap.forEach((String di, Integer val) -> (System.out.println(di
				+ ": " + val.intValue())));
	}

	void printTrigraphs() {
		trigraphMap.forEach((String di, Integer val) -> (System.out.println(di
				+ ": " + val.intValue())));
	}

	private void find_THE() {
		THE_p = trigraphMap.firstKey();
		int freq = trigraphMap.get(THE_p);

		for (Map.Entry<String, Integer> trigraph : trigraphMap.entrySet()) {
			if (trigraph.getValue() > freq) {
				THE_p = trigraph.getKey();
				freq = trigraph.getValue();
			}
		}
	}

	private void find_AIO(SortedMap<String, Integer> vowels, SortedMap<String, Integer> consonants) {
		// potential vowels:
		// W I F S
		
		for(Map.Entry<String, Integer> vowelEntry : vowels.entrySet()){
			int byVowels = 0;
			int byCons = 0;
			int byOthers = 0;
			char vowel = vowelEntry.getKey().charAt(0);
			for(int i = cipher.indexOf(vowel); i != -1; i = cipher.indexOf(vowel, i+1) ){
				char before = 0;
				char after = 0;
				try{
					before = cipher.charAt(i-1);
					after = cipher.charAt(i+1);
					if(vowels.containsKey(""+before)){
						byVowels++;
					}else if(consonants.containsKey(""+before)){
						byCons++;
					}
					else{
						byOthers++;
					}
					
					if(vowels.containsKey(""+after)){
						byVowels++;
					} else if(consonants.containsKey(""+after)){
						byCons++;
					} else{
						byOthers++;
					}
				}
				catch(IndexOutOfBoundsException iobe){
					continue;
				}
			}
			System.out.printf("Potential Vowel: %c %d occurences:\n", vowel, histogram[vowel-'A']);
			System.out.printf("\t\t\t\t%d by vowels, %d by consonants, %d by others.\n", byVowels, byCons, byOthers);
		}
		
	}
	
	/**
	 * N in plain text.
	 * 
	 * @param vowels
	 * @param consonants
	 */
	private void find_N(SortedMap<String, Integer> vowels) {
		// for each letter, the stupid way!
		for(char currchar = 'A'; currchar <= 'Z'; ++currchar){
			int vowelBefore = 0;
			// start at looking from the first index
			for(int i = cipher.indexOf(currchar,1); i != -1; i = cipher.indexOf(currchar, i+1) ){
				// look at the letter before it
				char before = cipher.charAt(i-1);
				// see if it's a vowel
				if(vowels.containsKey(""+before)){
					vowelBefore++;
				}
			}
			System.out.printf("%c %2d : vowel before %4.0f%%\n", currchar, histogram[currchar-'A'], ((double)vowelBefore/histogram[currchar-'A'])*100);
		}
	}
	

	private void checkIfN(SortedMap<String, Integer> Ns, SortedMap<String, Integer> vowels) {
		// for each letter, the stupid way!
		for(Map.Entry<String, Integer> entry : Ns.entrySet()){
			char currchar = entry.getKey().charAt(0);
			
			int vowelBefore = 0;
			// start at looking from the first index
			for(int i = cipher.indexOf(currchar,1); i != -1; i = cipher.indexOf(currchar, i+1) ){
				// look at the letter before it
				char before = cipher.charAt(i-1);
				// see if it's a vowel
				if(vowels.containsKey(""+before)){
					vowelBefore++;
				}
			}
			System.out.printf("%c %2d : vowel before %4.0f%%\n", currchar, histogram[currchar-'A'], ((double)vowelBefore/histogram[currchar-'A'])*100);
		}
	}

	static String replaceCharWith(String cipher, String replacement){
		StringBuilder sb = new StringBuilder(cipher.length());
		for(char c : cipher.toCharArray()){
			sb.append( replacement.charAt( c - 'A' ) );
		}
		return sb.toString();
	}
}
