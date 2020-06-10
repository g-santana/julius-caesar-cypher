package dev.codenation.aceleradev.java.online.challenge.io.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.ws.http.HTTPException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InputOutputUtilities {
	
	private InputOutputUtilities() {}
	
	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String GET_URL = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=";
	private static final String POST_URL = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=";
	
	public static JSONObject requestData() throws IOException, ParseException {
		URL url = new URL(GET_URL.concat(readToken()));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(GET);
		conn.connect();
		int responseCode = conn.getResponseCode();
		
		if(responseCode != HttpURLConnection.HTTP_OK) {
			throw new HTTPException(responseCode);
		}
		
		return parseResponse(conn);
	}
	
	private static JSONObject parseResponse(HttpURLConnection conn) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String in;
		StringBuilder response = new StringBuilder();
		
		while((in = br.readLine()) != null) {
			response.append(in);
		}
		
		br.close();
		return (JSONObject) new JSONParser().parse(response.toString());
	}
	
	//post
	public static void postAnswer() {
		
	}
	
	public static void writeResultToFile(JSONObject challengeData, String decrypted, String sha1Resume) throws IOException {
		FileWriter fw = new FileWriter(new File("answer.json"));
		
		fw.write("{\n"
				+ "\t\"numero_casas\": " + challengeData.get("numero_casas") + ",\n"
				+ "\t\"token\": \"" + challengeData.get("token") + "\",\n"
				+ "\t\"cifrado\": \"" + challengeData.get("cifrado") + "\",\n"
				+ "\t\"decifrado\": \"" + decrypted + "\",\n"
				+ "\t\"resumo_criptografico\": \"" + sha1Resume + "\"\n"
				+ "}");
		
		fw.close();
	}
	
	private static String readToken() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File("token.txt")));
		String token = br.readLine();
		br.close();
		return token;
	}
}
