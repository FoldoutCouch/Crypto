package KeyWordTranspose;

public class Decrypt {
	
	
	private static String cipher_ex1 = "FKYVYSTDDBYQYLPUHFVGFTPUHGFFKYJPLENFYVJYUFYVUYIFSYYAFPEVPFYHFFKYVYHFVTJFTPUHPUYUVPDDLYUFHTUJPLENFYVHJTYUJYJPNVHYHTUFKYRGDDFKYJPLENFYVJYUFYVSTDDBYJDPHYQQNVTUCFKYEVPFYHFHGUQJNVVYUFJPLENFYVHJTYUJYHFNQYUFHFPDQFPVNUFKYTVEVPCVGLHGFJPLENFYVHGFPFKYVDPUCTHDGUQHJKPPDH";
	private static String cipher_post = "FJYHPKKYRHYKYRFHYVYKPRQYISFIFPRNAVPPUDQCCAYJYCOQRFJYRYDTQYCOJPMIYFJQINYSVTPVFJYTQVIFFQKYQRFJYESIFIFMOYRFIJSWYPTFYRKQIAYJQWYOQRSVPDONRPQINTSIJQPRPMFIQOYQFQIJPEYOFJSFFJYQROPPVIYFFQRBDQCCAVQRBPMFFJYAYIFQRFJQINYSVIBVSOMSFQRBHCSIIFJYVYDQCCAYRPOYCQWYVQYIPTOPKQRPIEQXXSSCCPDYOQRIQOY";
	
	// second keyword length === 4
	private static String cipher_final = "QCKJWCCXZCIBKKWBNJIKEILDTNSXTXOKEFOKTZLNACOOEBMJIRFASFASSFSSYHLTJVASYWKYYBFFWOVJQEIYOFFLEVPSAFNBNJIBNOLDGFXYIOCDOKLXNGASSFSJYCWTTJILEZIHEFXYCNOPIZOYQFIPSFXYKJYFKIWBTZAKNWQVKBUPELCBFCOAWWSVDCSYGNCTZCSRFTTKQNQJATLVEBGBFCKGEILVNLLSXFXPQLFXTVOSJEEJALICNJOKTKNYATQDEZ";
	
	public static void main(String[] args) {

		//FirstCode.parse(cipher_ex1);
		//FirstCode.parse(cipher_post);
		//FirstCode.parse(cipher_final);
		for(int i=4; i< 7; ++i){
			System.out.println("Second keyword length: "+i+", I.C. : "+ SecondCodeword.calculatePrintIndexOfCoinc(cipher_final, i) );
		}
		
		//SecondCode.printAlphabetFreqs(cipher_final, 4);
		SecondCodeword.alignAlphabets(cipher_final, 4);
	}

}
