<%@page import="java.util.List"%>
<%@page import="clases.Persona"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="estilos/usuarios.css">

<%
    String lista = "";
    List<Persona> datos = Persona.getListaEnObjetos("rol<>'C'", null);
    for (int i = 0; i < datos.size(); i++) {
        Persona usuario = datos.get(i);
        lista += "<tr>";
        lista += "<td>" + usuario.getIdentificacion() + "</td>";
        lista += "<td>" + usuario.getNombre() + "</td>";
        lista += "<td>" + usuario.getApellido()+ "</td>";
        lista += "<td>" + usuario.getTipoEnObjeto() + "</td>";

        lista += "<td>";
        lista += "<a href='principal.jsp?CONTENIDO=usuariosFormulario.jsp&accion=Modificar&identificacion=" + usuario.getIdentificacion() + "' title='Modificar'><img src='iconos/editarL.png'></a>";
        lista += "<img src='iconos/archivar.png' style=' title='Eliminar' onClick='eliminar(" + usuario.getIdentificacion() + ")'>";
        lista += "</td>";
        lista += "</tr>";
    }
%>
<h3>LISTA DE USUARIOS</h3>

<p>
    <a href="principal.jsp?CONTENIDO=usuariosFormulario.jsp&accion=Adicionar" title="Adicionar">
         <button type="button" class="btn-J">Nuevo usuario</button>
    </a>
    
<table>
    <tr>
        <th>Identificación</th>
        <th>Nombres</th>
        <th>Apellidos</th>
        <th>Tipo</th>
        <th>Opciones</th>
         
    </tr>
    <%= lista %>
</table>

<script type="text/javascript">
    function eliminar(identificacion) {
        resultado = confirm("¿Realmente desea eliminar el usuario con identificación " + identificacion + "?");
        if (resultado) {
            document.location = "principal.jsp?CONTENIDO=usuarioActualizar.jsp&accion=Eliminar&identificacion=" + identificacion;
        }
    }
</script>
