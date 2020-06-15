package dev.codenation.aceleradev.java.online.challenge.io.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import javax.xml.ws.http.HTTPException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class InputOutputUtilities {
	
	private InputOutputUtilities() {}
		
	private static final String CHARSET = "UTF-8";
	private static final String CRLF = "\r\n";
	private static final String GET = "GET";
	private static final String GET_URL = "https://api.codenation.dev/v1/challenge/dev-ps/generate-data?token=";
	private static final String POST_URL = "https://api.codenation.dev/v1/challenge/dev-ps/submit-solution?token=";
	
	public static JSONObject requestData() throws IOException, ParseException {
		HttpURLConnection conn = (HttpURLConnection) new URL(GET_URL.concat(readToken())).openConnection();;
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
	
	public static void postAnswer() throws IOException {
		File answer = new File("answer.json");
		String boundary = Long.toHexString(System.currentTimeMillis());

		HttpURLConnection conn = (HttpURLConnection) new URL(POST_URL.concat(readToken())).openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

		OutputStream output = conn.getOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, CHARSET), true);
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"answer\"; filename=\"" + answer.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: text/plain; charset=" + CHARSET).append(CRLF);
	    writer.append(CRLF).flush();
	    Files.copy(answer.toPath(), output);
	    output.flush();
	    writer.append(CRLF).flush();
	    writer.append("--" + boundary + "--").append(CRLF).flush();
	    writer.close();
	   
		int responseCode = conn.getResponseCode();
		System.out.println(responseCode);
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
