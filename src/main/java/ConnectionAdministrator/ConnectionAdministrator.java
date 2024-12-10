package ConnectionAdministrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pojos.Administrator;

public class ConnectionAdministrator {

    private static Socket socket;
    private static PrintWriter printWriter;
    private static BufferedReader bufferedReader;

    public static void connectToServer(String ip_address) throws IOException {
        if (socket == null || socket.isClosed()) {
            System.out.println("Connecting to server...");
            socket = new Socket(ip_address, 9090); // cambiar mas adelante 
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    public static void closeConnection() {
        try {

            printWriter.println("LOGOUT");

            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionAdministrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean sendRegisterServer(Administrator administrator) {
        try {
            printWriter.println("REGISTER_ADMINISTRATOR");
            printWriter.println(administrator.getDni());
            printWriter.println(administrator.getPassword());

            String serverResponse = bufferedReader.readLine();
            if ("VALID".equals(serverResponse)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            Logger.getLogger(ConnectionAdministrator.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public static boolean validateLogin(String dni, String password) {
        try {
            printWriter.println("LOGIN_ADMINISTRATOR");
            printWriter.println(dni);
            printWriter.println(password);

            String serverResponse = bufferedReader.readLine();
            if ("VALID".equals(serverResponse)) {
                System.out.println("Login successful!");
                return true;
            } else {
                System.out.println("Invalid credentials.");
                return false;
            }

        } catch (IOException e) {
            Logger.getLogger(ConnectionAdministrator.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public static int getNumberOfConnectedClients() {
        try {
            printWriter.println("GET_CLIENTS_CONNECTED");
            String response = bufferedReader.readLine();
            return Integer.parseInt(response); // El servidor debe responder con un entero
        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(ConnectionAdministrator.class.getName()).log(Level.SEVERE, null, e);
            return -1;
        }
    }

    public static void closeServerApp() throws IOException {
        printWriter.println("SHUTDOWN");
        System.out.println("Shutdown request sent to the server.");
    }

}
