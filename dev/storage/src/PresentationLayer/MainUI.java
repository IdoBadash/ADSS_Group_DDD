package PresentationLayer;

import BuisnessLayer.Storage;
import DataAccessLayer.Database;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MainUI {
    private static Storage storage;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        // Choose storage type first
        chooseStorageType(scanner);

        do {
            System.out.println("\nMain Menu");
            System.out.println("1. Store Keeper Menu");
            System.out.println("2. Management Menu");
            System.out.println("3. User Menu");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.nextLine(); // Consume invalid input
                continue; // Continue to next iteration of the loop
            }
            switch (choice) {
                case 1:
                    StorekeeperUI storekeeperUI = new StorekeeperUI(storage);
                    storekeeperUI.displayMenu(scanner);
                    break;
                case 2:
                    ManagementUI departmentMenu = new ManagementUI(storage);
                    departmentMenu.displayMenu(scanner);
                    break;
                case 3:
                    UserUI userUI = new UserUI(storage);
                    userUI.displayMenu(scanner);
                    break;
                case 4:
                    System.out.println("Exiting Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);
    }

    private static void chooseStorageType(Scanner scanner) {
        int storageChoice = 0;
        do {
            System.out.println("\nChoose Storage Type");
            System.out.println("1. Use Empty Storage");
            System.out.println("2. Use Pre-populated Database");
            System.out.print("Enter your choice: ");

            try {
                storageChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.nextLine(); // Consume invalid input
                continue; // Continue to next iteration of the loop
            }

            switch (storageChoice) {
                case 1:
                    initializeEmptyStorage();
                    break;
                case 2:
                    initializeDatabaseStorage();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (storageChoice < 1 || storageChoice > 2);
    }

    private static void initializeEmptyStorage() {
        storage = new Storage();
    }

    private static void initializeDatabaseStorage() {
        storage = Database.getStorage();
    }
}
