/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author james
 */
public class ActivosModel {
    private String code;
    private String active;
    private int fabrication;
    private int value;
    private int age;
    private String category;
    private long depreciation;
    private long newValue;

    public ActivosModel(String code, String active, int fabrication, int value, int age, String category, long depreciation, long newValue) {
        this.code = code;
        this.active = active;
        this.fabrication = fabrication;
        this.value = value;
        this.age = age;
        this.category = category;
        this.depreciation = depreciation;
        this.newValue = newValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public int getFabrication() {
        return fabrication;
    }

    public void setFabrication(int fabrication) {
        this.fabrication = fabrication;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(long depreciation) {
        this.depreciation = depreciation;
    }

    public long getNewValue() {
        return newValue;
    }

    public void setNewValue(long newValue) {
        this.newValue = newValue;
    }
    
    
    
    
}
