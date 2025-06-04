package org.example.exhibitly.models;

public class Feedback {
    private int feedbackID;
    private int exhibitID;
    private int visitorID;
    private String comment;

    public Feedback() {
    }

    public Feedback(int feedbackID, int exhibitID, int visitorID, String comment) {
        this.feedbackID = feedbackID;
        this.exhibitID = exhibitID;
        this.visitorID = visitorID;
        this.comment = comment;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public int getExhibitID() {
        return exhibitID;
    }

    public void setExhibitID(int exhibitID) {
        this.exhibitID = exhibitID;
    }

    public int getVisitorID() {
        return visitorID;
    }

    public void setVisitorID(int visitorID) {
        this.visitorID = visitorID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}