package cp2.cp2motorphpayroll;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class SystemEmployeePanel {

    static final String[] COLUMNS = {
        "Emp #", "Last Name", "First Name", "Birthday",
        "SSS Number", "PhilHealth No.", "TIN", "Pag-IBIG No.",
        "Position", "Status", "Hourly Rate"
    };

    static final int[] CSV_INDICES = {0, 1, 2, 3, 5, 6, 7, 8, 10, 9, 16};

    static DefaultTableModel tableModel;
    static JTable            table;
    static JFrame            frame;

    // SHOW PANEL
    static void show() {
        frame = new JFrame("MotorPh — Employee Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1107, 680);
        frame.setLocationRelativeTo(null);

        JPanel header = SystemGUIHelper.buildHeader(
            "Employee Portal", frame, 1207,
            () -> SystemLogInPanel.showLogin());

        JPanel searchBar = buildSearchBar();
        buildTable();

        // Double-click a row → View dialog
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) showViewDialog(row);
                }
            }
        });

        JLabel statusBar = new JLabel(
            "  " + EntryPoint.employeeMap.size()
            + " employee(s) loaded.  |  Double-click a row to view details.");
        statusBar.setFont(SystemGUIHelper.FONT_SMALL);
        statusBar.setForeground(Color.GRAY);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                SystemGUIHelper.COLOR_BORDER));
        statusBar.setPreferredSize(new Dimension(1107, 24));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                SystemGUIHelper.COLOR_BORDER));

        JPanel actionBar = buildActionBar(table);

        JPanel center = new JPanel(new BorderLayout());
        center.add(searchBar,  BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        center.add(actionBar,  BorderLayout.SOUTH);

        frame.setLayout(new BorderLayout());
        frame.add(header,    BorderLayout.NORTH);
        frame.add(center,    BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // BUILD ACTION BAR — View only
    private static JPanel buildActionBar(JTable table) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bar.setBackground(SystemGUIHelper.COLOR_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                SystemGUIHelper.COLOR_BORDER));

        JButton viewBtn = SystemGUIHelper.makeButton("View Employee",
                SystemGUIHelper.COLOR_PRIMARY);
        viewBtn.setPreferredSize(new Dimension(130, 28));
        viewBtn.setEnabled(false);

        // Enable View button only when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                viewBtn.setEnabled(table.getSelectedRow() != -1);
        });

        viewBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) showViewDialog(row);
        });

        bar.add(viewBtn);
        return bar;
    }

    // BUILD TABLE
    private static void buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(SystemGUIHelper.FONT_TABLE);
        table.setRowHeight(24);
        table.setGridColor(SystemGUIHelper.COLOR_BORDER);
        table.setSelectionBackground(new Color(84, 149, 233));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(SystemGUIHelper.FONT_BOLD);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] colWidths = {60, 110, 110, 90, 110, 110, 100, 100, 130, 80, 90};
        for (int i = 0; i < colWidths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
    }

    // REFRESH TABLE — shows ALL rows
    static void refreshTable() {
        tableModel.setRowCount(0);
        var keys = new ArrayList<String>(EntryPoint.employeeMap.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            String[] data = EntryPoint.employeeMap.get(key);
            Object[] row  = new Object[COLUMNS.length];
            for (int i = 0; i < CSV_INDICES.length; i++)
                row[i] = DataProcessing.safeGet(data, CSV_INDICES[i]);
            tableModel.addRow(row);
        }
    }

    // REFRESH TABLE FILTERED
    static void refreshTableFiltered(String empNum, String name) {
        tableModel.setRowCount(0);
        var keys = new ArrayList<String>(EntryPoint.employeeMap.keySet());
        Collections.sort(keys);

        for (String key : keys) {
            String[] data = EntryPoint.employeeMap.get(key);
            Object[] row  = new Object[COLUMNS.length];
            for (int i = 0; i < CSV_INDICES.length; i++)
                row[i] = DataProcessing.safeGet(data, CSV_INDICES[i]);

            boolean matchEmp  = empNum.isEmpty()
                || String.valueOf(row[0]).contains(empNum);
            boolean matchName = name.isEmpty()
                || String.valueOf(row[1]).toLowerCase().contains(name)
                || String.valueOf(row[2]).toLowerCase().contains(name);

            if (matchEmp && matchName)
                tableModel.addRow(row);
        }
    }

    // VIEW DIALOG — shows full details for the selected row
    static void showViewDialog(int tableRow) {
        String empNum = String.valueOf(tableModel.getValueAt(tableRow, 0));
        String[] data = EntryPoint.employeeMap.get(empNum);

        if (data == null) {
            SystemGUIHelper.showError(frame,
                "Could not load details for Employee #" + empNum);
            return;
        }

        String[][] fields = {
            { "Employee #",            "0"  },
            { "Last Name",             "1"  },
            { "First Name",            "2"  },
            { "Birthday",              "3"  },
            { "Phone Number",          "4"  },
            { "SSS #",                 "5"  },
            { "PhilHealth #",          "6"  },
            { "TIN #",                 "7"  },
            { "Pag-IBIG #",            "8"  },
            { "Status",                "9"  },
            { "Position",              "10" },
            { "Basic Salary",          "11" },
            { "Rice Subsidy",          "12" },
            { "Phone Allowance",       "13" },
            { "Clothing Allowance",    "14" },
            { "Gross Semi-monthly",    "15" },
            { "Hourly Rate",           "16" },
            { "Immediate Supervisor",  "17" },
            { "Address",               "18" },
        };

        JDialog dialog = new JDialog(frame,
            "Employee Details — " + DataProcessing.safeGet(data, 2)
            + " " + DataProcessing.safeGet(data, 1), true);
        dialog.setSize(480, 580);
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);

        JPanel dialogHeader = new JPanel(new BorderLayout());
        dialogHeader.setBackground(SystemGUIHelper.COLOR_PRIMARY);
        dialogHeader.setPreferredSize(new Dimension(480, 66));
        dialogHeader.setBorder(BorderFactory.createEmptyBorder(15, 14, 0, 14));

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

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
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

    // BUILD SEARCH BAR
    private static JPanel buildSearchBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bar.setBackground(SystemGUIHelper.COLOR_PANEL);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                SystemGUIHelper.COLOR_BORDER));

        JLabel empNumLbl = new JLabel("Employee Number:");
        empNumLbl.setFont(SystemGUIHelper.FONT_BOLD);

        JTextField empNumField = SystemGUIHelper.makeField(10);
        empNumField.setPreferredSize(new Dimension(110, 28));

        JLabel nameLbl = new JLabel("Employee Name:");
        nameLbl.setFont(SystemGUIHelper.FONT_BOLD);

        JTextField nameField = SystemGUIHelper.makeField(14);
        nameField.setPreferredSize(new Dimension(160, 28));

        JButton searchBtn = SystemGUIHelper.makeButton("Search",
                SystemGUIHelper.COLOR_PRIMARY);
        searchBtn.setPreferredSize(new Dimension(90, 28));

        JButton clearBtn = SystemGUIHelper.makeButton("Clear",
                new Color(120, 120, 120));
        clearBtn.setPreferredSize(new Dimension(70, 28));

        JButton showAllBtn = SystemGUIHelper.makeButton("Show All Records",
                SystemGUIHelper.COLOR_PRIMARY);
        showAllBtn.setPreferredSize(new Dimension(150, 28));

        searchBtn.addActionListener(e -> {
            String empNum = empNumField.getText().trim();
            String name   = nameField.getText().trim().toLowerCase();

            if (!empNum.isEmpty() && !empNum.matches("\\d+")) {
                SystemGUIHelper.showError(frame,
                    "Employee number must contain digits only.\nExample: 10001");
                empNumField.requestFocus();
                return;
            }
            if (empNum.isEmpty() && name.isEmpty()) {
                SystemGUIHelper.showWarning(frame,
                    "Please enter an employee number or name to search.");
                return;
            }
            if (!empNum.isEmpty()
                    && !EntryPoint.employeeMap.containsKey(empNum)) {
                SystemGUIHelper.showError(frame,
                    "Employee Number \"" + empNum + "\" was not found.\n"
                    + "Please check the number and try again.");
                empNumField.requestFocus();
                return;
            }

            refreshTableFiltered(empNum, name);

            if (tableModel.getRowCount() == 0 && !name.isEmpty()) {
                SystemGUIHelper.showError(frame,
                    "No employee found with name containing \""
                    + nameField.getText().trim() + "\".\n"
                    + "Please check the name and try again.");
            }
        });

        showAllBtn.addActionListener(e -> {
            empNumField.setText("");
            nameField.setText("");
            refreshTable();
        });

        clearBtn.addActionListener(e -> {
            empNumField.setText("");
            nameField.setText("");
            tableModel.setRowCount(0);
            SystemGUIHelper.showInfo(frame, "Table Cleared");
        });

        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    searchBtn.doClick();
            }
        };
        empNumField.addKeyListener(enterKey);
        nameField.addKeyListener(enterKey);

        bar.add(empNumLbl);  bar.add(empNumField);
        bar.add(Box.createHorizontalStrut(8));
        bar.add(nameLbl);    bar.add(nameField);
        bar.add(searchBtn);  bar.add(clearBtn);
        bar.add(showAllBtn);

        return bar;
    }
}
