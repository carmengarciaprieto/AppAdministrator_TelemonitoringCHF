package Main;

import ConnectionAdministrator.ConnectionAdministrator;
import Utilities.Encryption;
import Utilities.Utilities;
import java.io.IOException;
import pojos.Administrator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministratorMenu {

    public static void main(String[] args) {
        String ip_address_valid = null;
        try {

            ip_address_valid = Utilities.getValidIPAddress();
            try {
                ConnectionAdministrator.connectToServer(ip_address_valid);
            } catch (IOException ex) {
                Logger.getLogger(AdministratorMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            mainMenu();
        } finally {
            ConnectionAdministrator.closeConnection(); // Cierra la conexión al finalizar
        }
    }

    public static void mainMenu() {
        boolean exit = false;
        String encryptedPassword;

        while (!exit) {
            System.out.println("=== Main Menu ===");
            System.out.println("1. Register as Administrator");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.println("Select an option: ");
            int option = Utilities.readInteger();

            String dni;
            String password;

            switch (option) {
                case 1:
                    // Register a new administrator
                    System.out.println("=== Administrator Registration ===");
                    System.out.println("Enter your DNI: ");
                    dni = Utilities.readString();
                    System.out.println("Enter your Password: ");
                    password = Utilities.readString();
                    encryptedPassword = Encryption.encryptPasswordMD5(password);

                    Administrator administrator = new Administrator(dni, password);

                    try {
                        if (ConnectionAdministrator.sendRegisterServer(administrator)) {
                            System.out.println("User registered successfully with DNI: " + dni);
                        } else {
                            System.out.println("DNI : " + dni + " is already registered. Try to login to access your account.");
                        }
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred.");
                        //System.out.println(e);
                    }
                    break;

                case 2:
                    do {
                        System.out.println("\nEnter DNI: ");
                        dni = Utilities.readString();
                        dni = dni.toUpperCase();

                        if (!Utilities.validateDNI(dni)) {
                            System.out.println("Invalid DNI. Please try again.");
                        }
                    } while (!Utilities.validateDNI(dni)); // Repite hasta que el formato sea correcto

                    System.out.println("Enter password: ");
                    password = Utilities.readString();
                    encryptedPassword = Encryption.encryptPasswordMD5(password);

                    try {
                        // Valida login
                        if (ConnectionAdministrator.validateLogin(dni, password)) {
                            System.out.println("\nDoctor login successful!");
                            // loginSuccess = true; 
                            administratorMenu(dni); // Redirige al menú del doctor
                        } else {
                            System.out.println("ERROR. Make sure you entered your DNI and password correctly.");
                            System.out.println("If you're not registered, please do it first. \n");
                        }

                    } catch (IOException e) {
                        System.out.println("ERROR:" + e);
                    }
                    break;

                case 3:
                    System.out.println("Exiting the program...");
                    ConnectionAdministrator.closeConnection();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

    }

    private static void administratorMenu(String administratorDni) throws IOException {
        System.out.println("Do you want to close the Server App? Type yes or no");

        String response = Utilities.readString().toLowerCase();

        if ("yes".equals(response)) {
            int connectedClients = ConnectionAdministrator.getNumberOfConnectedClients();
            if (connectedClients <= 1) {
                ConnectionAdministrator.closeServerApp();
                System.out.println("The Server App is shutting down...");
            } else {
                System.out.println("Cannot shut down the Server App. There are " + connectedClients + " clients still connected.");
            }
        } else if ("no".equals(response)) {
            System.out.println("The Server App will continue running.");
        } else {
            System.out.println("Invalid input. Please type 'yes' or 'no'.");
            administratorMenu(administratorDni);
        }
    }
}