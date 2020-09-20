package com.example.vlsm.data.model;

public class SubRed {

    private int nodesAmount;
    private String subredDescriptcion;

    private String subredStartIP;
    private String subredEndIP;
    private String subredStartMask;

    public SubRed(int nodesAmount, String subredDescriptcion, String subredStartIP, String subredEndIP, String subredStartMask) {
        this.nodesAmount = nodesAmount;
        this.subredDescriptcion = subredDescriptcion;
        this.subredStartIP = subredStartIP;
        this.subredEndIP = subredEndIP;
        this.subredStartMask = subredStartMask;
    }

    public int getNodesAmount() {
        return nodesAmount;
    }

    public void setNodesAmount(int nodesAmount) {
        this.nodesAmount = nodesAmount;
    }

    public String getSubredDescriptcion() {
        return subredDescriptcion;
    }

    public void setSubredDescriptcion(String subredDescriptcion) {
        this.subredDescriptcion = subredDescriptcion;
    }

    public String getSubredStartIP() {
        return subredStartIP;
    }

    public void setSubredStartIP(String subredStartIP) {
        this.subredStartIP = subredStartIP;
    }

    public String getSubredEndIP() {
        return subredEndIP;
    }

    public void setSubredEndIP(String subredEndIP) {
        this.subredEndIP = subredEndIP;
    }

    public String getSubredStartMask() {
        return subredStartMask;
    }

    public void setSubredStartMask(String subredStartMask) {
        this.subredStartMask = subredStartMask;
    }
}
