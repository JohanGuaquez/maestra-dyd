package servlets;

import clases.Clientes;
import clases.Documentos;
import clases.OneDriveUploader;

import java.io.InputStream;
import java.io.IOException;
import java.net.URLEncoder;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet(name = "subirDocumento", urlPatterns = {"/subirDocumento"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 50) // Máx. 50 MB
public class subirDocumento extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nit_cliente = request.getParameter("nit_cliente");
        String tipo = request.getParameter("tipo");
        Part archivo = request.getPart("archivo");

        if (archivo == null || archivo.getSize() == 0) {
            String error = URLEncoder.encode("Archivo no seleccionado", "UTF-8");
            response.sendRedirect("documentosCliente.jsp?nit_cliente=" + nit_cliente + "&error=" + error);
            return;
        }

        String nombreOriginal = extraerNombreArchivo(archivo);

        String extension = "";
        int punto = nombreOriginal.lastIndexOf(".");
        if (punto >= 0) {
            extension = nombreOriginal.substring(punto);
        }

        Clientes cliente = new Clientes(nit_cliente);
        String razon = cliente.getRazon_social();
        razon = razon.replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");

        clases.TipoDocumento tipoDoc = new clases.TipoDocumento(tipo);
        String nombreTipo = tipoDoc.getTipo();
        nombreTipo = nombreTipo.replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");

        String nuevoNombre = nit_cliente + "_" + razon + "_" + nombreTipo + extension;

        Documentos docCheck = new Documentos();
        String baseNombre = nit_cliente + "_" + razon + "_" + nombreTipo;
        String nombreFinal = nuevoNombre;

        int contador = 1;
        while (docCheck.existeNombreArchivo(nombreFinal)) {
            nombreFinal = baseNombre + "(" + contador + ")" + extension;
            contador++;
        }
        nuevoNombre = nombreFinal;

        InputStream inputStream = archivo.getInputStream();

        try {
            HttpSession session = request.getSession();
            String accessToken = (String) session.getAttribute("access_token");

            if (accessToken == null || accessToken.isEmpty()) {
                throw new Exception("No hay token de acceso válido. Debes iniciar sesión con OneDrive primero.");
            }

            String userPrincipalName = "nubedyd@dulcesydulces01.onmicrosoft.com";

            String[] resultado = OneDriveUploader.subirArchivo(
                    accessToken,
                    nuevoNombre,
                    inputStream,
                    userPrincipalName
            );

            if (resultado == null || resultado.length < 2 || "ERROR".equals(resultado[0])) {
                throw new Exception("Error al subir archivo a OneDrive: " + (resultado != null ? resultado[1] : "Respuesta nula"));
            }

            String onedriveId = resultado[0];
            String onedriveUrl = resultado[1];

            Documentos doc = new Documentos();
            doc.setNit_cliente(nit_cliente);
            doc.setNombreArchivo(nuevoNombre);
            doc.setRuta_Documento("OneDrive");
            doc.setOnedrive_id(onedriveId);
            doc.setOnedrive_url(onedriveUrl);
            doc.setTipo(tipo);

            boolean guardado = doc.grabar();
            if (!guardado) {
                throw new Exception("Error al guardar el documento en la base de datos.");
            }

            String ok = URLEncoder.encode("Archivo subido correctamente", "UTF-8");
            response.sendRedirect("documentosCliente.jsp?nit_cliente=" + nit_cliente + "&ok=" + ok);

        } catch (Exception e) {
            e.printStackTrace();
            String error = URLEncoder.encode("Error: " + e.getMessage(), "UTF-8");
            response.sendRedirect("documentosCliente.jsp?nit_cliente=" + nit_cliente + "&error=" + error);

        } finally {
            if (inputStream != null) inputStream.close();
        }
    }

    private String extraerNombreArchivo(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf('=') + 2, s.length() - 1);
            }
        }
        return "archivo_desconocido";
    }
}
