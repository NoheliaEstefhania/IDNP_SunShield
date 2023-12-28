package com.example.idnp_sunshield.Models;

public class weather {
    // Fields representing weather information
    private int id;             // Weather condition ID
    private String main;        // Main weather group (e.g., Clear, Clouds, Rain)
    private String description; // Weather condition description
    private String icon;        // Weather icon identifier

    // Constructor to initialize the object with specific values
    public weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    // Getter and Setter methods for each field
    // (Getters are used to retrieve the values, and Setters are used to update the values)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
