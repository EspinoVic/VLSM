package com.example.vlsm.calculate;

import androidx.annotation.NonNull;

public class IP {
    private String ip;
    private int octetos[];

    public IP(String ip) {
        this.ip = ip;
        String[] split = ip.split("\\.");
        this.octetos = new int[4];
        for(int i = 0;i<4;i++){
            octetos[i] = Integer.parseInt(split[i]);
        }
    }

    public IP(int[] ip){
        this.octetos = ip.clone();
        this.ip = "";
        for(int i = 0;i<4;i++){
             this.ip += octetos[i]+"";
             if(i<3){
                 this.ip+=".";
             }
        }
    }
    public static IP calculateStartIP(IP ip,int mask){

        String maskStr = "";
        for(int i = 0;i<35;i++){

            if(i==8||i==17||i==26){
                maskStr = maskStr.concat(".");
                continue;
            }
            if(mask>23){
                maskStr = maskStr.concat( (i<mask+3?1:0)+"");
            }else
            if(mask>15){
                maskStr = maskStr.concat( (i<mask+2?1:0)+"");
            }else
            if(mask>7){
                maskStr = maskStr.concat( (i<mask+1?1:0)+"");
            }else
            if(mask<=7){
                maskStr = maskStr.concat( (i<mask?1:0)+"");
            }

        }
        String[] maskBinSplit = maskStr.split("\\.");

       /* int[] maskSplit = new int[4];*/
        int[] octetosIP = ip.getOctetos();

        int[] ipMasked = new int[4];

        for(int i = 0;i<4;i++){
            ipMasked[i] = octetosIP[i] & Integer.parseInt(maskBinSplit[i],2);
        }

        return  new IP(ipMasked);



    }

    public int[] getOctetos() {
        return octetos;
    }

    public String getIp() {
        return ip;
    }

    @NonNull
    @Override
    public IP clone()  {

        return new IP(this.ip);
    }

    @NonNull
    @Override
    public String toString() {
        return this.ip;
    }

    public IP plusNodes(int nodesAmount) {

        int[] cloneOctetos = octetos.clone();

        int residuo = 0;
        int cociente = 0;

        cloneOctetos[3] += nodesAmount;

        if(cloneOctetos[3]>255){
            cociente = cloneOctetos[3]/255;
            residuo = cloneOctetos[3]%255;

            cloneOctetos[3] = residuo;
            cloneOctetos[2]+= cociente;

            if(cloneOctetos[2]>255){
                cociente = cloneOctetos[2]/255;
                residuo = cloneOctetos[2]%255;

                cloneOctetos[2] = residuo;
                cloneOctetos[1]+= cociente;

                if(cloneOctetos[1]>255){
                    cociente = cloneOctetos[1]/255;
                    residuo = cloneOctetos[1]%255;

                    cloneOctetos[1] = residuo;
                    cloneOctetos[0]+= cociente;


                    if(cloneOctetos[0]>255){
                        /*throw new Exception("Cant calculate the operation.");*/
                        return null;
                    }
                }
            }
        }

        IP ipPlus = new IP(
                cloneOctetos[0] +"."
                        +cloneOctetos[1]+"."
                        +cloneOctetos[2]+"."
                        +cloneOctetos[3]
        );

        return ipPlus;

    }

}
