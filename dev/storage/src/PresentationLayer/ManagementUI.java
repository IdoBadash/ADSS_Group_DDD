package PresentationLayer;

import BuisnessLayer.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ManagementUI {
    private static Storage storage;

    public ManagementUI(Storage storage) {
        this.storage = storage;
    }

    public void displayMenu(Scanner scanner) {
        int choice = 0;
        do {
            System.out.println("\nManagement Menu");
            System.out.println("1. Show All Items");
            System.out.println("2. Show All Products");
            System.out.println("3. Show Specific Items (By Category, Status, or Place)");
            System.out.println("4. Details about a specific item");
            System.out.println("5. Apply Discount");
            System.out.println("6. Generate Report");
            System.out.println("7. Set Minimum Quantity For Product");
            System.out.println("8. View Total Amount in Storage");
            System.out.println("9. Exit");
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
                    showAllItems();
                    break;
                case 2:
                    showAllProducts();
                    break;
                case 3:
                    showSpecificItemsMenu(scanner);
                    break;
                case 4:
                    showItemDetails(scanner);
                    break;
                case 5:
                    applyDiscountMenu(scanner);
                    break;
                case 6:
                    generateReportMenu(scanner);
                    break;
                case 7:
                    setMinimumQuantityForProduct(scanner);
                    break;
                case 8:
                    displayTotalAmountInStorage();
                    break;
                case 9:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
                    break;
            }
        } while (choice != 9);
    }

    private void showSpecificItemsMenu(Scanner scanner) {
        System.out.println("\nShow Specific Items");
        System.out.println("1. By Category, Sub Category and Size ");
        System.out.println("2. By Status");
        System.out.println("3. By Place");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                showItemsInCategoriesMenu(scanner);
                break;
            case 2:
                showItemsByStatus(scanner);
                break;
            case 3:
                showItemsByPlace(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }
    private void showItemsInCategoriesMenu(Scanner scanner) {
        System.out.print("\nEnter category (leave blank if no filter): ");
        String category = scanner.nextLine();
        System.out.print("Enter sub-category (leave blank if no filter): ");
        String subCategory = scanner.nextLine();
        System.out.print("Enter size (leave blank if no filter): ");
        String sizeInput = scanner.nextLine();

        // Convert size input to double if provided
        Double size = null;
        if (!sizeInput.isEmpty()) {
            try {
                size = Double.parseDouble(sizeInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid size input. Please enter a valid number.");
                return;
            }
        }

        List<Product> products = storage.getProductsByFilters(category, subCategory, size);
        if (products.isEmpty()) {
            System.out.println("No products found matching the given filters.");
        } else {
            products.forEach(product -> {
                System.out.println("Product: " + product.getProductName());
                product.getItems().values().forEach(item -> System.out.println("  " + item));
            });
        }
    }

    private void setMinimumQuantityForProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();
        System.out.print("Enter minimum quantity: ");
        int minQuantity = scanner.nextInt();
        storage.setMinimumQuantityForProduct(productName, minQuantity);
        System.out.println("Minimum quantity set for product " + productName);
    }

    private void showAllItems() {
        List<Product> allProducts = storage.getAllProducts();
        allProducts.forEach(product -> {
            System.out.println("Product: " + product.getProductName());
            product.getItems().values().forEach(item -> System.out.println("  " + item));
        });
    }

    private void showAllProducts() {
        System.out.println("\nAll Products In Storage:");
        for (Product product : storage.getAllProducts()) {
            if (product.getStatus().equals(ProductStatus.InStorage)) {
                System.out.println(product.toString());
                System.out.println(); // Empty line between products
            }
        }
    }



    private void showItemsByStatus(Scanner scanner) {
        System.out.print("Enter status (Available, Defective, Sold, Expired): ");
        String statusStr = scanner.nextLine();
        ItemStatus status = ItemStatus.valueOf(statusStr);
        List<Item> items = storage.getItemsByStatus(status);
        items.forEach(item -> System.out.println("Item: " + item));
    }

    private void showItemsByPlace(Scanner scanner) {
        System.out.print("Enter place (Store, Warehouse): ");
        String placeStr = scanner.nextLine();
        ItemPlace place = ItemPlace.valueOf(placeStr);
        List<Item> items = storage.getItemsByPlace(place);
        items.forEach(item -> System.out.println("Item: " + item));
    }

    private void showItemDetails(Scanner scanner) {
        System.out.print("Enter item code: ");
        String itemCode = scanner.nextLine();
        Item item = storage.getItemByCode(itemCode);
        if (item != null) {
            System.out.println("Item details: " + item);
        } else {
            System.out.println("Item not found.");
        }
    }

    private void applyDiscountToCategory(Scanner scanner) {
        System.out.print("Enter category to apply discount: ");
        String category = scanner.nextLine();
        System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
        double rate = scanner.nextDouble();

        // Handle date input
        LocalDate startDate = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.print("Enter discount start date (YYYY-MM-DD): ");
                String startDateStr = scanner.next();
                startDate = LocalDate.parse(startDateStr);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Similar handling for end date
        LocalDate endDate = null;
        validDate = false;
        while (!validDate) {
            try {
                System.out.print("Enter discount end date (YYYY-MM-DD): ");
                String endDateStr = scanner.next();
                endDate = LocalDate.parse(endDateStr);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Create and apply the discount
        Discount discount = new Discount(rate, startDate, endDate);
        storage.applyDiscountToCategory(category, discount);
        System.out.println("Discount applied to category " + category);
    }

    private void applyDiscountToProduct(Scanner scanner) {
        System.out.print("Enter product name to apply discount: ");
        String productName = scanner.nextLine();
        System.out.print("Enter discount rate (e.g., 0.1 for 10%): ");
        double rate = scanner.nextDouble();

        // Handle start date input
        LocalDate startDate = null;
        boolean validStartDate = false;
        while (!validStartDate) {
            try {
                System.out.print("Enter discount start date (YYYY-MM-DD): ");
                String startDateStr = scanner.next();
                startDate = LocalDate.parse(startDateStr);
                validStartDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Similar handling for end date
        LocalDate endDate = null;
        boolean validEndDate = false;
        while (!validEndDate) {
            try {
                System.out.print("Enter discount end date (YYYY-MM-DD): ");
                String endDateStr = scanner.next();
                endDate = LocalDate.parse(endDateStr);
                validEndDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Create and apply the discount
        Discount discount = new Discount(rate, startDate, endDate);
        storage.applyDiscountToProduct(productName, discount);
        System.out.println("Discount applied to product " + productName);
    }

    private void generateReportForCategories(Scanner scanner) {
        System.out.print("Enter categories (separated with ','): ");
        String[] categories = scanner.nextLine().split(",");
        for (String category : categories) {
            List<Product> products = storage.generateCategoryReport(category.trim());
            System.out.println("Category: " + category);
            products.forEach(product -> {
                System.out.println("  Product: " + product.getProductName());
                product.getItems().values().forEach(item -> System.out.println("    " + item));
            });
        }
    }

    private void applyDiscountMenu(Scanner scanner) {
        System.out.println("\nApply Discount");
        System.out.println("1. Apply Discount to Category");
        System.out.println("2. Apply Discount to Product");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                applyDiscountToCategory(scanner);
                break;
            case 2:
                applyDiscountToProduct(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void generateReportMenu(Scanner scanner) {
        System.out.println("\nGenerate Report");
        System.out.println("1. Generate Report for Categories");
        System.out.println("2. Generate Report for Defective Products");
        System.out.println("3. Generate Report for Expired Products");
        System.out.println("4. Generate Report for Products Below Minimum Quantity");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                generateReportForCategories(scanner);
                break;
            case 2:
                generateDefectiveProductsReport();
                break;
            case 3:
                generateExpiredProductsReport();
                break;
            case 4:
                generateBelowMinimumReport();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void displayTotalAmountInStorage() {
        int totalQuantity = storage.getTotalProductQuantity();
        int totalQuantityInStore = storage.getTotalProductQuantityInStore();
        int totalQuantityInWarehouse = storage.getTotalProductQuantityInWarehouse();
        System.out.println("Total Amount in Storage:");
        System.out.println("Total Product Quantity: " + totalQuantity);
        System.out.println("Total Quantity in Store: " + totalQuantityInStore);
        System.out.println("Total Quantity in Warehouse: " + totalQuantityInWarehouse);
    }

    private void generateDefectiveProductsReport() {
        List<Item> defectiveProducts = storage.generateDefectiveProductsReport();
        if (defectiveProducts.isEmpty()) {
            System.out.println("No defective products found.");
        } else {
            System.out.println("\nDefective Products:");
            defectiveProducts.forEach(item -> System.out.println("Item: " + item));
        }
    }

    private void generateExpiredProductsReport() {
        List<Item> expiredProducts = storage.generateExpiredProductsReport();
        if (expiredProducts.isEmpty()) {
            System.out.println("No expired products found.");
        } else {
            System.out.println("\nExpired Products:");
            expiredProducts.forEach(item -> System.out.println("Item: " + item));
        }
    }

    private void generateBelowMinimumReport() {
        List<Product> productsBelowMinimum = storage.generateBelowMinimumReport();
        if (productsBelowMinimum.isEmpty()) {
            System.out.println("No products below minimum quantity.");
        } else {
            System.out.println("\nProducts Below Minimum Quantity:");
            for (Product product : productsBelowMinimum) {
                System.out.println("Product: " + product.getProductName());
                System.out.println("Current Quantity: " + product.getTotalQuantity());
                System.out.println("Minimum Quantity: " + product.getMinimumQuantityForAlert());
            }
        }
    }
}
