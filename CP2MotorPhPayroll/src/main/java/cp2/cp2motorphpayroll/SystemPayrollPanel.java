package cp2.cp2motorphpayroll;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;


public class SystemPayrollPanel {

    static JFrame            frame;
    static DefaultTableModel tableModel;
    static JLabel            statusBar;

    static void show() {

        // FRAME
        frame = new JFrame("MotorPh - Payroll Staff Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1275, 660);
        frame.setLocationRelativeTo(null);
        frame.getContentPane()
             .setBackground(SystemGUIHelper.COLOR_BG);

        // HEADER
        JPanel header = SystemGUIHelper.buildHeader(
            "Payroll Staff Portal",
            frame,
            1000,
            () -> SystemLogInPanel.showLogin()
        );

        // CONTROLS PANEL
        JPanel controls = new JPanel(new GridBagLayout());
        controls.setBackground(SystemGUIHelper.COLOR_PANEL);
        controls.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(
                0, 0, 1, 0, SystemGUIHelper.COLOR_BORDER),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.anchor = GridBagConstraints.WEST;
        c.fill   = GridBagConstraints.NONE;

        int currentYear = LocalDate.now().getYear();

        // ROW 0 - YEAR
        c.gridx = 0; c.gridy = 0;

        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(yearLbl, c);

        c.gridx = 1;

        SpinnerNumberModel yearModel =
            new SpinnerNumberModel(currentYear, 2020, currentYear, 1);
        JSpinner yearSpinner = new JSpinner(yearModel);
        yearSpinner.setFont(SystemGUIHelper.FONT_LABEL);
        yearSpinner.setPreferredSize(new Dimension(80, 28));
        yearSpinner.setToolTipText(
            "Select year 2020 – " + currentYear);
        controls.add(yearSpinner, c);
        
        // Remove comma formatting from year display
        JSpinner.NumberEditor yearEditor =
            new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(yearEditor);


        // ROW 0 - MONTH
        c.gridx = 2;

        JLabel monthLbl = new JLabel("Month:");
        monthLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(monthLbl, c);

        c.gridx = 3;

        String[] months = {
            "All Months", "January",  "February",
            "March",      "April",    "May",
            "June",       "July",     "August",
            "September",  "October",  "November",
            "December"
        };

        JComboBox<String> monthCombo =
            new JComboBox<>(months);
        monthCombo.setFont(SystemGUIHelper.FONT_LABEL);
        monthCombo.setPreferredSize(new Dimension(130, 28));
        monthCombo.setBackground(
                SystemGUIHelper.COLOR_FIELD_BG);
        controls.add(monthCombo, c);

        // ROW 0 - RADIO BUTTONS
        c.gridx = 4;

        JLabel scopeLbl = new JLabel("Process:");
        scopeLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(scopeLbl, c);

        JRadioButton oneEmpBtn =
            new JRadioButton("One Employee");
        JRadioButton allEmpBtn =
            new JRadioButton("All Employees");

        oneEmpBtn.setFont(SystemGUIHelper.FONT_LABEL);
        allEmpBtn.setFont(SystemGUIHelper.FONT_LABEL);
        oneEmpBtn.setBackground(SystemGUIHelper.COLOR_PANEL);
        allEmpBtn.setBackground(SystemGUIHelper.COLOR_PANEL);
        oneEmpBtn.setSelected(true);

        ButtonGroup scopeGroup = new ButtonGroup();
        scopeGroup.add(oneEmpBtn);
        scopeGroup.add(allEmpBtn);

        c.gridx = 5; controls.add(oneEmpBtn, c);
        c.gridx = 6; controls.add(allEmpBtn, c);


        // ROW 1 - EMPLOYEE NUMBER
        c.gridy = 1; c.gridx = 0;

        JLabel empNumLbl = new JLabel("Employee #:");
        empNumLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(empNumLbl, c);

        c.gridx = 1;

        JTextField empNumField =
                SystemGUIHelper.makeField(10);
        empNumField.setPreferredSize(new Dimension(110, 28));
        controls.add(empNumField, c);

        // ROW 1 - EMPLOYEE NAME
        c.gridx = 2;

        JLabel empNameLbl = new JLabel("Employee Name:");
        empNameLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(empNameLbl, c);

        c.gridx = 3;

        JTextField empNameField =
                SystemGUIHelper.makeField(14);
        empNameField.setEditable(false);
        empNameField.setPreferredSize(new Dimension(160, 28));
        empNameField.setBackground(new Color(238, 238, 238));
        controls.add(empNameField, c);

        // ROW 1 - PAY COVERAGE
        c.gridx = 4;

        JLabel payCovLbl = new JLabel("Pay Coverage:");
        payCovLbl.setFont(SystemGUIHelper.FONT_SMALL);
        controls.add(payCovLbl, c);

        c.gridx = 5; c.gridwidth = 2;

        JTextField payCovField =
                SystemGUIHelper.makeField(18);
        payCovField.setPreferredSize(new Dimension(200, 28));
        controls.add(payCovField, c);

        c.gridwidth = 1;

 
        // ROW 2 - BUTTONS
        c.gridy = 2; c.gridx = 0; c.gridwidth = 2;

        JButton summaryBtn = SystemGUIHelper.makeButton(
            "Generate Summary",
            new Color(30, 80, 160));
        summaryBtn.setPreferredSize(new Dimension(180, 32));
        controls.add(summaryBtn, c);

        c.gridx = 4; c.gridwidth = 3;

        JLabel hint = new JLabel(
            "                          * Pay Coverage: e.g. 2024-01-01 to 2024-01-31");
        hint.setFont(SystemGUIHelper.FONT_SMALL);
        hint.setForeground(Color.GRAY);
        controls.add(hint, c);

        c.gridwidth = 3;

  
        // TABLE
        String[] cols = {
            "Emp #", "Last Name", "First Name", "Month", "Cutoff",
            "Hours", "Gross", "SSS", "PhilHealth",
            "Pag-IBIG", "Tax", "Total Deductions", "Net Pay"
        };

        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(SystemGUIHelper.FONT_TABLE);
        table.setRowHeight(24);
        table.setGridColor(SystemGUIHelper.COLOR_BORDER);
        table.setSelectionBackground(new Color(200, 210, 255));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getTableHeader().setFont(
                SystemGUIHelper.FONT_BOLD);
        table.getTableHeader().setBackground(
            new Color(240, 240, 240));
        table.getTableHeader().setForeground(
                SystemGUIHelper.COLOR_SECONDARY);

        // Column widths
        int[] colWidths = {
            60, 150, 150, 90, 110,
            60, 90,  70, 90,
            80, 90,  130, 90
        };
        for (int i = 0; i < colWidths.length; i++)
            table.getColumnModel()
                 .getColumn(i)
                 .setPreferredWidth(colWidths[i]);

        // Right-align numeric columns (Hours onward)
        DefaultTableCellRenderer right =
            new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i = 4; i < cols.length; i++)
            table.getColumnModel()
                 .getColumn(i)
                 .setCellRenderer(right);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                    SystemGUIHelper.COLOR_BORDER));



        // STATUS BAR
        statusBar = new JLabel("  Ready.");
        statusBar.setFont(SystemGUIHelper.FONT_SMALL);
        statusBar.setForeground(Color.GRAY);
        statusBar.setOpaque(true);
        statusBar.setBackground(SystemGUIHelper.COLOR_PANEL);
        statusBar.setBorder(
            BorderFactory.createMatteBorder(
                1, 0, 0, 0,
                    SystemGUIHelper.COLOR_BORDER));
        statusBar.setPreferredSize(new Dimension(1000, 24));

        // MAIN CONTENT PANEL
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(SystemGUIHelper.COLOR_BG);
        content.add(controls,   BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(statusBar,  BorderLayout.SOUTH);

        // FRAME LAYOUT
        frame.setLayout(new BorderLayout());
        frame.add(header,  BorderLayout.NORTH);
        frame.add(content, BorderLayout.CENTER);
        frame.setVisible(true);

        // EVENT - radio buttons toggle emp# field
        oneEmpBtn.addActionListener(e -> {
            empNumField.setEnabled(true);
            empNameField.setText("");
        });

        allEmpBtn.addActionListener(e -> {
            empNumField.setEnabled(false);
            empNumField.setText("");
            empNameField.setText("");
        });

 
        // EVENT - autofill name when emp# field loses focus
        empNumField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String num = empNumField.getText().trim();
                if (!num.isEmpty() && num.matches("\\d+")) {
                    String[] data =
                        EntryPoint.employeeMap.get(num);
                    empNameField.setText(data != null
                        ? DataProcessing.safeGet(data, 1)
                          + ", "
                          + DataProcessing.safeGet(data, 2)
                        : "Not found");
                } else {
                    empNameField.setText("");
                }
            }
        });

        // EVENT - Generate Summary button
        summaryBtn.addActionListener(e -> {
            if (EntryPoint.employeeMap.isEmpty()) {
                SystemGUIHelper.showError(frame,
                    "Employee data is not loaded.\n"
                    + "Please verify the CSV file exists.");
                return;
            }
            SystemGUIHelper.showInfo(frame,
                "Payroll Summary Generated.");
        });
    }
}