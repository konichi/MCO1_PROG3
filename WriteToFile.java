import java.io.*;

//can move to readFile and rename readFile to Files
public class WriteToFile {

    // adding patients
    public int writeToPatients(String fileName, Patient patient) {
        int error;

        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getName(),true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(patient.getPatientCodeIdentifier() + ";");
            bw.write(patient.getLastName() + ";");
            bw.write(patient.getFirstName() + ";");
            bw.write(patient.getMiddleName() + ";");
            bw.write(patient.getBirthday() + ";");
            bw.write(patient.getGender() + ";");
            bw.write(patient.getAddress() + ";");
            bw.write(patient.getPhoneNo() + ";");
            bw.write(patient.getNationalIdNo() + ";");
            bw.newLine();

            bw.close();
            error = 0;
        } catch(IOException e){
            System.out.println("Error occurred. Please try again");
            error = 1;
        }
        return error;
    }

    // adding laboratory requests
    public int writeToLabRequests(String fileName, Request request) {
        int error;

        try {
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file.getName(),true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(request.getRequestUID() + ";");
            bw.write(request.getPatientUID() + ";");
            bw.write(request.getRequestDate() + ";");
            bw.write(request.getRequestTime() + ";");
            bw.newLine();

            bw.close();
            error = 0;
        } catch(IOException e){
            System.out.println("Error occurred. Please try again");
            error = 1;
        }
        return error;
    }


}