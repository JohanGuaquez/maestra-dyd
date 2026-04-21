package clasesGenericas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConectorBD {

    private String servidor;
    private String puerto;
    private String usuario;
    private String clave;
    private String baseDatos;

    private static Connection conexion;

    public ConectorBD() {
        //servidor = "localhost";
        servidor = "base-prueba.cpgga6o4qm0l.us-east-2.rds.amazonaws.com";
        puerto = "3306";
        usuario = "cartera";
        clave = "administrar";  // misma que probaste en consola
        baseDatos = "clientes";
    }

    // Método para obtener la conexión
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            ConectorBD conector = new ConectorBD();
            boolean ok = conector.conectar();

            // 🔴 si NO se pudo conectar, no sigas como si nada
            if (!ok || conexion == null || conexion.isClosed()) {
                throw new SQLException("No se pudo establecer conexión con la base de datos (conexion == null). Revisa ConectorBD.conectar()");
            }
        }
        return conexion;
    }

    public boolean conectar() {
        boolean conectado = false;
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("Driver ok");

                String cadenaConexion =
                    "jdbc:mysql://" + servidor + ":" + puerto + "/" + baseDatos +
                    "?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=utf8&serverTimezone=UTC";

                System.out.println("Intentando conectar a: " + cadenaConexion);

                conexion = DriverManager.getConnection(cadenaConexion, usuario, clave);

                System.out.println("Conectado a la BD.");
            }
            conectado = true;
        } catch (ClassNotFoundException ex) {
            System.out.println("Error en el controlador de la BD. " + ex.getMessage());
            ex.printStackTrace();  // 👈 MUY IMPORTANTE
        } catch (SQLException ex) {
            System.out.println("Error al conectarse a la BD. " + ex.getMessage());
            ex.printStackTrace();  // 👈 MUY IMPORTANTE
        }

        return conectado;
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Se ha desconectado de la BD.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al desconectarse de la BD. " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static ResultSet consultar(String cadenaSQL) {
        ResultSet resultado = null;
        try {
            Connection conexion = getConexion(); // Obtener la conexión (ya valida o lanza excepción)
            PreparedStatement sentencia = conexion.prepareStatement(
                    cadenaSQL,
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );
            resultado = sentencia.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error en la cadena SQL " + cadenaSQL + ". " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }

    public static boolean ejecutarQuery(String cadenaSQL, Object... params) {
        boolean resultado = false;
        try {
            Connection conexion = getConexion(); // Obtener la conexión
            PreparedStatement sentencia = conexion.prepareStatement(cadenaSQL);
            for (int i = 0; i < params.length; i++) {
                sentencia.setObject(i + 1, params[i]);
            }
            sentencia.execute();
            resultado = true;
        } catch (SQLException ex) {
            System.out.println("Error en la cadena SQL, " + cadenaSQL + " . " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }

    public ResultSet ejecutarConsultaConParametros(String cadenaSQL, Object... params) {
        ResultSet resultado = null;
        try {
            Connection conexion = getConexion(); // Obtener la conexión
            PreparedStatement sentencia = conexion.prepareStatement(cadenaSQL);
            for (int i = 0; i < params.length; i++) {
                sentencia.setObject(i + 1, params[i]);
            }
            resultado = sentencia.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error en la cadena SQL " + cadenaSQL + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return resultado;
    }
}
