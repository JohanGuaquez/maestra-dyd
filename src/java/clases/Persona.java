package clases;

import clasesGenericas.ConectorBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Persona {
    private String identificacion;
    private String nombre;
    private String apellido;
    private String rol;
    private String clave;

    public Persona() {
    }

    public Persona(String identificacion) {
        String cadenaSQL = "SELECT nombre, apellido, rol, clave FROM Persona "
                + "WHERE identificacion ='" + identificacion + "'";
        ResultSet resultado = ConectorBD.consultar(cadenaSQL);

        try {
            if (resultado.next()) {
                this.identificacion = identificacion;
                nombre = resultado.getString("nombre");
                apellido = resultado.getString("apellido");
                rol = resultado.getString("rol");
                clave = resultado.getString("clave");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Getters y setters actualizados
    public String getIdentificacion() {
        return identificacion != null ? identificacion : "";
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion != null ? identificacion.trim() : "";
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido != null ? apellido : "";
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRol() {
        return rol != null ? rol : "";
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getClave() {
        return clave != null ? clave : "";
    }

    // Método setClave corregido
    public void setClave(String clave) {
        this.clave = clave != null && !clave.trim().isEmpty() ? clave : identificacion;
    }

    // Método para retornar el tipo de persona según el rol
    public TipoPersona getTipoEnObjeto() {
        return new TipoPersona(rol);
    }

    // Método grabar corregido con MD5 en la consulta SQL
    public boolean grabar() {
        String cadenaSQL = "INSERT INTO Persona (identificacion, nombre, apellido, rol, clave) "
                + "VALUES ('"
                + identificacion + "','" + nombre + "','" + apellido + "','" + rol + "', MD5('" + clave + "'))"; 
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }

    
    public boolean modificar(String identificacionAnterior) {
        String cadenaSQL = "UPDATE Persona SET identificacion='" + identificacion + "', nombre='" + nombre + "', apellido='" + apellido + "', rol='" + rol + "', clave=MD5('" + clave + "') WHERE identificacion='" + identificacionAnterior + "'";
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }

    // Método para eliminar una persona
    public boolean eliminar() {
        String cadenaSQL = "DELETE FROM Persona WHERE identificacion='" + identificacion + "'";
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }

    // Métodos para obtener listas de personas
    public static ResultSet getLista(String filtro, String orden) {
        if (filtro != null && !filtro.isEmpty()) {
            filtro = " WHERE " + filtro;
        } else {
            filtro = "";
        }
        if (orden != null && !orden.isEmpty()) {
            orden = " ORDER BY " + orden;
        } else {
            orden = "";
        }
        String cadenaSQL = "SELECT identificacion, nombre, apellido, rol, clave FROM Persona" + filtro + orden;
        return ConectorBD.consultar(cadenaSQL);
    }

    public static List<Persona> getListaEnObjetos(String filtro, String orden) {
        List<Persona> lista = new ArrayList<>();
        ResultSet datos = Persona.getLista(filtro, orden);
        if (datos != null) {
            try {
                while (datos.next()) {
                    Persona persona = new Persona();
                    persona.setIdentificacion(datos.getString("identificacion"));
                    persona.setNombre(datos.getString("nombre"));
                    persona.setApellido(datos.getString("apellido"));
                    persona.setRol(datos.getString("rol"));
                    persona.setClave(datos.getString("clave"));
                    lista.add(persona);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }

    // Validar una persona según identificación y clave
    public static Persona validar(String identificacion, String clave) {
        Persona persona = null;
        List<Persona> lista = Persona.getListaEnObjetos("identificacion='" + identificacion + "' and clave = MD5('" + clave + "')", null);
        if (lista.size() > 0) {
            persona = lista.get(0);
        }
        return persona;
    }
    
    public static String getListaEnOptions(String idSeleccionado) {
    StringBuilder listaOptions = new StringBuilder();
    ResultSet datos = Persona.getLista(null, "nombre"); // Ordenar por nombres

    try {
        while (datos.next()) {
            String identificacion = datos.getString("identificacion");
            String nombres = datos.getString("nombre");

            // Agregar el atributo 'selected' si es la opción seleccionada
            String selected = identificacion.equals(idSeleccionado) ? "selected" : "";
            listaOptions.append("<option value='").append(identificacion).append("' ")
                        .append(selected).append(">")
                        .append(nombres).append("</option>");
        }
    } catch (SQLException ex) {
        Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
    }

    return listaOptions.toString();
}
}
