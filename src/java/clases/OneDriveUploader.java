package clases;

import java.io.*;
import java.net.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class OneDriveUploader {

    // -------------------- SUBIR ARCHIVO --------------------
    public static String[] subirArchivo(String accessToken, String nombreArchivo, InputStream inputStream, String userPrincipalName) throws Exception {
        // 1️⃣ Obtener driveId del usuario
        String driveId = obtenerDriveId(accessToken, userPrincipalName);
        System.out.println("📂 Drive ID del usuario: " + driveId);

        // 2️⃣ Asegurar carpeta "DocumentosClientes" en ese drive
        String carpetaId = asegurarCarpeta(accessToken, driveId, "DocumentosClientes");
        System.out.println("📁 Carpeta asegurada. ID: " + carpetaId);

        // 3️⃣ URL para subir archivo a la carpeta
        // 📤 URL correcta para subir el contenido real del archivo
String uploadUrl = "https://graph.microsoft.com/v1.0/drives/"
        + driveId + "/items/" + carpetaId + ":/"
        + URLEncoder.encode(nombreArchivo, "UTF-8") + ":/content";

System.out.println("📤 URL final de subida: " + uploadUrl);

HttpURLConnection conn = (HttpURLConnection) new URL(uploadUrl).openConnection();
conn.setRequestMethod("PUT");
conn.setRequestProperty("Authorization", "Bearer " + accessToken);
conn.setRequestProperty("Content-Type", "application/octet-stream");
conn.setRequestProperty("Accept", "application/json");
conn.setDoOutput(true);

// 📦 Leer bytes del archivo
ByteArrayOutputStream buffer = new ByteArrayOutputStream();
byte[] tmp = new byte[8192];
int bytesRead;
while ((bytesRead = inputStream.read(tmp)) != -1) {
    buffer.write(tmp, 0, bytesRead);
}
byte[] fileBytes = buffer.toByteArray();

System.out.println("📏 Tamaño del archivo a subir: " + fileBytes.length + " bytes");

// 🔄 Enviar archivo a OneDrive
try (OutputStream os = conn.getOutputStream()) {
    os.write(fileBytes);
    os.flush();
}

int responseCode = conn.getResponseCode();
InputStream responseStream = (responseCode >= 200 && responseCode < 300)
        ? conn.getInputStream()
        : conn.getErrorStream();

StringBuilder response = new StringBuilder();
try (BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        response.append(line);
    }
}

System.out.println("📦 Respuesta OneDrive:");
System.out.println(response.toString());

if (responseCode < 200 || responseCode >= 300) {
    throw new IOException("❌ Error al subir archivo: " + response.toString());
}

JSONObject json = new JSONObject(response.toString());
String id = json.optString("id", "SIN_ID");
String webUrl = json.optString("webUrl", "SIN_URL");

System.out.println("✅ Archivo subido correctamente.");
System.out.println("🆔 ID: " + id);
System.out.println("🔗 URL: " + webUrl);

return new String[]{id, webUrl};

    }

    // -------------------- OBTENER DRIVE ID --------------------
    public static String obtenerDriveId(String accessToken, String userPrincipalName) throws Exception {
        String url = "https://graph.microsoft.com/v1.0/users/" + URLEncoder.encode(userPrincipalName, "UTF-8") + "/drive";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        int code = conn.getResponseCode();
        InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
        }

        if (code < 200 || code >= 300) {
            throw new IOException("❌ Error al obtener driveId: " + response.toString());
        }

        JSONObject json = new JSONObject(response.toString());
        return json.getString("id");
    }

    // -------------------- ASEGURAR CARPETA --------------------
    private static String asegurarCarpeta(String accessToken, String driveId, String carpetaNombre) throws Exception {
        // Listar carpetas en root
        String urlList = "https://graph.microsoft.com/v1.0/drives/" + driveId + "/root/children";
        HttpURLConnection connList = (HttpURLConnection) new URL(urlList).openConnection();
        connList.setRequestMethod("GET");
        connList.setRequestProperty("Authorization", "Bearer " + accessToken);
        connList.setRequestProperty("Accept", "application/json");

        int codeList = connList.getResponseCode();
        InputStream listStream = (codeList >= 200 && codeList < 300) ? connList.getInputStream() : connList.getErrorStream();
        StringBuilder responseList = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(listStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) responseList.append(line);
        }

        if (codeList < 200 || codeList >= 300) {
            throw new IOException("❌ Error al listar carpetas: " + responseList.toString());
        }

        JSONObject jsonList = new JSONObject(responseList.toString());
        JSONArray items = jsonList.getJSONArray("value");
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if (item.has("folder") && carpetaNombre.equals(item.getString("name"))) {
                return item.getString("id"); // Carpeta ya existe
            }
        }

        // Crear carpeta si no existe
        String urlCreate = "https://graph.microsoft.com/v1.0/drives/" + driveId + "/root/children";
        HttpURLConnection connCreate = (HttpURLConnection) new URL(urlCreate).openConnection();
        connCreate.setRequestMethod("POST");
        connCreate.setRequestProperty("Authorization", "Bearer " + accessToken);
        connCreate.setRequestProperty("Content-Type", "application/json");
        connCreate.setRequestProperty("Accept", "application/json");
        connCreate.setDoOutput(true);

        String body = "{ \"name\": \"" + carpetaNombre + "\", \"folder\": {}, \"@microsoft.graph.conflictBehavior\": \"rename\" }";
        try (OutputStream os = connCreate.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        int codeCreate = connCreate.getResponseCode();
        InputStream createStream = (codeCreate >= 200 && codeCreate < 300) ? connCreate.getInputStream() : connCreate.getErrorStream();
        StringBuilder responseCreate = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(createStream, "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) responseCreate.append(line);
        }

        if (codeCreate < 200 || codeCreate >= 300) {
            throw new IOException("❌ Error al crear carpeta: " + responseCreate.toString());
        }

        JSONObject jsonCreated = new JSONObject(responseCreate.toString());
        return jsonCreated.getString("id");
    }
    
    
   public static void eliminarArchivo(String accessToken, String driveId, String archivoId) throws Exception {
    String url = "https://graph.microsoft.com/v1.0/drives/" + driveId + "/items/" + archivoId;
    System.out.println("🗑️ Eliminando archivo en OneDrive: " + url);

    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
    conn.setRequestMethod("DELETE");
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);

    int code = conn.getResponseCode();
    System.out.println("🔢 Código de respuesta: " + code);

    if (code < 200 || code >= 300) {
        InputStream errorStream = conn.getErrorStream();
        StringBuilder sb = new StringBuilder();
        if (errorStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
            }
        }
        throw new IOException("Error al eliminar archivo en OneDrive: " + sb.toString());
    }

    System.out.println("✅ Archivo eliminado correctamente de OneDrive.");
}


}
