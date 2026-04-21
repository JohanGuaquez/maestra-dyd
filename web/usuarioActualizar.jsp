<%@page import="clases.Persona"%>
<%
String accion = request.getParameter("accion");
String identificacionAnterior = request.getParameter("identificacionAnterior");

Persona usuario = new Persona();
usuario.setIdentificacion(request.getParameter("identificacion"));
usuario.setNombre(request.getParameter("nombre"));
usuario.setApellido(request.getParameter("apellido"));
usuario.setRol(request.getParameter("tipo"));
usuario.setClave(request.getParameter("clave"));// Asumiendo que 'tipo' se refiere al rol

switch (accion) {
    case "Adicionar":
        usuario.grabar();
        break;
    case "Modificar":
        usuario.modificar(identificacionAnterior);
        break;
    case "Eliminar":
        usuario.eliminar();
        break;
}
%>
<script type="text/javascript">
    document.location = "principal.jsp?CONTENIDO=Usuarios/usuarios.jsp";
</script>
