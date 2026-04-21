package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/oauth/callback")
public class OAuthCallbackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            response.getWriter().println("No se recibió el código de autorización.");
            return;
        }

        String tenantId = "*******************";
        String clientId = "*******************";
        String clientSecret = "*******************";

        
        String redirectUri = "https://cartera.dulcesydulces.co/oauth/callback";



        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        String params =
                "client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                "&scope=" + URLEncoder.encode("offline_access Files.ReadWrite.All User.Read", "UTF-8") +
                "&code=" + URLEncoder.encode(code, "UTF-8") +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                "&grant_type=authorization_code" +
                "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8");

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        InputStream inputStream = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
        }

        if (responseCode != 200) {
            response.getWriter().println("❌ Error en token request (" + responseCode + "):<br>" + sb.toString());
            return;
        }

        JSONObject json = new JSONObject(sb.toString());
        String accessToken = json.getString("access_token");

        HttpSession session = request.getSession();
        session.setAttribute("access_token", accessToken);

       response.sendRedirect("/principal.jsp?CONTENIDO=Usuarios/clientes.jsp");

    }
}
