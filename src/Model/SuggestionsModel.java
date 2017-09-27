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
public class SuggestionsModel {

    String sId;
    String uId;
    String suggestion;

    public SuggestionsModel(String sId, String uId, String suggestion) {
        this.sId = sId;
        this.uId = uId;
        this.suggestion = suggestion;
    }

    public String getsId() {
        return sId;
    }

    public String getuId() {
        return uId;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

}
