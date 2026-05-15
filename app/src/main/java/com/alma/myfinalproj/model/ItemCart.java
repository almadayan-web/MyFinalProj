package com.alma.myfinalproj.model;

public class ItemCart {

    protected String itemId;
    protected String itemName;
    protected double itemPrice;
    protected int amount;

    public ItemCart(String itemId, String itemName, double itemPrice, int amount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.amount = amount;
    }

    public ItemCart() {}

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public double getItemPrice() { return itemPrice; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "ItemCart{itemId='" + itemId + "', itemName='" + itemName + "', amount=" + amount + '}';
    }
}