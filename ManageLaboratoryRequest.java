import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Scanner;

public class ManageLaboratoryRequest {
    ArrayList<Request> requests = new ArrayList<>();
    WriteToFile wtf = new WriteToFile();
    MainMenu mm = new MainMenu();
    ReadFile rf = new ReadFile();


    public void manageLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Manage Laboratory Request");
        System.out.println("[1] Add New Laboratory Request");
        System.out.println("[2] Search Laboratory Request");
        System.out.println("[3] Delete Patient Record");
        System.out.println("[4] Edit Laboratory Request");
        System.out.println("[X] Return to Main Menu");
        System.out.print("Select a transaction: ");
        String input = scanner.next().toUpperCase();
        System.out.println();

        switch (input) {
            case "1" -> addNewLaboratoryRequest();
            case "2" -> editLaboratoryRequest();
            case "3" -> deleteLaboratoryRequest();
            case "4" -> searchLaboratoryRequest();
            case "X" -> mm.mainMenu();
            default -> manageLaboratoryRequest();
        }

    }

    public String generateUID(String code) {
        String[] tempUID = new String[8];

        int Y = Calendar.getInstance().get(Calendar.YEAR);          //Y 0-3
        String temp = String.valueOf(Y);
        char[] cTemp = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++)
            tempUID[i] = String.valueOf(cTemp[i]);

        int M = Calendar.getInstance().get(Calendar.MONTH)+1;       //M 4,5
        temp = String.valueOf(M);
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++) {
            if (M > 9)
                tempUID[i + 4] = String.valueOf(cTemp[i]);
            else {
                tempUID[4] = "0";
                tempUID[5] = String.valueOf(cTemp[i]);
            }
        }

        int D = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);  //D 6,7
        temp = String.valueOf(D);
        for (int i = 0; i < temp.length(); i++)
            cTemp[i] = temp.charAt(i);
        for (int i = 0; i < temp.length(); i++) {
            if (D > 9)
                tempUID[i + 6] = String.valueOf(cTemp[i]);
            else {
                tempUID[6] = "0";
                tempUID[7] = String.valueOf(cTemp[i]);
            }
        }

        //GET REQUESTS FROM <CODE>_REQUESTS.TXT
        String fileName = code + "_Requests.txt";
        int isFirst = rf.readUID(fileName);
        String prevUID = rf.getUID();

        String A;
        String B;
        String newUID;

        //CHECK IF FIRST UID TO BE GENERATED
        if(isFirst==1){
            newUID = "AAA00";
            String str = String.join("", tempUID);
            return String.join("", str, newUID);
        }
        else {
            A = prevUID.substring(11, 13);
            B = prevUID.substring(prevUID.length()-2);
        }

        //CHECK AA - AA/ZZ and BB - 00/99
        int num = Integer.parseInt(B);
        char strTemp;
        int numTemp;
        if(num==99) {
            if (A.charAt(1)!='Z' && A.charAt(1)<='Z') {
                strTemp = A.charAt(1);
                strTemp++;
                A = String.join("", "A", String.valueOf(strTemp));
            } else {
                strTemp = A.charAt(0);
                strTemp++;
                A = String.join("", String.valueOf(strTemp), "Z");
            }
        } else {
            numTemp = num;
            if(numTemp<9) {
                numTemp++;
                strTemp = '0';
                B = String.join("", String.valueOf(strTemp), String.valueOf(numTemp));
            } else {
                numTemp++;
                B = String.valueOf(numTemp);
            }
        }
        newUID = String.join("", A, B);
        String str = String.join("", tempUID);
        return String.join("", code, str, newUID);
    }

    public void addNewLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter patient's UID: ");
        String UID = scanner.next().toUpperCase();
        String pFile = "Patients.txt";
        int pExists = rf.checkUID(pFile, UID);

        // get input for service code
        System.out.print("Enter service code: ");
        String code = scanner.next().toUpperCase();
        // check if code exists in file services

        String input;
        if(pExists!=1) {
            System.out.println("Patient Record does not exist!");
            System.out.print("Would you like to search for another patient or return to the main menu?");
            System.out.println("[1] Search again");
            System.out.println("[2] Return to the Main Menu");
            do {
                System.out.print("Select a transaction: ");
                input = scanner.next();

                if(input.equals("1"))
                    addNewLaboratoryRequest();
                else if(input.equals("2"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("1") && !input.equals("2"));
        } else {
            //check if code exists
        }

        String requestUID = generateUID(code);
        int requestDate = Calendar.getInstance().get(Calendar.YEAR);
        int temp1 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String str1 = null;
        if(temp1<9)
            str1 = "0" + temp1;
        String str2 = null;
        int temp2 = Calendar.getInstance().get(Calendar.MINUTE);
        if(temp2<9)
            str2 = "0" + temp2;
        String requestTime = str1 + str2;
        String result = "XXX";
        scanner.nextLine();

        Request request = new Request(requestUID, UID, requestDate, requestTime, result);
        requests.add(request);

        String fileName = code+"_Requests.txt";
        int error = wtf.writeToLabRequests(fileName, request);
        if(error==1)
            addNewLaboratoryRequest();
        System.out.println();
        System.out.println("Laboratory Request " + requestUID + " has been added to file " + fileName);

        do {
            System.out.print("Do you want to add another Laboratory Request? [Y/N]: ");
            input = scanner.next().toUpperCase();

            if(input.equals("Y"))
                addNewLaboratoryRequest();
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"));
    }

    public void searchLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);

        String input;
        int scan = 0;
        do {
            System.out.print("Do you know the request's UID?[Y/N]: ");
            input = scanner.next().toUpperCase();
            if (input.equals("Y"))
                scan = 1;
            else if(input.equals("N"))
                scan = 2;
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));

        String UID = "";
        String fileName = "";
        switch (scan) {
            case 1 -> {
                System.out.print("Enter request's UID: ");
                UID = scanner.next().toUpperCase();
                String temp = UID.substring(0,3);
                fileName = String.join(temp + "_Requests.txt");
            }
            case 2 -> {
                System.out.println("Enter patient's UID: ");
                UID = scanner.next().toUpperCase();
                fileName = "Patients.txt";
            }
            default -> {
                System.out.println("Invalid input! Please try again.");
                searchLaboratoryRequest();
            }
        }
        int error = rf.readFile(fileName);
        if(error==1)
            searchLaboratoryRequest();
        String[][] records = rf.getTempSearch();

        int count = 0;
        for(int i=0; i<records.length; i++)
            for(int j=0; j<records[0].length; j++)
                if(records[j] != null)
                    count++;

        int[] lines = new int[256];
        int searched = 0;
        for(int i=0; i<count; i++)
            if(Objects.equals(records[i][0], UID)){
                lines[searched] = i;
                searched++;
            }

        int line = 0;
        String[][] temp = new String[256][9];
        if(searched>1 && scan==2) {
            System.out.println("Request's UID \tLab Test Type \tRequest Date \tResult");
            for(int i =0; i<lines.length; i++)
                for (int j = 0; j < 4; j++)
                    if ((records[i][j] != null) && (Objects.equals(records[i][0], UID))) {
                        System.out.print(records[i][j] + " \t");
                        temp[i][j] = records[i][j];
                        if (j == 3)
                            System.out.println();
                    }
            System.out.print("Enter the request's UID you want to display: ");
            input = scanner.next().toUpperCase();
            searched = 0;
            for(int i=0; i< temp.length; i++)
                if (Objects.equals(temp[i][0], input)) {
                    searched = 1;
                    line = i;
                    break;
                }

            for(int i=0; i<records.length; i++)
                if ((Objects.equals(records[i][0], temp[line][0])) && (records[i][0] != null))
                    line = i;
        } else {
            line = lines[0];
        }

        if(searched==0) {
            System.out.println("No record found.");
            System.out.println("Would you like to try again or return to the main menu?");
            System.out.println("[1] Search for another record");
            System.out.println("[2] Return to the Main Menu");
            do {
                System.out.print("Select a transaction: ");
                input = scanner.next();

                if(input.equals("1"))
                    searchLaboratoryRequest();
                else if(input.equals("2"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("1") && !input.equals("2"));
        }
        else {
            System.out.println();
            System.out.println("Request's UID \tLab Test Type \tRequest Date \tResult");
            System.out.println(records[line][0]);
            //print Lab Test Type from service.txt
            System.out.println(records[line][2]);
            System.out.println(records[line][4]);
        }
    }

    public void deleteLaboratoryRequest() {

    }

    public void editLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);

        String input;
        int scan = 0;
        do {
            System.out.print("Do you know the request's UID?[Y/N]: ");
            input = scanner.next().toUpperCase();
            if (input.equals("Y"))
                scan = 1;
            else if(input.equals("N"))
                scan = 2;
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));

        String UID = "";
        String fileName = "";
        switch (scan) {
            case 1 -> {
                System.out.print("Enter request's UID: ");
                UID = scanner.next().toUpperCase();
                String temp = UID.substring(0,3);
                fileName = String.join(temp + "_Requests.txt");
            }
            case 2 -> {
                System.out.println("Enter patient's UID: ");
                UID = scanner.next().toUpperCase();
                fileName = "Patients.txt";
            }
            default -> {
                System.out.println("Invalid input! Please try again.");
                searchLaboratoryRequest();
            }
        }
        int error = rf.readFile(fileName);
        if(error==1)
            searchLaboratoryRequest();
        String[][] records = rf.getTempSearch();

        int count = 0;
        for(int i=0; i<records.length; i++)
            for(int j=0; j<records[0].length; j++)
                if(records[j] != null)
                    count++;

        int[] lines = new int[256];
        int searched = 0;
        for(int i=0; i<count; i++)
            if(Objects.equals(records[i][0], UID)){
                lines[searched] = i;
                searched++;
            }

        int line = 0;
        String[][] temp = new String[256][9];
        if(searched>1 && scan==2) {
            System.out.println("Request's UID \tLab Test Type \tRequest Date \tResult");
            for(int i =0; i<lines.length; i++)
                for (int j = 0; j < 4; j++)
                    if ((records[i][j] != null) && (Objects.equals(records[i][0], UID))) {
                        System.out.print(records[i][j] + " \t");
                        temp[i][j] = records[i][j];
                        if (j == 3)
                            System.out.println();
                    }
            System.out.print("Enter the request's UID of the laboratory request you want to edit: ");
            input = scanner.next().toUpperCase();
            searched = 0;
            for(int i=0; i< temp.length; i++)
                if (Objects.equals(temp[i][0], input)) {
                    searched = 1;
                    line = i;
                    break;
                }

            for(int i=0; i<records.length; i++)
                if ((Objects.equals(records[i][0], temp[line][0])) && (records[i][0] != null))
                    line = i;
        } else {
            line = lines[0];
        }

        if(searched==0) {
            System.out.println("No record found.");
            System.out.println("Would you like to try again or return to the main menu?");
            System.out.println("[1] Search for another record");
            System.out.println("[2] Return to the Main Menu");
            do {
                System.out.print("Select a transaction: ");
                input = scanner.next();

                if(input.equals("1"))
                    searchLaboratoryRequest();
                else if(input.equals("2"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("1") && !input.equals("2"));
        }
        else {
            scanner.nextLine();
            System.out.print("Enter the result of the laboratory request: ");
            String newLine = scanner.nextLine();

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
                splitLine[4] = newLine;

                String line1 = String.join(";", splitLine);

                fileContents = fileContents.replaceAll(tempLine,line1);
                FileWriter fw = new FileWriter(fileName);
                fw.append(fileContents);
                fw.flush();

                UID = splitLine[0];
                System.out.println("The Laboratory Request " + UID + " has been updated.");
            } catch (IOException e) {
                System.out.println("Error occurred. Please try again");
                editLaboratoryRequest();
            }
        }
    }

}
