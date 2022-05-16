package accessModifiers;
import java.util.ArrayList;
import java.util.Scanner;

import static accessModifiers.Service.*;

public class Delete {
    public static ArrayList<String> d = new ArrayList<>();
    public static ArrayList<String> delReason = new ArrayList<>();

    public static void delService(Scanner input){
        Search key = new Search();

        System.out.println("Input Code or Key Word to Search:");
        key.keyWord = input.next();
        delKey(key);
    }

    public static void delKey(Search key){
        boolean found = false;
        String reason;
        Scanner del = new Scanner(System.in);

        for(int i = 0; i < newCode.size(); i++){
            if(key.keyWord.equalsIgnoreCase(newCode.get(i))){
                System.out.println("Found: " + newCode.get(i)  + " " + newDesc.get(i) + " " + newPrice.get(i));
                found = true;
            }
        }

        for(int j = 0; j < newDesc.size(); j++){
            if((newDesc.get(j).toLowerCase()).contains(key.keyWord.toLowerCase())){
                System.out.println("Found: " + newCode.get(j)  + " " + newDesc.get(j) + " " + newPrice.get(j));
                found = true;
            }
        }

        if(found){
            System.out.println("Enter Code of Service to Delete:");
            String toDel = del.next().toUpperCase();

            for(int l = 0; l < newCode.size(); l++){
                if(toDel.equals(newCode.get(l))){
                    System.out.println("Reason for deletion:");
                    reason = del.next();
                    d.add("D");
                    delReason.add(reason);

                    System.out.println(newCode.get(l) + " " + newDesc.get(l) + " has been deleted");
                }else{
                    d.add("0");
                    delReason.add("0");
                }
            }

            System.out.println("Do you want to delete another service? [Y/N]");

            String delMore = del.next().toUpperCase();

            switch(delMore){
                case "Y" -> delService(del);
                case "N" -> //back to main menu
                        System.out.println("Main Menu");
            }
        }

        if(!found){
            System.out.println("Record Not Found...");
            System.out.println("Would you like to search again? [Y/N]");
            Scanner input = new Scanner(System.in);
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

}
