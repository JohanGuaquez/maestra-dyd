<%-- 
    Document   : clientesArchivados
    Created on : 15/11/2025, 11:23:37 AM
    Author     : HP
--%>

<%@ page import="clases.Clientes" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="estilos/usuarios.css">

<%  
    String buscador = request.getParameter("buscador");
    if (buscador == null) buscador = "";

    int pagina = 1;
    int registrosPorPagina = 30;
    
    try {
        pagina = Integer.parseInt(request.getParameter("pagina"));
    } catch (Exception e) { pagina = 1; }

    int inicio = (pagina - 1) * registrosPorPagina;

    // ---------------------
    // CONDICIÓN SQL
    // ---------------------
    String condicion = "estado='Inactivo'";

    if (!buscador.trim().isEmpty()) {
        condicion += " AND (nit_cliente LIKE '%" + buscador + "%' " +
                     "OR razon_social LIKE '%" + buscador + "%')";
    }

    // ---------------------
    // TOTAL DE REGISTROS
    // ---------------------
    int totalRegistros = Clientes.getCantidad(condicion); 
    int totalPaginas = (int)Math.ceil((double)totalRegistros / registrosPorPagina);

    // ---------------------
    // CONSULTA PAGINADA
    // ---------------------
    String limite = inicio + "," + registrosPorPagina;
    
    
    
    
   List<Clientes> datos = Clientes.getListaEnObjetos(condicion, "razon_social ASC LIMIT " + limite);

    // ---------------------
 

    String lista = "";

    for (Clientes cliente : datos) {
        lista += "<tr>";
        lista += "   <td>" + cliente.getNit_cliente() + "</td>";
        lista += "   <td>" + cliente.getRazon_social() + "</td>";
        lista += "   <td>";
        lista += "      <img src='iconos/arriba.png' title='Activar cliente' onclick='activar(\""
                + cliente.getNit_cliente() + "\")' style='cursor:pointer;'>";
        lista += "      &nbsp;&nbsp;";
        lista += "      <a href=\"principal.jsp?CONTENIDO=documentosCliente.jsp&nit_cliente="
                + cliente.getNit_cliente() + "\" title=\"Ver Documentos\">";
        lista += "          <img src='iconos/agregarE.png'>";
        lista += "      </a>";
        lista += "   </td>";
        lista += "</tr>";
    }
%>

<h3>CLIENTES INACTIVOS</h3>

<p>
    <a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp">
        <button type="button" class="btn-J">Volver a Clientes Activos</button>
    </a>
</p>

<!-- BUSCADOR -->
<h2>Buscador</h2>
<input type="text" id="buscar" placeholder="Buscar cliente..." style="width:250px; padding:5px;">

<br><br>

<!-- TABLA -->
<table border="1" id="tablaClientes">
    <thead>
        <tr>
            <th>Identificación</th>
            <th>Nombre / Razón Social</th>
            <th>Opciones</th>
        </tr>
    </thead>

    <tbody id="tablaBody">
        <%= lista %>
    </tbody>
</table>


<div style="margin-top:15px; text-align:center;">

    <% if (pagina > 1) { %>
        <a href="principal.jsp?CONTENIDO=clientesArchivados.jsp&pagina=<%= pagina - 1 %>&buscador=<%= buscador %>">
            <button class="btn-J">Anterior</button>
        </a>
    <% } %>

    Página <%= pagina %> de <%= totalPaginas %> 

    <% if (pagina < totalPaginas) { %>
        <a href="principal.jsp?CONTENIDO=clientesArchivados.jsp&pagina=<%= pagina + 1 %>&buscador=<%= buscador %>">
            <button class="btn-J">Siguiente</button>
        </a>
    <% } %>
    
    Total registros: <%= totalRegistros %> 

</div>

<script>
// =======================
//  BUSCADOR DINÁMICO
// =======================
document.getElementById("buscar").addEventListener("keyup", function () {
    let filtro = this.value.toLowerCase();
    let filas = document.querySelectorAll("#tablaBody tr");

    filas.forEach(fila => {
        let texto = fila.textContent.toLowerCase();
        fila.style.display = texto.includes(filtro) ? "" : "none";
    });
});

// =======================
//  ACTIVAR CLIENTE
// =======================
function activar(nit_cliente) {
    if (confirm("¿Desea activar nuevamente el cliente con NIT " + nit_cliente + "?")) {
        document.location = "principal.jsp?CONTENIDO=clientesActualizar.jsp&accion=Activar&nit_cliente=" + nit_cliente;
    }
}
</script>
