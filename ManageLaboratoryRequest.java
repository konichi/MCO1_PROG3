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

        switch (input) {
            case "1" -> addNewLaboratoryRequest();
            case "2" -> searchLaboratoryRequest(0);
            case "3" -> deleteLaboratoryRequest();
            case "4" -> editLaboratoryRequest();
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
            newUID = "AA00";
            String str = String.join("", tempUID);
            return String.join("", code, str, newUID);
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
        String cFile = "services.txt";
        int cExists = rf.checkCode(cFile, code);

        String input;
        if(pExists!=1) {
            System.out.println("Patient Record does not exist!");
            System.out.println("Would you like to search for another patient or return to the main menu?");
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
        } else if(cExists!=1){
            System.out.println("Service does not exist!");
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
        }

        String requestUID = generateUID(code);

        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month;
        int temp = Calendar.getInstance().get(Calendar.MONTH)+1;
        if(temp<9)
            month = "0" + temp;
        else
            month = String.valueOf(temp);
        String date;
        temp = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if(temp<9)
            date = "0" + temp;
        else
            date = String.valueOf(temp);
        String requestDate = String.join("", String.valueOf(year), month, date);

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String str1;
        if(hour<9)
            str1 = "0" + hour;
        else
            str1 = String.valueOf(hour);
        String str2;
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        if(minute<9)
            str2 = "0" + minute;
        else
            str2 = String.valueOf(minute);
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

    public String[] searchLaboratoryRequest(int type) {
        Scanner scanner = new Scanner(System.in);
        String[] ret = new String[3];
        String[] retPrint = new String[4];

        String UID;
        String input;
        String fileName;
        int scan = 0;
        int error;

        if(type!=2) {
            do {
                System.out.print("Do you know the request's UID?[Y/N]: ");
                input = scanner.next().toUpperCase();
                if (input.equals("Y"))
                    scan = 1;
                else if (input.equals("N"))
                    scan = 2;
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while (!input.equals("Y") && !input.equals("N"));
        } else
            scan=1;

        int line = 0;
        int searched = 0;
        String finalFileName = null;
        String code;                    // will use later for printing :D
        if(scan==1) {
            System.out.print("Enter request's UID: ");
            UID = scanner.next().toUpperCase();
            code = UID.substring(0, 3);
            fileName = code + "_Requests.txt";
            error = rf.readRequests(fileName);
            if(error==1) {
                if(type==0)
                    searchLaboratoryRequest(0);
                else {
                    ret[1] = String.valueOf(0);
                    return ret;
                }
            }
            String[][] requests = rf.getTempSearch();

            // count total non-null entries in String[][] requests
            int count = 0;
            for (String[] request : requests)
                for (int j = 0; j < requests[0].length; j++)
                    if (request[0] != null && request[0].length()==15) {
                        count++;
                        break;
                    }

            for(int i=0; i<count; i++) {
                if (UID.equals(requests[i][0])) {
                    line = i;
                    searched = 1;
                    finalFileName = fileName;
                    break;
                }
            }

            if(type==2) {
                int sLine = 0;
                // get all lines in services.txt and save to String[][] services
                fileName = "services.txt";
                rf.readServices(fileName);
                String[][] services = rf.getTempSearch();

                for (int i = 0; i< services.length; i++)
                    if(code.equals(services[i][0])) {
                        sLine = i;
                        break;
                    }
                retPrint[0] = requests[line][0];    // request UID
                retPrint[1] = requests[line][2];    // request date
                retPrint[2] = requests[line][4];    // request result
                retPrint[3] = services[sLine][1];    // service code/description
                return retPrint;
            }
        } else { //if scan = 2 - patient's UID
            System.out.print("Enter Patient's UID: ");
            UID = scanner.next().toUpperCase();

            fileName = "services.txt";
            error = rf.readServices(fileName);
            if(error==1) {
                if(type==0)
                    searchLaboratoryRequest(0);
                else {
                    ret[1] = String.valueOf(0);
                    return ret;
                }
            }
            String[][] services = rf.getTempSearch();
        // count total non-null entries in String[][] services
            int countServices = 0;
            for (String[] service : services)
                for (int j = 0; j < services[0].length; j++)
                    if (service[0] != null && service[0].length() == 3) {
                        countServices++;
                        break;
                    }

            System.out.println();
            System.out.printf("%-18s", "Request's UID");
            System.out.printf("%-15s", "Lab Test Type");
            System.out.printf("%-15s", "Request Date");
            System.out.printf("%-15s", "Result");
            System.out.println();

            int countRequests=0;
            for (int i = 0; i < countServices; i++) {
                code = services[i][0];
                fileName = services[i][0] + "_Requests.txt";
                error = rf.readRequests(fileName);
                if(error==-1)
                    break;
                String[][] temp = rf.getTempSearch();

                // count total non-null entries in String[][] temp
                int countTemp = 0;
                for (String[] t : temp)
                    for (int j = 0; j < temp[0].length; j++)
                        if (!Objects.equals(t[0], null) && t[0].length()==15) {
                            countTemp++;
                            break;
                        }

                for(int j=0; j< countTemp; j++) {
                    if (temp[j][0] != null && temp[j][1].equals(UID) && !Objects.equals(temp[j][5], "D")) {
                        System.out.printf("%-18s", temp[j][0]);
                        System.out.printf("%-15s", code);
                        System.out.printf("%-15s", temp[j][2]);
                        System.out.printf("%-15s", temp[j][4]);
                        System.out.println();
                        countRequests++;
                        searched=1;
                    }
                }
            }
            if(countRequests>1) {
                System.out.println();
                System.out.print("Enter the Request's UID: ");
                UID = scanner.next();
                if(UID.length()!=15) {
                    System.out.println("Invalid input! Please try again\n");
                    if(type==0)
                        searchLaboratoryRequest(0);
                    else {
                        ret[1] = String.valueOf(0);
                        return ret;
                    }
                }
                String serviceCode = UID.substring(0, 3);
                fileName = serviceCode + "_Requests.txt";
                rf.readRequests(fileName);
                String[][] requests = rf.getTempSearch();

                // count total non-null entries in String[][] temp
                int countTemp = 0;
                for (String[] request : requests)
                    for (int j = 0; j < requests[0].length; j++)
                        if (!Objects.equals(request[0], null) && request[0].length()==15) {
                            countTemp++;
                            break;
                        }

                for (int i = 0; i < countTemp; i++)
                    if (requests[i][0].equalsIgnoreCase(UID)) {
                        line = i;
                        System.out.println(requests[i][0]);
                        finalFileName = fileName;
                        break;
                    }
            }
        }
        if(type==1) {
            ret[0] = finalFileName;
            ret[1] = String.valueOf(searched);
            ret[2] = String.valueOf(line);
            return ret;
        }
        rf.readRequests(finalFileName);
        String[][] requests = rf.getTempSearch();
        code = requests[0][0].substring(0, 3);
        System.out.println();
        if(searched==0) {
            System.out.println("No record found.");
            System.out.println("Would you like to try again or return to the main menu?");
            System.out.println("[1] Search for another record");
            System.out.println("[2] Return to the Main Menu");
            do {
                System.out.print("Select a transaction: ");
                input = scanner.next();
                if(input.equals("1"))
                    searchLaboratoryRequest(0);
                else if(input.equals("2"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("1") && !input.equals("2"));
        } else {
            System.out.printf("%-18s", "Request's UID");
            System.out.printf("%-15s", "Lab Test Type");
            System.out.printf("%-15s", "Request Date");
            System.out.printf("%-15s", "Result");
            System.out.println();
            for (String[] request : requests)
                if (Objects.equals(request[0], requests[line][0]) && request[0] != null) {
                    System.out.printf("%-18s", request[0]);
                    System.out.printf("%-15s", code);
                    System.out.printf("%-15s", request[2]);
                    System.out.printf("%-15s", request[4]);
                    System.out.println();
                    break;
                }
            do {
                System.out.println();
                System.out.print("Do you want to search for another Laboratory Request? [Y/N]: ");
                input = scanner.next().toUpperCase();

                if(input.equals("Y"))
                    searchLaboratoryRequest(0);
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N"));
        }
        return ret;
    }

    public void deleteLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);

        String[] ret = searchLaboratoryRequest(1);
        String fileName = ret[0];
        int searched = Integer.parseInt(ret[1]);
        int line = Integer.parseInt(ret[2]);
        String input;

        if(searched==0) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Delete a laboratory request");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    deleteLaboratoryRequest();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                deleteLaboratoryRequest();
            else
                mm.mainMenu();
        } else {
            System.out.print("Please state reason for deletion: ");
            String reason = scanner.nextLine();
            String D = "D";
            String newLine = String.join(";", D, reason);

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

                System.out.println("\n" + UID + " has been deleted.");
            } catch(IOException e) {
                System.out.println("Error occurred. Please try again.\n");
                deleteLaboratoryRequest();
            }
            do {
                System.out.print("Do you want to delete another laboratory request? [Y/N]: ");
                input = scanner.next().toUpperCase();
                if(input.equals("Y"))
                    deleteLaboratoryRequest();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

    public void editLaboratoryRequest() {
        Scanner scanner = new Scanner(System.in);

        String[] ret = searchLaboratoryRequest(1);
        String fileName = ret[0];
        int searched = Integer.parseInt(ret[1]);
        int line = Integer.parseInt(ret[2]);
        String input;

        if(searched==0) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Edit a laboratory request");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    deleteLaboratoryRequest();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                editLaboratoryRequest();
            else
                mm.mainMenu();
        } else {
            int error;
            error = rf.readRequests(fileName);
            if(error==1)
                editLaboratoryRequest();
            String[][] requests = rf.getTempSearch();

            if(!requests[line][4].equals("XXX"))
                System.out.println("Laboratory Request " + requests[line][0] + " already has results.");
            else {
                String newLine;
                scanner.nextLine();
                System.out.print("Enter the laboratory request result: ");
                newLine = scanner.nextLine();

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
                    line1 = String.join("", line1, ";");

                    fileContents = fileContents.replaceAll(tempLine,line1);
                    FileWriter fw = new FileWriter(fileName);
                    fw.append(fileContents);
                    fw.flush();

                    String UID = splitLine[0];
                    System.out.println("The Laboratory Request " + UID + " has been updated.");
                } catch (IOException e) {
                    System.out.println("Error occurred. Please try again");
                    editLaboratoryRequest();
                }
            }
            do {
                System.out.print("Do you want to edit another laboratory request? [Y/N]: ");
                input = scanner.next().toUpperCase();
                if(input.equals("Y"))
                    editLaboratoryRequest();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

}
