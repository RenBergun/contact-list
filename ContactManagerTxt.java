import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagerTxt {
    private static final String FILE_NAME = "contacts.txt"; // stored in project folder

    //Load contacts from file
    public static List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(FILE_NAME);

        try {

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("contacts.txt created.");
            }

            //Read existing contacts
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Contact c = Contact.fromString(line);
                    if (c != null) {
                        contacts.add(c);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading contacts: " + e.getMessage());
        }

        return contacts;
    }

    //Save contacts to file
    public static void saveContacts(List<Contact> contacts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Contact c : contacts) {
                writer.write(c.toString());
                writer.newLine();
            }
            System.out.println("Contacts saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving contacts: " + e.getMessage());
        }
    }
}
