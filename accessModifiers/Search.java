package accessModifiers;

import java.util.Scanner;

import static accessModifiers.Service.*;

public class Search {
    public String keyWord;

    public static void scanKey(Scanner input){
        Search key = new Search();

        System.out.println("Input Code or Key Word to Search:");
        key.keyWord = input.next();
        searchService(key);
    }

    static void searchService(Search key){

        boolean found = false;

        for(int i = 0; i < newCode.size(); i++){
            if(key.keyWord.equals(newCode.get(i)) && !found){
                System.out.println("Found: " + newCode.get(i)  + " " + newDesc.get(i) + " " + newPrice.get(i));
                found = true;
            }
        }

        for(int j = 0; j < newDesc.size(); j++){
            if(newDesc.get(j).contains(key.keyWord)){
                System.out.println("Found: " + newCode.get(j)  + " " + newDesc.get(j) + " " + newPrice.get(j));
                found = true;
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
                    searchService(newKey);
                }
                case "N" -> //back to main menu
                        System.out.println("Main Menu");
            }
        }
    }
}

