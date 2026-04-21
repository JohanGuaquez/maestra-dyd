package servlets;

import clases.Documentos;
import clases.OneDriveUploader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "eliminarDocumento", urlPatterns = {"/eliminarDocumento"})
public class eliminarDocumento extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idDoc = request.getParameter("id");
        String nitCliente = request.getParameter("nit_cliente");
        String onedrive_id = request.getParameter("onedrive_id");

        String accessToken = (String) request.getSession().getAttribute("access_token");
        String userPrincipalName = "nubedyd@dulcesydulces01.onmicrosoft.com";

        try {
            String driveId = OneDriveUploader.obtenerDriveId(accessToken, userPrincipalName);
            System.out.println("📂 Drive ID: " + driveId);

            if (onedrive_id != null && !onedrive_id.isEmpty()) {
                OneDriveUploader.eliminarArchivo(accessToken, driveId, onedrive_id);
            } else {
                System.out.println("⚠️ No se recibió onedrive_id, solo se eliminará de la BD.");
            }

            Documentos.eliminarPorId(Integer.parseInt(idDoc));
            System.out.println("🗑️ Documento eliminado de la base de datos.");

            response.sendRedirect("documentosCliente.jsp?nit_cliente=" + nitCliente);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error al eliminar documento: " + e.getMessage());
        }
    }
}
