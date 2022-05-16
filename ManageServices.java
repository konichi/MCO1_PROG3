import java.util.Scanner;

import static accessModifiers.Delete.*;
import static accessModifiers.Edit.*;
import static accessModifiers.Search.*;
import static accessModifiers.Service.*;

public class ManageServices {
    public void manageServices() {
        Scanner input = new Scanner(System.in);

        newCode.add("CAT");
        newCode.add("CBT");
        newCode.add("CRP");

        newDesc.add("Covid-19 Antigen Test");
        newDesc.add("Covid-19 Antibody Test");
        newDesc.add("Covid-19 RT-PCR");

        newPrice.add("1000");
        newPrice.add("500");
        newPrice.add("2000");

        System.out.println("Manage Services");
        System.out.println("[1]Add New Service");
        System.out.println("[2]Search Service");
        System.out.println("[3]Delete Service");
        System.out.println("[4]Edit Service");

        int service = inp(input);
        transaction(service, input);
    }

    static int inp(Scanner input){
        int service;
        do {
            System.out.println("\nSelect a transaction:");
            service = input.nextInt();
        } while (service <= 0 || service >= 5);

        return service;
    }

    static void transaction(int service, Scanner input) {
        switch (service) {
            case 1 -> {
                System.out.println("\nAdd New Service\n");
                scanNew(input);
            }
            case 2 -> {
                System.out.println("\nSearch Service\n");
                scanKey(input);
            }
            case 3 -> {
                System.out.println("\nDelete Service\n");
                delService(input);
            }
            case 4 -> {
                System.out.println("\nEdit Service\n");
                editService();
            }
        }
    }
}
