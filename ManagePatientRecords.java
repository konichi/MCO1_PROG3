import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ManagePatientRecords {
    ArrayList<Patient> patients = new ArrayList<>();

    ManageLaboratoryRequest mlr = new ManageLaboratoryRequest();
    WriteToFile wtf = new WriteToFile();
    ReadFile rf = new ReadFile();
    MainMenu mm = new MainMenu();

    public void managePatientRecords(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Manage Patient Records");
        System.out.println("[1] Add New Patient");
        System.out.println("[2] Edit Patient Record");
        System.out.println("[3] Delete Patient Record");
        System.out.println("[4] Search Patient Record");
        System.out.println("[X] Return to Main Menu");
        System.out.print("Select a transaction: ");
        String input = scanner.next().toUpperCase();
        System.out.println();

        switch (input) {
            case "1" -> addNewPatient();
            case "2" -> editPatientRecord();
            case "3" -> deletePatientRecord();
            case "4" -> searchPatientRecord();
            case "X" -> mm.mainMenu();
            default -> managePatientRecords();
        }
    }

    public String generateUID() {
        String[] tempUID = new String[7];
        tempUID[0] = "P";                                           //A 0

        int B = Calendar.getInstance().get(Calendar.YEAR);
        String temp = String.valueOf(B);
        char[] cTemp = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++)
            tempUID[i + 1] = String.valueOf(cTemp[i]);              //B 1, 2, 3, 4

        int C = Calendar.getInstance().get(Calendar.MONTH)+1;
        temp = String.valueOf(C);
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++) {
            if (C > 9)
                tempUID[i + 5] = String.valueOf(cTemp[i]);          //C 5, 6
            else {
                tempUID[5] = "0";
                tempUID[6] = String.valueOf(cTemp[i]);
            }
        }

        //GET UID FROM PATIENTS.TXT
        String fileName = "Patients.txt";
        int isFirst = rf.readUID(fileName);
        String prevUID = rf.getUID();

        String D;
        String E;
        String newUID;

        //CHECK IF FIRST UID TO BE GENERATED
        if(isFirst==1){
            newUID = "AAA00";
            String str = String.join("", tempUID);
            return String.join("",str, newUID);
        }
        else {
            D = prevUID.substring(7, 10);
            E = prevUID.substring(prevUID.length()-2);
        }

        //CHECK DDD - AAA/ZZZ and EE - 00/99
        int num = Integer.parseInt(E);
        char strTemp;
        int numTemp;
        if(num==99){
            if(D.charAt(2)!='Z' && D.charAt(2)<='Z') {              //last D
                strTemp = D.charAt(2);
                strTemp++;
                D = D.substring(0, D.length()-1);
                D = String.join("", D, String.valueOf(strTemp));
            } else if (D.charAt(1)!='Z' && D.charAt(1)<='Z') {
                strTemp = D.charAt(1);
                strTemp++;
                D = D.substring(0, D.length()-2);
                D = String.join("", D, String.valueOf(strTemp), "Z");
            } else {
                strTemp = D.charAt(0);
                strTemp++;
                D = String.join("", String.valueOf(strTemp), "ZZ");
            }
            E = "00";
        } else {
            numTemp = num;
            if(numTemp<9) {
                numTemp++;
                strTemp = '0';
                E = String.join("", String.valueOf(strTemp), String.valueOf(numTemp));
            } else {
                numTemp++;
                E = String.valueOf(numTemp);
            }
        }
        newUID = String.join("", D, E);

        String str = String.join("", tempUID);
        return String.join("", str, newUID);

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
        String gender = scanner.next().toUpperCase();
        gender = String.valueOf(gender.charAt(0));
        scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone No.: ");
        String phoneNo = scanner.next();
        long temp = 0;
        System.out.print("National ID No.: ");
        try {
            temp = scanner.nextLong();
        } catch (InputMismatchException e) {
            System.out.println("Invalid format! Please try again.");
            addNewPatient();
        }
        long nationalIdNo = temp;

        String savePatientRecord;
        do {
            System.out.print("Save Patient Record?[Y/N]: ");
            savePatientRecord = scanner.next().toUpperCase();

            if(!savePatientRecord.equals("Y") && !savePatientRecord.equals("N"))
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!savePatientRecord.equals("Y") && !savePatientRecord.equals("N"));

        Patient patient = new Patient(patientCodeIdentifier, lastName, firstName, middleName, birthday, gender, address, phoneNo, nationalIdNo);
        patients.add(patient);

        String input;
        if(savePatientRecord.equals("Y")) {
            String fileName = "Patients.txt";
            int error = wtf.writeToPatients(fileName, patient);
            if(error == 1)
                addNewPatient();
            System.out.println("Successfully added patient record!");
        }
        else
            System.out.println("Patient record not added.");

        System.out.println();
        do {
            System.out.println("Would you like to add another patient or return to the main menu?");
            System.out.println("[1] Add new patient");
            System.out.println("[2] Return to the Main Menu");
            System.out.print("Select a transaction: ");
            input = scanner.next().toUpperCase();
            System.out.println();
            if(!input.equals("1") && !input.equals("2"))
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("1") && !input.equals("2"));
        if(input.equals("1"))
            addNewPatient();
        else
            mm.mainMenu();

    }

    public void searchPatientRecord() {
        Scanner scanner = new Scanner(System.in);

        int line=searchRecord();
        String input;

        // get all files in Patients.txt and save to String[][] patients
        String fileName = "Patients.txt";
        int error = rf.readPatients(fileName);
        if(error==1)
            searchPatientRecord();
        String[][] patients = rf.getTempSearch();

        if(line==-1)
            searchPatientRecord();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Search for another patient record");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    searchPatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                searchPatientRecord();
            else
                mm.mainMenu();
        } else {
            String patientUID = patients[line][0];
            System.out.println();
            System.out.println("Patient's UID: \t\t" + patients[line][0]);
            System.out.println("Name: \t\t\t\t" + patients[line][1] + ", " + patients[line][2] + " " + patients[line][3]);
            System.out.println("Birthday: \t\t\t" + patients[line][4]);
            System.out.println("Address: \t\t\t" + patients[line][6]);
            System.out.println("Phone Number: \t\t" + patients[line][7]);
            System.out.println("National ID no.: \t" + patients[line][8]);

            // get all lines in services.txt and save to String[][] services
            fileName = "services.txt";
            error = rf.readServices(fileName);
            if(error==1)
                searchPatientRecord();
            String[][] services = rf.getTempSearch();
            // count all services
            int count = 0;
            for (String[] service : services)
                for (int j = 0; j < services[0].length; j++)
                    if (service[0] != null && service[0].length() == 3) {
                        count++;
                        break;
                    }

            System.out.println();
            System.out.println("Request's UID \t\t\tLab Test Type \t\t\tRequest Date \t\t\tResult");
            for(int i=0; i<count; i++) {
                String code = services[i][0];
                fileName = String.join("",code, "_Requests.txt");
                error = rf.readRequests(fileName);
                if(error==1)
                    searchPatientRecord();
                String[][] requests = rf.getTempSearch();

                int length = 0;
                for (String[] request : requests)
                    for (int j = 0; j < services[0].length; j++)
                        if (request[0] != null && request[0].length() == 15) {
                            length++;
                            break;
                        }

                for(int j=0; j<length; j++)
                    if (patientUID.equals(requests[j][1]))
                        System.out.println(requests[j][0] + "\t\t\t" + code + "\t\t\t\t\t\t" + requests[j][2] + "\t\t\t\t\t" + requests[j][4]);
            }
            System.out.println();
            System.out.print("Do you want to print a laboratory test result? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y")) {
                String[] ret = mlr.searchLaboratoryRequest(2);

                fileName = "Patients.txt";
                rf.readPatients(fileName);
                patients = rf.getTempSearch();

                String name = patients[line][1] + ", " + patients[line][2] + " " + patients[line][3];
                String pUID = patients[line][0];
                String gender = patients[line][5];
                String sUID = ret[0];
                String cDate = ret[1];
                String birthday = patients[line][4];
                String phoneNo = patients[line][7];
                String test = ret[3];
                String result = ret[2];

                // calculating age
                int birthYear = Integer.parseInt(birthday.substring(0, 4));
                int birthMonth = Integer.parseInt(birthday.substring(4, 6));
                int birthDay = Integer.parseInt(birthday.substring(birthday.length()-2));
                LocalDate start = LocalDate.of(birthYear, birthMonth, birthDay); // use for age-calculation: LocalDate.now()
                LocalDate end = LocalDate.now(ZoneId.systemDefault());
                long age = ChronoUnit.YEARS.between(start, end);

                //print pdf here
            } else if(input.equals("N"))
                mm.mainMenu();
            else {
                System.out.println("Invalid input! Please try again.");
                searchPatientRecord();
            }
        }
    }

    public void deletePatientRecord() {
        Scanner scanner = new Scanner(System.in);

        int line=searchRecord();
        String input;

        if(line==-1)
            deletePatientRecord();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Delete a patient record");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    deletePatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                deletePatientRecord();
            else
                mm.mainMenu();
        } else {
            System.out.print("Please state reason for deletion: ");
            String reason = scanner.nextLine();
            String D = "D;";
            String newLine = String.join("", D, reason);

            String fileName = "Patients.txt";
            try {
                File file = new File(fileName);
                Scanner scannerFile = new Scanner(file);

                String tempLine = Files.readAllLines(Paths.get(fileName)).get(line);
                StringBuilder buffer = new StringBuilder();
                while(scannerFile.hasNextLine()) {
                    buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
                }
                String fileContents = buffer.toString();
                scannerFile.close();

                String line1 = String.join("",tempLine,newLine,";");
                fileContents = fileContents.replaceAll(tempLine,line1);
                FileWriter fw = new FileWriter(fileName);
                fw.append(fileContents);
                fw.flush();

                String[] splitLine = tempLine.split(";");
                String UID = splitLine[0];

                System.out.println("Data of patient " + UID + " has been deleted.");
            } catch(IOException e) {
                System.out.println("Error occurred. Please try again.\n");
                deletePatientRecord();
            }
            do {
                System.out.print("Do you want to delete another patient record? [Y/N]: ");
                input = scanner.next().toUpperCase();
                if(input.equals("Y"))
                    deletePatientRecord();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

    public void editPatientRecord() {
        Scanner scanner = new Scanner(System.in);

        int line=searchRecord();
        String input;
        int update=0;

        if(line==-1)
            editPatientRecord();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Edit another patient record");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    editPatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                editPatientRecord();
            else
                mm.mainMenu();
        } else {
            do {
                System.out.println("Would you like to update the patient's Address or Phone Number?");
                System.out.println("[1] Address");
                System.out.println("[2] Phone Number");
                System.out.print("Select information to update: ");
                input = scanner.next();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    editPatientRecord();
                }
            } while(!input.equals("1") && !input.equals("2"));
            System.out.println();
            if(input.equals("1"))
                update = 1;
            else
                update = 2;
        }

        String newLine = "";
        scanner.nextLine();
        switch (update) {
            case 1 -> {
                System.out.print("Enter the patient's new Address: ");
                newLine = scanner.nextLine();
            }
            case 2 -> {
                System.out.print("Enter the patient's new Phone Number: ");
                newLine = scanner.next();
            }
        }
        String fileName = "Patients.txt";
        try {
            File file = new File(fileName);
            Scanner scannerFile = new Scanner(file);

            String tempLine = Files.readAllLines(Paths.get(fileName)).get(line);
            StringBuilder buffer = new StringBuilder();
            while(scannerFile.hasNextLine()) {
                buffer.append(scannerFile.nextLine()).append(System.lineSeparator());
            }
            String fileContents = buffer.toString();
            scannerFile.close();

            String[] splitLine = tempLine.split(";");
            if(update==1)
                splitLine[6] = newLine;
            else
                splitLine[7] = newLine;

            String line1 = String.join(";", splitLine);
            line1 = String.join("", line1, ";");

            fileContents = fileContents.replaceAll(tempLine,line1);
            FileWriter fw = new FileWriter(fileName);
            fw.append(fileContents);
            fw.flush();

            String UID = splitLine[0];
            System.out.println("The Address/Phone Number of patient " + UID + " has been updated.");
        } catch (IOException e) {
            System.out.println("Error occurred. Please try again.\n");
            editPatientRecord();
        }

        do {
            System.out.print("Do you want to edit another patient record? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                editPatientRecord();
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

    public int searchRecord() {
        Scanner scanner = new Scanner(System.in);

        int scan;
        int line = 0;
        String input;

        System.out.print("Do you know the patient's UID?[Y/N]: ");
        input = scanner.next().toUpperCase();
        if(input.equals("Y"))
            scan = 1;
        else if(input.equals("N")){
            System.out.print("Do you know the patient's National ID no.?[Y/N]: ");  // National ID
            input = scanner.next().toUpperCase();
            if(input.equals("Y")) {
                scan = 2;
            } else if(input.equals("N"))
                scan = -1;
            else {
                System.out.println("Invalid input. Please try again.");
                System.out.println();
                return -1;
            }
        } else {
            System.out.println("Invalid input. Please try again.");
            System.out.println();
            return -1;
        }
        if(scan!=1 && scan!=2)
            scan = 3;

        // get all lines in Patients.txt and save to String[][] patients
        String fileName = "Patients.txt";
        int error = rf.readPatients(fileName);
        if(error==1)
            return -1;
        String[][] patients = rf.getTempSearch();

        // count total non-null entries in String[][] patients
        int count = 0;
        for (String[] patient : patients)
            if (patient[0] != null)
                count++;

        // switch case
        // get input from user to search match/es in String Patients[][]
        int searched = 0;
        int[] lines = new int[256];
        String searchLast = null;
        String searchFirst = null;
        String searchBirthday = null;
        switch (scan) {
            case 1 -> {
                System.out.print("Enter patient's UID: ");
                String searchUID = scanner.next().toUpperCase();
                for (int i = 0; i < count; i++)
                    if(Objects.equals(patients[i][0], searchUID) && !Objects.equals(patients[i][9], "D")) {
                        searched = 1;
                        lines[0] = i;
                        break;
                    } else
                        lines[0] = -2;
            }
            case 2 -> {
                System.out.print("Enter patient's National ID no.: ");
                String searchID;
                try {
                    searchID = scanner.next();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format! Please try again");
                    System.out.println();
                    return -1;
                }
                for (int i = 0; i < count; i++)
                    if (Objects.equals(patients[i][8], searchID) && !Objects.equals(patients[i][9], "D")) {
                        searched = 1;
                        lines[0] = i;
                        break;
                    } else
                        lines[0] = -2;
            }
            case 3 -> {
                scanner.nextLine();
                System.out.print("Enter patient's Last name: ");
                searchLast = scanner.nextLine();
                System.out.print("Enter patient's First name: ");
                searchFirst = scanner.nextLine();
                System.out.print("Enter patient's Birthday(YYYYMMDD): ");
                searchBirthday = scanner.next();

                for (int i = 0; i < count; i++) {
                    try {
                        if (!Objects.equals(patients[i][9], "D") && patients[i][1].equalsIgnoreCase(searchLast) && patients[i][2].equalsIgnoreCase(searchFirst) && patients[i][4].equalsIgnoreCase(searchBirthday)) {
                            lines[searched] = i;
                            searched++;
                        }
                    } catch (NullPointerException ignored) {}
                }
            }
            default -> {
                System.out.println("An error occurred. Please try again.");
                System.out.println();
                return -1;
            }
        }

        // if there is only 1 match search, assign line number to line
        // else: ask user to input the patient's UID to display
        if(searched==0)
            return -2;
        else if(searched>1) {
            System.out.println();
            System.out.printf("%-16s", "Patient's UID");
            System.out.printf("%-15s", "Last Name");
            System.out.printf("%-15s", "First Name");
            System.out.printf("%-15s", "Middle Name");
            System.out.printf("%-15s", "Birthday");
            System.out.printf("%-15s", "Gender");
            System.out.printf("%-15s", "Address");
            System.out.printf("%-15s", "Phone Number");
            System.out.printf("%-15s", "National ID. No");
            System.out.println();
            for(int i = 0; i<lines.length; i++)
                for (int j = 0; j < 9; j++)
                    if(patients[i][0] != null) {
                        if (patients[i][1].equalsIgnoreCase(searchLast) && patients[i][2].equalsIgnoreCase(searchFirst) && patients[i][4].equalsIgnoreCase(searchBirthday)) {
                            if(j==0)
                                System.out.printf("%-16s", patients[i][j]);
                            System.out.printf("%-15s", patients[i][j]);
                            if (j == 8)
                                System.out.println();
                        }
                    }
            System.out.println();
            System.out.print("Enter the patient's UID: ");
            input = scanner.next().toUpperCase();
            System.out.println();
            for(int i=0; i< patients.length; i++)
                if (Objects.equals(patients[i][0], input)) {
                    line = i;
                    searched = 1;
                    break;
                }
            if(searched!=1)
                return -2;
        } else
            line = lines[0];

        return line;
    }

}