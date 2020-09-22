package com.example.vlsm.data.model;




import com.example.vlsm.calculate.IP;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class Project {

    private String projectName;

    private IP ipProject;
    private int mask;

    private ArrayList<SubRed> listNodos;

    String dateTimeCreation;

    private int totalPossibleNodes;
    private int currentNodesCount;

    public Project(String projectName, IP ip) {
        this(projectName,ip, calculateDefaultMask(ip));
        /*If there's no mask it's needed to calculate the default mask by checking the IP begining 192,172... */
    }

    public Project(String projectName, IP ipProject, int mask) {
        this(projectName,ipProject,mask,new ArrayList<SubRed>());
    }

    public Project(String projectName, IP ipProject, int mask, ArrayList<SubRed> listNodos) {
        this.projectName = projectName;
        this.ipProject = IP.calculateStartIP( ipProject,mask);
        this.mask = mask;
        this.listNodos = listNodos;
/*
        this.tempListNodosToCalculateSizesAndOrder = new ArrayList<>();
*/
        Calendar currentTime = Calendar.getInstance();/*.getTime();*/
        this.dateTimeCreation = DateFormat.getDateTimeInstance().format(currentTime.getTime());

        /*if mask = 22, then the total ip that could exist are (2^(32-22))-2 */
        this.totalPossibleNodes =  ( (int) Math.pow(2,32-mask) ) /*-2*/;

        this.currentNodesCount = 0;
        this.currentNodesCount = getCurrentNodesCount(this.listNodos);

    }

    private static int getCurrentNodesCount(ArrayList<SubRed> listNodos){
        int currentNodesCount = 0;
        for(SubRed subRed : listNodos){
            currentNodesCount += subRed.getSubredSize();
        }
        return  currentNodesCount;
    }


    public static int calculateDefaultMask(IP ip){
        String[] octetos = ip.getIp().split("\\.");
        int octeto1 = Integer.parseInt(octetos[0]);
        if(octeto1 >=1 && octeto1 <=127){
            /*Clase A*/
            /*return "255.0.0.0";*/
            return 8;
        }
        else if(octeto1<=191){/*implicit >=128*/
            /*Clase B*/
            /*return "255.255.0.0";*/
            return 16;

        }
        else if(octeto1<=223){/*implicit >=192*/
            /*Clase C*/
            /*return "255.255.255.0";*/
            return 24;

        }
        else if(octeto1>=224){/*IP 224.0.0.0 a 239.255.255.255 for multidifusión no mask needed*/
            return 0;
        }

        return -1;/*TODO change*/
    }
    public void recalculateNodesRange() {
        ArrayList<SubRed> aux = new ArrayList<>();
        for(SubRed subRed:listNodos){
            try {
                aux.add(new SubRed(subRed.getNodesAmount(),subRed.getSubredDescriptcion()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        listNodos.clear();
        for(SubRed subRed:aux){
            try {
                this.addSubRed(subRed.getNodesAmount(),subRed.getSubredDescriptcion());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public ArrayList<SubRed> addSubRed(int numNodes, String description) throws Exception {

        this.currentNodesCount = getCurrentNodesCount(listNodos);
        SubRed tempSubred =new  SubRed(numNodes,description);
        if(tempSubred.getSubredSize()+currentNodesCount >totalPossibleNodes){
            throw new Exception("The nodes count exceeded the possible nodes amount," +
                    "change the current mask or remove some subreds.",new Throwable("Nodes Amount exceeded"));
            /*return null;*/
        }else{

            ArrayList<SubRed> tempListNodosToCalculateSizesAndOrder = new ArrayList<>();
            tempListNodosToCalculateSizesAndOrder.add(tempSubred);
            tempListNodosToCalculateSizesAndOrder.addAll(this.listNodos);
            int sizeListt =  this.listNodos.size();
            this.listNodos.clear();

            Collections.sort(tempListNodosToCalculateSizesAndOrder, new Comparator<SubRed>() {
                @Override
                public int compare(SubRed subRed1, SubRed subRed2) {
                    int beforeAfter = subRed2.getSubredSize()-subRed1.getSubredSize();
                    if(beforeAfter == 0){/*Same subred size, so will check the amount of nodes given by the user*/
                        beforeAfter = subRed2.getNodesAmount()-subRed1.getNodesAmount();
                    }
                    return beforeAfter;
                }
            });

            recalculateNodesRange();
            for(SubRed subRedTemp : tempListNodosToCalculateSizesAndOrder){
                final SubRed subRedToInsert = new SubRed(
                        subRedTemp.getNodesAmount(),
                        subRedTemp.getSubredDescriptcion(),
                                /*empty list, then the "next" node start inn the ip project,
                                no emprt, then the next node start at the end of the last node*/
                        this.listNodos.size() == 0 ?
                                this.ipProject
                                /*: subRedTemp.getSubredEndIP()*/
                                : this.listNodos.get(this.listNodos.size()-1).getSubredEndIP().plusNodes(1)
                );
                this.listNodos.add(subRedToInsert);
            }



            return listNodos;

        }




    }

    public String getProjectName() {
        return projectName;
    }

    public IP getIpProject() {
        return ipProject;
    }

    public int getMask() {
        return mask;
    }

    public ArrayList<SubRed> getListNodos() {
        return listNodos;
    }



    public String getDateTimeCreation() {
        return dateTimeCreation;
    }

    public int getTotalPossibleNodes() {
        return totalPossibleNodes;
    }

    public int getCurrentNodesCount() {
        return currentNodesCount;
    }


}
