package cp2.cp2motorphpayroll;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;


public class SystemDataOperations {
    //  EMPLOYEE PICKER - searchable list
    static void showEmployeePicker(String title,
                                    java.util.function.Consumer<String> onSelect) {
        JDialog picker = new JDialog(SystemPayrollPanel.frame, title, true);
        picker.setSize(460, 400);
        picker.setLocationRelativeTo(SystemPayrollPanel.frame);
        picker.setResizable(false);

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchBar.setBackground(SystemGUIHelper.COLOR_PANEL);
        searchBar.setBorder(BorderFactory.createMatteBorder(
            0, 0, 1, 0, SystemGUIHelper.COLOR_BORDER));

        JLabel searchLbl = new JLabel("Search:");
        searchLbl.setFont(SystemGUIHelper.FONT_BOLD);

        JTextField searchField = SystemGUIHelper.makeField(20);
        searchField.setPreferredSize(new Dimension(250, 28));
        searchField.setToolTipText(
            "Search by Emp #, First Name, or Last Name");

        searchBar.add(searchLbl);
        searchBar.add(searchField);

        // Table
        String[] pickerCols = { "Emp #", "Last Name", "First Name" };
        DefaultTableModel pickerModel =
            new DefaultTableModel(pickerCols, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

        JTable pickerTable = new JTable(pickerModel);
        pickerTable.setFont(SystemGUIHelper.FONT_TABLE);
        pickerTable.setRowHeight(24);
        pickerTable.setGridColor(SystemGUIHelper.COLOR_BORDER);
        pickerTable.setSelectionBackground(new Color(84, 149, 233));
        pickerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pickerTable.getTableHeader().setFont(SystemGUIHelper.FONT_BOLD);
        pickerTable.getTableHeader().setBackground(new Color(240, 240, 240));
        pickerTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        pickerTable.getColumnModel().getColumn(1).setPreferredWidth(170);
        pickerTable.getColumnModel().getColumn(2).setPreferredWidth(170);

        // Populate sorted by Emp #
        java.util.List<String> keys =
            new java.util.ArrayList<>(EntryPoint.employeeMap.keySet());
        java.util.Collections.sort(keys);

        for (String key : keys) {
            String[] data = EntryPoint.employeeMap.get(key);
            pickerModel.addRow(new Object[]{
                key,
                DataProcessing.safeGet(data, 1),
                DataProcessing.safeGet(data, 2)
            });
        }

        // Live search filter
        searchField.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                public void insertUpdate(
                    javax.swing.event.DocumentEvent e)  { filter(); }
                public void removeUpdate(
                    javax.swing.event.DocumentEvent e)  { filter(); }
                public void changedUpdate(
                    javax.swing.event.DocumentEvent e)  { filter(); }

                void filter() {
                    String q = searchField.getText().trim().toLowerCase();
                    pickerModel.setRowCount(0);
                    for (String key : keys) {
                        String[] data = EntryPoint.employeeMap.get(key);
                        String ln =
                            DataProcessing.safeGet(data, 1).toLowerCase();
                        String fn =
                            DataProcessing.safeGet(data, 2).toLowerCase();
                        if (key.contains(q)
                                || ln.contains(q)
                                || fn.contains(q)) {
                            pickerModel.addRow(new Object[]{
                                key,
                                DataProcessing.safeGet(data, 1),
                                DataProcessing.safeGet(data, 2)
                            });
                        }
                    }
                }
            });

        JScrollPane pickerScroll = new JScrollPane(pickerTable);
        pickerScroll.setBorder(BorderFactory.createLineBorder(
            SystemGUIHelper.COLOR_BORDER));

        // Bottom bar
        JButton confirmBtn = SystemGUIHelper.makeButton(
            title, SystemGUIHelper.COLOR_PRIMARY);
        confirmBtn.setPreferredSize(new Dimension(150, 32));
        confirmBtn.setEnabled(false);

        JButton cancelBtn = SystemGUIHelper.makeButton(
            "Cancel", new Color(120, 120, 120));
        cancelBtn.setPreferredSize(new Dimension(90, 32));

        // Enable confirm only when a row is selected
        pickerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                confirmBtn.setEnabled(pickerTable.getSelectedRow() != -1);
        });

        // Double-click also confirms
        pickerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2
                        && pickerTable.getSelectedRow() != -1)
                    confirmBtn.doClick();
            }
        });

        confirmBtn.addActionListener(e -> {
            int row = pickerTable.getSelectedRow();
            if (row == -1) return;
            String empNum =
                String.valueOf(pickerModel.getValueAt(row, 0));
            picker.dispose();
            onSelect.accept(empNum);
        });

        cancelBtn.addActionListener(e -> picker.dispose());

        JPanel bottomBar =
            new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        bottomBar.setBackground(SystemGUIHelper.COLOR_PANEL);
        bottomBar.setBorder(BorderFactory.createMatteBorder(
            1, 0, 0, 0, SystemGUIHelper.COLOR_BORDER));
        bottomBar.add(cancelBtn);
        bottomBar.add(confirmBtn);

        picker.setLayout(new BorderLayout());
        picker.add(searchBar,    BorderLayout.NORTH);
        picker.add(pickerScroll, BorderLayout.CENTER);
        picker.add(bottomBar,    BorderLayout.SOUTH);
        picker.setVisible(true);
    }

    //  VIEW EMPLOYEE DIALOG
    static void showViewDialog(String empNum) {
        String[] data = EntryPoint.employeeMap.get(empNum);
        if (data == null) {
            SystemGUIHelper.showError(SystemPayrollPanel.frame,
                "Could not load details for Employee #" + empNum);
            return;
        }

        String[][] fields = {
            { "Employee #",           "0"  },
            { "Last Name",            "1"  },
            { "First Name",           "2"  },
            { "Birthday",             "3"  },
            { "Phone Number",         "4"  },
            { "SSS #",                "5"  },
            { "PhilHealth #",         "6"  },
            { "TIN #",                "7"  },
            { "Pag-IBIG #",           "8"  },
            { "Status",               "9"  },
            { "Position",             "10" },
            { "Basic Salary",         "11" },
            { "Rice Subsidy",         "12" },
            { "Phone Allowance",      "13" },
            { "Clothing Allowance",   "14" },
            { "Gross Semi-monthly",   "15" },
            { "Hourly Rate",          "16" },
            { "Immediate Supervisor", "17" },
            { "Address",              "18" },
        };

        JDialog dialog = new JDialog(SystemPayrollPanel.frame,
            "Employee Details — "
            + DataProcessing.safeGet(data, 2) + " "
            + DataProcessing.safeGet(data, 1), true);
        dialog.setSize(480, 580);
        dialog.setLocationRelativeTo(SystemPayrollPanel.frame);
        dialog.setResizable(false);

        JPanel dialogHeader = new JPanel(new BorderLayout());
        dialogHeader.setBackground(SystemGUIHelper.COLOR_PRIMARY);
        dialogHeader.setPreferredSize(new Dimension(480, 66));
        dialogHeader.setBorder(
            BorderFactory.createEmptyBorder(15, 14, 0, 14));

        JLabel titleLbl = new JLabel(
            DataProcessing.safeGet(data, 2) + " "
            + DataProcessing.safeGet(data, 1));
        titleLbl.setFont(SystemGUIHelper.FONT_BOLD);
        titleLbl.setForeground(Color.WHITE);

        JLabel posLbl = new JLabel(DataProcessing.safeGet(data, 10));
        posLbl.setFont(SystemGUIHelper.FONT_SMALL);
        posLbl.setForeground(new Color(180, 200, 255));

        JPanel titleStack = new JPanel();
        titleStack.setOpaque(false);
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.add(titleLbl);
        titleStack.add(posLbl);
        dialogHeader.add(titleStack, BorderLayout.CENTER);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        GridBagConstraints gl = new GridBagConstraints();
        gl.anchor = GridBagConstraints.WEST;
        gl.insets = new Insets(4, 4, 4, 10);

        GridBagConstraints gv = new GridBagConstraints();
        gv.anchor    = GridBagConstraints.WEST;
        gv.fill      = GridBagConstraints.HORIZONTAL;
        gv.weightx   = 1.0;
        gv.insets    = new Insets(4, 0, 4, 4);
        gv.gridwidth = GridBagConstraints.REMAINDER;

        for (String[] field : fields) {
            String label    = field[0];
            int    csvIndex = Integer.parseInt(field[1]);
            String value    = DataProcessing.safeGet(data, csvIndex);

            gl.gridx = 0; gl.gridy = GridBagConstraints.RELATIVE;
            JLabel lbl = new JLabel(label + ":");
            lbl.setFont(SystemGUIHelper.FONT_BOLD);
            lbl.setForeground(SystemGUIHelper.COLOR_SECONDARY);
            grid.add(lbl, gl);

            gv.gridx = 1;
            JLabel val = new JLabel(value.isEmpty() ? "—" : value);
            val.setFont(SystemGUIHelper.FONT_TABLE);
            grid.add(val, gv);
        }

        JButton closeBtn = SystemGUIHelper.makeButton(
            "Close", SystemGUIHelper.COLOR_PRIMARY);
        closeBtn.setPreferredSize(new Dimension(100, 32));
        closeBtn.addActionListener(e -> dialog.dispose());

        JPanel btnRow =
            new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
        btnRow.setBackground(SystemGUIHelper.COLOR_BG);
        btnRow.setBorder(BorderFactory.createMatteBorder(
            1, 0, 0, 0, SystemGUIHelper.COLOR_BORDER));
        btnRow.add(closeBtn);

        JScrollPane gridScroll = new JScrollPane(grid);
        gridScroll.setBorder(null);

        dialog.setLayout(new BorderLayout());
        dialog.add(dialogHeader, BorderLayout.NORTH);
        dialog.add(gridScroll,   BorderLayout.CENTER);
        dialog.add(btnRow,       BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    //  SHARED VALIDATION - used by both Add and Edit
    static boolean validateEmployeeInputs(String[] newData,
                                           JTextField[] inputs,
                                           Component parent) {
        // Required fields
        String[] requiredLabels = {
            "Employee #", "Last Name", "First Name",
            "Birthday", "SSS #", "PhilHealth #",
            "TIN #", "Pag-IBIG #", "Status",
            "Position", "Basic Salary", "Hourly Rate"
        };
        int[] requiredIndices = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 16};

        for (int i = 0; i < requiredIndices.length; i++) {
            if (newData[requiredIndices[i]].isEmpty()) {
                SystemGUIHelper.showError(parent,
                    "\"" + requiredLabels[i] + "\" is required.\n"
                    + "Please fill in all required fields.");
                inputs[requiredIndices[i]].requestFocus();
                return false;
            }
        }

        if (!newData[0].matches("\\d+")) {
            SystemGUIHelper.showError(parent,
                "Employee # must contain digits only.\nExample: 10045");
            inputs[0].requestFocus();
            return false;
        }

        if (!Validation.validateDate(newData[3])) {
            SystemGUIHelper.showError(parent,
                "Birthday must be in MM/DD/YYYY format.\n"
                + "Example: 06/15/1995");
            inputs[3].requestFocus();
            return false;
        }

        if (!newData[4].isEmpty()
                && !Validation.validatePhoneNumber(newData[4])) {
            SystemGUIHelper.showError(parent,
                "Phone Number must contain digits only.\n"
                + "Example: 090-123-456");
            inputs[4].setToolTipText("Format: xxx-xxx-xxxx - e.g. 090-123-456");
            return false;
        }

        if (!Validation.validateSSS(newData[5])) {
            SystemGUIHelper.showError(parent,
                "SSS # must follow the format: xx-xxxxxxx-x\n"
                + "Example: 33-1234567-8");
            inputs[5].requestFocus();
            return false;
        }

        if (!Validation.validatePhilHealth(newData[6])) {
            SystemGUIHelper.showError(parent,
                "PhilHealth # must be 12 digits.\n"
                + "Example: 123456789012");
            inputs[6].requestFocus();
            return false;
        }

        if (!Validation.validateTIN(newData[7])) {
            SystemGUIHelper.showError(parent,
                "TIN # must follow the format: xxx-xxx-xxx-xxx\n"
                + "Example: 123-456-789-000");
            inputs[7].requestFocus();
            return false;
        }

        if (!Validation.validatePagIbig(newData[8])) {
            SystemGUIHelper.showError(parent,
                "Pag-IBIG # must be 12 digits.\n"
                + "Example: 123456789012");
            inputs[8].requestFocus();
            return false;
        }

        int[]     numericIndices  = {11, 12, 13, 14, 15, 16};
        String[]  numericLabels   = {
            "Basic Salary", "Rice Subsidy", "Phone Allowance",
            "Clothing Allowance", "Gross Semi-monthly", "Hourly Rate"
        };
        boolean[] numericRequired = {
            true, false, false, false, false, true
        };

        for (int i = 0; i < numericIndices.length; i++) {
            String val = newData[numericIndices[i]];
            if (numericRequired[i] && val.isEmpty()) {
                SystemGUIHelper.showError(parent,
                    "\"" + numericLabels[i] + "\" is required.\n"
                    + "Please enter a valid amount.\nExample: 15000.00");
                inputs[numericIndices[i]].requestFocus();
                return false;
            }
            if (!val.isEmpty() && !Validation.validateDouble(val)) {
                SystemGUIHelper.showError(parent,
                    "\"" + numericLabels[i]
                    + "\" must be a valid number with up to 2 decimal "
                    + "places.\nExample: 15000.00");
                inputs[numericIndices[i]].requestFocus();
                return false;
            }
        }

        return true;
    }

    //  ADD EMPLOYEE DIALOG
    static void showAddDialog() {

        Object[][] col1 = {
            { "Employee #",            0,  true  },
            { "First Name",            2,  true  },
            { "Last Name",             1,  true  },
            { "Birthday (MM/DD/YYYY)", 3,  true  },
            { "Phone Number",          4,  true  },
        };
        Object[][] col2 = {
            { "Position",              10, true  },
            { "Status",                9,  true  },
            { "Basic Salary",          11, true  },
            { "Hourly Rate",           16, true  },
            { "Address",               18, false },
        };
        Object[][] col3 = {
            { "SSS Number",            5,  true  },
            { "PhilHealth Number",     6,  true  },
            { "TIN Number",            7,  true  },
            { "Pag-IBIG Number",       8,  true  },
            { "Immediate Supervisor",  17, false },
        };
        Object[][] col4 = {
            { "Rice Subsidy",          12, false },
            { "Phone Allowance",       13, false },
            { "Clothing Allowance",    14, false },
            { "Gross Semi-monthly",    15, false },
        };

        JTextField[] inputs = new JTextField[19];

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(240, 240, 245));
        titleBar.setPreferredSize(new Dimension(1180, 40));
        titleBar.setBorder(BorderFactory.createMatteBorder(
            0, 0, 1, 0, SystemGUIHelper.COLOR_BORDER));
        JLabel titleLbl = new JLabel("  Add New Employee");
        titleLbl.setFont(SystemGUIHelper.FONT_BOLD);
        titleBar.add(titleLbl, BorderLayout.WEST);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(Color.WHITE);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 14, 5, 14);

        addColumn(grid, gbc, col1, 0, inputs);
        addColumn(grid, gbc, col2, 1, inputs);
        addColumn(grid, gbc, col3, 2, inputs);
        addColumn(grid, gbc, col4, 3, inputs);

        // Auto-generate and lock Employee #
        String nextEmpNum = DataProcessing.generateNextEmpNumber(
            EntryPoint.employeeMap);
        inputs[0].setText(nextEmpNum);
        inputs[0].setEditable(false);
        inputs[0].setBackground(new Color(238, 238, 238));

        // Placeholder hints
        applyHint(grid, inputs, 3,  "06/15/1995");
        applyHint(grid, inputs, 4,  "090-123-456");
        applyHint(grid, inputs, 5,  "33-1234567-8");
        applyHint(grid, inputs, 6,  "123456789012");
        applyHint(grid, inputs, 7,  "123-456-789-000");
        applyHint(grid, inputs, 8,  "123456789012");
        applyHint(grid, inputs, 11, "15000.00");
        applyHint(grid, inputs, 12, "1500.00");
        applyHint(grid, inputs, 13, "500.00");
        applyHint(grid, inputs, 14, "500.00");
        applyHint(grid, inputs, 15, "7500.00");
        applyHint(grid, inputs, 16, "135.00");

        JPanel bottomBar =
            new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        bottomBar.setBackground(new Color(240, 240, 240));
        bottomBar.setBorder(BorderFactory.createMatteBorder(
            1, 0, 0, 0, SystemGUIHelper.COLOR_BORDER));
        bottomBar.setPreferredSize(new Dimension(1180, 50));

        JLabel legend = new JLabel("Required fields");
        legend.setFont(SystemGUIHelper.FONT_SMALL);
        legend.setForeground(SystemGUIHelper.COLOR_REQFIELD);
        bottomBar.add(legend);

        JButton cancelBtn = SystemGUIHelper.makeButton("Cancel",
                new Color(120, 120, 180));
        cancelBtn.setPreferredSize(new Dimension(90, 28));
        bottomBar.add(cancelBtn);

        JButton confirmBtn = SystemGUIHelper.makeButton("Confirm",
                new Color(120, 120, 180));
        confirmBtn.setPreferredSize(new Dimension(90, 28));
        bottomBar.add(confirmBtn);

        JPanel finalPanel = new JPanel(new BorderLayout());
        finalPanel.setPreferredSize(new Dimension(1180, 560));
        finalPanel.add(titleBar,  BorderLayout.NORTH);
        finalPanel.add(grid,      BorderLayout.CENTER);
        finalPanel.add(bottomBar, BorderLayout.SOUTH);
        finalPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        JDialog dialog = new JDialog(SystemPayrollPanel.frame, true);
        dialog.setUndecorated(true);
        dialog.getContentPane().add(finalPanel);
        dialog.pack();

        Point loc = SystemPayrollPanel.frame.getLocationOnScreen();
        dialog.setLocation(loc.x + 80, loc.y + 60);

        cancelBtn.addActionListener(e -> dialog.dispose());

        confirmBtn.addActionListener(e -> {
            String[] newData = new String[19];
            for (int i = 0; i < 19; i++)
                newData[i] = inputs[i] != null
                    ? inputs[i].getText().trim() : "";

            if (!validateEmployeeInputs(newData, inputs,
                    SystemPayrollPanel.frame)) return;

            boolean saved = DataProcessing.saveEmployee(
                EntryPoint.EMPLOYEE_FILE, newData,
                EntryPoint.employeeMap);

            if (saved) {
                dialog.dispose();
                SystemGUIHelper.showInfo(SystemPayrollPanel.frame,
                    "Employee #" + newData[0]
                    + " has been added successfully.");
            }
        });

        dialog.setVisible(true);
    }

    //  ADD COLUMN 
    private static void addColumn(JPanel container, GridBagConstraints gbc,
                                   Object[][] fields, int colIndex,
                                   JTextField[] inputs) {
        gbc.gridx = colIndex;
        for (int row = 0; row < fields.length; row++) {
            String  label    = (String)  fields[row][0];
            int     csvIdx   = (Integer) fields[row][1];
            boolean required = (Boolean) fields[row][2];

            gbc.gridy = row * 2;
            JLabel lbl = new JLabel(label);
            lbl.setFont(SystemGUIHelper.FONT_BOLD);
            lbl.setForeground(required
                ? SystemGUIHelper.COLOR_REQFIELD
                : Color.GRAY);
            container.add(lbl, gbc);

            gbc.gridy = row * 2 + 1;
            JTextField tf = SystemGUIHelper.makeField(12);
            tf.setPreferredSize(new Dimension(220, 28));
            inputs[csvIdx] = tf;
            container.add(tf, gbc);
        }
    }

    //  APPLY HINT - placeholder text for specific fields in Add dialog
    private static void applyHint(JPanel grid, JTextField[] inputs,
                                   int csvIdx, String hint) {
        if (inputs[csvIdx] == null) return;
        Component[] components = grid.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == inputs[csvIdx]) {
                JTextField hinted =
                    SystemGUIHelper.makeFieldWithHint(12, hint);
                hinted.setPreferredSize(new Dimension(220, 28));
                GridBagConstraints existingGbc =
                    ((GridBagLayout) grid.getLayout())
                        .getConstraints(components[i]);
                grid.remove(i);
                grid.add(hinted, existingGbc, i);
                inputs[csvIdx] = hinted;
                grid.revalidate();
                grid.repaint();
                return;
            }
        }
    }

    //  EDIT EMPLOYEE DIALOG
    static void showEditDialog(String empNum) {
        String[] currentData = EntryPoint.employeeMap.get(empNum);
        if (currentData == null) {
            SystemGUIHelper.showError(SystemPayrollPanel.frame,
                "Could not load details for Employee #" + empNum);
            return;
        }

        Object[][] fieldDefs = {
            { "Employee #",            0,  true  },
            { "Last Name",             1,  true  },
            { "First Name",            2,  true  },
            { "Birthday (MM/DD/YYYY)", 3,  true  },
            { "Phone Number",          4,  true  },
            { "SSS #",                 5,  true  },
            { "PhilHealth #",          6,  true  },
            { "TIN #",                 7,  true  },
            { "Pag-IBIG #",            8,  true  },
            { "Status",                9,  true  },
            { "Position",              10, true  },
            { "Basic Salary",          11, true  },
            { "Rice Subsidy",          12, false },
            { "Phone Allowance",       13, false },
            { "Clothing Allowance",    14, false },
            { "Gross Semi-monthly",    15, false },
            { "Hourly Rate",           16, true  },
            { "Immediate Supervisor",  17, false },
            { "Address",               18, false },
        };

        JDialog dialog = new JDialog(SystemPayrollPanel.frame,
            "Edit Employee — #" + empNum, true);
        dialog.setSize(500, 640);
        dialog.setLocationRelativeTo(SystemPayrollPanel.frame);
        dialog.setResizable(false);

        JPanel dialogHeader = new JPanel(new BorderLayout());
        dialogHeader.setBackground(SystemGUIHelper.COLOR_PRIMARY);
        dialogHeader.setPreferredSize(new Dimension(500, 46));
        dialogHeader.setBorder(
            BorderFactory.createEmptyBorder(0, 14, 0, 14));
        JLabel headerLbl = new JLabel("Edit Employee — #" + empNum);
        headerLbl.setFont(SystemGUIHelper.FONT_BOLD);
        headerLbl.setForeground(Color.WHITE);
        dialogHeader.add(headerLbl, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        GridBagConstraints gl = new GridBagConstraints();
        gl.anchor = GridBagConstraints.WEST;
        gl.insets = new Insets(4, 4, 4, 10);

        GridBagConstraints gf = new GridBagConstraints();
        gf.fill      = GridBagConstraints.HORIZONTAL;
        gf.weightx   = 1.0;
        gf.insets    = new Insets(4, 0, 4, 4);
        gf.gridwidth = GridBagConstraints.REMAINDER;

        JTextField[] inputs = new JTextField[19];

        for (Object[] def : fieldDefs) {
            String  label    = (String)  def[0];
            int     csvIdx   = (Integer) def[1];
            boolean required = (Boolean) def[2];

            gl.gridx = 0; gl.gridy = GridBagConstraints.RELATIVE;
            JLabel lbl = new JLabel(
                required ? label + " :    " : label + ":");
            lbl.setFont(SystemGUIHelper.FONT_BOLD);
            lbl.setForeground(required
                ? SystemGUIHelper.COLOR_REQFIELD
                : Color.GRAY);
            form.add(lbl, gl);

            gf.gridx = 1;
            JTextField tf = SystemGUIHelper.makeField(20);
            tf.setPreferredSize(new Dimension(240, 28));
            tf.setText(DataProcessing.safeGet(currentData, csvIdx));
            inputs[csvIdx] = tf;
            form.add(tf, gf);
        }

        // Tooltips applied AFTER loop
        inputs[3].setToolTipText("Format: MM/DD/YYYY - e.g. 06/15/1995");
        inputs[4].setToolTipText("Digits only - e.g. 09171234567");
        inputs[5].setToolTipText("Format: xx-xxxxxxx-x - e.g. 33-1234567-8");
        inputs[6].setToolTipText("12 digits - e.g. 123456789012");
        inputs[7].setToolTipText(
            "Format: xxx-xxx-xxx-xxx - e.g. 123-456-789-000");
        inputs[8].setToolTipText("12 digits - e.g. 123456789012");
        inputs[11].setToolTipText("Format: xxxxx.xx - e.g. 15000.00");
        inputs[12].setToolTipText("Format: xxxx.xx - e.g. 1500.00");
        inputs[13].setToolTipText("Format: xxx.xx - e.g. 500.00");
        inputs[14].setToolTipText("Format: xxx.xx - e.g. 500.00");
        inputs[15].setToolTipText("Format: xxxxx.xx - e.g. 7500.00");
        inputs[16].setToolTipText("Format: xxx.xx - e.g. 135.00");

        JLabel legend = new JLabel("   Required fields");
        legend.setFont(SystemGUIHelper.FONT_SMALL);
        legend.setForeground(SystemGUIHelper.COLOR_REQFIELD);

        JButton saveBtn = SystemGUIHelper.makeButton(
            "Save Changes", SystemGUIHelper.COLOR_SUCCESS);
        saveBtn.setPreferredSize(new Dimension(130, 32));

        JButton cancelBtn = SystemGUIHelper.makeButton(
            "Cancel", new Color(120, 120, 120));
        cancelBtn.setPreferredSize(new Dimension(90, 32));

        JPanel btnRow =
            new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnRow.setBackground(SystemGUIHelper.COLOR_BG);
        btnRow.setBorder(BorderFactory.createMatteBorder(
            1, 0, 0, 0, SystemGUIHelper.COLOR_BORDER));
        btnRow.add(legend);
        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);

        cancelBtn.addActionListener(e -> dialog.dispose());

        saveBtn.addActionListener(e -> {
            String[] newData = new String[19];
            for (int i = 0; i < 19; i++)
                newData[i] = inputs[i] != null
                    ? inputs[i].getText().trim() : "";

            if (!validateEmployeeInputs(newData, inputs, dialog)) return;

            String newEmpNum = newData[0];
            if (!newEmpNum.equals(empNum)) {
                int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to change \"Employee #\"?\n"
                    + "Current: " + empNum + "  →  New: " + newEmpNum,
                    "Confirm Employee # Change",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            boolean updated = DataProcessing.updateEmployee(
                EntryPoint.EMPLOYEE_FILE, empNum, newData,
                EntryPoint.employeeMap);

            if (updated) {
                dialog.dispose();
                SystemGUIHelper.showInfo(SystemPayrollPanel.frame,
                    "Employee #" + newEmpNum
                    + " has been updated successfully.");
            }
        });

        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setBorder(null);

        dialog.setLayout(new BorderLayout());
        dialog.add(dialogHeader, BorderLayout.NORTH);
        dialog.add(formScroll,   BorderLayout.CENTER);
        dialog.add(btnRow,       BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    //  DELETE CONFIRMATION
    static void confirmAndDelete(String empNum) {
        String[] data     = EntryPoint.employeeMap.get(empNum);
        String firstName  = DataProcessing.safeGet(data, 2);
        String lastName   = DataProcessing.safeGet(data, 1);

        int confirm = JOptionPane.showConfirmDialog(
            SystemPayrollPanel.frame,
            "Are you sure you want to delete this employee?\n\n"
            + "Employee #: " + empNum + "\n"
            + "Name: " + firstName + " " + lastName + "\n\n"
            + "This action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean deleted = DataProcessing.deleteEmployee(
            EntryPoint.EMPLOYEE_FILE, empNum, EntryPoint.employeeMap);

        if (deleted) {
            // Remove all payroll rows for this employee
            // (reverse loop prevents index-shift bugs)
            for (int i = SystemPayrollPanel.tableModel.getRowCount() - 1;
                     i >= 0; i--) {
                if (String.valueOf(
                        SystemPayrollPanel.tableModel.getValueAt(i, 0))
                        .equals(empNum))
                    SystemPayrollPanel.tableModel.removeRow(i);
            }
            SystemGUIHelper.showInfo(SystemPayrollPanel.frame,
                "Employee #" + empNum
                + " has been deleted successfully.");
            SystemPayrollPanel.statusBar.setText(
                "  Employee #" + empNum + " deleted.");
        }
    }
}