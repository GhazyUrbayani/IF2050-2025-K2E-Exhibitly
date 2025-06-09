package org.example.exhibitly.models;

import java.util.List;
import java.util.ArrayList;

public class Exhibit {
    private int exhibitID;
    private int kuratorID;
    private String title;
    private String description;
    private String mediaURL;
    private String curatorName;
    private List<Artefact> artefacts;

    public Exhibit() {
        this.artefacts = new ArrayList<>();
    }

    public Exhibit(int exhibitID, int kuratorID, String title, String description, String mediaURL) {
        this.exhibitID = exhibitID;
        this.kuratorID = kuratorID;
        this.title = title;
        this.description = description;
        this.mediaURL = mediaURL;
        this.artefacts = new ArrayList<>();
    }

    public int getExhibitID() {
        return exhibitID;
    }

    public void setExhibitID(int exhibitID) {
        this.exhibitID = exhibitID;
    }

    public int getKuratorID() {
        return kuratorID;
    }

    public void setKuratorID(int kuratorID) {
        this.kuratorID = kuratorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Artefact> getArtefacts() { 
        return artefacts; 
    }
    public void setArtefacts(List<Artefact> artefacts) { 
        this.artefacts = artefacts; 
    }
    public void addArtefact(Artefact artefact) { 
        this.artefacts.add(artefact); 
    }
    public String getCuratorName() { 
        return curatorName; 
    }
    public void setCuratorName(String curatorName) { 
        this.curatorName = curatorName; 
    }
}