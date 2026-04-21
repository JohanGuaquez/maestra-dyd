package clases;

public class TipoPersona {
    
     private String codigo;

    public TipoPersona(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
   
    public String getNombre(){
        String nombre= null;
        switch (codigo){
            case "A" : nombre ="Administrador"; break;
            case "U" : nombre ="Usuarios"; break;
            default : nombre ="Desconocido"; break;
        }
        return nombre;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    public String getMenu() {
    StringBuilder menu = new StringBuilder("<ul class='menu'>");
    menu.append("<li><a href='/principal.jsp?CONTENIDO=inicio.jsp'>Inicio</a></li>");

    switch(this.codigo) {
        case "A":
            
            menu.append("<li><a href='/principal.jsp?CONTENIDO=Usuarios/usuarios.jsp'>Usuarios</a></li>");
            
            menu.append("<li class='submenu'><a href='#'>Clientes</a>");
            menu.append("<ul>");
            menu.append("<li><a href='/principal.jsp?CONTENIDO=Usuarios/clientes.jsp'>Clientes activos</a></li>");
            menu.append("<li><a href='/principal.jsp?CONTENIDO=clientesArchivados.jsp'>Clientes inactivos</a></li>");
            menu.append("</ul></li>");
            
            menu.append("<li><a href='/principal.jsp?CONTENIDO=tipoDocumento.jsp'>Tipos documentos</a></li>");
            break;          
        case "U":
            menu.append("<li class='submenu'><a href='#'>Clientes</a>");
            menu.append("<ul>");
            menu.append("<li><a href='/principal.jsp?CONTENIDO=Usuarios/clientes.jsp'>Clientes activos</a></li>");
            menu.append("<li><a href='/principal.jsp?CONTENIDO=clientesArchivados.jsp'>Clientes inactivos</a></li>");
            menu.append("</ul></li>");
            break;
        default:
            menu.append("<li><a href='/principal.jsp?CONTENIDO=inicio.jsp'>Inicio</a></li>");
            break;
    }
    
    menu.append("<li><a href='/index.jsp'>Salir</a></li>");
    menu.append("</ul>");
    return menu.toString();
}

   
    public String getListaEnOptions() {
    String lista = "";
    if (codigo == null) codigo = "";
    
    lista += "<option value='A'" + (codigo.equals("A") ? " selected" : "") + ">Administrador</option>";
    lista += "<option value='U'" + (codigo.equals("U") ? " selected" : "") + ">Usuarios</option>";
    
    return lista;
}
}