/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.ActivosList;
import Model.ActivosModel;
import Model.XMLData;
import View.ActivosView;
import java.awt.TextField;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author james
 */
public class ActivosController {
    static ActivosView view;
    private XMLData data;
    ActivosList activosList;
    String filePath = "Activos.xml";

    public ActivosController() {
        this.view = new ActivosView();
        this.view.setView(this);
        this.activosList = new ActivosList();
    }
    
    public void startAplication(){
        view.setLocationRelativeTo(null);
        view.setVisible(true);
        data = new XMLData();
        data.createXML();
        List<String> options = XMLData.updateComboBox(filePath);
        view.getCmbCategory().removeAllItems();
        for (String option : options) {
            view.getCmbCategory().addItem(option);
        }
        view.getBtnSave().addActionListener(e->save());
        view.getBtnClean().addActionListener(e->clean());
        view.getBtnSearch().addActionListener(e->search());
        view.getTxtAge().setEnabled(false);
        view.getTxtDepretiation().setEnabled(false);
        view.getTxtNewValue().setEnabled(false);
        
    }
    public static void showMessage(JFrame parent, String message, String info) {
        if (info == "error") {
            JOptionPane.showMessageDialog(parent, message, "Validación", JOptionPane.ERROR_MESSAGE);
        } else if (info == "success") {
            JOptionPane.showMessageDialog(parent, message, "Validación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void save() {
        // Año actual
        Calendar calendario = Calendar.getInstance();
        int actualYear = calendario.get(Calendar.YEAR);
        Integer validationFabrication = validateTextField(view.getTxtFabrication().getText());
        Integer validationValue = validateTextField(view.getTxtValue().getText());
        if (validationFabrication == null) {
            showMessage(view, "Complete la fabricación del activo únicamente con números", "error");//Valida que el año de fabricación sea solo números
        } else if (validationValue == null) {
            showMessage(view, "Complete el valor del activo únicamente con números", "error"); //Valida que el valor sea solo números
        } else if (view.getTxtActive().getText().trim().isEmpty() || view.getTxtCode().getText().trim().isEmpty()
                || view.getTxtFabrication().getText().trim().isEmpty() || view.getTxtValue().getText().trim().isEmpty()) { //Valida espacios en blanco
            showMessage(view, "Error: Debe llenar todos los campos de texto.", "error");
        } else if (Integer.parseInt(view.getTxtFabrication().getText()) > actualYear) { //Valida año correcto
            showMessage(view, "Error: Debe ingresar un año de fabricación válido.", "error");
        } else {
            String category = (String) view.getCmbCategory().getSelectedItem();
            int fabricationYear = Integer.parseInt(view.getTxtFabrication().getText());
            int originalValue = Integer.parseInt(view.getTxtValue().getText());

            int age = actualYear - fabricationYear; // Años de antigüedad del activo
            int yearsOfCategory = Integer.parseInt(category.replaceAll("\\D+", "")); // Extraer el número de años de la categoría

            // Calcular la depreciación
            long cantOfYears = originalValue / yearsOfCategory;

            long depreciation = cantOfYears * age;

            // Calcular el valor actual
            long newValue = originalValue - depreciation;

            ActivosModel newActivos = new ActivosModel(view.getTxtCode().getText(),
                    view.getTxtActive().getText(),
                    Integer.parseInt(view.getTxtFabrication().getText()),
                    originalValue, age, category,
                    depreciation,
                    newValue);

            activosList.getList().add(newActivos);
            XMLData.save(filePath, category, activosList.getList());
            activosList.getList().clear();
        }
    }

    public static void clean() {
        view.getTxtCode().setText("");
        view.getTxtActive().setText("");
        view.getTxtFabrication().setText("");
        view.getTxtValue().setText("");
        view.getTxtAge().setText("");
        view.getTxtDepretiation().setText("");
        view.getTxtNewValue().setText("");
        view.getTxtCode().setEnabled(true);
    }
    
    private void search() {
        
        String code = view.getTxtCode().getText();
        String category = (String) view.getCmbCategory().getSelectedItem();
        String categoryName = category.split("\\s+\\(")[0];
        if(code.trim().isEmpty()){
            showMessage(view, "Ingrese el código del activo que desea buscar", "error");
        }else{
            List<String> activoData = XMLData.search(filePath, categoryName, code);   
            if(activoData !=null){
                view.getTxtCode().setEnabled(false);
                String active = activoData.get(0);
                String fabrication = activoData.get(1);
                String value = activoData.get(2);
                String age = activoData.get(3);
                String depretiation = activoData.get(4);
                String newValue = activoData.get(5);
                
                
                view.getTxtActive().setText(active);
                view.getTxtFabrication().setText(fabrication);
                view.getTxtValue().setText(value);
                view.getTxtAge().setText(age);
                view.getTxtDepretiation().setText(depretiation);
                view.getTxtNewValue().setText(newValue);
            }else{
                showMessage(view, "Activo no encontrado con ese código", "error");
            }
        }
       
    }
    
    public static Integer validateTextField(String text) {
    

    // Utiliza una expresión regular para verificar si el texto contiene solo números
    Pattern pattern = Pattern.compile("^[0-9]+$");
    Matcher matcher = pattern.matcher(text);

    if (matcher.matches()) {
        // El texto contiene solo números, lo convertimos a un entero y lo retornamos
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // Si el texto no se puede convertir a un entero, retorna null
            return null;
        }
    } else {
        // El texto contiene caracteres que no son números, retorna null
        return null;
    }
}
}
