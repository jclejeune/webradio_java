// src/com/webradio/model/Radio.java
package com.webradio.model;

public class Radio {
    private String name;
    private String url;
    
    public Radio(String name, String url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() { return name; }
    public String getUrl() { return url; }
    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    
    @Override
    public String toString() {
        return name;
    }
}