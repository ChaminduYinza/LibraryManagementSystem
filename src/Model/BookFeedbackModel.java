/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Sameera
 */
public class BookFeedbackModel {

    String fId,bId,feedback,fullName;

    public BookFeedbackModel(String fId, String bId, String feedback, String fullName) {
        this.fId = fId;
        this.bId = bId;
        this.feedback = feedback;
        this.fullName = fullName;
        
    }

    public String getfId() {
        return fId;
    }

    public String getbId() {
        return bId;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getFullName() {
        return fullName;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
