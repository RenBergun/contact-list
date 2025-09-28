public class Contact {
    private String name;
    private String phoneNum;
    private String emailAdd;

    public Contact(String name, String phoneNum, String emailAdd) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.emailAdd = emailAdd;
    }

    // Getters
    public String getName() { return name; }
    public String getPhoneNum() { return phoneNum; }
    public String getEmailAdd() { return emailAdd; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }
    public void setEmailAdd(String emailAdd) { this.emailAdd = emailAdd; }


    @Override
    public String toString() {

        String safeName = name.replace(",", " ");
        String safePhone = phoneNum.replace(",", " ");
        String safeEmail = emailAdd.replace(",", " ");
        return safeName + "," + safePhone + "," + safeEmail;
    }


    public static Contact fromString(String line) {
        if (line == null) return null;

        String[] parts = line.split(",", -1);
        if (parts.length < 3) return null;
        String name = parts[0].trim();
        String phone = parts[1].trim();
        String email = parts[2].trim();

        return new Contact(name, phone, email);
    }


    public String display() {
        return name + " | " + phoneNum + " | " + emailAdd;
    }
}
