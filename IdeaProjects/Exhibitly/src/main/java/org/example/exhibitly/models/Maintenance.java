package org.example.exhibitly.models;

import java.util.Date;

public class Maintenance {
    private int maintenanceID;
    private Integer kuratorID;
    private Integer staffID;
    private String requestName;
    private int artefactID;
    private String artefactName;
    private Date requestDate;
    private Date performedDate;
    private String type;
    private String status;
    private String description;

    public Maintenance(String newRequestName, int dummyArtefactID, Date currentDateTime, String permintaanUser, String notDone, String description) {
    }

    public Maintenance(int maintenanceID, Integer kuratorID, Integer staffID, String requestName, int artefactID, String artefactName,
                       Date requestDate, Date performedDate, String type, String status, String description) {
        this.maintenanceID = maintenanceID;
        this.kuratorID = kuratorID;
        this.staffID = staffID;
        this.requestName = requestName;
        this.artefactID = artefactID;
        this.artefactName = artefactName;
        this.requestDate = requestDate;
        this.performedDate = performedDate;
        this.type = type;
        this.status = status;
        this.description = description;
    }

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(int maintenanceID) {
        this.maintenanceID = maintenanceID;
    }

    public Integer getKuratorID() {
        return kuratorID;
    }

    public void setKuratorID(Integer kuratorID) {
        this.kuratorID = kuratorID;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public int getArtefactID() {
        return artefactID;
    }

    public void setArtefactID(int artefactID) {
        this.artefactID = artefactID;
    }

    public String getArtefactName() {
        return artefactName;
    }

    public void setArtefactName(String artefactName) {
        this.artefactName = artefactName;
    }

    public String getArtefactID_Name() {
        return artefactID + "_" + artefactName;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(Date performedDate) {
        this.performedDate = performedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}