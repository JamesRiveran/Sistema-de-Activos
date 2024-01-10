/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.ActivosController;
import static Controller.ActivosController.showMessage;
import View.ActivosView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author james
 */
public class XMLData {
     private static final String NOMBRE_ARCHIVO = "Activos.xml";
    static ActivosView view;
    ActivosController activosController;
    public static void createXML() {
        try {

            List<String[]> categorias = new ArrayList<>();
            categorias.add(new String[]{"CAS", "Casa", "58"});
            categorias.add(new String[]{"COM", "Computadora", "5"});
            categorias.add(new String[]{"VEH", "Vehiculo", "20"});
            // Verificar si el archivo ya existe
            if (archivoExiste()) {
                System.out.println("El archivo '" + NOMBRE_ARCHIVO + "' ya existe. No se sobrescribirá.");
                return;
            }

            // Crear un objeto DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Crear un objeto DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Crear un documento XML
            Document document = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);

            // Crear el elemento "categorias" y agregarlo como hijo de "data"
            Element categoriasElement = document.createElement("categorias");
            rootElement.appendChild(categoriasElement);

            // Agregar cada categoría a la lista
            for (String[] categoria : categorias) {
                Element categoriaElement = document.createElement("categoria");

                Element codigoElement = document.createElement("Codigo");
                codigoElement.appendChild(document.createTextNode(categoria[0]));
                categoriaElement.appendChild(codigoElement);

                Element nombreElement = document.createElement("Nombre");
                nombreElement.appendChild(document.createTextNode(categoria[1]));
                categoriaElement.appendChild(nombreElement);

                Element vidaElement = document.createElement("Vida");
                vidaElement.appendChild(document.createTextNode(categoria[2]));
                categoriaElement.appendChild(vidaElement);

                categoriasElement.appendChild(categoriaElement);
            }

            // Crear un objeto TransformerFactory
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // Crear un objeto Transformer
            Transformer transformer = transformerFactory.newTransformer();

            // Especificar la codificación y la indentación
            transformer.setOutputProperty("encoding", "UTF-8");
            transformer.setOutputProperty("indent", "yes");

            // Crear una fuente DOM para la transformación
            DOMSource source = new DOMSource(document);

            // Crear un resultado de transmisión para escribir en un archivo XML
            StreamResult result = new StreamResult(new File(NOMBRE_ARCHIVO));

            // Realizar la transformación y escribir el documento en el archivo XML
            transformer.transform(source, result);

            System.out.println("Archivo XML '" + NOMBRE_ARCHIVO + "' creado con éxito.");
        } catch (ParserConfigurationException | TransformerException | DOMException  e) {
            e.printStackTrace();
        }
    }

    private static boolean archivoExiste() {
        File archivo = new File(NOMBRE_ARCHIVO);
        return archivo.exists();
    }
    
    public static List<String> updateComboBox(String filePath) {
        List<String> opciones = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));

            NodeList categoriaNodes = document.getElementsByTagName("categoria");
            for (int i = 0; i < categoriaNodes.getLength(); i++) {
                Element categoriaElement = (Element) categoriaNodes.item(i);
                String nombre = categoriaElement.getElementsByTagName("Nombre").item(0).getTextContent();
                String vida = categoriaElement.getElementsByTagName("Vida").item(0).getTextContent();
                String opcion = nombre + " (" + vida + " años)";
                opciones.add(opcion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return opciones;
    }
public static void update (String filePath, String categoriaSeleccionada, List<ActivosModel> instrumentList){
     // Verificar si la lista de instrumentos es válida
    if (instrumentList == null || instrumentList.isEmpty()) {
        throw new IllegalArgumentException("La lista de instrumentos no puede ser nula ni estar vacía");
    }

    try {
        Document doc;

        // Verificar si el archivo ya existe
        File file = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(file);

        Element instruments = doc.getDocumentElement();
        // Agregar o actualizar los nuevos instrumentos
        for (ActivosModel instrument : instrumentList) {
            // Buscar la categoría existente por el nombre
            Element categoriaElement = findCategoryByName(instruments, categoriaSeleccionada);
            Element activoElement = findActivoByCode(categoriaElement, instrument.getCode());
            
            if (activoElement != null) {
                //El activo ya existe, actualizar los datos excepto el código
                Element nombre = (Element) activoElement.getElementsByTagName("Activo").item(0);
                Element fabricacion = (Element) activoElement.getElementsByTagName("Fabricacion").item(0);
                Element valor = (Element) activoElement.getElementsByTagName("Valor").item(0);
                Element edad = (Element) activoElement.getElementsByTagName("Edad").item(0);
                Element depreciacion = (Element) activoElement.getElementsByTagName("Depreciacion").item(0);
                Element valorActual = (Element) activoElement.getElementsByTagName("ValorActual").item(0);

                nombre.setTextContent(instrument.getActive());
                fabricacion.setTextContent(String.valueOf(instrument.getFabrication()));
                valor.setTextContent(String.valueOf(instrument.getValue()));
                edad.setTextContent(String.valueOf(instrument.getAge()));
                depreciacion.setTextContent(String.valueOf(instrument.getDepreciation()));
                valorActual.setTextContent(String.valueOf(instrument.getNewValue()));
                showMessage(view, "Activo actualizado en la categoría '" + categoriaSeleccionada + "' en el archivo XML.", "success");
            }

//             Guardar el documento actualizado en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            
            ActivosController.clean();
        }
    } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
        e.printStackTrace();
    }
}
    public static void save(String filePath, String categoriaSeleccionada, List<ActivosModel> instrumentList) {
        // Verificar si la lista de instrumentos es válida
        if (instrumentList == null || instrumentList.isEmpty()) {
            throw new IllegalArgumentException("La lista de instrumentos no puede ser nula ni estar vacía");
        }

        try {
            Document doc;

            // Verificar si el archivo ya existe
            File file = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(file);

            Element instruments = doc.getDocumentElement();
            // Agregar o actualizar los nuevos instrumentos
            for (ActivosModel instrument : instrumentList) {
                // Buscar la categoría existente por el nombre
                Element categoriaElement = findCategoryByName(instruments, categoriaSeleccionada);
                Element activoElement = findActivoByCode(categoriaElement, instrument.getCode());

                if (activoElement == null) {
                    // El activo no existe, crear un nuevo elemento "activo" y agregar los datos
                    activoElement = doc.createElement("Activo");

                    Element codigo = doc.createElement("Codigo");
                    codigo.setTextContent(instrument.getCode());
                    Element nombre = doc.createElement("Activo");
                    nombre.setTextContent(instrument.getActive());
                    Element fabricacion = doc.createElement("Fabricacion");
                    fabricacion.setTextContent(String.valueOf(instrument.getFabrication()));
                    Element valor = doc.createElement("Valor");
                    valor.setTextContent(String.valueOf(instrument.getValue()));
                    Element edad = doc.createElement("Edad");
                    edad.setTextContent(String.valueOf(instrument.getAge()));
                    Element depreciacion = doc.createElement("Depreciacion");
                    depreciacion.setTextContent(String.valueOf(instrument.getDepreciation()));
                    Element valorActual = doc.createElement("ValorActual");
                    valorActual.setTextContent(String.valueOf(instrument.getNewValue()));

                    activoElement.appendChild(codigo);
                    activoElement.appendChild(nombre);
                    activoElement.appendChild(fabricacion);
                    activoElement.appendChild(valor);
                    activoElement.appendChild(edad);
                    activoElement.appendChild(depreciacion);
                    activoElement.appendChild(valorActual);

                    categoriaElement.appendChild(activoElement);
                    // Guardar el documento actualizado en el archivo
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = null;
                    try {
                        transformer = transformerFactory.newTransformer();
                    } catch (TransformerConfigurationException ex) {
                        Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(filePath));
                    try {
                        transformer.transform(source, result);
                    } catch (TransformerException ex) {
                        Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ActivosController.clean();
                    showMessage(view, "Activo añadido en la categoría '" + categoriaSeleccionada + "' en el archivo XML.", "success");
                } else {
                    showMessage(view, "Activo existente, consulte primero para modificar", "error");
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }


    private static Element findCategoryByName(Element instruments, String categoryName) {
        NodeList categoriaNodes = instruments.getElementsByTagName("categoria");
        for (int i = 0; i < categoriaNodes.getLength(); i++) {
            Element categoriaElement = (Element) categoriaNodes.item(i);
            Element nombreElement = (Element) categoriaElement.getElementsByTagName("Nombre").item(0);
            String nombre = nombreElement.getTextContent();

            // Extraer el nombre sin la información adicional como "(58 años)"
            String nombreSinInfoAdicional = categoryName.split("\\s+\\(")[0];

            if (nombreSinInfoAdicional.equals(nombre)) {
                return categoriaElement;
            }
        }
        return null;
    }

   public static List<String> search(String filePath, String categoryName, String code) {
    List<String> activoData = new ArrayList<>();
    
    // Cargar el archivo XML
    File archivo = new File(filePath);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = null;
    try {
        dBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
        Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null, ex);
    }
    Document doc = null;
    try {
        doc = dBuilder.parse(archivo);
    } catch (SAXException ex) {
        Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(XMLData.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Obtener la raíz del documento
    Element activos = doc.getDocumentElement();
    NodeList categoriaNodes = activos.getElementsByTagName("categoria");

    for (int i = 0; i < categoriaNodes.getLength(); i++) {
        Element categoriaElement = (Element) categoriaNodes.item(i);
        String categoriaNombre = getElementTextContent(categoriaElement, "Nombre");

        // Verificar si la categoría coincide con la proporcionada
        if (categoriaNombre.equals(categoryName)) {
            NodeList instrumentElements = categoriaElement.getElementsByTagName("Activo");

            for (int j = 0; j < instrumentElements.getLength(); j++) {
                Element typeInstrument = (Element) instrumentElements.item(j);
                Element codeElement = (Element) typeInstrument.getElementsByTagName("Codigo").item(0);

                if (codeElement != null && codeElement.getTextContent().equals(code)) {
                    // Si se encuentra el código, extraer los datos del activo
                    Element activeElement = (Element) typeInstrument.getElementsByTagName("Activo").item(0);
                    Element fabricationElement = (Element) typeInstrument.getElementsByTagName("Fabricacion").item(0);
                    Element valueElement = (Element) typeInstrument.getElementsByTagName("Valor").item(0);
                    Element ageElement = (Element) typeInstrument.getElementsByTagName("Edad").item(0);
                    Element depreciationElement = (Element) typeInstrument.getElementsByTagName("Depreciacion").item(0);
                    Element newValueElement = (Element) typeInstrument.getElementsByTagName("ValorActual").item(0);

                    String active = activeElement.getTextContent();
                    String fabrication = fabricationElement.getTextContent();
                    String value = valueElement.getTextContent();
                    String age = ageElement.getTextContent();
                    String depreciation = depreciationElement.getTextContent();
                    String newValue = newValueElement.getTextContent();

                    activoData.add(active);
                    activoData.add(fabrication);
                    activoData.add(value);
                    activoData.add(age);
                    activoData.add(depreciation);
                    activoData.add(newValue);

                    return activoData;
                }
            }
        }
    }

    return null; // Devuelve una lista vacía si no se encuentra el código en la categoría especificada
}

    private static String getElementTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        } else {
            return "";
        }
    }

    private static Element findActivoByCode(Element categoriaElement, String code) {
        NodeList activoElements = categoriaElement.getElementsByTagName("Activo");

        for (int i = 0; i < activoElements.getLength(); i++) {
            Element activoElement = (Element) activoElements.item(i);
            Element codigoElement = (Element) activoElement.getElementsByTagName("Codigo").item(0);

            if (codigoElement != null && codigoElement.getTextContent().equals(code)) {
                return activoElement; // Devuelve el elemento del activo si se encuentra
            }
        }

        return null; // Devuelve null si el activo no se encuentra en la categoría
    }





}
