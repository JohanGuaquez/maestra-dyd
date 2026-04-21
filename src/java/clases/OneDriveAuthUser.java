package clases;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class OneDriveAuthUser {
    private static final String CLIENT_ID = "*******************************";
    private static final String CLIENT_SECRET = "*******************************";
    private static final String TENANT_ID = "*******************************";
    private static final String REDIRECT_URI = "*******************************";
    private static final String SCOPE = "*******************************";

    // URL que debes abrir para que el usuario inicie sesión
    public static String getAuthorizationUrl() throws UnsupportedEncodingException {
        return "*******************" + TENANT_ID + "*******************" +
                "client_id=" + CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&response_mode=query" +
                "&scope=" + URLEncoder.encode(SCOPE, "UTF-8");
    }

    // Intercambia el "code" por un token real
    public static JSONObject getTokenFromCode(String code) throws Exception {
        String tokenUrl = "*******************" + TENANT_ID + "*******************";
        String params =
                "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
                "&scope=" + URLEncoder.encode(SCOPE, "UTF-8") +
                "&code=" + URLEncoder.encode(code, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8") +
                "&grant_type=authorization_code" +
                "&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8");

        HttpURLConnection conn = (HttpURLConnection) new URL(tokenUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = reader.lines().reduce("", (a, b) -> a + b);
        reader.close();

        return new JSONObject(response);
    }
}
