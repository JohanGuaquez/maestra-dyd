<%@ page import="clases.Clientes" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<link rel="stylesheet" type="text/css" href="estilos/usuarios.css">

<%
    // ---------------------
    // PARÁMETROS
    // ---------------------
    String buscador = request.getParameter("buscador");
    String fechaPrograma = request.getParameter("fecha_programa");
    String fechaSiesa = request.getParameter("fecha_siesa");
    String estado = request.getParameter("estado");


    if (fechaPrograma == null) fechaPrograma = "";
    if (fechaSiesa == null) fechaSiesa = "";
    if (buscador == null) buscador = "";
    if (estado == null) estado = "";

    int pagina = 1;
    int registrosPorPagina = 30;
    
    try {
        pagina = Integer.parseInt(request.getParameter("pagina"));
    } catch (Exception e) { pagina = 1; }

    int inicio = (pagina - 1) * registrosPorPagina;

    // ---------------------
    // CONDICIÓN SQL
    // ---------------------
    String condicion = "estado='Activo'";

    if (!buscador.trim().isEmpty()) {
        condicion += " AND (nit_cliente LIKE '%" + buscador + "%' " +
                     "OR razon_social LIKE '%" + buscador + "%')";
    }
    
    // FILTRO POR FECHA PROGRAMA
    if (!fechaPrograma.trim().isEmpty()) {
       condicion += " AND fecha_programa = '" + fechaPrograma + "'";
    }

    // FILTRO POR FECHA SIESA
    if (!fechaSiesa.trim().isEmpty()) {
       condicion += " AND fecha_siesa = '" + fechaSiesa + "'";
    }

    if (!estado.trim().isEmpty()){
        condicion += " AND estado_documentacion = '" + estado + "'";
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
    // ARMAR TABLA
    // ---------------------
    String lista = "";
    for (Clientes c : datos) {

    String color = "";
    if ("INCOMPLETO".equalsIgnoreCase(c.getEstado_documentacion())) {
        color = " style='background-color:#F5F5DC;' ";
    }

    lista += "<tr " + color + ">";
    lista += "  <td>" + c.getNit_cliente() + "</td>";
    lista += "  <td>" + c.getRazon_social() + "</td>";
    lista += "  <td>" + c.getEstado() + "</td>";
    lista += "  <td>" + c.getEstado_documentacion() + "</td>";
    lista += "  <td>" + c.getFecha_programa() + "</td>";
    lista += "  <td>" + c.getFecha_siesa() + "</td>";
    
    lista += "  <td>";

    lista += "<a href='principal.jsp?CONTENIDO=clientesFormulario.jsp&accion=Modificar&nit_cliente="
        + c.getNit_cliente() + "' title='Modificar'><img src='iconos/editarL.png' width='40' height='40'></a>";

    lista += "<img src='iconos/borrar.png' width='40' height='40' title='Inactivar' "
        + "onClick=\"Archivar('" + c.getNit_cliente() + "')\">";

    lista += "<a href='principal.jsp?CONTENIDO=documentosCliente.jsp&nit_cliente=" 
        + c.getNit_cliente() + "' title='Ver Documentos'><img src='iconos/agregarE.png' width='40' height='40'></a>";

    lista += "</td></tr>";
}


%>

<h3>LISTA DE CLIENTES ACTIVOS</h3>

<!-- ========================= -->
<!-- BUSCADOR GENERAL -->
<!-- ========================= -->
<form method="GET" action="principal.jsp" class="buscador-container">
    <input type="hidden" name="CONTENIDO" value="Usuarios/clientes.jsp">
        
    <label>Buscar nit/razon social:</label>
    <input type="text" name="buscador" placeholder="Buscar por NIT o Razón Social"
           value="<%= buscador %>" style="width:250px">

    <label>Fecha ingreso:</label>
    <input type="date" name="fecha_programa" value="<%= fechaPrograma %>" style="width:250px">

    <label>Fecha ingreso Siesa:</label>
    <input type="date" name="fecha_siesa" value="<%= fechaSiesa %>" style="width:250px">
    
    <label>Estado documento</label>
    <select name="estado">
        <option value="">Todos</option>
        <option value="COMPLETO" <%= "COMPLETO".equals(estado) ? "selected" : "" %>>Completo</option>
        <option value="INCOMPLETO" <%= "INCOMPLETO".equals(estado) ? "selected" : "" %>>Incompleto</option>
    </select>

    <button type="submit" class="btn-J">Filtrar</button>

    <a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp">
        <button type="button" class="btn-J">Limpiar</button>
    </a>
</form>

    
    




       
<p>
    <a href="principal.jsp?CONTENIDO=clientesFormulario.jsp&accion=Adicionar" title="Adicionar">
        <button type="submit" class="btn-J">Nuevo cliente</button>
    </a>
    
   <a class="btn btn-primary"
   href="https://login.microsoftonline.com/dulcesydulces01.onmicrosoft.com/oauth2/v2.0/authorize?client_id=ab80e6d9-28a4-4897-b439-6748fecf7b63&response_type=code&redirect_uri=https%3A%2F%2Fcartera.dulcesydulces.co%2Foauth%2Fcallback&response_mode=query&scope=openid%20profile%20offline_access%20User.Read%20Files.ReadWrite">
  Conectar OneDrive
</a>

</p>
     <%--
    
    <a href="https://login.microsoftonline.com/dulcesydulces01.onmicrosoft.com/oauth2/v2.0/authorize?
    client_id=ab80e6d9-28a4-4897-b439-6748fecf7b63&
    response_type=code&
    redirect_uri=http://cartera.dulcesydulces.co:8080/Maestra_dyd/principal.jsp?CONTENIDO=Usuarios/clientes.jsp
    <%--redirect_uri=http://localhost:8080/Maestra_dyd/oauth/callback&
    response_mode=query&
    scope=Files.ReadWrite.All%20offline_access" 
    class="btn btn-primary">
        <button type="submit" class="btn-con">Conectar OneDrive</button>
    </a>
 
</p>
--%>


<table border="1" style="border-collapse: collapse;">

    <tr>
        <th>Identificación</th>
        <th>Nombre / Razón Social</th>
        <th>Estado</th>
        <th>Estado documentaicion</th>
        <th>Fecha ingreso</th>
        <th>Fecha ingreso siesa</th>
        <th>Opciones</th>
    </tr>
    <%= lista %>
</table>

<!-- ========================= -->
<!-- PAGINACIÓN -->
<!-- ========================= -->
<div style="margin-top:15px; text-align:center;">

    <% if (pagina > 1) { %>
        <a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp&pagina=<%= pagina - 1 %>&buscador=<%= buscador %>">
            <button class="btn-J">Anterior</button>
        </a>
    <% } %>

    Página <%= pagina %> de <%= totalPaginas %> 

    <% if (pagina < totalPaginas) { %>
        <a href="principal.jsp?CONTENIDO=Usuarios/clientes.jsp&pagina=<%= pagina + 1 %>&buscador=<%= buscador %>">
            <button class="btn-J">Siguiente</button>
        </a>
    <% } %>
    
    Total registros: <%= totalRegistros %> 

</div>

<script type="text/javascript">
function Archivar(nit) {
    if (confirm("¿Desea archivar el cliente con NIT " + nit + "?")) {
        document.location = "principal.jsp?CONTENIDO=clientesActualizar.jsp&accion=Archivar&nit_cliente=" + nit;
    }
}
</script>
