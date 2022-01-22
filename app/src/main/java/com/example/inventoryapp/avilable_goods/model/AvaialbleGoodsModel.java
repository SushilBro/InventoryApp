package com.example.inventoryapp.avilable_goods.model;

public class AvaialbleGoodsModel {
    private String productName,productImage;
    private String purchaseDate;
    private int quantity,purchasePrice,salesPrice;

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPrductName() {
        return productName;
    }

    public void setPrductName(String prductName) {
        this.productName = prductName;

    }
    public void Update(String text){
        productName=text;

    }


    public AvaialbleGoodsModel(String productName, String purchaseDate, int quantity, int purchasePrice, int salesPrice, String productImage) {
        this.productName = productName;
        this.purchaseDate = purchaseDate;
        this.productImage = productImage;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.salesPrice = salesPrice;
    }
}
