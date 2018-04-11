package com.phereapp.phere.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andres on 2/24/2018.
 */

public class Phere implements Serializable{
    private String phereName;
    private String phereLocation;
    private String privacy;
    private String host;
    private List<String> members =  new ArrayList<>();
    private String phereDescription;
    private String phereDate;
    private String imageURL;
    private String time;


    public Phere(){}

    // Constructor
    public Phere(String phereName, String phereLocation, String privacy, String host){
        this.phereName = phereName;
        this.phereLocation = phereLocation;
        this.privacy = privacy;
        this.host = host;
        this.members.add(host);
    }

    //getters
    public String getPhereName() { return phereName;}
    public String getPhereLocation() { return phereLocation;}
    public String getPrivacy() { return privacy;}
    public String getHost() {return host;}
    public String getPhereDescription() {
        return phereDescription;
    }
    public List<String> getMembers() {
        return members;
    }
    public String getDisplayPhereName(){
        return this.phereName.replaceAll("_"," ");
    }
    public String getPhereDate() {
        return phereDate;
    }

    public String getTime() {
        return time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public boolean addMembers(String newMember){
        if(!this.members.contains(newMember)){
            this.members.add(newMember);
            return true;
        }
        return false;
    }
    public void setPhereDescription(String phereDescription) {
        this.phereDescription = phereDescription;
    }

    public void setPhereDate(String phereDate) {
        this.phereDate = phereDate;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
