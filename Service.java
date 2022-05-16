package accessModifiers;

import java.util.ArrayList;
import java.util.Scanner;

public class Service {
    public static ArrayList<String> newCode = new ArrayList<>();
    public static ArrayList<String> newDesc = new ArrayList<>();
    public static ArrayList<String> newPrice = new ArrayList<>();
    public static String code;
    public static String desc;
    public static String price;

    public static void scanNew(Scanner input){
        System.out.println("Enter New Service Code:");
        code = input.next().toUpperCase();

        if(checkCode(code) && nCode(code)){
            newCode.add(code);

            System.out.println("Enter New Service Description:");
            desc = input.next();
            newDesc.add(desc);

            System.out.println("Enter New Service Price:");
            price = input.next();
            newPrice.add(price);

            System.out.println(code + "-" + desc + " has been added");
            System.out.println("Do you want to add another service? [Y/N]");

            String addMore = input.next().toUpperCase();

            switch(addMore){
                case "Y" -> scanNew(input);
                case "N" -> //back to main menu
                        System.out.println("Main Menu");
            }
        }
    }

    public static boolean checkCode(String s){
        Scanner inp = new Scanner(System.in);
        for (String value : newCode) {
            if (s.equals(value)) {
                System.out.println("\nInput a unique code!");
                scanNew(inp);
                return false;
            }
        }
        return true;
    }

    public static boolean nCode(String n){
        Scanner inp = new Scanner(System.in);
        if(n.length() == 3){
            return true;
        }
        System.out.println("Input a 3-CHARACTER Service Code!");
        scanNew(inp);
        return false;
    }
}
