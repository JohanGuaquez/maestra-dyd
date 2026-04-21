<%@page import="clases.Persona"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="estilos/usuariosFormulario.css">

<%
    String accion = request.getParameter("accion");
    String identificacion = request.getParameter("identificacion");
    Persona usuario;

    if ("Modificar".equals(accion)) {
        usuario = new Persona(identificacion);
    } else if ("Adicionar".equals(accion)) {
        usuario = new Persona(); // Inicializa con valores vacíos
    } else {
        usuario = new Persona(); // Asegúrate de inicializar en otros casos
    }
%>
<h3><%=accion.toUpperCase()%> USUARIO</h3>


<form name="formulario" method="post" action="usuarioActualizar.jsp">
    <table border="0">
        <tr>
            <th>Identificación</th>
            <td><input type="text" name="identificacion" maxlength="12" value="<%=usuario.getIdentificacion().trim()%>" required></td>
        </tr>
        <tr>
            <th>Nombres</th>
            <td><input type="text" name="nombre" value="<%=usuario.getNombre()%>" size="50" maxlength="50" required></td>            
        </tr>
        <tr>
            <th>Apellidos</th>
            <td><input type="text" name="apellido" value="<%=usuario.getApellido()%>" size="50" maxlength="50" required></td>
        </tr>
        
        <tr>
            <th>Rol</th>
            <td>
                <select name="tipo">
                    <%=usuario.getTipoEnObjeto().getListaEnOptions()%>
                </select>
            </td>
        </tr>
        <tr>
            <th>Contraseña</th>
            <td><input type="password" name="clave" value="<%=usuario.getClave()%>"></td>
        </tr>
    </table>
    <input type="hidden" name="identificacionAnterior" value="<%=usuario.getIdentificacion()%>">
    <p><input type="submit" name="accion" value="<%=accion%>"></p>
</form>
<br>
<center><a href="principal.jsp?CONTENIDO=Usuarios/usuarios.jsp" class="btn-volver">⬅ Volver</a></center>