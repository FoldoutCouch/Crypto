package KeyWordTranspose;

public class Decrypt {
	
	
	private static String cipher_ex1 = "FKYVYSTDDBYQYLPUHFVGFTPUHGFFKYJPLENFYVJYUFYVUYIFSYYAFPEVPFYHFFKYVYHFVTJFTPUHPUYUVPDDLYUFHTUJPLENFYVHJTYUJYJPNVHYHTUFKYRGDDFKYJPLENFYVJYUFYVSTDDBYJDPHYQQNVTUCFKYEVPFYHFHGUQJNVVYUFJPLENFYVHJTYUJYHFNQYUFHFPDQFPVNUFKYTVEVPCVGLHGFJPLENFYVHGFPFKYVDPUCTHDGUQHJKPPDH";
	private static String cipher_post = "FJYHPKKYRHYKYRFHYVYKPRQYISFIFPRNAVPPUDQCCAYJYCOQRFJYRYDTQYCOJPMIYFJQINYSVTPVFJYTQVIFFQKYQRFJYESIFIFMOYRFIJSWYPTFYRKQIAYJQWYOQRSVPDONRPQINTSIJQPRPMFIQOYQFQIJPEYOFJSFFJYQROPPVIYFFQRBDQCCAVQRBPMFFJYAYIFQRFJQINYSVIBVSOMSFQRBHCSIIFJYVYDQCCAYRPOYCQWYVQYIPTOPKQRPIEQXXSSCCPDYOQRIQOY";
	
	// second keyword length === 4
	private static String cipher_final = "QCKJWCCXZCIBKKWBNJIKEILDTNSXTXOKEFOKTZLNACOOEBMJIRFASFASSFSSYHLTJVASYWKYYBFFWOVJQEIYOFFLEVPSAFNBNJIBNOLDGFXYIOCDOKLXNGASSFSJYCWTTJILEZIHEFXYCNOPIZOYQFIPSFXYKJYFKIWBTZAKNWQVKBUPELCBFCOAWWSVDCSYGNCTZCSRFTTKQNQJATLVEBGBFCKGEILVNLLSXFXPQLFXTVOSJEEJALICNJOKTKNYATQDEZ";
	//	With second keyword IRAN:		 "IKKVOKCKRKINCSWNFRIWVQLPLVSKLGOWVNOWLILARKOBVJMVAAFMKNAFKNSFQPLGBEAFQFKLQJFROWVVIMILGNFXVEPFRNNNFRINFWLPXNXLAWCPGSLKFOAFKNSVQKWGLRIXVIITVNXLTVOCAIOLINICKNXLCRYRCQWNLIAWFFQICJUCVTCNWKOMOFSIUKSLXVCGRKSEWCTWIVQVRCLIVJGNWKKSVQLIFTLFPNXCITFKLEOFBMEVRTIOFROWLSNLRCQPVI"
	// The collapsed code alphabet histogram.
	//	9	A: |||||||||
	//	3	B: |||
	//	15	C: |||||||||||||||
	//	0	D: 
	//	5	E: |||||
	//	21	F: |||||||||||||||||||||
	//	7	G: |||||||
	//	0	H: 
	//	21	I: |||||||||||||||||||||
	//	0	J: 
	//	15	K: |||||||||||||||
	//	32	L: ||||||||||||||||||||||||||||||||
	//	1	M: |
	//	6	N: ||||||
	//	29	O: |||||||||||||||||||||||||||||
	//	4	P: ||||
	//	12	Q: ||||||||||||
	//	5	R: |||||
	//	18	S: ||||||||||||||||||
	//	5	T: |||||
	//	7	U: |||||||
	//	2	V: ||
	//	23	W: |||||||||||||||||||||||
	//	16	X: ||||||||||||||||
	//	5	Y: |||||
	//	0	Z: 

	public static void main(String[] args) {

		//FirstCodeword.parse(cipher_ex1);
		//FirstCodeword.parse(cipher_post);
		//FirstCodeword.parse(cipher_final);
		
		for(int i=4; i< 7; ++i){
			System.out.println("Second keyword length: "+i+", I.C. : "+ SecondCodeword.calculatePrintIOC(cipher_final, i) );
		}
		
		//SecondCodeword.printAlphabetFreqs(cipher_final, 4);
		int[] bestOffsets = SecondCodeword.alignAlphabets(cipher_final, 4, true);
		//SecondCodeword.printAlignedAlphabets(cipher_final, 4, bestOffsets);
		
		// IRAN is my second keyword for cipher_final, offset for I, 8
		// these new offsets are used to translate the original cipher into
		// the single keyword-transpose cipher.
		for(int i = 0; i < bestOffsets.length; ++i){
			bestOffsets[i] = (bestOffsets[i] + 8)%26; 
		}
		SecondCodeword.printAlignedAlphabets(cipher_final, 4, bestOffsets);
		
		int[][] alphaFreqs = SecondCodeword.createHistogram(cipher_final, 4);
		int[] codeHistogram = SecondCodeword.collapseHistograms(alphaFreqs, 4, bestOffsets);
		FirstCodeword.printHistogram(codeHistogram, 0);
		
		String firstCipher = SecondCodeword.translateToSingleKeyTranspose(cipher_final, 4, bestOffsets);
		System.out.println(firstCipher);
		
		FirstCodeword firstSolver = new FirstCodeword(firstCipher);
	}

}
