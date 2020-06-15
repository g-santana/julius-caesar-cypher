package dev.codenation.aceleradev.java.online.challenge.cypher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import dev.codenation.aceleradev.java.online.challenge.io.utils.InputOutputUtilities;

public class JuliusCaesarCypher {
	
	private JuliusCaesarCypher() {}
	
	private static final int ASCII_LOWER_A = 97;
	private static final int ASCII_LOWER_Z = 122;
	private static final int ALPHABET_SIZE = 26;
	
	public static void run() throws IOException, ParseException {
		JSONObject challengeData = InputOutputUtilities.requestData();
		String decrypted = decrypt((String) challengeData.get("cifrado"), (int) (long) challengeData.get("numero_casas"));
		String sha1Resume = DigestUtils.sha1Hex(decrypted);
		InputOutputUtilities.writeResultToFile(challengeData, decrypted, sha1Resume);
		InputOutputUtilities.postAnswer();
	}
	
	private static String decrypt(String phrase, int shiftBack) {
		char[] separated = phrase.toLowerCase().toCharArray();
		List<Character> ascii = new ArrayList<>();
		
		for (char c : separated) {
			int num = (int) c;
			
			if (ASCII_LOWER_A <= num && num <= ASCII_LOWER_Z) {
				if(num - shiftBack < ASCII_LOWER_A) {
					ascii.add((char) (num - shiftBack + ALPHABET_SIZE));
				} else {
					ascii.add((char) (num - shiftBack));
				}
			} else {
				ascii.add((char) (num));
			}
		}
		
		return ascii.stream().map(String::valueOf).collect(Collectors.joining());
	}
}
