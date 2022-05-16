import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class ReadFile {
    String[][] tempSearch = new String[256][11];
    String tempLine = new String();

    ManagePatientRecords mpr = new ManagePatientRecords();
    String newLine = mpr.getNewLine();

    public int readFile(String fileName, int type, int line) {
        int error;
        BufferedReader br = null;
        int counter = 0;

        try {
            File file = new File(fileName);
            Scanner scannerFile = new Scanner(file);

                switch (type) {
                    case 1:                 //search
                        while(scannerFile.hasNextLine()) {
                            String temp = scannerFile.nextLine();
                            String[] tempLine = temp.split(";");
                            if (tempLine[9] == "D")
                                continue;
                            else {
                                for (int i = counter; i < counter + 1; i++) {
                                    for (int j = 0; j < 11; i++) {
                                        tempSearch[i][j] = tempLine[j];
                                    }
                                }
                                counter++;
                            }
                        }
                        break;
                    case 2:                 //append delete
                        tempLine = Files.readAllLines(Paths.get(fileName)).get(line);
                        String[] splitLine = tempLine.split(";");
                        String UID = splitLine[0];
                        FileWriter fw = new FileWriter(file.getName(),true);
                        BufferedWriter bw = new BufferedWriter(fw);
                        int lineCount = 0;
                        while (lineCount<line)
                            lineCount++;
                        bw.write(newLine+";");

                        System.out.println("Data of patient "+UID+" has been deleted.");
                        break;
                }
            error = 0;
        } catch (FileNotFoundException e) {
            System.out.println("Error occurred. Please try again");
            error = 1;
        } catch (IOException io) {
            System.out.println("Error occurred. Please try again");
            error = 1;
        }
        return error;
    }

    public String[][] getTempSearch() {
        return tempSearch.clone();
    }
}
