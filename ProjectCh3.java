package gr.aueb.cf.exercises;

import java.util.Scanner;

public class ProjectCh3 {
    final static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean quit = false;
        String response;

        do {
            printMenu();
            response = getChoice();
            try {
                if (response.matches("[qQ]")) {
                    quit = true;
                } else {
                    printOnChoice(response);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid choice");
            }
        } while (!quit);
    }

    public static void printMenu() {
        System.out.println("Μενού:");
        System.out.println("1.Εισαγωγή");
        System.out.println("2.Διαγραφή");
        System.out.println("3.Ενημέρωση");
        System.out.println("4.Αναζήτηση");
        System.out.println("Q or q to quit");
    }

    public static String getChoice() {
        return in.nextLine().trim();
    }

    public static void printOnChoice(String s) throws IllegalArgumentException {
        int choice;

        try {
            choice = Integer.parseInt(s);

            switch (choice) {
                case 1:
                    System.out.println("Επιλέξατε Εισαγωγή");
                    break;
                case 2:
                    System.out.println("Επιλέξατε Διαγραφή");
                    break;
                case 3:
                    System.out.println("Επιλέξατε Ενημέρωση");
                    break;
                case 4:
                    System.out.println("Επιλέξατε Αναζήτηση");
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
