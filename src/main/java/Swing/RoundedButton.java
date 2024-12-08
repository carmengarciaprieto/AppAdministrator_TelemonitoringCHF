/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Swing;

/**
 *
 * @author mariagorgojo
 */
import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private final Color backgroundColor;

    public RoundedButton(String text, Color backgroundColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        setOpaque(false); // Makes sure the custom painting is visible
        setContentAreaFilled(false); // Disables default button background
        setFocusPainted(false); // Removes focus indicator
        setBorderPainted(false); // Removes border
        setForeground(Color.BLACK); // Sets text color to white for contrast
        setFont(new Font("Calibri", Font.PLAIN, 18)); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Anti-aliasing for smooth edges

        // Draw the rounded rectangle with the background color
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corners (30px radius)

        // Draw the button text
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        // Do nothing to avoid default border painting
    }
}

