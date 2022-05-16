package accessModifiers;

import java.util.Scanner;

import static accessModifiers.Delete.*;
import static accessModifiers.Service.*;

public class Edit {
    public static void editService(){
        Scanner input = new Scanner(System.in);

        System.out.println("The services cannot be edited. If you would like to edit an existing service, the service will first be deleted, and new service will be created. Would you like to proceed? [Y/N]");
        String eServ = input.next().toUpperCase();

        switch(eServ){
            case "Y" -> delServ(input);
            case "N" ->// go to main menu
                    System.out.println("Main Menu");
        }
    }

    public static void delServ(Scanner input){
        Search key = new Search();

        System.out.println("Input Code or Key Word to Search:");
        key.keyWord = input.next().toLowerCase();

        boolean found = false;
        String reason;
        Scanner del = input;

        for(int i = 0; i < newCode.size(); i++){
            if(key.keyWord.equals(newCode.get(i).toLowerCase())){
                if(!d.isEmpty() && d.get(i).equals("D")) {
                    System.out.println("Found: " + newCode.get(i)  + " " + newDesc.get(i) + " " + newPrice.get(i) + " D " + delReason.get(i));
                }else{
                    System.out.println("Found: " + newCode.get(i)  + " " + newDesc.get(i) + " " + newPrice.get(i));
                }
                found = true;
            }
        }

        for(int j = 0; j < newDesc.size(); j++){
            if(newDesc.get(j).toLowerCase().contains(key.keyWord)){
                if(!d.isEmpty() && d.get(j).equals("D")) {
                    System.out.println("Found: " + newCode.get(j)  + " " + newDesc.get(j) + " " + newPrice.get(j) + " D " + delReason.get(j));
                }else{
                    System.out.println("Found: " + newCode.get(j)  + " " + newDesc.get(j) + " " + newPrice.get(j));
                }
                found = true;
            }
        }

        if(found) {
            System.out.println("Enter Code of Service to Delete:");
            String toDel = del.next().toUpperCase();
            if(d.isEmpty()){
                for (int l = 0; l < newCode.size(); l++) {
                    if (toDel.equals(newCode.get(l))) {
                        System.out.println("Reason for deletion:");
                        reason = del.next();
                        d.add("D");
                        delReason.add(reason);

                        System.out.println(newCode.get(l) + " " + newDesc.get(l) + " has been deleted");
                    }else {
                        d.add("0");
                        delReason.add("0");
                    }
                }
            }else{
                for (int z = 0; z < newCode.size(); z++) {
                    if (toDel.equals(newCode.get(z)) && !d.get(z).equals("D")) {
                        System.out.println("Reason for deletion:");
                        reason = del.next();
                        d.add("D");
                        delReason.add(reason);

                        System.out.println(newCode.get(z) + " " + newDesc.get(z) + " has been deleted");
                    }else {
                        d.add("0");
                        delReason.add("0");
                    }
                }
            }
            addServ(input);
        }else{
            System.out.println("Record Not Found...");
            System.out.println("Would you like to search again? [Y/N]");
            String addMore = input.next().toUpperCase();
            Search newKey = new Search();
            switch(addMore){
                case "Y" -> {
                    System.out.println("Input Code or Key Word to Search:");
                    newKey.keyWord = input.next();
                    delKey(key);
                }
                case "N" -> //back to main menu
                        System.out.println("Main Menu");
            }
        }
    }

    public static void addServ(Scanner input){
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
            d.add("0");
            delReason.add("0");

            System.out.println(code + "-" + desc + " has been added");
            System.out.println("Do you want to edit another service? [Y/N]");

            String addMore = input.next().toUpperCase();

            switch(addMore){
                case "Y" -> editService();
                case "N" -> //back to main menu
                        System.out.println("Main Menu");
            }
        }
    }

    public static boolean checkCode(String s){
        Scanner inp = new Scanner(System.in);
        for (int i = 0; i < newCode.size(); i++) {
            if (s.equals(newCode.get(i)) && !d.get(i).equals("D")) {
                System.out.println("\nThis Service Code already exists!");
                addServ(inp);
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
        addServ(inp);
        return false;
    }
}
