<%-- 
    Document   : clientesFormulario
    Created on : 7/11/2025, 03:14:01 PM
    Author     : HP
--%>

<%@page import="clases.Clientes"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="estilos/usuariosFormulario.css">
<%
    String accion = request.getParameter("accion");
    String nit_cliente = request.getParameter("nit_cliente");
    Clientes clientes;

    if ("Modificar".equals(accion)) {
        clientes = new Clientes(nit_cliente);
    } else {
        clientes = new Clientes(); // Para adicionar o casos indefinidos
        clientes.setEstado("Activo"); // POR DEFECTO
        clientes.setEstado_documentacion("INCOMPLETO");
    }
%>

<h3><%=accion.toUpperCase()%> NUEVO TERCERO</h3>

<form name="formulario" method="post" action="clientesActualizar.jsp">
    <table border="0">
        
    <tr>
        <th>Nit cliente</th>
        <td>
            <input type="text" name="nit_cliente"
            value="<%= clientes.getNit_cliente() %>"
            size="50" maxlength="50" required
            pattern="[0-9]+"
            title="Solo se permiten números"
            <%= "Modificar".equals(accion) ? "readonly style='background:#eee;'" : "" %> >
        </td>
    </tr>
    
    <tr>
        <th>Razon social</th>
        <td>
            <input type="text" name="razon_social" value="<%= clientes.getRazon_social() %>" 
                   size="50" maxlength="50" required>
        </td>
    </tr>
    <!-- AQUI AGREGAS EL CAMPO DE FECHA -->
    <tr>
        <th>Fecha creación</th>
        <td>
            <input type="date" name="fecha_siesa"
                   value="<%= clientes.getFecha_siesa()%>">
        </td>
    </tr>
</table>


    <!-- ESTADO DEL CLIENTE -->
    <input type="hidden" name="estado" value="<%=clientes.getEstado()%>">
    <input type="hidden" name="estado_documentacion" value="<%=clientes.getEstado_documentacion()%>">
    

    <!-- NIT anterior -->
    <input type="hidden" name="nitAnterior" value="<%=clientes.getNit_cliente()%>">

    <p><input type="submit" name="accion" value="<%=accion%>"></p>
</form>
<br>
<center><a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp" class="btn-volver">⬅ Volver</a></center>