package proyecto.progra2.conexion;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import proyecto.progra2.modelos.Cliente;
import proyecto.progra2.modelos.GestionClientes;

public class ServidorApi {

    private static final int PUERTO = 80;
    private static final GestionClientes gestionClientes = new GestionClientes();
    private static boolean isServidorIniciado = false;

    public static synchronized void iniciarServidor() {
        // Evita reabrir el puerto 80 al cerrar/reabrir sesión en la app Swing
        if (isServidorIniciado) {
            System.out.println("El Servidor API ya se encuentra activo en el puerto " + PUERTO + ".");
            return;
        }

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PUERTO), 0);
            
            // Registrar los endpoints de la API
            server.createContext("/api/puntos", new PuntosHandler());
            server.createContext("/api/canjear", new CanjearHandler());
            
            server.setExecutor(null);
            server.start();
            
            isServidorIniciado = true;
            System.out.println("API HTTP Servidor escuchando en el puerto " + PUERTO + "...");
        } catch (IOException e) {
            System.err.println("Error iniciando el servidor API: " + e.getMessage());
        }
    }

    // Handler para consultar puntos mediante GET /api/puntos?nit=XXX
    static class PuntosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            configurarCors(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            String nitBuscado = "";

            if (query != null && query.contains("nit=")) {
                nitBuscado = query.split("nit=")[1].split("&")[0].trim();
            }

            Cliente c = gestionClientes.buscarPorNit(nitBuscado);

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

            enviarRespuesta(exchange, statusCode, jsonResponse);
        }
    }

    // Handler para procesar el canje mediante POST /api/canjear
    static class CanjearHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            configurarCors(exchange);

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder body = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }

                String jsonBody = body.toString();
                String nit = extraerValorJson(jsonBody, "nit");
                int puntosADescontar = extraerIntJson(jsonBody, "puntos");

                boolean exito = gestionClientes.descontarPuntos(nit, puntosADescontar);

                String jsonResponse;
                int statusCode;

                if (exito) {
                    Cliente c = gestionClientes.buscarPorNit(nit);
                    // Generación del código único de cupón para el cajero
                    String codigoCanje = "PAN-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

                    statusCode = 200;
                    jsonResponse = String.format(
                        "{\"success\":true, \"message\":\"Canje realizado con exito\", \"codigo\":\"%s\", \"puntosRestantes\":%d}",
                        codigoCanje, (c != null ? c.getPuntos() : 0)
                    );
                } else {
                    statusCode = 400;
                    jsonResponse = "{\"success\":false, \"message\":\"Puntos insuficientes o cliente no encontrado\"}";
                }

                enviarRespuesta(exchange, statusCode, jsonResponse);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private static void configurarCors(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
    }

    private static void enviarRespuesta(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    private static String extraerValorJson(String json, String clave) {
        try {
            String patron = "\"" + clave + "\":";
            if (!json.contains(patron)) return "";
            int inicio = json.indexOf(patron) + patron.length();
            String sub = json.substring(inicio).trim();
            if (sub.startsWith("\"")) {
                return sub.substring(1, sub.indexOf("\"", 1));
            } else {
                int fin = sub.indexOf(",");
                if (fin == -1) fin = sub.indexOf("}");
                return sub.substring(0, fin).trim();
            }
        } catch (Exception e) {
            return "";
        }
    }

    private static int extraerIntJson(String json, String clave) {
        try {
            String valor = extraerValorJson(json, clave);
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        iniciarServidor();
    }
}