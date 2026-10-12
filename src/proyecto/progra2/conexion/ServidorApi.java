package proyecto.progra2.conexion;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import proyecto.progra2.modelos.Cliente;
import proyecto.progra2.modelos.GestionClientes;

public class ServidorApi {

    private static final int PUERTO = 80;

    public static void iniciarServidor() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);
            
            // Endpoint para la app Android: /api/puntos
            server.createContext("/api/puntos", new PuntosHandler());
            server.setExecutor(null); // Usar el executor por defecto
            server.start();
            
            System.out.println("API HTTP Servidor escuchando en el puerto " + PUERTO + "...");
        } catch (IOException e) {
            System.err.println("Error iniciando el servidor API: " + e.getMessage());
        }
    }

    static class PuntosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Permitir peticiones cross-origin (CORS)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");

            String query = exchange.getRequestURI().getQuery(); // Obtener nit de la URL (?nit=XXX)
            String nitBuscado = "";

            if (query != null && query.contains("nit=")) {
                nitBuscado = query.split("nit=")[1].split("&")[0].trim();
            }

            GestionClientes gc = new GestionClientes();
            Cliente c = gc.buscarPorNit(nitBuscado);

            String jsonResponse;
            int statusCode = 200;

            if (c != null && c.isActivo()) {
                jsonResponse = String.format(
                    "{\"status\":\"success\", \"nit\":\"%s\", \"nombre\":\"%s\", \"puntos\":%d}",
                    c.getNit(), c.getNombre(), c.getPuntos()
                );
            } else {
                statusCode = 404;
                jsonResponse = "{\"status\":\"error\", \"message\":\"Cliente no encontrado o inactivo\"}";
            }

            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    public static void main(String[] args) {
        iniciarServidor();
    }
}
