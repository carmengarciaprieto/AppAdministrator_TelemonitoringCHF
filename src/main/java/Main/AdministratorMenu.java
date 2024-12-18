package Main;

import ConnectionAdministrator.ConnectionAdministrator;
import Utilities.Encryption;
import Utilities.Utilities;
import pojos.Administrator;

import Swing.RoundedButton;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministratorMenu {

    private static String ipAddress;
    private static String dni;
    private static String password;
    private static String encryptedPassword;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Loop to get a valid IP address
                ipAddress = null;
                while (ipAddress == null || ipAddress.isEmpty() || !Utilities.valid_ipAddress(ipAddress)) {
                    if (ipAddress == null) {
                        ipAddress = JOptionPane.showInputDialog(null,
                                "Please enter a valid IP address:",
                                "IP Address Input",
                                JOptionPane.QUESTION_MESSAGE);
                    } else {
                        ipAddress = JOptionPane.showInputDialog(null,
                                "The IP address entered is invalid. Please try again:",
                                "IP Address Input",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    // Exit gracefully if the user cancels the input
                    if (ipAddress == null) {
                        JOptionPane.showMessageDialog(null, "No IP address entered. Exiting.");
                        System.exit(0);
                    }
                }

                // Attempt to connect to the server
                try {
                    ConnectionAdministrator.connectToServer(ipAddress);
                } catch (ConnectException e) {
                    JOptionPane.showMessageDialog(null,
                            "Unable to connect to the server. Please check if the server is running at the specified IP: " + ipAddress,
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AdministratorMenu.class.getName()).log(Level.WARNING,
                            "Failed to connect to the server at IP: " + ipAddress);
                    System.exit(0); // Exit gracefully
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Unexpected error during connection: " + e.getMessage(),
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AdministratorMenu.class.getName()).log(Level.SEVERE,
                            "Unexpected error occurred while connecting to the server", e);
                    System.exit(0); // Exit gracefully
                }

                // Launch the GUI if connection succeeds
                new AdministratorMenu().createAndShowGUI();
            } catch (Exception ex) {
                Logger.getLogger(AdministratorMenu.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Unexpected error. Exiting.");
                System.exit(1);
            }
        });
    }

    public void createAndShowGUI() {
    // Create the frame
    JFrame frame = new JFrame("Administrator Menu");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(750, 500);
    frame.setMinimumSize(new Dimension(400, 300));

    // Set layout
    frame.setLayout(new BorderLayout());

    // Create title label
    JLabel titleLabel = new JLabel("ADMINISTRATOR MENU", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Calibri", Font.BOLD, 40));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
    frame.add(titleLabel, BorderLayout.NORTH);

    // Create panel for buttons
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Adjusted to 4 rows for the Logout button
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    // Create buttons
    JButton registerButton = new JButton("Register");
    registerButton.setFont(new Font("Calibri", Font.PLAIN, 30));
    registerButton.setPreferredSize(new Dimension(300, 50));

    JButton loginButton = new JButton("Log In");
    loginButton.setFont(new Font("Calibri", Font.PLAIN, 30));
    loginButton.setPreferredSize(new Dimension(300, 50));

    JButton logoutButton = new JButton("Logout");
    logoutButton.setFont(new Font("Calibri", Font.PLAIN, 30));
    logoutButton.setPreferredSize(new Dimension(300, 50));

    JButton exitButton = new JButton("Exit");
    exitButton.setFont(new Font("Calibri", Font.PLAIN, 30));
    exitButton.setPreferredSize(new Dimension(300, 50));

    // Add buttons to panel
    buttonPanel.add(registerButton);
    buttonPanel.add(loginButton);
    buttonPanel.add(logoutButton);
    buttonPanel.add(exitButton);

    // Add button panel to frame
    frame.add(buttonPanel, BorderLayout.CENTER);

    // Set action listeners
    registerButton.addActionListener(e -> showRegistrationForm(frame));
    loginButton.addActionListener(e -> showLoginForm(frame));

    // Logout button logic
    logoutButton.addActionListener(e -> {
        ConnectionAdministrator.closeConnection(); // Call method to close user connection
        JOptionPane.showMessageDialog(frame, "Session closed successfully.");
    });

    exitButton.addActionListener(e -> {
        int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    });

    // Display the frame
    frame.setVisible(true);
}


    private void showRegistrationForm(JFrame parentFrame) {
        JDialog registrationDialog = new JDialog(parentFrame, "Administrator Registration", true);
        registrationDialog.setSize(400, 300);
        registrationDialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel dniLabel = new JLabel("      DNI:");
        dniLabel.setFont(new Font("Calibri", Font.PLAIN, 18));

        JTextField dniField = new JTextField();
        dniField.setFont(new Font("Calibri", Font.PLAIN, 16));

        JLabel passwordLabel = new JLabel("     Password:");
        passwordLabel.setFont(new Font("Calibri", Font.PLAIN, 18));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Calibri", Font.PLAIN, 16));

        JButton cancelButton = new RoundedButton("Cancel", Color.WHITE);
        JButton registerButton = new RoundedButton("Register", new Color(135, 206, 235));

        registrationDialog.add(dniLabel);
        registrationDialog.add(dniField);
        registrationDialog.add(passwordLabel);
        registrationDialog.add(passwordField);
        registrationDialog.add(new JLabel());
        registrationDialog.add(new JLabel());
        registrationDialog.add(cancelButton);
        registrationDialog.add(registerButton);

        registerButton.addActionListener(e -> {
            dni = dniField.getText().toUpperCase();
            if (!Utilities.validateDNI(dni)) {
                JOptionPane.showMessageDialog(registrationDialog, "Invalid DNI format.");
                return;
            }
            password = new String(passwordField.getPassword());
            encryptedPassword = Encryption.encryptPasswordMD5(password);
            Administrator administrator = new Administrator(dni, encryptedPassword);

            try {
                if (ConnectionAdministrator.sendRegisterServer(administrator)) {
                    JOptionPane.showMessageDialog(registrationDialog, "Administrator registered successfully.");
                    registrationDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(registrationDialog, "DNI already registered. Please log in.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(registrationDialog, "Unexpected error: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> registrationDialog.dispose());

        registrationDialog.setVisible(true);
    }

    private void showLoginForm(JFrame parentFrame) {
        JDialog loginDialog = new JDialog(parentFrame, "Administrator Log In", true);
        loginDialog.setSize(400, 300);
        loginDialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel dniLabel = new JLabel("      DNI:");
        dniLabel.setFont(new Font("Calibri", Font.PLAIN, 18)); // Set font to Calibri and size to 18

        JTextField dniField = new JTextField();
        dniField.setFont(new Font("Calibri", Font.PLAIN, 16)); // Set font to Calibri and size to 16

        JLabel passwordLabel = new JLabel("     Password:");
        passwordLabel.setFont(new Font("Calibri", Font.PLAIN, 18)); // Set font to Calibri and size to 18

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Calibri", Font.PLAIN, 16)); // Set font to Calibri and size to 16

        // Custom Rounded Button for "Log-In" and "Cancel"
        JButton cancelButton = new RoundedButton("Cancel", Color.WHITE);
        JButton loginButton = new RoundedButton("Log In", new Color(135, 206, 235)); // Sky blue

        loginDialog.add(dniLabel);
        loginDialog.add(dniField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(new JLabel()); // Spacer
        loginDialog.add(new JLabel()); // Spacer
        loginDialog.add(cancelButton);
        loginDialog.add(loginButton);

        loginButton.addActionListener(e -> {
            dni = dniField.getText().toUpperCase();
            if (!Utilities.validateDNI(dni)) {
                JOptionPane.showMessageDialog(loginDialog, "Invalid DNI.");
                return;
            }
            password = new String(passwordField.getPassword());
            encryptedPassword = Encryption.encryptPasswordMD5(password);

            try {
                if (ConnectionAdministrator.validateLogin(dni, encryptedPassword)) {
                    JOptionPane.showMessageDialog(loginDialog, "Log in successful.");
                    showAdministratorMenu(dni);
                    loginDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid DNI or password.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginDialog, "Error: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> loginDialog.dispose());

        loginDialog.setVisible(true);
    }

    private void showAdministratorMenu(String administratorDni) {
        JFrame adminFrame = new JFrame("Administrator Menu");
        adminFrame.setSize(400, 200);
        adminFrame.setLayout(new GridBagLayout()); // Use GridBagLayout for precise positioning
        GridBagConstraints gbc = new GridBagConstraints(); // Create a GridBagConstraints object for custom positioning

        // Create the label
        JLabel label = new JLabel("Close the server? (yes/no)", JLabel.CENTER);
        label.setFont(new Font("Calibri", Font.PLAIN, 18)); // Set font to Calibri and size to 18

        // Create the response field
        JTextField responseField = new JTextField();
        responseField.setFont(new Font("Calibri", Font.PLAIN, 16)); // Set font to Calibri and size to 16

        // Create the submit button (with custom rounded sky-blue color)
        JButton submitButton = new RoundedButton("Submit", new Color(135, 206, 235)); // Sky blue

        // Add the label to the top section (spans both columns)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around the label
        gbc.anchor = GridBagConstraints.CENTER; // Center the label in its cell
        adminFrame.add(label, gbc);

        // Add the response field (below the label, spans both columns)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across two columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make the field fill horizontally
        adminFrame.add(responseField, gbc);

        // Add the submit button (bottom-right corner)
        gbc.gridx = 1; // Place it in the second column
        gbc.gridy = 2; // Place it in the second row (bottom row)
        gbc.gridwidth = 1; // Use one cell for the button
        gbc.fill = GridBagConstraints.NONE; // Don't stretch the button
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Align it to the bottom-right corner
        adminFrame.add(submitButton, gbc);

        // Add action listener for the submit button
        submitButton.addActionListener(e -> {
            String response = responseField.getText().trim().toLowerCase();
            if ("yes".equals(response)) {
                try {
                    int connectedClients = ConnectionAdministrator.getNumberOfConnectedClients();
                    if (connectedClients <= 1) {
                        ConnectionAdministrator.closeServerApp();
                        JOptionPane.showMessageDialog(adminFrame, "Server shutting down.");
                        System.exit(0);
                    } else {
                        JOptionPane.showMessageDialog(adminFrame, "Cannot shut down. " + (connectedClients - 1) + " clients connected.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(adminFrame, "Error: " + ex.getMessage());
                }
            } else if ("no".equals(response)) {
                JOptionPane.showMessageDialog(adminFrame, "Server will continue running.");
                adminFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(adminFrame, "Invalid input. Type 'yes' or 'no'.");
            }
        });

        // Make the frame visible
        adminFrame.setVisible(true);
    }

}
