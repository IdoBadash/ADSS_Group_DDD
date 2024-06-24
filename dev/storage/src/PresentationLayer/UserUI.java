package PresentationLayer;

import BuisnessLayer.Item;
import BuisnessLayer.ItemStatus;
import BuisnessLayer.Product;
import BuisnessLayer.Storage;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UserUI {
    private static Storage storage;

    public UserUI(Storage storage) {
        this.storage = storage;
    }

    public void displayMenu(Scanner scanner) {
        int choice=0;
        do {
                System.out.println("\nMain Menu:");
                System.out.println("1. Show all Available products");
                System.out.println("2. Details about a specific Product");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
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
                        showAvailableProducts();
                        break;
                    case 2:
                        showProductDetails(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 3);
        }
    private void showAvailableProducts() {
        System.out.println("Available products in storage:");
        for (Product product : storage.getAllProducts()) {
            for (Item item : product.getItems().values()) {
                if (item.getStatus() == ItemStatus.Available) {
                    System.out.println(product.getProductName());
                    break;
                }
            }
        }
    }
    private void showProductDetails(Scanner scanner) {
        System.out.print("Enter Product name: ");
        String ProductName = scanner.nextLine();
        Product product = storage.getProductByName(ProductName);
        if (product != null) {
            System.out.println("Product details: " + product);
        } else {
            System.out.println("Product not found.");
        }
    }
}