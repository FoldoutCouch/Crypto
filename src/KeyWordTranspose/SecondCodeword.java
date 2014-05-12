package KeyWordTranspose;

public class SecondCodeword {

	/**
	 * Create the histograms and calculate index of coincidence.
	 * 
	 * @param cipher
	 *            The cipher text. Bad behavior if cipher contains any
	 *            characters except 'A' to 'Z' inclusive.
	 * @param numAlphabets
	 *            The length of the second keyword is also the number of
	 *            alphabets that need to be collapsed.
	 */
	public static double calculatePrintIndexOfCoinc(String cipher,
			int numAlphabets) {

		int[][] alphaFreqs = createHistogram(cipher, numAlphabets);
		int[] offsets = new int[numAlphabets];
		//return calculateIndexOfCoinc(cipher.length(), numAlphabets, alphaFreqs);
		return rotate_collapse_calcIndexOfCoinc(cipher.length(),
				alphaFreqs, numAlphabets, offsets);
	}

	/**
	 * Creates the histograms.
	 * 
	 * @param cipher
	 * @param numAlphabets
	 * @return
	 */
	private static int[][] createHistogram(String cipher, int numAlphabets) {
		int[][] alphaFreqs = new int[numAlphabets][26];

		int N = cipher.length() / numAlphabets;

		// outer loop progress to the next alphabet in the cipher
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {

			// inner loop progesses within the current alphabet
			for (int i = alpha; i < (cipher.length() - 1); i += numAlphabets) {
				char curr = cipher.charAt(i);
				alphaFreqs[alpha][curr - 'A']++;
			}
		}
		return alphaFreqs;
	}

	private static double calculateIndexOfCoinc(int cipherLength,
			int numAlphabets, int[][] alphaFreqs) {
		int N = cipherLength / numAlphabets;

		// now calculate index of coincidence of each alphabet
		double[] indexes = new double[numAlphabets];
		// outer loop progress to the next alphabet in the cipher
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {

			for (int curr = 0; curr < 26; ++curr) {
				// sub total the letter frequencies form A to Z
				indexes[alpha] += (alphaFreqs[alpha][curr])
						* (-1 + alphaFreqs[alpha][curr]);
			}

			indexes[alpha] = indexes[alpha] / (N * (N - 1));
		}

		// now average the indexes
		double avgIndex = 0;
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {
			// System.out.println("Second keyword length: "+keyLength+", Alphabet#"+alpha+", I.C. : "+indexes[alpha]);
			avgIndex += indexes[alpha];
		}
		avgIndex = avgIndex / numAlphabets;
		return avgIndex;
	}

	/**
	 * Print all the results of FirstCode.parseLetterFreq for each alphabet in
	 * the cipher.
	 * 
	 * @param cipher
	 *            The cipher text. Bad behavior if cipher contains any
	 *            characters except 'A' to 'Z' inclusive.
	 * @param numAlphabets
	 *            The length of the second keyword is also the number of
	 *            alphabets that need to be collapsed.
	 */
	public static void printAlphabetFreqs(String cipher, int numAlphabets) {

		// for each alphabet
		for (int alpha = 0; alpha < 4; ++alpha) {

			// build a new string to call FirstCode.parse
			StringBuilder sb = new StringBuilder();

			for (int curr = alpha; curr < (cipher.length() - 1); curr += 4) {
				sb.append(cipher.charAt(curr));
			}
			System.out.println("Printing Alphabet #" + alpha);
			FirstCodeword.parseLettersAndPrint(sb.toString());
		}
	}

	/**
	 * Aligns the numAlphabets alphabets to have the maximum index of
	 * coincidence.
	 * 
	 * @param cipher
	 *            The cipher text. Bad behavior if cipher contains any
	 *            characters except 'A' to 'Z' inclusive.
	 * @param numAlphabets
	 *            The length of the second keyword is also the number of
	 *            alphabets that need to be collapsed. Breaks if numAlphabets is
	 *            less than 2.
	 * @return int[] An int[numAlphabets], int[i] is the rotation amount of the
	 *         ith alphabet relative to the 0th alphabet. int[0] is always 0.
	 *         ex) when 1st: ABCD lines up with 2nd: WXYZ then int[0]=0,
	 *         int[i]=4
	 */
	public static int[] alignAlphabets(String cipher, int numAlphabets) {

		int[][] alphaFreqs = createHistogram(cipher, numAlphabets);

		int N = cipher.length() / numAlphabets;

		int[] bestOffsets = new int[numAlphabets];
		int[] rotateOffsets = new int[numAlphabets];
		double maxIOC = 0.0;

		// start of looping for collapsing the alphabets
		// x is the alphabet to rotate
		for (int x = 1; x < numAlphabets; ++x) {
			for (int j = numAlphabets-1; j > x; --j) {
				for (int offset = 0; offset < 26; offset++) {
					// set the current rotation offset
					rotateOffsets[j] = offset;
					double ioc = rotate_collapse_calcIndexOfCoinc(cipher.length(),
							alphaFreqs, numAlphabets, rotateOffsets);
					if(ioc > maxIOC){
						// if this ioc is bigger then save the ioc and offsets
						maxIOC = ioc;
						for(int numOffsets = 0; numOffsets < numAlphabets; ++numOffsets){
							bestOffsets[numOffsets] = rotateOffsets[numOffsets];
						}
					}

				}
			}
		}
		System.out.println("Best IOC: "+maxIOC);
		System.out.print("Best Offsets: ");
		for (int alpha = 1; alpha < numAlphabets; ++alpha) {
			System.out.print(bestOffsets[alpha] + " ,");
		}
		System.out.println("");
		
		int[] collapsedFreqs = new int[26];
		for (int alpha = 1; alpha < numAlphabets; ++alpha) {
			for (int curr = 0; curr < 26; curr++) {
				collapsedFreqs[curr] += alphaFreqs[alpha][curr];
			}
		}
		return collapsedFreqs;
	}

	private static double rotate_collapse_calcIndexOfCoinc(int cipherLength,
			int[][] alphaFreqs, int numAlphabets, int[] rotateOffsets) {

		int N = cipherLength / numAlphabets;

		// now calculate index of coincidence of each alphabet
		double[] indexes = new double[numAlphabets];
		// outer loop progress to the next alphabet in the cipher
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {

			for (int curr = 0; curr < 26; ++curr) {
				// sub total the letter frequencies form A to Z
				indexes[alpha] += (alphaFreqs[alpha][(curr + rotateOffsets[alpha]) % 26])
						* (-1 + alphaFreqs[alpha][(curr + rotateOffsets[alpha]) % 26]);
			}

			indexes[alpha] = indexes[alpha] / (N * (N - 1));
		}

		// now average the indexes
		double avgIndex = 0;
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {
			// System.out.println("Second keyword length: "+keyLength+", Alphabet#"+alpha+", I.C. : "+indexes[alpha]);
			avgIndex += indexes[alpha];
		}
		avgIndex = avgIndex / numAlphabets;
		return avgIndex;
	}
}
