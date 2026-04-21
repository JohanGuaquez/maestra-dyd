<%-- 
    Document   : tipoDocumentoFormulario
    Created on : 14/11/2025, 02:58:02 PM
    Author     : HP
--%>

<%@page import="clases.TipoDocumento"%>
<link rel="stylesheet" type="text/css" href="estilos/usuariosFormulario.css">

<%
    String accion = request.getParameter("accion") != null ? request.getParameter("accion") : "Adicionar";
    String id = request.getParameter("id") != null ? request.getParameter("id") : "";
    String tipo = "";

    // Si estamos modificando, precargar datos
    if ("Modificar".equalsIgnoreCase(accion) && !id.isEmpty()) {
        TipoDocumento tipoDocumento = new TipoDocumento(id);
        tipo = tipoDocumento.getTipo() != null ? tipoDocumento.getTipo() : "";
    }
%>

<div id="banner">
    <h3><%=accion.toUpperCase()%> TIPO DOCUMENTO</h3>

    <form method="post" action="principal.jsp?CONTENIDO=tipoDocumentoActualizar.jsp">
        <table border="1">
            <% if ("Modificar".equalsIgnoreCase(accion)) { %>
                <tr>
                    <th>ID</th>
                    <td><%=id%></td>
                </tr>
            <% } %>

            <tr>
                <th>Nombre Tipo</th>
                <td>
                    <input type="text" 
                           name="tipo" 
                           value="<%=tipo%>" 
                           size="50" 
                           maxlength="100" 
                           required>
                </td>
            </tr>
        </table>

        <input type="hidden" name="id" value="<%=id%>">
        <input type="submit" name="accion" value="<%=accion%>">
    </form>
</div>

    <br><center><a href="principal.jsp?CONTENIDO=tipoDocumento.jsp" class="btn-volver">Volver</a></center>
