import java.util.*;

public class ManagePatientRecords {
    ArrayList<Patient> patients = new ArrayList<>();

    //adding patients
    WriteToFile wtf = new WriteToFile();
    MainMenu mm = new MainMenu();
    private String savePatientRecord;
    private int patientCounter = 0;
    String[] tempUID = new String[12];

    //deleting records
    String reason;
    String newLine;

    //general
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
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        tempUID[0] = "P";

        int B = Calendar.getInstance().get(Calendar.YEAR);
        String temp = String.valueOf(B);
        char[] cTemp = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++)
            tempUID[i + 1] = String.valueOf(cTemp[i]);

        int C = Calendar.getInstance().get(Calendar.MONTH)+1;
        temp = String.valueOf(C);
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++) {
            if (C > 9)
                tempUID[i + 5] = String.valueOf(cTemp[i]);
            else {
                tempUID[5] = "0";
                tempUID[6] = String.valueOf(cTemp[i]);
            }
        }

        if (patientCounter == 0) {               // if it's the first entry
            for (int i = 0; i < 3; i++)
                tempUID[i + 7] = "A";         //D
            for (int i = 0; i < 2; i++)
                tempUID[i + 10] = "0";        //E
        } else if (day == 1) {                // if it's the first day
            patientCounter = 0;
            for (int i = 0; i < 3; i++)
                tempUID[i + 7] = "A";         //D
            for (int i = 0; i < 2; i++)
                tempUID[i + 10] = "0";        //E
        } else {
            if (patientCounter != 99) {
                if(patientCounter<9) {
                    int iTemp = patientCounter;
                    tempUID[11] = String.valueOf(iTemp);
                }
                else {
                    int iTemp = patientCounter;
                    String sTemp = Integer.toString(iTemp);
                    tempUID[10] = String.valueOf(sTemp.charAt(0));
                    tempUID[11] = String.valueOf(sTemp.charAt(1));
                }
            }
            else {
                tempUID[10] = "0";
                tempUID[11] = "0";
                patientCounter = 0;
                if (tempUID[9] != "Z") {
                    char cTemp1 = tempUID[9].charAt(0);
                    cTemp1++;
                    tempUID[9] = String.valueOf(cTemp1);
                } else if (tempUID[8] != "Z") {
                    char cTemp1 = tempUID[8].charAt(0);
                    cTemp1++;
                    tempUID[8] = String.valueOf(cTemp1);
                } else {
                    char cTemp1 = tempUID[7].charAt(0);
                    cTemp1++;
                    tempUID[7] = String.valueOf(cTemp1);
                }
            }
        }

        String patientCodeIdentifier = String.join("",tempUID[0],tempUID[1],
                tempUID[2], tempUID[3], tempUID[4], tempUID[5], tempUID[6],
                tempUID[7], tempUID[8], tempUID[9], tempUID[10], tempUID[11]);

        return patientCodeIdentifier;
    }

    public void addNewPatient(){
        Scanner scanner = new Scanner(System.in);

        String patientCodeIdentifier = generateUID();
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Middle Name: ");
        String middleName = scanner.nextLine();
        System.out.print("Birthday(YYYYMMDD): ");
        String birthday = scanner.next();
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
        savePatientRecord = scanner.next();
        String yes = "Y";
        Patient patient = new Patient(patientCodeIdentifier, lastName, firstName, middleName, birthday, gender, address, phoneNo, nationalIdNo);
        patients.add(patient);

        System.out.println(patients.size());

        if(Objects.equals(savePatientRecord, yes)) {
            patientCounter++;
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
        String fileName = "Patients.txt";

        ReadFile rf = new ReadFile();
        int error = rf.readFile(fileName, 1, 0);
        String[][] patients = rf.getTempSearch();
        if(error==1)
            searchPatientRecord();

        int searched = 0;

        System.out.print("Do you know the patient's UID?[Y/N]: ");
        String input = scanner.next();
        if(input == "Y") {                                                            // UID
            System.out.print("Enter patient's UID: ");
            String searchUID = scanner.next();
            for(int i=0; i<patients.length; i++) {
                if(patients[i][0] == searchUID) {
                    searched = 1;
                    int line = i;
                    break;
                }
            }
        } else {
            System.out.print("Do you know the patient's National ID no.?[Y/N]: ");  // National ID
            input = scanner.next();
            if(input == "Y") {
                System.out.print("Enter patient's National ID no.: ");
                String searchID = scanner.next();
                for(int i=0; i<patients.length; i++) {
                    if(patients[i][8] == searchID) {
                        searched = 1;
                        int line = i;
                        break;
                    }
                }
            } else {
                scanner.nextLine();
                System.out.print("Enter patient's Last name: ");                    // f, l, birthday combi 1,2,4
                String searchLast = scanner.nextLine();
                System.out.print("Enter patient's First name: ");
                String searchFirst = scanner.nextLine();
                System.out.print("Enter patient's Birthday(YYYYMMDD): ");
                String searchBirthday = scanner.next();
                int[] lines = new int[256];
                for(int i=0; i<patients.length; i++) {
                    if(patients[i][1]==searchLast && patients[i][2]==searchFirst && patients[i][4]==searchBirthday) {
                        lines[searched] = i;
                        searched++;
                    }
                }
                if(lines.length>1) {
                    System.out.println("Patient's UID \tLast Name \tFirst Name \tMiddle Name \tBirthday \tGender\t Address \tPhone Number \tNational ID. No");
                    for(int i =0; i<lines.length; i++) {
                        for(int j=0; j<9; j++) {
                            System.out.print(patients[i][j] + " \t");
                        }
                        System.out.println();
                    }
                    System.out.print("Enter the patient's UID you want to display: ");
                    input = scanner.next();
                    searched = 0;
                    for(int i=0; i<patients.length; i++) {
                        if(patients[i][0] == input) {
                            searched = 1;
                            int line = i;
                            break;
                        }
                    }
                }
                else {
                    int line = lines[0];
                }
            }
        }
        if(searched==0) {
            System.out.println("No record found.");
            System.out.println("Would you like to search again or return to the main menu?");
            System.out.println("[1] Search for another patient record");
            System.out.println("[2] Return to the Main Menu");
            System.out.print("Select a transaction: ");
            int ans = scanner.nextInt();
            System.out.println();
            if(ans == 1)
                searchPatientRecord();
            else
                mm.mainMenu();
        }
        else {
            System.out.println("Request's UID \t\tLab Test Type \t\tRequest Date \t\tResult");
            //print from manage laboratory request
            //print from manage service request
            System.out.println("Do you want to print a laboratory test result? [Y/N]");
            input = scanner.next();
            if(input == "Y") {
                System.out.print("Enter Request's UID: ");
                String reqUID = scanner.next();

                //print laboratory request
            } else {
                mm.mainMenu();
            }
        }
    }

    public void deletePatientRecord() {
        Scanner scanner = new Scanner(System.in);
        String fileName = "Patients.txt";

        ReadFile rf = new ReadFile();
        int error = rf.readFile(fileName, 1, 0);
        String[][] patients = rf.getTempSearch();
        if(error==1)
            searchPatientRecord();

        int searched = 0;
        int line = 0;
        System.out.print("Do you know the patient's UID?[Y/N]: ");
        String input = scanner.next();
        if(input == "Y") {                                                            // UID
            System.out.print("Enter patient's UID: ");
            String searchUID = scanner.next();
            for(int i=0; i<patients.length; i++) {
                if(patients[i][0] == searchUID) {
                    searched = 1;
                    line = i;
                    break;
                }
            }
        } else {
            System.out.print("Do you know the patient's National ID no.?[Y/N]: ");  // National ID
            input = scanner.next();
            if(input == "Y") {
                System.out.print("Enter patient's National ID no.: ");
                String searchID = scanner.next();
                for(int i=0; i<patients.length; i++) {
                    if(patients[i][8] == searchID) {
                        searched = 1;
                        line = i;
                        break;
                    }
                }
            } else {
                scanner.nextLine();
                System.out.print("Enter patient's Last name: ");                    // f, l, birthday combi 1,2,4
                String searchLast = scanner.nextLine();
                System.out.print("Enter patient's First name: ");
                String searchFirst = scanner.nextLine();
                System.out.print("Enter patient's Birthday(YYYYMMDD): ");
                String searchBirthday = scanner.next();
                int[] lines = new int[256];
                for(int i=0; i<patients.length; i++) {
                    if(patients[i][1]==searchLast && patients[i][2]==searchFirst && patients[i][4]==searchBirthday) {
                        lines[searched] = i;
                        searched++;
                    }
                }
                if(lines.length>1) {
                    System.out.println("Patient's UID \tLast Name \tFirst Name \tMiddle Name \tBirthday \tGender\t Address \tPhone Number \tNational ID. No");
                    for(int i =0; i<lines.length; i++) {
                        for(int j=0; j<9; j++) {
                            System.out.print(patients[i][j] + " \t");
                        }
                        System.out.println();
                    }
                    System.out.print("Enter the patient's UID you want to display: ");
                    input = scanner.next();
                    searched = 0;
                    for(int i=0; i<patients.length; i++) {
                        if(patients[i][0] == input) {
                            searched = 1;
                            line = i;
                            break;
                        }
                    }
                } else {
                    line = lines[0];
                }
            }
        }
        //check if a record was found
        if(searched==0) {
            System.out.println("No record found.");
            System.out.println("Would you like to search again or return to the main menu?");
            System.out.println("[1] Search for another patient record");
            System.out.println("[2] Return to the Main Menu");
            System.out.print("Select a transaction: ");
            int ans = scanner.nextInt();
            System.out.println();
            if(ans == 1)
                deletePatientRecord();
            else
                mm.mainMenu();
        } else {
            scanner.nextLine();
            System.out.println("Please state reason for deletion: ");
            reason = scanner.nextLine();
            String D = "D;";
            newLine = String.join("", D, reason);
            error = rf.readFile(fileName, 2, line);
            if(error == 1)
                deletePatientRecord();
            System.out.println();
            System.out.println("Would you like to delete another patient record? [Y/N]: ");
            String ans = scanner.next();
            System.out.println();
            if(ans == "Y")
                deletePatientRecord();
            else
                mm.mainMenu();
        }
    }

    public void editPatientRecord() {
        //ReadFile
    }


    public ArrayList<Patient> getPatients()    {
        return patients;
    }

    public String getNewLine() {
        return newLine;
    }

}

