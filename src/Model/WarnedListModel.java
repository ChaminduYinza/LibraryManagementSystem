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
public class WarnedListModel {

    String wId;
    String uId;
    String fullName;
    String description;
    String warningType;

    public WarnedListModel(String wId, String uId, String fullName, String description, String warningType) {
        this.wId = wId;
        this.uId = uId;
        this.fullName = fullName;
        this.description = description;
        this.warningType = warningType;
    }

    public String getwId() {
        return wId;
    }

    public String getuId() {
        return uId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String getWarningType() {
        return warningType;
    }

    public void setwId(String wId) {
        this.wId = wId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWarningType(String warningType) {
        this.warningType = warningType;
    }

}
