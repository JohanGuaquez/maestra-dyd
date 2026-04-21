<%-- 
    Document   : tipoDocumento
    Created on : 14/11/2025, 02:54:24 PM
    Author     : HP
--%>
<%@page import="java.util.List"%>
<%@page import="clases.TipoDocumento"%>
<link rel="stylesheet" type="text/css" href="estilos/usuarios.css">
<%
String lista = "";
List<TipoDocumento> datos = TipoDocumento.getListaEnObjetos(null, null);

for (TipoDocumento tipoDocumento : datos) {
        lista += "<tr>";
        lista += "<td>" + tipoDocumento.getId() + "</td>";
        lista += "<td>" + tipoDocumento.getTipo()+ "</td>";
        
        lista += "<td>";
        lista += "<a href='principal.jsp?CONTENIDO=tipoDocumentoFormulario.jsp&accion=Modificar&id=" + tipoDocumento.getId() + 
                 "' title='Modificar'><button type='submit' class='modificar'>Modificar</button></a>";
        
        lista += "<button type='button' title='Eliminar' onclick='if(confirm(\"¿Eliminar este documento?\")) eliminar(" 
        + tipoDocumento.getId() + ");'>Eliminar</button>";
 
        lista += "</td>";
        
        lista += "</tr>";
    }
%>
<div>
    <th><a href="principal.jsp?CONTENIDO=tipoDocumentoFormulario.jsp&accion=Adicionar" title="Adicionar">
                <button type='submit' class='agregar'>Agregar</button></a></th>
<table border="1">
    <tr>
        <th>id</th>
        <th>Tipo de documentacion</th>     
        <th>Opciones</th>
        
    </tr>
    <%= lista %>
</table>
</div>
<script>
    function eliminar(id) {
        let resultado = confirm("¿Realmente desea eliminar este tipo documento?");
        if (resultado) {
            document.location = "principal.jsp?CONTENIDO=tipoDocumentoActualizar.jsp&accion=Eliminar&id=" + id;
        }          
    }
</script>

