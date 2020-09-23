package com.example.vlsm.data.model;

import com.example.vlsm.calculate.IP;

public class SubRed {

    private int nodesAmount;
    private String subredDescriptcion;
    private int subredSize;/*Nodes Amount Possible*/

    private IP subredStartIP;
    private IP subredEndIP;
    private int subredMask;

    public SubRed(int nodesAmount, String subredDescriptcion,IP startIP) throws Exception {
        this.nodesAmount = nodesAmount;
        this.subredDescriptcion = subredDescriptcion;
        for(int i = 2;i<=8;i++){
            int potencia = (int) Math.pow(2,i);
            if(potencia>= (nodesAmount+2)){
                this.subredSize = potencia;
                this.subredMask = 32-i;
                break;
            }
        }
        this.subredStartIP = startIP.clone();
        this.subredEndIP = startIP.plusNodes(subredSize-1);
        if(subredEndIP == null){
            throw new Exception("Subred exceeded 255.255.255.255");

        }
    }

    /*For temporal list, that will set the order*/
    public SubRed(int nodesAmount, String subredDescriptcion) throws Exception {
        this.nodesAmount = nodesAmount;
        this.subredDescriptcion = subredDescriptcion;
        for(int i = 2;i<=8;i++){
            int potencia = (int) Math.pow(2,i);
            if(potencia>= (nodesAmount+2)){
                this.subredSize = potencia;
                this.subredMask = 32-i;
                break;
            }
        }

    }

    public int getNodesAmount() {
        return nodesAmount;
    }

    public String getSubredDescriptcion() {
        return subredDescriptcion;
    }

    public int getSubredSize() {
        return subredSize;
    }

    public IP getSubredStartIP() {
        return subredStartIP;
    }

    public IP getSubredEndIP() {
        return subredEndIP;
    }

    public int getSubredMask() {
        return subredMask;
    }

    public void setNodesAmount(int nodesAmount) {
        this.nodesAmount = nodesAmount;
    }

    public void setSubredDescriptcion(String subredDescriptcion) {
        this.subredDescriptcion = subredDescriptcion;
    }
}
