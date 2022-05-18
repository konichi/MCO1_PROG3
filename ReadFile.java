import java.io.*;
import java.util.Scanner;

public class ReadFile {
    String[][] tempSearch = new String[256][11];
    String UID;

    //getting all lines from text file
    public int readFile(String fileName) {
        int error;
        int counter = 0;

        try {
            File file = new File(fileName);
            Scanner scannerFile = new Scanner(file);

            while(scannerFile.hasNextLine()) {
                String temp = scannerFile.nextLine();
                String[] tempLine = temp.split(";");
                if (11!=tempLine.length) {
                    for (int i = counter; i < counter + 1; i++) {
                        for (int j = 0; j < 9; j++)
                            tempSearch[i][j] = tempLine[j];
                    }
                }
                counter++;
            }

            error = 0;
        } catch (IOException e) {
            System.out.println("Error occurred. Please try again");
            error = 1;
        }
        return error;
    }

    //reading UID for UID generation
    public int readUID(String fileName) {
        int isFirst;
        String line;
        String temp = null;
        try {
            FileReader file = new FileReader(fileName);
            BufferedReader buffer = new BufferedReader(file);

            while((line = buffer.readLine())!=null) {
                temp = line;
            }
            String[] split = temp.split(";");
            UID = split[0];

            isFirst = 0;
        } catch (IOException | NullPointerException e) {
            isFirst = 1;
        }
        return isFirst;
    }

    //reading patient's UID if it exists
    public int checkUID(String fileName, String UID) {
        int exists = 0;
        try {
            File file = new File(fileName);
            Scanner scannerFile = new Scanner(file);

            while(scannerFile.hasNextLine()) {
                String temp = scannerFile.nextLine();
                String[] tempLine = temp.split(";");
                if (tempLine[0].equalsIgnoreCase(UID)) {
                    return 1;
                }
            }
        } catch (IOException | NullPointerException ignored) {}
        return exists;
    }

    public String[][] getTempSearch() {
        return tempSearch.clone();
    }

    public String getUID() {
        return UID;
    }

}