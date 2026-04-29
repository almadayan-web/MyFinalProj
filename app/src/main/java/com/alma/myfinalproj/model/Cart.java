package com.alma.myfinalproj.model;

import java.util.ArrayList;

public class Cart {
    ArrayList<ItemCart> items;
    double total;

    public Cart(ArrayList<ItemCart> items, double total) {
        this.items = items;
        this.total = total;
    }

    public Cart() {
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public ArrayList<ItemCart> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemCart> items) {
        this.items = items;
    }

    public void addItemToCart(ItemCart itemCart) {
        if (itemCart == null) return;
        if (this.items == null) this.items = new ArrayList<>();

        boolean found = false;
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).getItemId().equals(itemCart.getItemId())) {
                found = true;
                this.items.get(i).setAmount(this.items.get(i).getAmount() + 1);
            }
        }
        if (!found) this.items.add(itemCart);
    }

    public void removeItemFromCart(ItemCart itemCart) {
        if (itemCart == null) return;
        this.items.remove(itemCart);
    }

    public double getTotal() {
        double sum = 0;
        if (this.items == null) return 0;
        for (ItemCart item : this.items) {
            sum += item.getItemPrice() * item.getAmount();
        }
        return sum;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotalPrice() {
        return getTotal();
    }

    @Override
    public String toString() {
        return "Cart{items=" + items + ", total=" + total + '}';
    }
}