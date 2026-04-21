package servlets;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/verDocumento")
public class verDocumento extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String itemId = request.getParameter("id");
        if (itemId == null || itemId.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta el parámetro id");
            return;
        }

        String accessToken = (String) request.getSession().getAttribute("access_token");
        if (accessToken == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autenticado con OneDrive");
            return;
        }

        String graphUrl = "https://graph.microsoft.com/v1.0/me/drive/items/" + itemId + "/content";
        HttpURLConnection conn = (HttpURLConnection) new URL(graphUrl).openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestMethod("GET");

        response.setContentType("application/pdf"); // ajusta según el tipo real del archivo
        try (InputStream in = conn.getInputStream();
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
