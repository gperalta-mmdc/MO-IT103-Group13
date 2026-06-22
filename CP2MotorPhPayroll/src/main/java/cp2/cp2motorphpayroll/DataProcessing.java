package cp2.cp2motorphpayroll;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * =========================================================
 * Employee CSV column layout (0-based index):
 * 0  = Employee # (key)    1  = Last Name
 * 2  = First Name          3  = Birthday
 * 4  = Phone Number        5  = SSS #
 * 6  = PhilHealth #        7  = TIN #
 * 8  = Pag-IBIG #          9  = Status
 * 10 = Position            11 = Basic Salary
 * 12 = Rice Subsidy        13 = Phone Allowance
 * 14 = Clothing Allowance  15 = Gross Semi-monthly Rate
 * 16 = Hourly Rate         17 = Immediate Supervisor
 * 18 = Address
 * =========================================================
 */
public class DataProcessing {

    static final int MIN_EMPLOYEE_COLS = 4;
    static int count;
    static String filePath = EntryPoint.EMPLOYEE_FILE;

    // LOAD EMPLOYEES
    static void loadEmployees(String employeeFile,
                              HashMap<String, String[]> employeeMap) {
        File file = new File(employeeFile);
        if (!file.exists()) {
            //debug line start
            System.out.println(file.getAbsolutePath());
            SystemGUIHelper.showWarning(null,
                    "Employee file not found");
            return;
        }
        employeeMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header row
            String line;
            count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                line += "," + count;
                System.out.println(line);
                if (line.isEmpty()) continue;
                String[] data = line.split(",", -1);
                if (data.length < MIN_EMPLOYEE_COLS) continue;
                employeeMap.put(data[0].trim(), data); //the key ID is
                count++;
            }
            System.out.println("[DataProcessing] Employees loaded: ");

        } catch (IOException e) {
            SystemGUIHelper.showWarning(null,
                    "Error reading employee file");
        }
    }

    // LOAD ATTENDANCE 
    static void loadAttendance(String attendanceFile,
                               HashMap<String, List<String[]>> attendanceMap) {
        File file = new File(attendanceFile);
        if (!file.exists()) {
            //debug line start
            System.out.println(file.getAbsolutePath());
            SystemGUIHelper.showWarning(null,
                    "Attendance File not Found");
            return;
        }
        attendanceMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header row
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",", -1);
                if (data.length < 6) continue;
                String empNumber = data[0].trim();
                attendanceMap.putIfAbsent(empNumber, new ArrayList<>());
                attendanceMap.get(empNumber).add(data);
                count++;
            }
            System.out.println("[DataProcessing] Attendance records loaded: " + count);
        } catch (IOException e) {
            SystemGUIHelper.showWarning(null,
                    "Error Reading Attendance File");
        }
    }

    static String safeGet(String[] data, int index) {
        if (data == null || index >= data.length) return "";
        return data[index].trim();
    }

    static String[] readNewEmployeeField(JTextField[] fields) {
        String[] content = new String[18];
        for (int i = 0; i < 11; i++) {
            JTextField field = fields[i];
            if (field == null) {
                continue;
            }
            if (i > 9) {
                content[16] = field.getText();
            }
            content[i] = field.getText();
            System.out.println("Index " + i + ": " + content[i]);
        }
        return content;
    }

    static String[] addEmployeeToRecords(JTextField[] fields) {
        String[] data = readNewEmployeeField(fields);
        //bug where if you add one entry and another it slips into the top of the latest entered array
        EntryPoint.employeeMap.put(data[0].trim(), data); //the key ID is
        System.out.println(data[0]);
        System.out.println(Arrays.toString(data));
        return data;
    }

    static void saveEmployeeToCSV(String[] data) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            // Join array elements with commas
            String line = String.join(",", data).replace("null", " ");
            // Append newline at the end
            writer.write(line + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Data validation methods
    static boolean onlyLetters(JTextField content) {
        return content != null && processJTextField(content).matches("[a-zA-Z]+");
    }

    static boolean onlyIntegers(JTextField contentField, int limit) {
        String content = processJTextField(contentField);
        if (content.length() != limit && limit != 0) {
            return false;
        }
        if (!content.matches("\\d+")) {
            return false;
        }
        return true;
    }

    static boolean onlyIntegers(String content, int limit) {
        if (content.length() != limit && limit != 0) {
            return false;
        }
        try {
            int parsedInteger = Integer.parseInt(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean onlyIntegers(String content) {
        try {
            int parsedInteger = Integer.parseInt(content);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    //Checks user input based on format defined by other methods (eg. xxx-xxx-xxx)
    //It splits input and format based on the format and checks if the input is an integer and of equal length to the template
    static boolean genericValidator(JTextField field, String format, String separator) {
        String input = processJTextField(field);
        String[] chunkedFormat = format.split(separator, -1);
        String[] chunkedInput = input.split(separator, -1);

        if(chunkedInput.length!= chunkedFormat.length){
            return false;
        }

        for (int i = 0; i < chunkedFormat.length; i++) {
            System.out.println("-----");
            System.out.println(chunkedInput[i]);
            System.out.println(chunkedFormat[i]);
            if (!(onlyIntegers(chunkedInput[i], chunkedFormat[i].length()))) {
                return false;
            }
        }
        return true;
    }


    // For SSS numbers: xx-xxxxxxx-x
    static boolean validateSSS(JTextField field) {
        return genericValidator(field, "xx-xxxxxxx-x", "-");
    }

    // For TIN numbers: xxxx-xxxx-xxxx
    static boolean validateTIN(JTextField field) {
        return genericValidator(field, "xxx-xxx-xxx-xxx", "-");
    }

    // FOr Pag-IBIG Numbers xxxx-xxxx-xxxx
    static boolean validatePagIbig(JTextField field) {
        return onlyIntegers(field, 12);
    }

    // For PhilHealth numbers: 12 digits, no delimiter
    static boolean validatePhilHealth(JTextField field) {
        return onlyIntegers(field, 12);
    }

    // For dates in MM/DD/YYYY format
    static boolean validateDate(JTextField dateField) {
        //call generic validator, if false return false. if it's true
        //check if dates and months are above the limit using a java library
        //if im gonna do that validateDate probably shouldn't use genericValidator()
        //but i had a cool eureka moment so ill use it anyways
        return genericValidator(dateField, "xx/xx/xxxx", "/");
    }

    static String processJTextField(JTextField field) {
        return field.getText();
    }
}