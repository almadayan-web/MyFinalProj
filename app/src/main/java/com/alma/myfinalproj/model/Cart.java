package com.alma.myfinalproj.model;

import java.util.ArrayList;

public class Cart {

    String id;
    ArrayList <Item>items;
    double total;


    public Cart(String id, ArrayList<Item> items, double total) {
        this.id = id;
        this.items = items;
        this.total = total;
    }


    public Cart() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


@Override
public String toString() {
    return "Cart{" +
            "id='" + id + '\'' +
            ", items=" + items +
            ", total=" + total +
            '}';
    }
}
