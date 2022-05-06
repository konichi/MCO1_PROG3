import java.text.SimpleDateFormat;
import java.util.Date;

public class Patient { //CHANGE TO Patient
    String pattern = "yyyy/MM/dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

    private String patientCodeIdentifier;
    private String firstName;
    private String lastName;
    private String middleName;
//    private Date birthday = new Date();
    private String birthday = simpleDateFormat.format(new Date());
    private String gender;
    private String address;
    private long phoneNo;
    private long nationalIdNo;

    public Patient(String patientCodeIdentifier, String firstName, String lastName, String middleName, String birthday, String gender, String address, long phoneNo, long nationalIdNo) {
        this.patientCodeIdentifier = patientCodeIdentifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthday = birthday;
        this.gender = gender;
        this.address = address;
        this.phoneNo = phoneNo;
        this.nationalIdNo = nationalIdNo;
    }


    public String getPatientCodeIdentifier() {
        return patientCodeIdentifier;
    }

    public void setPatientCodeIdentifier(String patientCodeIdentifier) {
        this.patientCodeIdentifier = patientCodeIdentifier;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(int phoneNo) {
        this.phoneNo = phoneNo;
    }

    public long getNationalIdNo() {
        return nationalIdNo;
    }

    public void setNationalIdNo(int nationalIdNo) {
        this.nationalIdNo = nationalIdNo;
    }
}
