package servlets;

import clases.Clientes;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/cambiarEstadoDoc")
public class CambiarEstadoDoc extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nit = request.getParameter("nit_cliente");
        String estado = request.getParameter("estado");

        Clientes c = new Clientes(nit);
        c.setEstado_documentacion(estado);
        c.actualizarEstadoDocumentacion();

        response.sendRedirect("principal.jsp?CONTENIDO=documentosCliente.jsp&nit_cliente=" + nit);
    }
}
