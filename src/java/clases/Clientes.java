/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import clasesGenericas.ConectorBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class Clientes {
    private String nit_cliente;
    private String razon_social;
    private String estado;
    private String estado_documentacion;
    private String fecha_programa;
    private String fecha_siesa;


    public Clientes() {
    }
    
    public Clientes(String nit_cliente) {
        String cadenaSQL = "SELECT razon_social, estado, estado_documentacion, fecha_programa, fecha_siesa FROM Clientes "
                + "WHERE nit_cliente ='" + nit_cliente + "'";
        ResultSet resultado = ConectorBD.consultar(cadenaSQL);

        try {
            if (resultado.next()) {
                this.nit_cliente = nit_cliente;
                razon_social = resultado.getString("razon_social");
                estado = resultado.getString("estado");
                estado_documentacion = resultado.getString("estado_documentacion");
                fecha_programa = resultado.getString("fecha_programa");
                fecha_siesa = resultado.getString("fecha_siesa");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNit_cliente() {
        return nit_cliente != null ? nit_cliente : "";
    }

    public void setNit_cliente(String nit_cliente) {
        this.nit_cliente = nit_cliente;
    }

    public String getRazon_social() {
        return razon_social != null ? razon_social : "";
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }
    public String getEstado() {
        return estado != null ? estado : "";
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
        
    }
    
    public String getEstado_documentacion() {
        return estado_documentacion;
    }

    public void setEstado_documentacion(String estado_documentacion) {
        this.estado_documentacion = estado_documentacion;
    }

    public String getFecha_programa() {
        return fecha_programa;
    }

    public void setFecha_programa(String fecha_programa) {
        this.fecha_programa = fecha_programa;
    }

    public String getFecha_siesa() {
        return fecha_siesa;
    }

    public void setFecha_siesa(String fecha_siesa) {
        this.fecha_siesa = fecha_siesa;
    }
    

    public boolean grabar() {
        String cadenaSQL = "INSERT INTO Clientes (nit_cliente, razon_social, estado, estado_documentacion, fecha_siesa) "
                + "VALUES ('"
                + nit_cliente + "','" + razon_social + "','" + estado + "','" + estado_documentacion + "','" + fecha_siesa + "')"; 
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }

    
    public boolean modificar(String nitAnterior) {
        String cadenaSQL = "UPDATE Clientes SET nit_cliente='" + nit_cliente + "', razon_social='" + razon_social + "', estado='" + estado + "', estado_documentacion='" + estado_documentacion + "', fecha_siesa='" + fecha_siesa + "' WHERE nit_cliente='" + nitAnterior + "'";
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }

    // Método para eliminar una persona
    public boolean eliminar() {
        String cadenaSQL = "DELETE FROM Clientes WHERE nit_cliente='" + nit_cliente + "'";
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
        String cadenaSQL = "SELECT nit_cliente, razon_social, estado, estado_documentacion, fecha_programa, fecha_siesa FROM Clientes" + filtro + orden;
        return ConectorBD.consultar(cadenaSQL);
    }

    public static List<Clientes> getListaEnObjetos(String filtro, String orden) {
        List<Clientes> lista = new ArrayList<>();
        ResultSet datos = Clientes.getLista(filtro, orden);
        if (datos != null) {
            try {
                while (datos.next()) {
                    Clientes clientes = new Clientes();
                    clientes.setNit_cliente(datos.getString("nit_cliente"));
                    clientes.setRazon_social(datos.getString("razon_social"));
                    clientes.setEstado(datos.getString("estado"));
                    clientes.setEstado_documentacion(datos.getString("estado_documentacion"));
                    clientes.setFecha_programa(datos.getString("fecha_programa"));
                    clientes.setFecha_siesa(datos.getString("fecha_siesa"));
                    lista.add(clientes);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Persona.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
    
    public boolean archivar(String nit) {
    String sql = "UPDATE Clientes SET estado='Inactivo' WHERE nit_cliente='" + nit + "'";
    return ConectorBD.ejecutarQuery(sql);
}
    public boolean activar(String nit) {
    String sql = "UPDATE Clientes SET estado='Activo' WHERE nit_cliente='" + nit + "'";
    return ConectorBD.ejecutarQuery(sql);
}
    public static int getCantidad(String condicion) {
    int cantidad = 0;
    
    try {
        String sql = "SELECT COUNT(*) AS total FROM Clientes";
        
        if (condicion != null && !condicion.trim().isEmpty()) {
            sql += " WHERE " + condicion;
        }
        
        ResultSet rs = ConectorBD.consultar(sql);
        
        if (rs != null && rs.next()) {
            cantidad = rs.getInt("total");
        }
        
    } catch (Exception e) {
        System.out.println("Error obteniendo cantidad de clientes: " + e.getMessage());
    }
    
    return cantidad;
}
public void actualizarEstadoDocumentacion() {
    String sql = "UPDATE Clientes SET estado_documentacion='" + estado_documentacion + "' WHERE nit_cliente='" + nit_cliente + "'";
    ConectorBD.ejecutarQuery(sql);
}


}   