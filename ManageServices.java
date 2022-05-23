import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class ManageServices {
    ArrayList<Service> services = new ArrayList<>();

    MainMenu mm = new MainMenu();
    ReadFile rf = new ReadFile();
    WriteToFile wtf = new WriteToFile();

    public void manageServices() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Manage Patient Records");
        System.out.println("[1] Add New Service");
        System.out.println("[2] Search Service");
        System.out.println("[3] Delete Service");
        System.out.println("[4] Edit Service");
        System.out.println("[X] Return to Main Menu");
        System.out.print("Select a transaction: ");
        String input = scanner.next().toUpperCase();
        System.out.println();

        switch (input) {
            case "1" -> addService(0);
            case "2" -> searchService();
            case "3" -> deleteService();
            case "4" -> editService();
            case "X" -> mm.mainMenu();
            default -> manageServices();
        }
    }

    public void addService(int type) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "services.txt";

        System.out.print("Enter unique 3-code Service Code: ");
        String code = scanner.next().toUpperCase();
        scanner.nextLine();
        System.out.print("Enter laboratory service Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter laboratory service Price: ");
        int price = 0;
        try {
            price = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid format! Please try again.");
            addService(0);
        }

        //check if service code already exists
        int exists;
        exists = rf.checkCode(fileName, code);
        do {
            if (exists == 1) {
                System.out.print("This service code already exists. Please enter a new code: ");
                code = scanner.next().toUpperCase();
            }
            exists = rf.checkCode(fileName, code);
        } while(exists==1);

        Service service = new Service(code, description, price);
        services.add(service);

        int error = wtf.writeToServices(fileName, service);
        if(error==1)
            addService(0);
        else
            System.out.println(code + " " + description + " has been added.");
        System.out.println();

        if(type == 1)
            return;

        String input;
        do {
            System.out.print("Do you want to add another service? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                addService(0);
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

    public void searchService() {
        Scanner scanner = new Scanner(System.in);

        int line=search();
        String input;

        if(line==-1)
            searchService();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to try again or return to the main menu?");
                System.out.println("[1] Search for another service");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    searchService();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                searchService();
            else
                mm.mainMenu();
        } else {
            do {
                System.out.println();
                System.out.print("Do you want to search again? [Y/N]: ");
                input = scanner.next().toUpperCase();
                System.out.println();
                if(input.equals("Y"))
                    searchService();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

    public void deleteService() {
        Scanner scanner = new Scanner(System.in);

        int line = search();
        String input;

        if(line==-1)
            deleteService();
        else if(line==-2) {
            System.out.println("No record found.");
            do {
                System.out.println("Would you like to search again or return to the main menu?");
                System.out.println("[1] Delete a service");
                System.out.println("[2] Return to the Main Menu");
                System.out.print("Select a transaction: ");
                input = scanner.next().toUpperCase();

                if(!input.equals("1") && !input.equals("2")) {
                    System.out.println("Invalid input format! Please try again");
                    deleteService();
                }
            } while(!input.equals("1") && !input.equals("2"));
            if(input.equals("1"))
                deleteService();
            else
                mm.mainMenu();
        } else {
            delete(1, line);

            System.out.println();
            do {
                System.out.print("Do you want to delete another patient record? [Y/N]: ");
                input = scanner.next().toUpperCase();
                if(input.equals("Y"))
                    deleteService();
                else if(input.equals("N"))
                    mm.mainMenu();
                else
                    System.out.println("Invalid input! Please enter a valid input.");
            } while(!input.equals("Y") && !input.equals("N"));
        }
    }

    public void editService() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.print("""
                The services cannot be edited.
                If you would like to edit an existing service, the service will be first deleted from the file, and a new service will be created.\s
                Would you like to proceed? [Y/N]:\s""");
        input = scanner.next().toUpperCase();
        if(input.equals("N"))
            mm.mainMenu();

        int line = search();
        delete(2, line);
        addService(1);

        System.out.println();
        do {
            System.out.print("Do you want to edit another patient record? [Y/N]: ");
            input = scanner.next().toUpperCase();
            if(input.equals("Y"))
                editService();
            else if(input.equals("N"))
                mm.mainMenu();
            else
                System.out.println("Invalid input! Please enter a valid input.");
        } while(!input.equals("Y") && !input.equals("N"));
    }

    public int search() {
        Scanner scanner = new Scanner(System.in);

        int scan;
        int line = 0;
        String input;

        System.out.print("Do you know the service code?[Y/N]: ");
        input = scanner.next().toUpperCase();
        if(input.equals("Y"))
            scan = 1;
        else {
            System.out.print("Input a keyword of the service's description: ");
            input = scanner.next();
            scan = 2;
        }

        // get all lines in services.txt and save to String[][] services
        String fileName = "services.txt";
        int error = rf.readServices(fileName);
        if(error==1)
            return -1;
        String[][] services = rf.getTempSearch();

        // count total non-null entries in String[][] services
        int count = 0;
        for (String[] service : services)
            for (int j = 0; j < services[0].length; j++)
                if (service[0] != null && service[0].length() == 3) {
                    count++;
                    break;
                }

        // switch case
        // get input from user to search match/es in String services[][]
        int searched = 0;
        int[] lines = new int[256];
        String searchCode;
        switch (scan) {
            case 1 -> {
                System.out.print("Enter service code: ");
                searchCode = scanner.next().toUpperCase();
                for (int i = 0; i < count; i++) {
                    if (Objects.equals(services[i][0], searchCode) && !Objects.equals(services[i][3], "D")) {
                        searched = 1;
                        lines[0] = i;
                        break;
                    } else
                        lines[0] = -2;
                }
            }
            case 2 -> {
                for (int i = 0; i < count; i++) {                       // for every service
                    String[] temp = services[i][1].split(" ");          // get words in description
                    // for every word in description
                    for (String s : temp)
                        try {
                            if (!Objects.equals(services[i][3], "D"))
                                if (s.equalsIgnoreCase(input)) {
                                    lines[searched] = i;
                                    searched++;
                                }
                        } catch (NullPointerException ignored) {
                        }
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
        String[] temp;
        if(searched==0)
            return -2;
        else if(searched>1) {
            System.out.printf("%-15s", "Service Code");
            System.out.printf("%-25s", "Description");
            System.out.printf("%-20s", "Price");
            System.out.println();
            try {
                for(int i=0; i<lines.length; i++) {
                    temp = services[i][1].split(" ");
                    for (String s : temp) {
                        if (!Objects.equals(services[i][3], "D"))
                            if (s.equalsIgnoreCase(input)) {
                                System.out.printf("%-15s", services[i][0]);
                                System.out.printf("%-25s", services[i][1]);
                                System.out.printf("%-20s", services[i][2]);
                                System.out.println();
                            }
                    }
                }
            } catch (NullPointerException ignored) {}
            System.out.println();
            System.out.print("Enter the service code: ");
            input = scanner.next().toUpperCase();
            System.out.println();
            for(int i=0; i< services.length; i++)
                if(Objects.equals(services[i][0], input)) {
                    line = i;
                    searched = 1;
                    break;
                }
            if(searched!=1)
                return -2;
        } else
            line = lines[0];

        System.out.printf("%-15s", "Service Code");
        System.out.printf("%-25s", "Description");
        System.out.printf("%-20s", "Price");
        System.out.println();
        System.out.printf("%-15s", services[line][0]);
        System.out.printf("%-25s", services[line][1]);
        System.out.printf("%-20s", services[line][2]);
        System.out.println();

        return line;
    }

    public void delete(int type, int line) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please state reason for deletion: ");
        String reason = scanner.nextLine();
        String D = "D;";
        String newLine = String.join("", D, reason);

        String fileName = "services.txt";
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
            String code = splitLine[0];
            String description = splitLine[1];

            System.out.println(code + " " +  description + " has been deleted.");
            System.out.println();
        } catch(IOException e) {
            System.out.println("Error occurred. Please try again");
            if (type==1)
                deleteService();
            else
                editService();
        }
    }
}
