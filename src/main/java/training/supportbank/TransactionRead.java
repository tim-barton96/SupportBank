package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TransactionRead {

    private static final Logger LOGGER = LogManager.getLogger();
    public static void readCSV(String name, String csv) {
        String line;
        String splitBy = ",";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csv));

            int counter = 0;

            while ((line = reader.readLine()) != null) {
                counter ++;

                String[] transaction = line.split(splitBy);

                if (transaction[0].equals("Date")) {
                    continue;
                }



                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


                if (transaction[1].equals(name) || transaction[2].equals(name)) {
                    System.out.println("Date: " + transaction[0]
                            + ",         From: " + transaction[1]
                            + ",         To: " + transaction[2]
                            + ",         Amount: £" + transaction[4]
                            + ",         Narrative: " + transaction[3]
                    );


                    try {
                        LocalDate.parse(transaction[0], formatter);
                    } catch (DateTimeParseException e) {
                        String warning = "Incorrect date value on line " + counter;
                        LOGGER.error(warning);
                    }

                }
            }
        }
        catch(IOException e){
            e.printStackTrace();

        }
    }


    public static void readJson(String name, String json){


        Gson gson = new Gson();

        JsonObject[] array;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(json));

            array = gson.fromJson(reader, JsonObject[].class);

            int errorCounter = 0;

            for (JsonObject object: array) {

                errorCounter++;

                String from = object.get("fromAccount").toString().replace("\"", "");
                String to = object.get("toAccount").toString().replace("\"", "");

                if (from.equals(name) || to.equals(name)) {
                    System.out.println("Date: " + object.get("date").toString().replace("\"", "")
                            + ",         From: " + from
                            + ",         To: " + to
                            + ",         Amount: £" + object.get("amount").toString().replace("\"", "")
                            + ",         Narrative: " + object.get("narrative").toString().replace("\"", ""));
                }

            }

        } catch (FileNotFoundException e) {
            LOGGER.fatal("File not found");
        }
    }


    public static void readXML(String name, String xml) {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(xml));

            NodeList listOfTransactions = document.getElementsByTagName("SupportTransaction");

            for (int i = 0; i < listOfTransactions.getLength(); i++) {

                Node node = listOfTransactions.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {



                    Element element = (Element) node;

                    long EPOCHDAYCORRECTION = (365 * 70) + Math.floorDiv(70, 4);
                    // Excel's epoch day is 1/1/1900 whereas LocalDate's epoch day is 1/1/1970 - this variable is just days between two dates
                    long dateLong = Long.parseLong(element.getAttribute("Date")) - EPOCHDAYCORRECTION;
                    LocalDate date = LocalDate.ofEpochDay(dateLong);
                    String amount = element.getElementsByTagName("Value").item(0).getTextContent();
                    String from = element.getElementsByTagName("From").item(0).getTextContent();
                    String to = element.getElementsByTagName("To").item(0).getTextContent();
                    String narrative = element.getElementsByTagName("Description").item(0).getTextContent();

                    if (!name.equals(from) || name.equals(to)) {
                        continue;
                    }

                    System.out.println(
                            "Date: " + date +
                            " From: " + from +
                            " To: " + to +
                            " Amount: " + amount +
                            " Narrative: " + narrative
                    );



                }
            }
        } catch(ParserConfigurationException | SAXException | IOException e){
            LOGGER.fatal("Invalid XML file");
        }
    }
}
