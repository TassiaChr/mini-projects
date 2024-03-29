package gr.aueb.cf.exercises;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.ScatteringByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Scanner;

public class MobileApp {
    final static Scanner in = new Scanner(System.in);
    final static Path path = Paths.get("C:/tmp/log-mobile.txt");
    final static String [][] contacts = new String[500][3];
    static int pivot = -1;

    public static void main(String[] args) {
        boolean quit = false;
        String s;

        do {
            printGenericMenu();
            s = getChoice();
            if (s.matches("[qQ]")) quit = true;
            else {
                try {
                    handleChoiceController(s);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        } while (!quit);

        System.out.println("Thank you for using the Mobile-Contacts Management System");
    }

    public static void printGenericMenu() {
        System.out.println("Please choose one from the following");
        System.out.println("1. Insert contact");
        System.out.println("2. Delete contact");
        System.out.println("3. Update contact");
        System.out.println("4. Search contact");
        System.out.println("5. Print contacts");
        System.out.println("Q/q. Exit");
    }

    public static String getChoice() {
        System.out.println("Please insert choice");
        return in.nextLine().trim();
    }

    /*
     * UI/User Agent Interaction provided
     * by the controller methods
     */

    public static void handleChoiceController(String s) {
        int choice;
        String phoneNumber;

        try {
            choice = Integer.parseInt(s);
            if (!isValid(choice)) {
                throw new IllegalArgumentException("Error - Choice between 1-5");
            }

            switch (choice) {
                case 1:
                    try {
                        printContactMenu();
                        insertContactService(getFirstName(), getLastName(), getPhoneNumber());
                        System.out.println("Successful Insert");
                    } catch (IllegalArgumentException e) {
                        log(e, "Insert Contact Error");
                        throw e;
                    }
                    break;
                case 2:
                    try {
                        phoneNumber = getPhoneNumber();
                        deleteContactService(phoneNumber);
                        System.out.println("Successful Delete");
                    } catch (IllegalArgumentException e) {
                        log(e, " Delete Contact Error");
                        throw e;
                    }
                    break;
                case 3:
                    try {
                        phoneNumber = getPhoneNumber();
                        getContactByPhoneNumberService(phoneNumber);
                        printContactMenu();
                        updateContactService(phoneNumber, getFirstName(), getLastName(), getPhoneNumber());
                        System.out.println("Successful Update");
                    } catch (IllegalArgumentException e) {
                        log(e, " Update Contact Error");
                        throw e;
                    }
                    break;
                case 4:
                    try {
                        phoneNumber = getPhoneNumber();
                        String[] contact = getContactByPhoneNumberService(phoneNumber);
                        printContact(contact);
                    } catch (IllegalArgumentException e) {
                        log(e, " Print Contact Error");
                        throw e;
                    }
                    break;
                case 5:
                    try {
                        String[][] allContacts = getAllContactsService();
                        printContacts(allContacts);
                    } catch (IllegalArgumentException e) {
                        log(e, " Print All Contacts Error");
                        throw e;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid choice");
            }
        } catch (IllegalArgumentException e) {

        }
    }

    public static void printContacts(String[][] contacts) {
        for (String[] contact : contacts) {
            System.out.printf("%s\t%s\t%s\n", contact[0], contact[1], contact[2]);
        }
    }

    public static void printContact(String[] contact) {
        for (String item: contact) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void printContactMenu() {
        System.out.println("Please insert Firstname, Lastname, Phonenumber");
    }

    public static boolean isValid(int choice) {
        return ((choice >= 1) && (choice <= 5));
    }

    public static String getFirstName() {
        System.out.println("Please insert Firstname");
        return in.nextLine().trim();
    }

    public static String getLastName() {
        System.out.println("Please insert Lastname");
        return in.nextLine().trim();
    }

    public static String getPhoneNumber() {
        System.out.println("Please insert Phonenumber");
        return in.nextLine().trim();
    }

    /*
     * Service layer - services provided to the client
     */

    public static String[] getContactByPhoneNumberService(String phoneNumber) throws IllegalArgumentException {
        try {
            String[] contact = getContactByPhoneNumber(phoneNumber);
            if (contact.length == 0) {
                throw new IllegalArgumentException("Contact not found");
            } else {
                return contact;
            }
        } catch( IllegalArgumentException e) {
            log(e);
            throw e;
        }
    }

    public static String[][] getAllContactsService() {
        String[][] contacts = getAllContacts();

        try {
            if (contacts.length == 0) throw new IllegalArgumentException("List is empty");
            return contacts;
        } catch (IllegalArgumentException e) {
            log(e);
            throw e;
        }
    }

    public static void insertContactService(String firstname, String lastname, String phoneNumber) {
        try {
            if (!insertContact(firstname, lastname, phoneNumber)) {
                throw new IllegalArgumentException("Error in insert");
            }
        } catch (IllegalArgumentException e) {
            log(e);
            throw e;
        }
    }

    public static void updateContactService(String oldPhoneNumber, String firstname, String lastname, String phoneNumber) {
        try {
            if (!updateContact(oldPhoneNumber, firstname, lastname, phoneNumber)) {
                throw new IllegalArgumentException("Update Error");
            }
        } catch (IllegalArgumentException e) {
            log(e);
            throw e;
        }
    }

    public static void deleteContactService(String phoneNumber) {
        try {
            if (!deleteContact(phoneNumber)) {
                throw new IllegalArgumentException("Error in Delete");
            }
        } catch (IllegalArgumentException e) {
            log(e);
            throw e;
        }
    }


    /*
     * CRUD Services
     */
    public static int getContactIndexByPhoneNumber(String phoneNumber) {
        for (int i = 0; i <= pivot; i++) {
            if (contacts[i][2].equals(phoneNumber)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean insertContact(String firstname, String lastname, String phoneNumber) {
        boolean inserted = false;

        if (isFull(contacts)) return false;

        if (getContactIndexByPhoneNumber(phoneNumber) == -1) {
            pivot++;
            contacts[pivot][0] = firstname;
            contacts[pivot][1] = lastname;
            contacts[pivot][2] = phoneNumber;
            inserted = true;
        }

        return inserted;
    }

    public static boolean isFull(String[][] contacts) {
        return (pivot == contacts.length - 1);
    }

    public static boolean updateContact(String oldPhoneNumber, String firstname, String lastname, String phoneNumber) {
        boolean updated = false;
        int positionToUpdate = getContactIndexByPhoneNumber(oldPhoneNumber);

        if (isFull(contacts)) return false;

        if (positionToUpdate != -1) {
            contacts[positionToUpdate][0] = firstname;
            contacts[positionToUpdate][1] = lastname;
            contacts[positionToUpdate][2] = phoneNumber;
            updated = true;
        }

        return updated;
    }
    public static boolean deleteContact(String phoneNumber) {
        int positionToDelete = getContactIndexByPhoneNumber(phoneNumber);
        boolean deleted = false;

        if (positionToDelete != -1) {
            System.arraycopy(contacts, positionToDelete + 1, contacts, positionToDelete, pivot - positionToDelete -1);
            pivot --;
            deleted = true;
        }

        return deleted;
    }

    public static String[] getContactByPhoneNumber(String phoneNumber) {
        int positionToReturn = getContactIndexByPhoneNumber(phoneNumber);

        if (positionToReturn == -1) {
            return new String[] {};
        } else {
            return contacts[positionToReturn];
        }
    }

    public static String[][] getAllContacts() {
        return Arrays.copyOf(contacts, pivot +1);
    }

    /*
     * logger
     */
    public static void log(Exception e, String...messages) {
        try (PrintStream ps = new PrintStream(new FileOutputStream(path.toFile(), true))) {
            ps.println(LocalDateTime.now() + "\n" + e + "\n");
            ps.printf("%s", (messages.length == 1) ? messages[0] : "");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
