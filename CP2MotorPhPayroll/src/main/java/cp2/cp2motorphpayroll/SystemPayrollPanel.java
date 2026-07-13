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


        // ROW 2 - GENERATE PAYROLL
        c.gridy = 1; c.gridx = 4; c.gridwidth = 2;

        JButton summaryBtn = SystemGUIHelper.makeButton(
                "Compute Salaries",
                new Color(30, 80, 160));
        summaryBtn.setPreferredSize(new Dimension(180, 32));
        controls.add(summaryBtn, c);
        
        

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


        // EVENT - auto-fill name when emp# field loses focus
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

            int selectedYear = (int) yearSpinner.getValue();
            String selectedMonth = (String) monthCombo.getSelectedItem();

            String[] monthNames = {
                    "All Months","January","February","March","April","May","June",
                    "July","August","September","October","November","December"
            };
            int monthIndex = -1;
            for (int i = 0; i < monthNames.length; i++) {
                if (monthNames[i].equals(selectedMonth)) { monthIndex = i; break; }
            }
            if (monthIndex == -1) {
                SystemGUIHelper.showError(frame, "Please select a month.");
                return;
            }

            tableModel.setRowCount(0);

            // MONTHY FILTERS
            java.util.List<Integer> monthsToProcess = new java.util.ArrayList<>();
            if (monthIndex == 0) {
                for (int m = 1; m <= 12; m++) monthsToProcess.add(m);
            } else {
                monthsToProcess.add(monthIndex);
            }

            if (oneEmpBtn.isSelected()) {
                String empNum = empNumField.getText().trim();

                // Safeguard: employee number must not be empty
                if (empNum.isEmpty()) {
                    SystemGUIHelper.showError(frame, "Please enter an Employee #.");
                    return;
                }

                // Safeguard: employee number must be numeric
                if (!empNum.matches("\\d+")) {
                    SystemGUIHelper.showError(frame,
                            "Employee number must contain digits only.\nExample: 10001");
                    empNumField.requestFocus();
                    return;
                }

                // Safeguard: employee number must exist in loaded data
                if (!EntryPoint.employeeMap.containsKey(empNum)) {
                    SystemGUIHelper.showError(frame,
                            "Employee # \"" + empNum + "\" not found.\n"
                                    + "Please check the number and try again.");
                    empNumField.requestFocus();
                    return;
                }

                for (int m : monthsToProcess) {
                    String yearMonth = selectedYear + "-" + String.format("%02d", m);
                    addPayrollRows(empNum, yearMonth, selectedYear);
                }
            } else {
                java.util.List<String> keys =
                        new java.util.ArrayList<>(EntryPoint.employeeMap.keySet());
                java.util.Collections.sort(keys);
                for (String key : keys) {
                    for (int m : monthsToProcess) {
                        String yearMonth = selectedYear + "-" + String.format("%02d", m);
                        addPayrollRows(key, yearMonth, selectedYear);
                    }
                }
            }

           int rows = tableModel.getRowCount();
            statusBar.setText("  " + rows + " row(s) generated for "
                    + selectedMonth + " " + selectedYear + ".");

            if (rows == 0) {
                SystemGUIHelper.showWarning(frame,
                        "No attendance records found for "
                                + selectedMonth + " " + selectedYear + ".");
            } else {
                SystemGUIHelper.showInfo(frame,
                        rows + " row(s) generated successfully for "
                                + selectedMonth + " " + selectedYear + ".");
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════
    //  PAYROLL HELPERS
    // ════════════════════════════════════════════════════════════════════

    private static void addPayrollRows(String empNum, String yearMonth, int selectedYear) {
        String[] data = EntryPoint.employeeMap.get(empNum);
        if (data == null) return;

        String lastName    = DataProcessing.safeGet(data, 1);
        String firstName   = DataProcessing.safeGet(data, 2);
        double hourlyRate  = parseDouble(DataProcessing.safeGet(data, 16));

        if (hourlyRate == 0)
            System.out.println("[Warning] Employee " + empNum
                    + " has a missing or invalid Hourly Rate in the CSV.");

        // Compute hours for both cutoffs
        double hours1st = computeHoursWorked(empNum, yearMonth, 1);
        double hours2nd = computeHoursWorked(empNum, yearMonth, 2);

        // Skip this month entirely if no attendance at all
        if (hours1st == 0 && hours2nd == 0) return;

        // Gross pay - modular method, array-based parameters
        double gross1st = computeGrossPay(new double[]{ hours1st, hourlyRate });
        double gross2nd = computeGrossPay(new double[]{ hours2nd, hourlyRate });

        // Combined monthly gross - this is the basis for deductions
        double totalMonthlyGross = gross1st + gross2nd;

        // Deductions - each computed independently, then summed 
        double[] deductions = computeDeductions(new double[]{ totalMonthlyGross });
        double sss        = deductions[0];
        double philHealth = deductions[1];
        double pagIbig     = deductions[2];
        double tax         = deductions[3];
        double totalDed    = deductions[4]; 

        // Net pay - modular method 
        double netPay2nd = computeNetPay(new double[]{ gross2nd, totalDed });
        if (netPay2nd < 0) netPay2nd = 0; 

        String[] ym = yearMonth.split("-");
        int ymYear  = Integer.parseInt(ym[0]);
        int ymMonth = Integer.parseInt(ym[1]);

        String monthDisplay = java.time.Month.of(ymMonth)
                .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)
                + " " + ym[0];

        
        // Get the actual last day of this month (handles leap years correctly)
        int lastDay = java.time.YearMonth.of(ymYear, ymMonth).lengthOfMonth();
        String monthEnd = "2nd (16–" + lastDay + ")";

        
        // 1st cutoff row - gross pay only, no deductions
        tableModel.addRow(new Object[]{
                empNum, lastName, firstName, monthDisplay,
                "1st (1–15)",
                String.format("%.2f", hours1st),
                String.format("%.2f", gross1st),
                "—", "—", "—", "—", "—",
                String.format("%.2f", gross1st)
        });

        // 2nd cutoff row - deductions applied here
        tableModel.addRow(new Object[]{
                empNum, lastName, firstName, monthDisplay,
                monthEnd,
                String.format("%.2f", hours2nd),
                String.format("%.2f", gross2nd),
                String.format("%.2f", sss),
                String.format("%.2f", philHealth),
                String.format("%.2f", pagIbig),
                String.format("%.2f", tax),
                String.format("%.2f", totalDed),
                String.format("%.2f", netPay2nd)
        });
    }

    private static double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(",", "")); }
        catch (Exception e) {
            System.out.println("[Warning] Could not parse numeric value: \""
                    + s + "\" — defaulting to 0.");
            return 0;
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  SALARY COMPUTATION
    // ════════════════════════════════════════════════════════════════════

    static final double PHILHEALTH_RATE = 0.03;

    // GROSS PAY 
    private static double computeGrossPay(double[] params) {
        double hoursWorked = params[0];
        double hourlyRate  = params[1];
        return hoursWorked * hourlyRate;
    }

    // SSS DEDUCTION 
    private static double computeSSS(double[] params) {
        double monthlyGross = params[0];

        double[][] sssTable = {
            {  3250,  135.00 }, {  3750,  157.50 }, {  4250,  180.00 },
            {  4750,  202.50 }, {  5250,  225.00 }, {  5750,  247.50 },
            {  6250,  270.00 }, {  6750,  292.50 }, {  7250,  315.00 },
            {  7750,  337.50 }, {  8250,  360.00 }, {  8750,  382.50 },
            {  9250,  405.00 }, {  9750,  427.50 }, { 10250,  450.00 },
            { 10750,  472.50 }, { 11250,  495.00 }, { 11750,  517.50 },
            { 12250,  540.00 }, { 12750,  562.50 }, { 13250,  585.00 },
            { 13750,  607.50 }, { 14250,  630.00 }, { 14750,  652.50 },
            { 15250,  675.00 }, { 15750,  697.50 }, { 16250,  720.00 },
            { 16750,  742.50 }, { 17250,  765.00 }, { 17750,  787.50 },
            { 18250,  810.00 }, { 18750,  832.50 }, { 19250,  855.00 },
            { 19750,  877.50 }, { 20250,  900.00 }, { 20750,  922.50 },
            { 21250,  945.00 }, { 21750,  967.50 }, { 22250,  990.00 },
            { 22750, 1012.50 }, { 23250, 1035.00 }, { 23750, 1057.50 },
            { 24250, 1080.00 }, { 24750, 1102.50 }, { Double.MAX_VALUE, 1125.00 }
        };
        for (double[] bracket : sssTable) {
            if (monthlyGross <= bracket[0]) return bracket[1];
        }
        return 1125.00;
    }

    // PHILHEALTH DEDUCTION
    private static double computePhilHealth(double[] params) {
        double monthlyGross = params[0];
        return Math.round((monthlyGross * PHILHEALTH_RATE / 2) * 100.0) / 100.0;
    }

    // PAG-IBIG DEDUCTION
    private static double computePagIBIG(double[] params) {
        double monthlyGross = params[0];
        return monthlyGross <= 1_500 ? 100.0 : 200.0;
    }

    // WITHHOLDING TAX 
    private static double computeWithholdingTax(double[] params) {
        double monthlySalary = params[0];
        double tax;
        if      (monthlySalary <= 20_833)  tax = 0;
        else if (monthlySalary <= 33_333)  tax = (monthlySalary - 20_833)  * 0.20;
        else if (monthlySalary <= 66_667)  tax = 2_500  + (monthlySalary - 33_333)  * 0.25;
        else if (monthlySalary <= 166_667) tax = 10_833 + (monthlySalary - 66_667)  * 0.30;
        else if (monthlySalary <= 666_667) tax = 40_833 + (monthlySalary - 166_667) * 0.32;
        else                                tax = 200_833 + (monthlySalary - 666_667) * 0.35;
        return Math.round(tax * 100.0) / 100.0;
    }

    // TOTAL DEDUCTIONS 
    private static double[] computeDeductions(double[] params) {
        double monthlyGross = params[0];

        double sss        = computeSSS(new double[]{ monthlyGross });
        double philHealth = computePhilHealth(new double[]{ monthlyGross });
        double pagIbig     = computePagIBIG(new double[]{ monthlyGross });
        double tax         = computeWithholdingTax(new double[]{ monthlyGross });
        double total       = sss + philHealth + pagIbig + tax;

        return new double[]{ sss, philHealth, pagIbig, tax, total };
    }

    // NET PAY 
    private static double computeNetPay(double[] params) {
        double grossPay        = params[0];
        double totalDeductions = params[1];
        return grossPay - totalDeductions;
    }

    // COMPUTE HOURS WORKED FOR 1 EMPLOYEE
    private static double computeHoursWorked(String empID,
                                             String yearMonth, int cutoff) {
        java.util.List<String[]> records = EntryPoint.attendanceMap.get(empID);
        if (records == null) return 0;
        double total = 0;
        for (String[] row : records) {
            String date = DataProcessing.safeGet(row, 3);
            if (!getYearMonth(date).equals(yearMonth)) continue;
            int day = getDayOfMonth(date);
            if ((day <= 15 ? 1 : 2) != cutoff) continue;
            total += computeHours(
                    DataProcessing.safeGet(row, 4),
                    DataProcessing.safeGet(row, 5));
        }
        return total;
    }

    // Computes working hours capped to 8:00-17:00 with 5-minute grace period
    private static double computeHours(String logIn, String logOut) {
        try {
            java.time.format.DateTimeFormatter fmt =
                    java.time.format.DateTimeFormatter.ofPattern("H:mm");
            java.time.LocalTime start          = java.time.LocalTime.parse(logIn.trim(),  fmt);
            java.time.LocalTime end            = java.time.LocalTime.parse(logOut.trim(), fmt);
            java.time.LocalTime officialStart  = java.time.LocalTime.of(8, 0);
            java.time.LocalTime gracePeriodEnd = java.time.LocalTime.of(8, 5);
            java.time.LocalTime officialEnd    = java.time.LocalTime.of(17, 0);

            if (start.isBefore(officialStart))  start = officialStart;
            if (!start.isAfter(gracePeriodEnd)) start = officialStart;
            if (end.isAfter(officialEnd))        end   = officialEnd;
            if (!end.isAfter(start))             return 0;

            return java.time.Duration.between(start, end).toMinutes() / 60.0;
        } catch (Exception e) { return 0; }
    }

    // Extracts "YYYY-MM" from a date string formatted as "M/D/YYYY"
    private static String getYearMonth(String date) {
        try {
            String[] p = date.split("/");
            String m = p[0].trim();
            return p[2].trim() + "-" + (m.length() == 1 ? "0" + m : m);
        } catch (Exception e) { return "Unknown"; }
    }

    // Extracts the day-of-month integer from a date string "M/D/YYYY"
    private static int getDayOfMonth(String date) {
        try { return Integer.parseInt(date.split("/")[1].trim()); }
        catch (Exception e) { return 0; }
    }
}
