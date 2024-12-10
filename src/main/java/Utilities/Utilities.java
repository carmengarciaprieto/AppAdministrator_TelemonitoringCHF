package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.Scanner;
import pojos.Administrator;

public class Utilities {

    private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

    // Read console inputs 
    public static int readInteger() {
        int num = 0;
        boolean ok = false;
        do {
            System.out.println("Introduce a number: ");

            try {
                num = Integer.parseInt(r.readLine());
                if (num < 0) {
                    ok = false;
                    System.out.println("You didn't type a valid number.");
                } else {
                    ok = true;
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (NumberFormatException nfe) {
                System.out.println("You didn't type a valid number!");
            }
        } while (!ok);

        return num;
    }

    public static String readString() {
        String text = null;
        boolean ok = false;
        do {
            try {
                text = r.readLine();
                if (!text.isEmpty()) {
                    ok = true;
                } else {
                    System.out.println("Empty string, please try again:");
                }
            } catch (IOException e) {

            }
        } while (!ok);

        return text;
    }

    // Validates the format of the DNI
    public static boolean validateDNI(String id) {

        boolean ok = true;

        if (id.length() != 9) {
            ok = false;

            return ok;
        }

        for (int i = 0; i < 8; i++) {
            if (!Character.isDigit(id.charAt(i))) {
                ok = false;
                return ok;
            }
        }
        String num = id.substring(0, 8);

        String validLeters = "TRWAGMYFPDXBNJZSQVHLCKE";
        int indexLeter = Integer.parseInt(num) % 23;
        char valid = validLeters.charAt(indexLeter);

        if (id.toUpperCase().charAt(8) != valid) {
            ok = false;
            return ok;
        }

        return ok;
    }

    public static boolean validateEmail(String email) {

        String emailpattern = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Check if the email matches the pattern
        return email != null && email.matches(emailpattern);
    }

// Method to display a menu and return the selected option
    public static int displayMenu(String title, String[] options) {
        System.out.println(title);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("%d. %s\n", i + 1, options[i]);
        }
        System.out.println("Choose an option: ");
        return getValidInput(0, options.length );
    }

    // Method to display a list of objects with a menu
    public static <T> int displayListWithMenu(List<T> list, String title, String backOption) {
        System.out.println("\n" + title);
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, list.get(i).toString());
        }
        System.out.println("0. " + backOption);
        System.out.println("Choose an option: ");
        return getValidInput(0, list.size());
    }

    // Method to validate input within a range
    public static int getValidInput(int min, int max) {
        while (true) {
            try {
                String input = r.readLine(); // Read input as a string
                int choice = Integer.parseInt(input); // Convert to integer
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (IOException | NumberFormatException e) {
                // Ignore and prompt again
            }
            System.out.println("Invalid input. Please try again: ");
        }

    }
    public static boolean valid_ipAddress(String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false; // Dirección IP vacía o nula no es válida
        }

        // Validar el formato de IPv4
        if (!isValidFormat(ipAddress)) {
            return false; // El formato no es válido
        }

        // Verificar si la IP responde en la red
        return isReachable(ipAddress);
    }

    // Validar formato IPv4
    private static boolean isValidFormat(String ipAddress) {
        String IPv4_PATTERN
                = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$";
        return ipAddress.matches(IPv4_PATTERN);
    }

    // Verificar si la dirección IP responde (Ping)
    private static boolean isReachable(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.isReachable(3000); // Esperar 3000 ms (3 segundos)
        } catch (IOException e) {
            return false; // Error al intentar alcanzar la IP
        }
    }

     public static String getValidIPAddress() {
        Scanner scanner = new Scanner(System.in);
        String ipAddress;

        while (true) {
            System.out.println("\nPlease, introduce a valid IP address: ");
            ipAddress = scanner.nextLine();

            if (valid_ipAddress(ipAddress)) {
                System.out.println("\nValid IP address: " + ipAddress);
                break;
            } else {
                System.out.println("IP address is not valid or not working. Try again.");
            }
        }

        return ipAddress; // Devuelve la dirección IP válida
    }

}