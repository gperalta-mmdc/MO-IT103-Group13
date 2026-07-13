package cp2.cp2motorphpayroll;

import javax.swing.*;
import java.awt.*;

public class SystemGUIHelper {
    // COLOR PALETTE
    static final Color COLOR_BG        = new Color(245, 245, 245);
    static final Color COLOR_PRIMARY   = new Color(0, 9, 140);
    static final Color COLOR_SECONDARY = new Color(30,  30,  30);
    static final Color COLOR_PANEL     = Color.WHITE;
    static final Color COLOR_BORDER    = new Color(210, 210, 210);
    static final Color COLOR_ERROR     = new Color(200, 30,  30);
    static final Color COLOR_SUCCESS   = new Color(30,  140, 70);
    static final Color COLOR_BTN_TEXT  = Color.WHITE;
    static final Color COLOR_FIELD_BG  = new Color(250, 250, 250);
    static final Color COLOR_REQFIELD  = new Color(0, 0, 139);

    // FONTS
    static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD,  20);
    static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    static final Font FONT_BTN   = new Font("Segoe UI", Font.BOLD,  13);
    static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 12);

    // UI HELPERS
    /*Creates a styled primary action button*/
    static JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(bg);
        btn.setForeground(COLOR_BTN_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /*Creates a styled text input field*/
    public static JTextField makeField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(FONT_LABEL);
        f.setBackground(COLOR_FIELD_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }

    /*Show an error JOptionPane dialog*/
    static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message,
                "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    /*Shows an information Success JOptionPaneDialog*/
    static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /*Show a warning JOptionPane Dialog*/
    static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message,
                "Warning", JOptionPane.WARNING_MESSAGE);
    }

    /*Builds a reusable header bar with title and logout button*/
    static JPanel buildHeader(String title, JFrame frame,
                              int width, Runnable onLogout) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(width, 52));
        header.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(Color.WHITE);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(FONT_SMALL);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(0, 9, 75));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
           
            //TODO: ADD A CHECK IF YOU MADE OR EDITED NEW RECORDS TO SAVE BEFORE YOU LOGOUT
            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to logout?",
                    "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
                if (onLogout != null) onLogout.run();
            }
        });

        header.add(titleLbl,  BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);
        return header;
    }

    // MAKE POPUP
   static JDialog makePopup(Component parent, Component contents,
                              int widthOffset, int heightOffset) {
        JFrame ownerFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(ownerFrame, true); // true = modal
        dialog.setUndecorated(true); // no OS title bar/border
        dialog.getContentPane().add(contents);
        dialog.pack();

        Point frameLocation = parent.getLocationOnScreen();
        dialog.setLocation(
            (int) frameLocation.getX() + widthOffset,
            (int) frameLocation.getY() + heightOffset);

        dialog.setVisible(true); // blocks here until closed (modal)
        return dialog;
    }

    // CLOSE POPUP — disposes the JDialog
    static void closePopup(JDialog popup) {
        if (popup != null) {
            popup.dispose();
        }
    }
    
    // Creates a styled text field with gray placeholder hint text.
    public static JTextField makeFieldWithHint(int cols, String hint) {
        JTextField f = new JTextField(cols) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(hint, ins.left + 2,
                        getHeight() / 2 + getFont().getSize() / 2 - 2);
                    g2.dispose();
                }
            }
        };
        f.setFont(FONT_LABEL);
        f.setBackground(COLOR_FIELD_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return f;
    }
}
