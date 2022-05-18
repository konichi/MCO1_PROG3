import java.util.Scanner;

public class MainMenu {

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        String input;
        System.out.println("Medical Laboratory System");
        System.out.println("[1] Manage Patient Records");
        System.out.println("[2] Manage Services");
        System.out.println("[3] Manage Laboratory Results");
        System.out.print("\nSelect a transaction: ");
        input = scanner.next();
        System.out.println();

        switch (input) {
            case "1" -> managePatientRecords();
            case "2" -> manageServices();
            case "3" -> manageLaboratoryRequest();
            default -> mainMenu();
        }
    }

    public void managePatientRecords() {
        ManagePatientRecords mpr = new ManagePatientRecords();
        mpr.managePatientRecords();
    }

    public void manageServices() {
        ManageServices ms = new ManageServices();
        ms.manageServices();
    }

    public void manageLaboratoryRequest() {
        ManageLaboratoryRequest mlr = new ManageLaboratoryRequest();
        mlr.manageLaboratoryRequest();
    }


}
