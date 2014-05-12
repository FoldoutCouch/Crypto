package KeyWordTranspose;

public class SecondCodeword {

	/**
	 * Creates the histograms for each alphabet and calculates the average index
	 * of coincidence.
	 * 
	 * @param cipher
	 *            The cipher text. Bad behavior if cipher contains any
	 *            characters except 'A' to 'Z' inclusive.
	 * @param numAlphabets
	 *            The length of the second keyword is also the number of
	 *            alphabets that need to be collapsed.
	 */
	public static double calculatePrintIOC(String cipher, int numAlphabets) {

		int[][] alphaFreqs = createHistogram(cipher, numAlphabets);
		int[] offsets = new int[numAlphabets];
		return calculateIOC(cipher.length(), numAlphabets, alphaFreqs);

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

	private static double calculateIOC(int cipherLength, int numAlphabets,
			int[][] alphaFreqs) {
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

		System.out.println("Aligning Alphabets...");
		int[][] alphaFreqs = createHistogram(cipher, numAlphabets);

		int N = cipher.length() / numAlphabets;

		int[] bestOffsets = new int[numAlphabets];
		int[] rotateOffsets = new int[numAlphabets];
		double maxIOC = 0.0;

		// only works for 4 alphabets
		for (int x = 0; x < numAlphabets; ++x) {
			for (int offset1 = 0; offset1 < 26; offset1++) {
				rotateOffsets[1] = offset1;
				for (int offset2 = 0; offset2 < 26; offset2++) {
					rotateOffsets[2] = offset2;
					for (int offset3 = 0; offset3 < 26; offset3++) {
						// set the current rotation offset
						rotateOffsets[3] = offset3;
						double ioc = rotate_collapse_calcIOC(cipher.length(),
								alphaFreqs, numAlphabets, rotateOffsets);
						if (ioc > maxIOC) {
							// if this ioc is bigger then save the ioc and
							// offsets
							maxIOC = ioc;
							for (int numOffsets = 0; numOffsets < numAlphabets; ++numOffsets) {
								bestOffsets[numOffsets] = rotateOffsets[numOffsets];
							}
						}
					}
				}
			}
		}

		/*
		 * // x is the alphabet to rotate for (int x = 0; x < numAlphabets; ++x)
		 * { for (int j = numAlphabets - 1; j > x; --j) { for (int offset = 0;
		 * offset < 26; offset++) { // set the current rotation offset
		 * rotateOffsets[j] = offset; double ioc =
		 * rotate_collapse_calcIOC(cipher.length(), alphaFreqs, numAlphabets,
		 * rotateOffsets); if (ioc > maxIOC) { // if this ioc is bigger then
		 * save the ioc and offsets maxIOC = ioc; for (int numOffsets = 0;
		 * numOffsets < numAlphabets; ++numOffsets) { bestOffsets[numOffsets] =
		 * rotateOffsets[numOffsets]; } }
		 * 
		 * } } }
		 */
		System.out.println("Best IOC: " + maxIOC);
		System.out.print("Best Offsets: ");
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {
			System.out.print(bestOffsets[alpha] + " ,");
		}
		System.out.println("");

		int[] collapsedFreqs = new int[26];
		for (int alpha = 0; alpha < numAlphabets; ++alpha) {
			for (int curr = 0; curr < 26; curr++) {
				collapsedFreqs[curr] += alphaFreqs[alpha][curr];
			}
		}

		System.out.println("Done aligning Alphabets...");

		for (int alpha = 0; alpha < numAlphabets; ++alpha) {
			System.out.println("Printing Aligned Histogram #" + alpha);
			FirstCodeword.printLetters(alphaFreqs[alpha], bestOffsets[alpha]);
		}
		return bestOffsets;
	}

	private static double rotate_collapse_calcIOC(int cipherLength,
			int[][] alphaFreqs, int numAlphabets, int[] rotateOffsets) {

		// we collapse the alphabets so N is just the length of the cipher.
		int N = cipherLength;

		// now calculate index of coincidence of each alphabet
		int[] collapsedFreqs = new int[26];
		int summationIOC = 0;
		// sub total the letter frequencies from A to Z
		for (int curr = 0; curr < 26; ++curr) {
			for (int alpha = 0; alpha < numAlphabets; ++alpha) {
				// sub total each letter from each alphabet
				collapsedFreqs[curr] += alphaFreqs[alpha][(curr + rotateOffsets[alpha]) % 26];
			}
			summationIOC += collapsedFreqs[curr] * (collapsedFreqs[curr] - 1);
		}

		// now compute the IOC
		double collapsedIOC = (double) summationIOC / (double) (N * (N - 1));
		return collapsedIOC;
	}

	/**
	 * Prints the alphabets aligned.
	 * 
	 * @param cipher
	 * @param numAlphabets
	 * @param bestOffsets
	 */
	public static void printAlignedAlphabets(String cipher, int numAlphabets,
			int[] offsets) {
		System.out.println("Printing Aligning Alphabets...");
		int[][] alphaFreqs = createHistogram(cipher, numAlphabets);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 26; ++i) {
			for(int alpha = numAlphabets-1; alpha > -1; --alpha){
				sb.append((char) ((i+offsets[alpha])%26 + 'A'));
			}
			sb.append("\n");
		}
		
		System.out.print(sb);
		
	}
}
