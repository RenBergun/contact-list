import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContactManagerGUI extends JFrame {
    private List<Contact> contacts;
    private JTable table;
    private DefaultTableModel tableModel;
    private boolean modified = false;

    public ContactManagerGUI() {
        // load contacts from file
        contacts = new ArrayList<>(ContactManagerTxt.loadContacts());

        tableModel = new DefaultTableModel(new Object[]{"Name", "Phone", "Email"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);

        //Buttons
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton saveBtn = new JButton("Save");
        JButton exitButton = new JButton("Exit");

        //Button actions
        addBtn.addActionListener(e -> {
            Contact c = showContactDialog(null);
            if (c != null) {
                contacts.add(c);
                refreshTable();
                modified = true;
            }
        });

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a contact first.");
                return;
            }
            Contact existing = contacts.get(row);
            Contact updated = showContactDialog(existing);
            if (updated != null) {
                contacts.set(row, updated);
                refreshTable();
                modified = true;
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a contact first.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Delete selected contact?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                contacts.remove(row);
                refreshTable();
                modified = true;
            }
        });

        saveBtn.addActionListener(e -> {
            ContactManagerTxt.saveContacts(contacts);
            modified = false;
            JOptionPane.showMessageDialog(this, "Contacts saved successfully.");
        });

        exitButton.addActionListener(e -> exitProgram());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(exitButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Contact Manager");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                exitProgram();
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Contact c : contacts) {
            tableModel.addRow(new Object[]{c.getName(), c.getPhoneNum(), c.getEmailAdd()});
        }
    }

    private Contact showContactDialog(Contact existing) {
        JTextField nameField = new JTextField(existing != null ? existing.getName() : "");
        JTextField phoneField = new JTextField(existing != null ? existing.getPhoneNum() : "");
        JTextField emailField = new JTextField(existing != null ? existing.getEmailAdd() : "");

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    this, panel,
                    existing == null ? "Add Contact" : "Edit Contact",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            nameField.setBackground(Color.WHITE);
            phoneField.setBackground(Color.WHITE);
            emailField.setBackground(Color.WHITE);

            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            //Validation
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
                nameField.setBackground(Color.PINK);
                continue;
            }
            if (name.matches(".*\\d.*")) {
                JOptionPane.showMessageDialog(this, "Name cannot contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                nameField.setBackground(Color.PINK);
                continue;
            }
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Phone is required.", "Error", JOptionPane.ERROR_MESSAGE);
                phoneField.setBackground(Color.PINK);
                continue;
            }
            if (!phone.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Phone number must be digits only.", "Error", JOptionPane.ERROR_MESSAGE);
                phoneField.setBackground(Color.PINK);
                continue;
            }
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email is required.", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.setBackground(Color.PINK);
                continue;
            }
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(this, "Invalid email address.", "Error", JOptionPane.ERROR_MESSAGE);
                emailField.setBackground(Color.PINK);
                continue;
            }

            //Duplicate check
            for (Contact c : contacts) {
                boolean isSame = c.getName().equalsIgnoreCase(name) && c.getPhoneNum().equals(phone);
                boolean isEditingSameContact = existing != null && c == existing;
                if (isSame && !isEditingSameContact) {
                    JOptionPane.showMessageDialog(this, "Contact already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    nameField.setBackground(Color.PINK);
                    phoneField.setBackground(Color.PINK);

                }
            }
            return new Contact(name, phone, email);
        }
    }

    private void exitProgram() {
        if (modified) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to save your contacts before exiting?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
            if (choice == JOptionPane.YES_OPTION) {
                ContactManagerTxt.saveContacts(contacts);
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactManagerGUI().setVisible(true));
    }
}
