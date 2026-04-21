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
public class TipoDocumento {
    
    private String id;
    private String tipo;

    public TipoDocumento() {
    }

    public TipoDocumento(String id) {
       String cadenaSQL = "SELECT tipo FROM TipoDocumento WHERE id=" + id;
       ResultSet resultado = ConectorBD.consultar(cadenaSQL);
       
        try {
            if(resultado.next()){
                this.id = id;
                tipo = resultado.getString("tipo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TipoDocumento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public boolean grabar(){
        String cadenaSQL = "INSERT INTO TipoDocumento (tipo) VALUES ('" + tipo + "')";
        return  ConectorBD.ejecutarQuery(cadenaSQL);
    }
    public boolean modificar(){
        String cadenaSQL = "UPDATE TipoDocumento SET tipo='" + tipo + "' WHERE id='" +id+ "'";
        return  ConectorBD.ejecutarQuery(cadenaSQL);
    }
    public boolean eliminar() {
        String cadenaSQL = "DELETE FROM TipoDocumento WHERE id='" + id + "'";
        return ConectorBD.ejecutarQuery(cadenaSQL);
    }
    
    

    public static ResultSet getLista(String filtro, String orden) {
        if (filtro != null && !filtro.isEmpty()) filtro = " WHERE " + filtro;
        else filtro = "";
        if (orden != null && !orden.isEmpty()) orden = " ORDER BY " + orden;
        else orden = "";
        String cadenaSQL = "SELECT id, tipo FROM TipoDocumento" + filtro + orden;
        return ConectorBD.consultar(cadenaSQL);
    }

    // Método estático para obtener una lista de TipoCancha en objetos
    public static List<TipoDocumento> getListaEnObjetos(String filtro, String orden) {
        List<TipoDocumento> lista = new ArrayList<>();
        ResultSet datos = TipoDocumento.getLista(filtro, orden);
        if (datos != null) {
            try {
                while (datos.next()) {
                    TipoDocumento tipoDocumento = new TipoDocumento();
                    tipoDocumento.setId(datos.getString("id"));
                    tipoDocumento.setTipo(datos.getString("tipo"));                
                    lista.add(tipoDocumento);
                }
            } catch (SQLException ex) {
                Logger.getLogger(TipoDocumento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lista;
    }
    
    
    // Método estático para obtener una lista de TipoCancha en formato <option>
    public static String getListaEnOptions(String idSeleccionado) {
        StringBuilder listaOptions = new StringBuilder();
        ResultSet datos = TipoDocumento.getLista(null, "tipo"); // Ordenar por nombre

    try {
        while (datos.next()) {
            String id = datos.getString("id");
            String tipo = datos.getString("tipo");

            // Agregar el atributo 'selected' si es la opción seleccionada
            String selected = id.equals(idSeleccionado) ? "selected" : "";
            listaOptions.append("<option value='").append(id).append("' ").append(selected).append(">")
                         .append(tipo).append("</option>");
        }
    } catch (SQLException ex) {
        Logger.getLogger(TipoDocumento.class.getName()).log(Level.SEVERE, null, ex);
    }

    return listaOptions.toString();
}

}

    

