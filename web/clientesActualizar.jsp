<%-- 
    Document   : clientesActualizar
    Created on : 7/11/2025, 03:14:24 PM
    Author     : HP
--%>

<%@page import="clases.Clientes"%>

<%
    String accion = request.getParameter("accion");
    String nit = request.getParameter("nit_cliente");
    String nitAnterior = request.getParameter("nitAnterior");

    // Nuevo: Estado que llega del formulario
    String estado = request.getParameter("estado");
    String estado_documentacion = request.getParameter("estado_documentacion");
    //String fecha_programa = request.getParameter("fecha_programa");
    String fecha_siesa = request.getParameter("fecha_siesa");

    Clientes cliente = new Clientes(nit);

    switch (accion) {

        case "Adicionar":
            cliente.setNit_cliente(nit);
            cliente.setRazon_social(request.getParameter("razon_social"));
            cliente.setEstado(estado);        // ? NECESARIO
            cliente.setEstado_documentacion(estado_documentacion);
            //cliente.setFecha_programa(fecha_programa);
            cliente.setFecha_siesa(fecha_siesa);
            cliente.grabar();
            break;

        case "Modificar":
            cliente.setNit_cliente(nit);
            cliente.setRazon_social(request.getParameter("razon_social"));
            cliente.setEstado(estado);        // ? NECESARIO
            cliente.setEstado_documentacion(estado_documentacion);
            //cliente.setFecha_programa(fecha_programa);
            cliente.setFecha_siesa(fecha_siesa);
            cliente.modificar(nitAnterior);
            break;

        case "Eliminar":
            cliente.eliminar();
            break;

        case "Archivar":
            cliente.archivar(nit);
            break;

        case "Activar":
            cliente.activar(nit);
            break;
    }

    // Redirecciones correctas según la acción
    String destino = "principal.jsp?CONTENIDO=Usuarios/clientes.jsp";

    if ("Archivar".equals(accion)) {
        destino = "principal.jsp?CONTENIDO=Usuarios/clientes.jsp";
    }

    if ("Activar".equals(accion)) {
        destino = "principal.jsp?CONTENIDO=clientesArchivados.jsp";
    }
%>

<script type="text/javascript">
    document.location = "<%= destino %>";
</script>
