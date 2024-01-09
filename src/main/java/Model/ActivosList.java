/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.ArrayList;

/**
 *
 * @author james
 */
public class ActivosList {
     private ArrayList<ActivosModel> list;

    public ActivosList(ArrayList<ActivosModel> list) {
        this.list = list;
    }
    
    public ActivosList() {
        this.list = new ArrayList<ActivosModel> ();
    }
    
     

    public ArrayList<ActivosModel> getList() {
        return list;
    }

    public void setList(ArrayList<ActivosModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        String datos=" ";
        for (int i = 0; i < list.size(); i++) {
            datos+=list.get(i).toString()+"\n";
        }
        return "Activos" + list ;
    }
    
    
     
     
}
