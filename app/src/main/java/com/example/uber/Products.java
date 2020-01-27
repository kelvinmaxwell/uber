package com.example.uber;

import com.google.gson.annotations.SerializedName;

public class Products {


    @SerializedName("item")
    public String item;
    @SerializedName("quantity")
    public String quantity;
    @SerializedName("price_paid")
    public String price_paid;
    @SerializedName("location")
    public String location;

}
