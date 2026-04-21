<%@page import="clases.TipoDocumento"%>

<%
    String accion = request.getParameter("accion");
    String tipo = request.getParameter("tipo");
    String idStr = request.getParameter("id");

    TipoDocumento tipoDocumento = new TipoDocumento();
    tipoDocumento.setTipo(tipo);

    switch (accion) {

        case "Adicionar":
            tipoDocumento.grabar();
            break;

        case "Modificar":
            tipoDocumento.setId(idStr); 
            tipoDocumento.modificar();
            break;

        case "Eliminar":
            tipoDocumento.setId(idStr);
            tipoDocumento.eliminar();
            break;
    }
%>
<script type="text/javascript">
    document.location = "principal.jsp?CONTENIDO=tipoDocumento.jsp";
</script>
