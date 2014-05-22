package KeyWordTranspose;

public class Decrypt {
	
	
	private static String cipher_ex1 = "FKYVYSTDDBYQYLPUHFVGFTPUHGFFKYJPLENFYVJYUFYVUYIFSYYAFPEVPFYHFFKYVYHFVTJFTPUHPUYUVPDDLYUFHTUJPLENFYVHJTYUJYJPNVHYHTUFKYRGDDFKYJPLENFYVJYUFYVSTDDBYJDPHYQQNVTUCFKYEVPFYHFHGUQJNVVYUFJPLENFYVHJTYUJYHFNQYUFHFPDQFPVNUFKYTVEVPCVGLHGFJPLENFYVHGFPFKYVDPUCTHDGUQHJKPPDH";
	private static String cipher_post = "FJYHPKKYRHYKYRFHYVYKPRQYISFIFPRNAVPPUDQCCAYJYCOQRFJYRYDTQYCOJPMIYFJQINYSVTPVFJYTQVIFFQKYQRFJYESIFIFMOYRFIJSWYPTFYRKQIAYJQWYOQRSVPDONRPQINTSIJQPRPMFIQOYQFQIJPEYOFJSFFJYQROPPVIYFFQRBDQCCAVQRBPMFFJYAYIFQRFJQINYSVIBVSOMSFQRBHCSIIFJYVYDQCCAYRPOYCQWYVQYIPTOPKQRPIEQXXSSCCPDYOQRIQOY";
	
	// second keyword length === 4
	private static String cipher_final = "QCKJWCCXZCIBKKWBNJIKEILDTNSXTXOKEFOKTZLNACOOEBMJIRFASFASSFSSYHLTJVASYWKYYBFFWOVJQEIYOFFLEVPSAFNBNJIBNOLDGFXYIOCDOKLXNGASSFSJYCWTTJILEZIHEFXYCNOPIZOYQFIPSFXYKJYFKIWBTZAKNWQVKBUPELCBFCOAWWSVDCSYGNCTZCSRFTTKQNQJATLVEBGBFCKGEILVNLLSXFXPQLFXTVOSJEEJALICNJOKTKNYATQDEZ";
	//	With second keyword IRAN:		 "ILKWOLCKRLIOCTWOFSIXWRLQLWSKLGOXWOOXLILASLOBWKMWAAFNKOAFKOSFQQLGBEAFQFKLQKFSOXVWINILGOFYWEPFSONOFSIOFXLQYOXLAXCQGTLKFPAFKOSWQLWGLSIYWIIUWOXLUWOCAIOLIOICKOXLCSYSCRWOLIAXFFQICKUCWUCOXLONOFSIVLSLYWCGRLSEXCTXIWQWSCLIWKGOXLKTWRLIFULFPOXCIUFKLEOFBNEWSUIPFSOXLTNLSCQQWI"
	//									  LINZAIIHXISBNNABUZSNYSEQGURHGHTNYOTNGXEUCITTYBKZSVOCROCRRORRPJEGZWCRPANPPBOOATWZLYSPTOOEYWFRCOUBUZSBUTEQDOHPSTIQTNEHUDCRRORZPIAGGZSEYXSJYOHPIUTFSXTPLOSFROHPNZPONSABGXCNUALWNBMFYEIBOITCAARWQIRPDUIGXIRVOGGNLULZCGEWYBDBOINDYSEWUEERHOHFLEOHGWTRZYYZCESIUZTNGNUPCGLQYX
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
		System.out.println("Fully Translated:\n");
		String translation_final = "CBIQYODJSZNEKUTFLVRGMWAHPX";
		System.out.println( FirstCodeword.replaceCharWith(firstCipher, translation_final) );
	}

}
