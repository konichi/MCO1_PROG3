import java.io.*;

public class WriteToFile {

    public int writeToFile(String fileName, Patient patient, int type) {
        int error;

        try {
            File file = new File(fileName);
            if(!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getName(),true);
            BufferedWriter bw = new BufferedWriter(fw);

            switch (type) {
                case 1:     //add patient
                    bw.write(patient.getPatientCodeIdentifier() + ";");
                    bw.write(patient.getFirstName() + ";");
                    bw.write(patient.getLastName() + ";");
                    bw.write(patient.getMiddleName() + ";");
                    bw.write(patient.getBirthday() + ";");
                    bw.write(patient.getGender() + ";");
                    bw.write(patient.getAddress() + ";");
                    bw.write(patient.getPhoneNo() + ";");
                    bw.write(patient.getNationalIdNo() + ";");
                    bw.newLine();
                    break;
                case 2:     //add service

                    break;
                case 3:     //add request

                    break;
            }

            bw.close();
            error = 0;
        } catch(IOException e){
            System.out.println("Error occurred. Please try again");
            error = 1;
        }
        return error;
    }


}