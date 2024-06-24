package PresentationLayer;

import BuisnessLayer.ItemPlace;
import BuisnessLayer.ItemStatus;
import BuisnessLayer.Product;
import BuisnessLayer.Storage;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StorekeeperUI {
    private Storage storage;

    public StorekeeperUI(Storage storage) {
        this.storage = storage;
    }

    public void displayMenu(Scanner scanner) {
        int choice = 0;
        do {
            System.out.println("\nStorekeeper Menu");
            System.out.println("1. Insert New Product");
            System.out.println("2. Update or remove Item Status");
            System.out.println("3. Exit");
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
                    insertNewProduct(scanner);
                    break;
                case 2:
                    updateItemStatus(scanner);
                    break;
                case 3:
                    System.out.println("Exiting Storekeeper Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private void insertNewProduct(Scanner scanner) {
        System.out.print("Enter manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter sub-category: ");
        String subCategory = scanner.nextLine();
        System.out.print("Enter product code: ");
        String productCode = scanner.nextLine();
        System.out.print("Enter purchase price: ");
        double purchasePrice = scanner.nextDouble();
        System.out.print("Enter selling price: ");
        double sellingPrice = scanner.nextDouble();
        System.out.print("Enter minimum quantity for alert: ");
        int minQuantity = scanner.nextInt();
        System.out.print("Enter quantity in store: ");
        int quantityInStore = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter item place (Store or Warehouse): ");
        String itemPlaceInput = scanner.nextLine();
        ItemPlace itemPlace = null;
        boolean validItemPlace = false;
        while (!validItemPlace) {
            try {
                itemPlace = ItemPlace.valueOf(itemPlaceInput);
                validItemPlace = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid item place. Please enter 'Store' or 'Warehouse'.");
                break;
            }
        }
        System.out.print("Enter expiration date (yyyy-mm-dd): ");
        String expirationDateString = scanner.nextLine();
        LocalDate expirationDate = LocalDate.parse(expirationDateString);

        Product product = new Product(manufacturer, category, productName, subCategory, productCode, purchasePrice, sellingPrice, minQuantity, quantityInStore, itemPlace, expirationDate);
        storage.insertProduct(product);
        System.out.println("Product inserted successfully.");
    }

    private void updateItemStatus(Scanner scanner) {
        System.out.print("Enter item code to update: ");
        String code = scanner.nextLine();
        System.out.print("Enter new status for the item: (Defective, Sold, Expired) ");
        String statusString = scanner.nextLine();
        if (isValidStatus(statusString)) {
            ItemStatus status = ItemStatus.valueOf(statusString);
            if (storage.updateItemStatus(code, status)) {
                System.out.println("Item status updated.");
            } else {
                System.out.println("Item not found.");
            }
        } else {
            System.out.println("Invalid status.");
        }
    }

    private static boolean isValidStatus(String statusInput) {
        for (ItemStatus status : ItemStatus.values()) {
            if (status.name().equals(statusInput)) {
                return true;
            }
        }
        return false;
    }
}
