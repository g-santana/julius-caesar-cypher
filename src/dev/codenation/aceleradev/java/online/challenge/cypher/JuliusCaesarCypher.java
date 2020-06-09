package dev.codenation.aceleradev.java.online.challenge.cypher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		//InputOutputUtilities.postAnswer();
	}
	
	private static String decrypt(String phrase, int shiftBack) {
		List<Integer> convertedChars = convertToASCII(phrase);
		List<Integer> encryptedASCII = new ArrayList<>();
		convertedChars.forEach(c -> {
			if (ASCII_LOWER_A <= c && c <= ASCII_LOWER_Z) {
				if(c - shiftBack < ASCII_LOWER_A) {
					encryptedASCII.add(c - shiftBack + ALPHABET_SIZE);
				} else {
					encryptedASCII.add(c - shiftBack);
				}
			} else {
				encryptedASCII.add(c);
			}
		});
		return convertToString(encryptedASCII);
	}
	
	private static List<Integer> convertToASCII(String phrase) {
		char[] separated = phrase.toLowerCase().toCharArray();
		List<Integer> converted = new ArrayList<>();
		for (char c : separated) {
			converted.add((int) c);
		}
		return converted;
	}
	
	private static String convertToString(List<Integer> asciiCodes) {
		char[] letters = new char[asciiCodes.size()];
		for(int i = 0; i < letters.length; i++) {
			letters[i] = (char) (int) asciiCodes.get(i);
		}
		return new String(letters);
	}
}
