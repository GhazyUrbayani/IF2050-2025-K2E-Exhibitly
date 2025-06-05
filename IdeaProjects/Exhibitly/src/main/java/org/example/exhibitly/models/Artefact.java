package org.example.exhibitly.models;

public class Artefact {
    private String id;
    private String name;
    private String region;
    private int periodStart;
    private int periodEnd;
    private String imageUrl; // Path relatif ke gambar

    public Artefact(String id, String name, String region, int periodStart, int periodEnd, String imageUrl) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getRegion() { return region; }
    public int getPeriodStart() { return periodStart; }
    public int getPeriodEnd() { return periodEnd; }
    public String getImageUrl() { return imageUrl; }

    public String getId() { return id; }
}

