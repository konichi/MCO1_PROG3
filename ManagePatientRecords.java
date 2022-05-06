import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManagePatientRecords {
    ArrayList<Patient> patients = new ArrayList<Patient>();
    WriteToFile wtf = new WriteToFile();
    MainMenu mm = new MainMenu();
    private char savePatientRecord;
    private int error = 0;

    public void managePatientRecords(){
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Manage Patient Records");
        System.out.println("[1] Add New Patient");
        System.out.println("[2] Edit Patient Record");
        System.out.println("[3] Delete Patient Record");
        System.out.println("[4] Search Patient Record");
        System.out.println("[X] Return to Main Menu");
        System.out.print("Select a transaction: ");
        input = scanner.next();
        System.out.println();

        switch (input) {
            case "1" -> addNewPatient();
            case "2" -> searchPatientRecord();
            case "3" -> deletePatientRecord();
            case "4" -> editPatientRecord();
            case "X" -> mm.mainMenu();
            default -> managePatientRecords();
        }
    }

    public String generateUID() {
        String A = null;
        String B = null;
        String C = null;
        String D = null;
        String E = null;
        String temp;
        char tempD;
        int tempInt;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        for(int i=0; i<11; i++) {
            switch (i) {
                case 0:
                    A = "P";
                    break;
                case 1:
                    B = String.valueOf(year);
                    break;
                case 5:
                    if(month<=9)
                        C = "0"+ month;
                    else
                        C = String.valueOf(month);
                    break;
                case 7:
                    if(day==1) {                        //resets DDDEE every start of the month
                        D = "AAA";
                        E = "00";
                    } else if(E.charAt(0)!='9' && E.charAt(1)!='9') {     //increments EE
                        tempInt = Integer.valueOf(E);
                        tempInt++;
                        if(tempInt<=9)
                            E = "0" + tempInt;
                        else
                            E = String.valueOf(tempInt);
                    } else {
                        E = "00";
                        if (D.charAt(2) != 'Z') {
                            tempD = D.charAt(2);
                            tempD++;
                            temp = D.substring(0, 2) + tempD + D.substring(3);
                            D = temp;
                        } else if (D.charAt(1) != 'Z') {
                            tempD = D.charAt(1);
                            tempD++;
                            temp = D.substring(0, 1) + tempD + D.substring(2);
                            D = temp;
                        } else if (D.charAt(0) != 'Z') {
                            tempD = D.charAt(0);
                            tempD++;
                            temp = tempD + D.substring(1);
                            D = temp;
                        } else
                            System.out.println("Number of Patient Records Exceeded!");
                    }
            }
        }
        String patientCodeIdentifier = A + B + C + D + E;
        return patientCodeIdentifier;
    }

    public void addNewPatient(){
        Scanner scanner = new Scanner(System.in);

//        String patientCodeIdentifier = generateUID();
        String patientCodeIdentifier = "ABCDE";
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Middle Name: ");
        String middleName = scanner.nextLine();
        System.out.print("Birthday(YYYYMMDD): ");
        String birthday = scanner.next();
//        String tempBirthday = scanner.next();
//        Date birthday= null;
//        try {
//            birthday = new SimpleDateFormat("yyyyMMdd").parse(tempBirthday);
//        } catch (ParseException e) {
//            System.out.println("Invalid format! Please try again.");
//            addNewPatient();
//        }
        System.out.print("Gender: ");
        String gender = scanner.next();
        scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        long temp = 0;
        try {
            System.out.print("Phone No.: ");
            temp = scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid format! Please try again.");
            addNewPatient();
        }
        long phoneNo = temp;
        try {
            System.out.print("National ID No.: ");
            temp = scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid format! Please try again.");
            addNewPatient();
        }
        long nationalIdNo = temp;
        System.out.print("Save Patient Record?[Y/N]: ");
        String input = scanner.next();
        String yes = "Y";
        Patient patient = new Patient(patientCodeIdentifier, firstName, lastName, middleName, birthday, gender, address, phoneNo, nationalIdNo);
        patients.add(patient);

        System.out.println(patients.size());

        if(Objects.equals(input, yes)) {
            String fileName = "Patients.txt";
            error = wtf.writeToFile(fileName, patient, 1);
            if(error == 1)
                addNewPatient();
            System.out.println();
            System.out.println("Successfully added patient record!");
            System.out.println("Would you like to add another patient or return to the main menu?");
            System.out.println("[1] Add new patient");
            System.out.println("[2] Return to the Main Menu");
            System.out.print("Select a transaction: ");
            int ans = scanner.nextInt();
            System.out.println();
            if(ans == 1)
                addNewPatient();
            else
                mm.mainMenu();
        }
        else {
            System.out.println();
            System.out.println("Patient record not added.");
            System.out.println("Would you like to add another patient or return to the main menu?");
            System.out.println("[1] Add new patient");
            System.out.println("[2] Return to the Main Menu");
            System.out.print("Select a transaction: ");
            int ans = scanner.nextInt();
            System.out.println();
            if(ans == 1)
                addNewPatient();
            else
                mm.mainMenu();
        }

    }

    public void searchPatientRecord() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.print("Enter patient information to search: ");
        input = scanner.nextLine();

        File file = new File("Patients.txt");

        if(input.length() == 12) {
            // try catch while to scan txt file for Patient's UID or National ID No. 0, 8
            try {
                Scanner scannerFile = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String temp = scanner.nextLine();
                    String[] line = temp.split(";");
                    if(line[0] == input || line[8] == input) {
                        char check = line[9].charAt(0); //check if file is deleted
                        if (check == 'D')
                            continue;
                        else {
                            // display record
                            break;                  //not sure what happens here
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("No record found.");
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Search again");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                int ans = scanner.nextInt();
                System.out.println();
                if(ans == 1)
                    searchPatientRecord();
                else
                    mm.mainMenu();
            }
        }
        else {
            String[] inputSplit = input.split("\\s");
            int lineNos[] = null; //store line numbers

            // try catch while to scan txt file for last name, first name, birthday combination 1,2,4
            try {
                Scanner scannerFile = new Scanner(file);
                int lineNo = 0;
                int i = 0;

                while (scanner.hasNextLine()) {
                    String temp = scanner.nextLine();
                    String[] line = temp.split(";");

                    if ((inputSplit[0] == line[1]) && (inputSplit[1] == line[2]) && (inputSplit[2] == line[4])) {
                        char check = line[9].charAt(0); //check if file is deleted
                        if (check == 'D')
                            continue;
                        else {
                            lineNos[i] = lineNo;
                            i++;
                        }
                    }
                    lineNo++;
                }

                if(!scanner.hasNextLine()) {
                    if(lineNos.length > 1) {
                        System.out.println("Patient's UID \tLast Name \tFirst Name \tMiddle Name \tBirthday \tGender\t Address \tPhone Number \tNational ID. No");
                        for (int j = 0; j < lineNos.length; j++) {
                            System.out.println("%s \t%s \t%s \t%s \t%s \t%c\t %s \t%d \t%d");
                        }
                        System.out.println("\nEnter the patient's UID that you want to display: ");
                        String input1 = scanner.next();
                        try {
                            scannerFile = new Scanner(file);

                            while (scanner.hasNextLine()) {
                                String temp = scanner.nextLine();
                                String[] line = temp.split(";");
                                if(line[0] == input1) {
                                    char check = line[9].charAt(0); //check if file is deleted
                                    if (check == 'D')
                                        continue;
                                    else {
                                        // display record
                                        break;                  //not sure what happens here
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("No record found.");
                            System.out.println("Would you like to search again or return to the main menu?");
                            System.out.println("[1] Search again");
                            System.out.println("[2] Return to the Main Menu");
                            System.out.print("Select a transaction: ");
                            int ans = scanner.nextInt();
                            System.out.println();
                            if(ans == 1)
                                searchPatientRecord();
                            else
                                mm.mainMenu();
                        }
                    }
                    else {
                        //display record
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("No record found.");
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Search again");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                int ans = scanner.nextInt();
                System.out.println();
                if(ans == 1)
                    searchPatientRecord();
                else
                    mm.mainMenu();
            }
        }
    }

    public void deletePatientRecord() {
        //ReadFile
    }

    public void editPatientRecord() {
        //ReadFile
    }

    public ArrayList<Patient> getPatients()    {
        return patients;
    }

//    public int writeToFile(String fileName, Patient patient) {
//        return error;
//    }

}

