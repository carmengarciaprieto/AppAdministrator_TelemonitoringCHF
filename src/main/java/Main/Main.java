package Main;

import ConnectionAdministrator.ConnectionAdministrator;
import Utilities.Utilities;
import java.io.IOException;
import pojos.Administrator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("=== Main Menu ===");
            System.out.println("1. Register as Administrator");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume the newline
            String dni;
            String password;

            switch (option) {
                case 1:
                    // Register a new administrator
                    System.out.println("=== Administrator Registration ===");
                    System.out.print("Enter your DNI: ");
                    dni = scanner.nextLine();
                    System.out.print("Enter your Password: ");
                    password = scanner.nextLine();

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
                        dni = scanner.nextLine();
                        dni = dni.toUpperCase();

                        if (!Utilities.validateDNI(dni)) {
                            System.out.println("Invalid DNI. Please try again.");
                        }
                    } while (!Utilities.validateDNI(dni)); // Repite hasta que el formato sea correcto

                    System.out.println("Enter password: ");
                    password = scanner.nextLine();

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
                        System.out.println("ERROR:" +e);
                    }
                    break;

                case 3:
                    System.out.println("Exiting the program...");
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    private static void administratorMenu(String administratorDni) throws IOException  {
        System.out.println("Do you want to close the Server App? Type yes or no");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();
        System.out.println(response);
        if ("yes".equals(response)) {
            ConnectionAdministrator.closeServerApp();
        } else if ("no".equals(response)) {
            System.out.println("The Server App will continue running.");
        } else {
            System.out.println("Invalid input. Please type 'yes' or 'no'.");
            administratorMenu(administratorDni);
        }
    }
}
