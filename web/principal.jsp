<%@page import="clases.Persona"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="estilos/menu.css">

<%
HttpSession sesion = request.getSession();
Persona USUARIO = null;
if (sesion.getAttribute("usuariot") == null) {
    response.sendRedirect("index.jsp?error=2");
} else {
    USUARIO = (Persona) sesion.getAttribute("usuariot");
}
%>
<html>
<body class="bg-custom text-custom">
    <div class="container-fluid">
        <div class="row">
            <!-- Menú lateral -->
            <div class="sidebar collapsed" id="menu">
                <%
                    if (USUARIO != null && USUARIO.getTipoEnObjeto() != null) {
                        out.print(USUARIO.getTipoEnObjeto().getMenu());
                    } else {
                        out.print("Menú no disponible");
                    }
                %>
            </div>

            <!-- Contenido principal -->
            <div class="content collapsed" id="contenido">
                <jsp:include page='<%= request.getParameter("CONTENIDO") %>' flush="true" />
            </div>
        </div>
    </div>
</body>
</html>
