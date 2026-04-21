package clases;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class OneDriveAuth {

    private static final String TENANT_ID = "******************";
    private static final String CLIENT_ID = "**********************";
    private static final String CLIENT_SECRET = "*********************************";
    private static final String SCOPE = "***************************";

    public static String obtenerAccessToken() throws Exception {
        String url = "*******************" + TENANT_ID + "*******************";
        String data = "client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8")
                    + "&scope=" + URLEncoder.encode(SCOPE, "UTF-8")
                    + "&Maestra_dyd=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8")
                    + "&grant_type=client_credentials";

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(data.getBytes());
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        String response = new BufferedReader(new InputStreamReader(is))
                .lines().reduce("", (a, b) -> a + b);

        if (responseCode != 200) {
            System.out.println("❌ Error token: " + response);
            throw new Exception("Error al obtener token. Código: " + responseCode);
        }

        JSONObject json = new JSONObject(response);
        return json.getString("access_token");
    }
    public static String obtenerDriveId(String accessToken) throws Exception {
    String endpoint = "*******************";
    HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);

    int responseCode = conn.getResponseCode();
    InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
    String response = new BufferedReader(new InputStreamReader(is))
            .lines().reduce("", (a, b) -> a + b);

    if (responseCode != 200) {
        throw new Exception("Error al obtener driveId: " + response);
    }

    JSONObject json = new JSONObject(response);
    return json.getString("id");
}
public static String obtenerAccessTokenDesdeCode(String code) throws Exception {
    String clientId = "*******************"; // tu App ID
    String clientSecret = "*******************";
    String redirectUri = "*******************";

    URL url = new URL("*******************");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

    String body = "client_id=" + clientId +
                  "*******************" +
                  "&code=" + code +
                  "&redirect_uri=" + redirectUri +
                  "&grant_type=authorization_code" +
                  "&client_secret=" + clientSecret;

    try (OutputStream os = conn.getOutputStream()) {
        os.write(body.getBytes("UTF-8"));
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
        response.append(line);
    }

    JSONObject json = new JSONObject(response.toString());
    return json.getString("access_token");
}


}
