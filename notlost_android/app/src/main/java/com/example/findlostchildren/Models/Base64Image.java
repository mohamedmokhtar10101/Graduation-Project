package com.example.findlostchildren.Models;

public class Base64Image {
    String victimId, base64Image;


    public Base64Image(){


    }

    public Base64Image(String victimId, String base64Image) {
        this.victimId = victimId;
        this.base64Image = base64Image;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}

