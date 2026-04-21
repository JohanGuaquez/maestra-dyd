<%@page import="clases.TipoDocumento"%>
<%@page import="clases.OneDriveAuthUser"%>
<%@page import="clases.Clientes"%>
<%@page import="clases.Documentos"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="estilos/documentos.css">

<%
    String nitCliente = request.getParameter("nit_cliente");

    // Obtener datos del cliente
    Clientes cliente = new Clientes(nitCliente);
    String razonSocial = cliente.getRazon_social();

    // Obtener lista de documentos
    List<Documentos> listaDocs = Documentos.getListaPorCliente(nitCliente);
%>

<h3>Documentos del Cliente</h3>
<p>
    <strong>NIT:</strong> <%= nitCliente %><br>
    <strong>Razón Social:</strong> <%= razonSocial %>
</p>
<!-- ✅ FORMULARIO DE SUBIDA -->
<form action="/subirDocumento" method="post" enctype="multipart/form-data">
    <input type="hidden" name="nit_cliente" value="<%= nitCliente %>">

    <label>Tipo de documento:</label>
    <select name="tipo">
        <%= TipoDocumento.getListaEnOptions(cliente.getNit_cliente()) %>
    </select>

    <br><br>

    <input type="file" name="archivo" required>

    <button type="submit">Subir a OneDrive</button>
</form>
    
    <form action="cambiarEstadoDoc" method="post">
    <input type="hidden" name="nit_cliente" value="<%= nitCliente %>">

    <label>Estado de Documentación:</label>
    <select name="estado">
        <option value="COMPLETO" <%= "COMPLETO".equals(cliente.getEstado_documentacion()) ? "selected" : "" %>>Completo</option>
        <option value="INCOMPLETO" <%= "INCOMPLETO".equals(cliente.getEstado_documentacion()) ? "selected" : "" %>>Incompleto</option>
    </select>

    <button type="submit">Actualizar Estado</button>
    
</form>

    
<hr>

<!-- ✅ LISTA DE DOCUMENTOS -->
<table border="1" cellpadding="5">
    <tr>
        <th>Tipo documento</th>
        <th>Nombre archivo</th>
        <th>Fecha subida</th>   
        <th>Acciones</th>
    </tr>
    <%
        for (Documentos doc : listaDocs) {
    %>
        <tr>
            <td><%= doc.getTipoLista() %></td>
            <td><%= doc.getNombreArchivo() %></td>
            <td><%= doc.getFecha_subida()%></td>
            <td>
                <a href="verDocumento?id=<%= doc.getOnedrive_id() %>" target="_blank" class="btn-ver">Ver</a>

                <form action="eliminarDocumento" method="post" style="display:inline;">
                    <input type="hidden" name="id" value="<%= doc.getId() %>">
                    <input type="hidden" name="nit_cliente" value="<%= nitCliente %>">
                    <input type="hidden" name="onedrive_id" value="<%= doc.getOnedrive_id() %>">
                    <button type="submit" onclick="return confirm('¿Eliminar este documento?')">
                        Eliminar
                    </button> 
                </form>
            </td>
        </tr>
    <%
        }
    %>
</table>

<!-- ✅ BOTÓN PARA VOLVER A CLIENTES -->
<br>
<a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp" class="btn-volver">⬅ Volver a Clientes</a>


