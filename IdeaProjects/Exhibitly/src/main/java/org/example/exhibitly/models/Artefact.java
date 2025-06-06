package org.example.exhibitly.models;

public class Artefact {
    private int artefactID;
    private String title;
    private String region;
    private int period;
    private String description;
    private String mediaURL;

    public Artefact() {
    }

    public Artefact(int artefactID, String title, String region, int period, String description, String mediaURL) {
        this.artefactID = artefactID;
        this.title = title;
        this.region = region;
        this.period = period;
        this.description = description;
        this.mediaURL = mediaURL;
    }

    public int getArtefactID() {
        return artefactID;
    }

    public void setArtefactID(int artefactID) {
        this.artefactID = artefactID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }
}