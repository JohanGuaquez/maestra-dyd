package clases;

import clasesGenericas.ConectorBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Documentos {

    private int id;
    private String nit_cliente;
    private String nombreArchivo;
    private String ruta_Documento;
    private String onedrive_id;
    private String onedrive_url;
    private String fecha_subida;
    private String tipo;
    

    // ──────────────── CONSTRUCTORES ────────────────
    public Documentos() {
    }

    public Documentos(int id, String nitCliente, String nombreArchivo, String ruta_Documento, String onedrive_id, String onedrive_url, String fecha_subida, String tipo) {
        this.id = id;
        this.nit_cliente = nitCliente;
        this.nombreArchivo = nombreArchivo;
        this.ruta_Documento = ruta_Documento;
        this.onedrive_id = onedrive_id;
        this.onedrive_url = onedrive_url;
        this.fecha_subida = fecha_subida;
        this.tipo = tipo;
        
    }

    // ──────────────── MÉTODOS CRUD ────────────────

     public boolean grabar() {
        ConectorBD conector = new ConectorBD();
        try {
            conector.conectar();

            String sql = "INSERT INTO Documentos (nit_cliente, nombreArchivo, ruta_Documento, onedrive_id, onedrive_url, tipo) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conector.getConexion().prepareStatement(sql);
            ps.setString(1, this.nit_cliente);
            ps.setString(2, this.nombreArchivo);
            ps.setString(3, this.ruta_Documento);
            ps.setString(4, this.onedrive_id);
            ps.setString(5, this.onedrive_url);
            ps.setString(6, this.tipo);

            ps.executeUpdate();
            System.out.println("✅ Documento guardado en la base de datos correctamente.");
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Error al guardar el documento: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public static boolean eliminarPorId(int id) {
        ConectorBD con = new ConectorBD();
        con.conectar();
        String sql = "DELETE FROM Documentos WHERE id = " + id;
        boolean resultado = con.ejecutarQuery(sql);
        con.desconectar();
        return resultado;
    }

    public static Documentos getPorId(int id) {
        ConectorBD con = new ConectorBD();
        con.conectar();
        String sql = "SELECT * FROM Documentos WHERE id = " + id;
        ResultSet rs = con.consultar(sql);
        Documentos doc = null;
        try {
            if (rs.next()) {
                doc = new Documentos();
                doc.setId(rs.getInt("id"));
                doc.setNit_cliente(rs.getString("nit_cliente"));
                doc.setNombreArchivo(rs.getString("nombreArchivo"));
                doc.setRuta_Documento(rs.getString("ruta_Documento"));
                doc.setOnedrive_id(rs.getString("onedrive_id"));
                doc.setOnedrive_url(rs.getString("onedrive_url"));
                doc.setFecha_subida(rs.getString("fecha_subida"));
                doc.setTipo(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.desconectar();
        return doc;
    }

    public static List<Documentos> getListaPorCliente(String nitCliente) {
        List<Documentos> lista = new ArrayList<>();
        ConectorBD con = new ConectorBD();
        con.conectar();
        String sql = "SELECT * FROM Documentos WHERE nit_cliente = '" + nitCliente + "' ORDER BY fecha_subida DESC";
        ResultSet rs = con.consultar(sql);
        try {
            while (rs.next()) {
                Documentos doc = new Documentos();
                doc.setId(rs.getInt("id"));
                doc.setNit_cliente(rs.getString("nit_cliente"));
                doc.setNombreArchivo(rs.getString("nombreArchivo"));
                doc.setRuta_Documento(rs.getString("ruta_Documento"));
                doc.setOnedrive_id(rs.getString("onedrive_id"));
                doc.setOnedrive_url(rs.getString("onedrive_url"));
                doc.setFecha_subida(rs.getString("fecha_subida"));
                doc.setTipo(rs.getString("tipo"));
                lista.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.desconectar();
        return lista;
    }

    // ──────────────── GETTERS Y SETTERS ────────────────

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNit_cliente() {
        return nit_cliente;
    }

    public void setNit_cliente(String nit_cliente) {
        this.nit_cliente = nit_cliente;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRuta_Documento() {
        return ruta_Documento;
    }

    public void setRuta_Documento(String ruta_Documento) {
        this.ruta_Documento = ruta_Documento;
    }

    public String getOnedrive_id() {
        return onedrive_id;
    }

    public void setOnedrive_id(String onedrive_id) {
        this.onedrive_id = onedrive_id;
    }

    public String getOnedrive_url() {
        return onedrive_url;
    }

    public void setOnedrive_url(String onedrive_url) {
        this.onedrive_url = onedrive_url;
    }

    public String getFecha_subida() {
        return fecha_subida;
    }

    public void setFecha_subida(String fecha_subida) {
        this.fecha_subida = fecha_subida;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getTipoLista() {
        TipoDocumento tipoDocumento = new TipoDocumento(tipo);
        return tipoDocumento.getTipo();
    }

    // ──────────────── MÉTODO EXTRA (opcional) ────────────────
    // Para actualizar información si fuera necesario
    public boolean actualizar() {
        ConectorBD con = new ConectorBD();
        con.conectar();
        String sql = "UPDATE Documentos SET "
                   + "nombreArchivo = '" + nombreArchivo + "', "
                   + "ruta_Documento = '" + ruta_Documento + "', "
                   + "onedrive_id = '" + onedrive_id + "', "
                   + "onedrive_url = '" + onedrive_url + "' "
                   + "tipo = '" + tipo + "' "
                   + "WHERE id = " + id;
        boolean resultado = con.ejecutarQuery(sql);
        con.desconectar();
        return resultado;
    }
    
   
    public boolean existeNombreArchivo(String nombreArchivo) {
    ConectorBD con = new ConectorBD();
    con.conectar();
    String sql = "SELECT id FROM Documentos WHERE nombreArchivo = '" + nombreArchivo + "'";
    java.sql.ResultSet rs = con.consultar(sql);
    boolean existe = false;

    try {
        if (rs.next()) {
            existe = true;
        }
    } catch (Exception e) {
        // evitar try/catch con SQLException como pediste
    }
    con.desconectar();
    return existe;
}

}
