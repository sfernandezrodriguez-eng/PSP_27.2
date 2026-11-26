import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ServidorUDP {
    public static void main(String[] args){
        try (DatagramSocket socket = new DatagramSocket(5555)) {
            System.out.println("Servidor UDP iniciado en el puerto 5555");
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(recibido);
                String texto = new String(recibido.getData(), 0,
                        recibido.getLength(), StandardCharsets.UTF_8);
                System.out.println(recibido.getAddress() + ":" + recibido.getPort() + " -> " + texto);
                String respuesta = compararTexto(texto);
                byte[] resp = respuesta.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(resp, resp.length,
                        recibido.getAddress(), recibido.getPort()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String compararTexto(String texto) {
        String[] palabras = texto.split(",");

        if (palabras.length < 2) {
            return "Formato incorrecto. Envía al menos dos palabras separadas por comas.";
        }

        String masLarga = "";
        boolean hayEmpate = false;

        for (String palabra : palabras) {
            palabra = palabra.trim();
            if (palabra.isEmpty()) {
                return "Formato incorrecto. Envía palabras separadas por comas.";
            }

            if (palabra.length() > masLarga.length()) {
                masLarga = palabra;
                hayEmpate = false;
            } else if (palabra.length() == masLarga.length()) {
                hayEmpate = true;
            }
        }

        if (hayEmpate) {
            return "Tienen la misma longitud (" + masLarga.length() + " caracteres).";
        }

        return "La palabra más larga es \"" + masLarga
                + "\" y tiene " + masLarga.length() + " caracteres.";
    }
}