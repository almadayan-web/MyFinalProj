package com.alma.myfinalproj.model;

import android.widget.Toast;

import java.util.ArrayList;

public class Cart {


    ArrayList <ItemCart>items;
    double total;


    public Cart(ArrayList<ItemCart> items, double total) {

        this.items = items;
        this.total = total;
    }


    public Cart(ArrayList<ItemCart> items) {

        this.items = items;
        this.total = 5;
    }

    public Cart() {

        this.items=new ArrayList<>();
        this.total=0;
    }


    public ArrayList<ItemCart> getItems() {
        return items;
    }

    public  void addItemToCart(ItemCart itemCart){

        boolean found=false;

        if (itemCart == null) {
            //Toast.makeText(Cart.this, "המוצר ktttt ", Toast.LENGTH_SHORT).show();
            return;
        }


        if(this.items==null) {

            this.items = new ArrayList<>();
        }
            else{

                for (int i = 0; i < this.items.size(); i++) {

                    if (this.items.get(i).getItem().getId().equals(itemCart.getItem().getId())) {
                        found = true;

                        this.items.get(i).setAmount(this.items.get(i).getAmount() + 1);

                    }

                }
            }


            if (!found)
                this.items.add(itemCart);
        }



    public  void  removeItemFromCart(ItemCart itemCart){
        if (itemCart == null) {
            //Toast.makeText(Cart.this, "המוצר ktttt ", Toast.LENGTH_SHORT).show();
            return;
        }

        this.items.remove(itemCart);

    }

    public void setItems(ArrayList<ItemCart> items) {
        this.items = items;
    }

    public double getTotal() {

        double sum = 0;

        if (this.items == null) {

            this.items = new ArrayList<>();
        } else {

            for (int i = 0; i < this.items.size(); i++) {

                sum += this.items.get(i).getItem().getPrice() * this.items.get(i).getAmount();


            }



        }
        return sum;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (ItemCart itemOrder : this.items) {
            totalPrice += itemOrder.getItem().getPrice()*itemOrder.getAmount();
        }
        return totalPrice;
    }



    public void setTotal() {
        this.total = getTotal();
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Cart{" +

                ", items=" + items +
                ", total=" + total +
                '}';
    }
}
