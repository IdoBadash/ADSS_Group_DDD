package BuisnessLayer;

import java.time.LocalDate;
import java.util.HashMap;

public class Product {
    private HashMap<String, Item> items;
    private int quantityInStore;
    private int quantityInWarehouse;
    private int minimumQuantityForAlert;
    private Discount discount;
    private String manufacturer;
    private double costPrice;
    private double sellingPrice;
    private String productName;
    private String category;
    private String subCategory;
    private Double size;
    private ProductStatus status;
    private String productCode;

    public Product(String manufacturer, String category, String productName, String subCategory, String productCode, double costPrice, double size, int quantity,int minimumQuantity, ItemPlace itemplace,LocalDate expirationDate) {
        this.manufacturer = manufacturer;
        this.category = category;
        this.productName = productName;
        this.subCategory = subCategory;
        this.size=size;
        this.productCode = productCode;
        this.costPrice = costPrice;
        this.sellingPrice=costPrice;
        this.status = ProductStatus.InStorage;
       this.minimumQuantityForAlert=minimumQuantity;
        items=new HashMap<>();
        addItems(quantity, itemplace, expirationDate, ItemStatus.Available);

    }

    public void addItems(int addquantity, ItemPlace itemplace, LocalDate expirationDate, ItemStatus itemstatus) {
        int counter;
        if(items.isEmpty()) {
            counter= 0;
        }
       else{
          counter=items.size();
        }
        for (int i = counter; i <counter+addquantity; i++) {
            String itemCode = productCode + "-" + i;
            Item newItem = new Item(itemplace, itemCode,expirationDate,itemstatus);
            items.put(newItem.getItemCode(), newItem);
        }
        if(itemplace.equals(ItemPlace.Store))
            quantityInStore+=addquantity;
        if(itemplace.equals(ItemPlace.Warehouse))
            quantityInWarehouse+=addquantity;
    }

    public void setMinimum(int minimum) {
        this.minimumQuantityForAlert = minimum;
    }

    public HashMap<String, Item> getItems() {
        return items;
    }

    public int getQuantityInStore() {
        return quantityInStore;
    }

    public void removeItem (String Itemcode, ItemStatus itemStatus) {
        items.get(Itemcode).setStatus(itemStatus);
            if ( items.get(Itemcode).getStored() == ItemPlace.Store) {
                quantityInStore--;
            } else if ( items.get(Itemcode).getStored() == ItemPlace.Warehouse) {
                quantityInWarehouse--;
            }
        checkQuantity();
        if (getTotalQuantity() <= 0) {
            status = ProductStatus.NotinStorage;
        }
    }
    public Item getItem(String Itemcode) {
        return items.get(Itemcode);
    }

    public int getQuantityInWarehouse() {
        return quantityInWarehouse;
    }

    public int getMinimumQuantityForAlert() {
        return minimumQuantityForAlert;
    }

    public int getTotalQuantity() {
        return quantityInStore + quantityInWarehouse;
    }

    public void applyDiscount(Discount newDiscount) {
        if (newDiscount.isDiscountActive()) {
                    sellingPrice = costPrice * (1 - newDiscount.getDiscountRate());
            discount = newDiscount;
        }
    }

    public void checkQuantity() {
        if (getTotalQuantity() < minimumQuantityForAlert) {
            System.out.println("Alert: The total quantity of product '" + productName + "' is below the minimum threshold. Current total quantity: " + getTotalQuantity());
        }
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getProductName() {
        return productName;
    }

    public Discount getDiscount() {
        return discount;
    }

    public String getProductCode() {
        return productCode;
    }

    public ProductStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Product: ").append(productName).append("\n");
        sb.append("Category: ").append(category).append("\n");
        sb.append("Sub-category: ").append(subCategory).append("\n");
        sb.append("Manufacturer: ").append(manufacturer).append("\n");
        sb.append("Quantity in Store: ").append(quantityInStore).append("\n");
        sb.append("Quantity in Warehouse: ").append(quantityInWarehouse).append("\n");
        sb.append("Minimum Quantity for Alert: ").append(minimumQuantityForAlert).append("\n");
        sb.append("Price: ").append(sellingPrice).append("\n");

        if (discount != null && discount.isDiscountActive()) {
            sb.append("Discount: ").append(discount.getDiscountRate()).append("\n");
        } else {
            sb.append("Discount: Not Active\n");
        }

        sb.append("Status: ").append(status).append("\n");
        return sb.toString();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String printItems() {
        StringBuilder sb = new StringBuilder();
        sb.append("Items in Product: " + productName);
        for (Item item : items.values()) {
           sb.append(item.toString());
        }
        return sb.toString();
    }

    public Double getSize() {
        return size;
    }
}
