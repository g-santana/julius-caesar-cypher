package dev.codenation.aceleradev.java.online.challenge.cypher;

import java.util.ArrayList;
import java.util.List;

public class JuliusCaesarCypher {
	
	private JuliusCaesarCypher() {}
	
	private static final int ASCII_LOWER_A = 97;
	private static final int ASCII_LOWER_Z = 122;
	private static final int ALPHABET_SIZE = 26;
	
	public static void run() {
		decrypt("ilmvyl fvb thyyf h wlyzvu fvb zovbsk mpyza thrl aolt bzl h jvtwbaly dpao zsvd pualyula av zll dov aolf ylhssf hyl. dpss mlyylssa", 7);
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
