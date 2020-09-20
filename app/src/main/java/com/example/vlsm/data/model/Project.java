package com.example.vlsm.data.model;




import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Project {

    private String projectName;

    private String ip;
    private String mask;

    private ArrayList<SubRed> listNodos;

    String dateTimeCreation;

    public Project(String projectName, String ip) {
        this(projectName,ip, calculateDefaultMask(ip));
        /*If there's no mask it's needed to calculate the default mask by checking the IP begining 192,172... */
    }

    public Project(String projectName, String ip, String mask) {
        this(projectName,ip,mask,new ArrayList<SubRed>());
    }

    public Project(String projectName, String ip, String mask, ArrayList<SubRed> listNodos) {
        this.projectName = projectName;
        this.ip = ip;
        this.mask = mask;
        this.listNodos = listNodos;
        Calendar currentTime = Calendar.getInstance();/*.getTime();*/
        this.dateTimeCreation = DateFormat.getDateTimeInstance().format(currentTime.getTime());

    }

    private static String calculateDefaultMask(String ip){
        return "default Mask";/*TODO change*/
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public ArrayList<SubRed> getListNodos() {
        return listNodos;
    }

    public void setListNodos(ArrayList<SubRed> listNodos) {
        this.listNodos = listNodos;
    }

    public String getDateTimeCreation() {
        return dateTimeCreation;
    }

    public void setDateTimeCreation(String dateTimeCreation) {
        this.dateTimeCreation = dateTimeCreation;
    }
}
