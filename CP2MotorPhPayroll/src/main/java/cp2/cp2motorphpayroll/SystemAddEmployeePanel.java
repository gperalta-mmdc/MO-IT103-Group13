package cp2.cp2motorphpayroll;

import javax.swing.*;
import java.awt.*;


public class SystemAddEmployeePanel {
    static Popup popup;
    public static void showEmployeeAddPanel(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setPreferredSize(new Dimension(500,500));
            panel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.NONE;
            gbc.insets = new Insets(5, 10, 5, 10);

            //Start User enterable fields
            JLabel labelEmployeeNumber = new JLabel("Employee Number");
            JTextField employeeNumber = new JTextField(8);

            JLabel labelFirstName = new JLabel("First Name");
            JTextField firstName = new JTextField(13);

            JLabel labelLastName = new JLabel("Last Name");
            JTextField lastName = new JTextField(13);

            JLabel labelBirthday = new JLabel("Birthday (MM/DD/YY)");
            JTextField birthday = new JTextField(13);

            JLabel labelSssNumber = new JLabel("SSS Number");
            JTextField sssNumber = new JTextField(12);

            JLabel labelPhilHealthNumber = new JLabel("PhilHealth Number");
            JTextField philHealthNumber = new JTextField(12);

            JLabel labelTinNumber = new JLabel("TIN Number");
            JTextField tinNumber = new JTextField(12);

            JLabel labelPagIbigNumber = new JLabel("Pag-IBIG Number");
            JTextField pagIbigNumber = new JTextField(12);


            JLabel labelPosition = new JLabel("Position");
            JTextField position = new JTextField(12);

            String[] employeeStatus = {"Status", "Regualar", "Probationary"};
            JLabel labelStatus = new JLabel("Status");
            JComboBox status = new JComboBox(employeeStatus);

            JLabel labelHourlyRate = new JLabel("Hourly rate");
            JTextField hourlyRate = new JTextField(6);
            //End user enterable fields

            //bottom bar
            JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
            statusBar.setFont(SystemGUIHelper.FONT_SMALL);
            statusBar.setForeground(Color.RED);
            statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                    SystemGUIHelper.COLOR_BORDER));
            statusBar.setPreferredSize(new Dimension(1107, 45));


            JButton cancelButton = SystemGUIHelper.makeButton("Cancel",
                    new Color(120, 120, 180));
            cancelButton.setPreferredSize(new Dimension(90, 28));

            statusBar.add(cancelButton);

            JButton confirmButton = SystemGUIHelper.makeButton("Confirm",
                    new Color(120, 120, 180));
            confirmButton.setPreferredSize(new Dimension(90, 28));
            statusBar.add(confirmButton);


            //gbc formatting
            gbc.gridx = 0;
            gbc.weightx = 0.3;
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridy = 0;
            panel.add(labelEmployeeNumber, gbc);
            gbc.gridy = 1;
            panel.add(employeeNumber, gbc);

            gbc.gridy = 2;
            panel.add(labelFirstName, gbc);
            gbc.gridy = 3;
            panel.add(firstName, gbc);

            gbc.gridy = 4;
            panel.add(labelLastName, gbc);
            gbc.gridy = 5;
            panel.add(lastName, gbc);

            gbc.gridy=6;
            panel.add(labelBirthday, gbc);
            gbc.gridy=7;
            panel.add(birthday, gbc);

            // middle
            gbc.gridx = 1;
            gbc.weightx = 0.3;
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridy = 0;
            panel.add(labelSssNumber, gbc);
            gbc.gridy = 1;
            panel.add(sssNumber, gbc);

            gbc.gridy = 2;
            panel.add(labelPhilHealthNumber, gbc);
            gbc.gridy = 3;
            panel.add(philHealthNumber, gbc);

            gbc.gridy = 4;
            panel.add(labelTinNumber, gbc);
            gbc.gridy = 5;
            panel.add(tinNumber, gbc);

            gbc.gridy=6;
            panel.add(labelPagIbigNumber, gbc);
            gbc.gridy=7;
            panel.add(pagIbigNumber, gbc);

            // right
            gbc.gridx = 2;
            gbc.weightx = 0.4;
            gbc.anchor = GridBagConstraints.WEST;

            gbc.gridy = 0;
            panel.add(labelPosition, gbc);
            gbc.gridy = 1;
            panel.add(position, gbc);

            gbc.gridy = 2;
            panel.add(labelStatus, gbc);
            gbc.gridy = 3;
            panel.add(status, gbc);

            gbc.gridy = 4;
            panel.add(labelHourlyRate, gbc);
            gbc.gridy = 5;
            panel.add(hourlyRate, gbc);

            gbc.gridy = 6;
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JPanel finalPanel = new JPanel(new BorderLayout());
            finalPanel.setPreferredSize(new Dimension(907, 500));
            finalPanel.add(panel);
            finalPanel.add(statusBar, BorderLayout.SOUTH);
            finalPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 2),       // Outer sharp edge
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)      // Inner padding
            ));

            JTextField[] entryFields = new JTextField[] {
                    employeeNumber,   // 0 Employee #
                    lastName,         // 1 Last Name
                    firstName,        // 2 First Name
                    birthday,         // 3 Birthday
                    sssNumber,        // 5 SSS #
                    philHealthNumber, // 6 Philhealth #
                    tinNumber,        // 7 TIN #
                    pagIbigNumber,    // 8 Pag-ibig #status,
                    // 9 Status removed
                    position,         // 10 Position
                    hourlyRate      // hourlyRate
            };

            boolean debugSetFields = true;
            if (debugSetFields){
                employeeNumber.setText("12345");
                firstName.setText("John");
                lastName.setText("John");
                birthday.setText("09/11/2001");
                sssNumber.setText("11-1111111-1");
                philHealthNumber.setText("123456789102");
                tinNumber.setText("123-123-123-123");
                status.setSelectedIndex(0);
                hourlyRate.setText("12345");
            }


            //Action Listeners
            cancelButton.addActionListener(e -> SystemGUIHelper.closePopup(popup));
            confirmButton.addActionListener(e -> {
                boolean isValid = true;
                if (!DataProcessing.onlyIntegers(employeeNumber,5)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid ID");
                }
                if (!DataProcessing.onlyLetters(lastName)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid First Name");
                }
                if (!DataProcessing.onlyLetters(firstName)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid Last Name");
                }
                //make case for validating dates
                if (!(DataProcessing.validateDate(birthday))) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid date format");
                }
                if (!DataProcessing.validateSSS(sssNumber)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid SSS number");
                }
                if (!DataProcessing.validatePhilHealth(philHealthNumber)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid PhilHealth number");
                }
                if (!DataProcessing.validateTIN(tinNumber)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid TIN number requires (xxx-xxx-xxx)");
                }if (!DataProcessing.validatePagIbig(pagIbigNumber)) {
                    isValid = false;
                }
                if (!DataProcessing.onlyLetters(position)) {
                    isValid = false;
                }
                if (status.getSelectedItem() == "Status"){
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Please select a status");
                }
                if (!DataProcessing.onlyIntegers(hourlyRate, 0)) {
                    isValid = false;
                    SystemGUIHelper.showError(frame, "Invalid hourly rate");
                }

                if(isValid){
                    String[] tempName = DataProcessing.addEmployeeToRecords(entryFields);
                    DataProcessing.saveEmployeeToCSV(tempName);
//                    SystemGUIHelper.closePopup(popup);
                    SystemEmployeePanel.refreshTable();

                }
            });


            popup = SystemGUIHelper.makePopup(frame, finalPanel, 50, 50);
        });

    }
}
