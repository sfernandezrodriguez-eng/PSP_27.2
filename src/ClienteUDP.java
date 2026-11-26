import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        InetAddress servidor = InetAddress.getByName("localhost");
        int puerto = 5555;

        try (DatagramSocket socket = new DatagramSocket();
             Scanner entrada = new Scanner(System.in)) {

            socket.setSoTimeout(2000);

            while (true) {
                System.out.print("Dos palabras separadas por coma (o 'salir' para terminar): ");
                String texto = entrada.nextLine();

                if (texto.equalsIgnoreCase("salir")) {
                    break;
                }

                byte[] datos = texto.getBytes(StandardCharsets.UTF_8);
                DatagramPacket paqueteEnvio = new DatagramPacket(datos, datos.length, servidor, puerto);
                socket.send(paqueteEnvio);

                byte[] buffer = new byte[1024];
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(respuesta);
                    String mensajeRespuesta = new String(
                            respuesta.getData(),
                            0,
                            respuesta.getLength(),
                            StandardCharsets.UTF_8
                    );
                    System.out.println("Respuesta: " + mensajeRespuesta);
                } catch (SocketTimeoutException e) {
                    System.out.println("El servidor no respondió a tiempo. Asegúrate de que ServidorUDP esté ejecutándose.");
                }
            }
        }
    }
}