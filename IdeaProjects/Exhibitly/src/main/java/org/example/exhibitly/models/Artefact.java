package org.example.exhibitly.models;

public class Artefact {
    private String id;
    private String name;
    private String region;
    private int period;
    private String imageUrl; // Path relatif ke gambar

    public Artefact(String id, String name, String region, int period, String imageUrl) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.period = period;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getRegion() { return region; }
    public int getPeriod() { return period; }
    public String getImageUrl() { return imageUrl; }
    public String getId() { return id; }
}

