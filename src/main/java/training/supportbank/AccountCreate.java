package training.supportbank;

import com.google.gson.*;
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
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class AccountCreate {


    private static final Logger LOGGER = LogManager.getLogger();
    public static ArrayList<Account> accountsCSV(String path) {
        String line;
        String splitBy = ",";


        ArrayList<Account> accounts = new ArrayList<>();

        ArrayList<String> nameStr = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            int counter = 0;

            while ((line = reader.readLine()) != null) {

                counter++;

                String[] transaction = line.split(splitBy);

                if (transaction[0].equals("Date")) {
                    continue;
                }

                if (!nameStr.contains(transaction[1])) {
                    accounts.add(new Account(transaction[1]));
                    nameStr.add(transaction[1]);
                }

                if (!nameStr.contains(transaction[2])) {
                    accounts.add(new Account(transaction[2]));
                    nameStr.add(transaction[2]);
                }

                BigDecimal cash;

                try {
                    cash = new BigDecimal(transaction[4]);
                } catch (NumberFormatException nfe) {
                    String error = "Incorrect value on line " + counter;
                    LOGGER.error(error);
                    continue;
                }

                accounts.stream()
                        .filter(x -> x.getName().equals(transaction[1]) )
                        .forEach(x -> x.addBalance(cash));

                accounts.stream()
                        .filter(x -> x.getName().equals(transaction[2]))
                        .forEach(x -> x.subtractBalance(cash));

            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return accounts;
    }

    public static ArrayList<Account> accountsJSON(String json)  {

        ArrayList<Account> accounts = new ArrayList<>();

        ArrayList<String> nameStr = new ArrayList<>();

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

                if (!nameStr.contains(from)) {
                    accounts.add(new Account(from));
                    nameStr.add(from);
                }

                if (!nameStr.contains(to)){
                    accounts.add(new Account(to));
                    nameStr.add(to);
                }
                BigDecimal cash;
                try {
                    cash = new BigDecimal(object.get("amount").toString());
                } catch (NumberFormatException nfe) {
                    String error = "Incorrect value on line " + errorCounter;
                    LOGGER.error(error);
                    continue;
                }

                accounts.stream()
                        .filter(x -> x.getName().equals(from) )
                        .forEach(x -> x.addBalance(cash));

                accounts.stream()
                        .filter(x -> x.getName().equals(to))
                        .forEach(x -> x.subtractBalance(cash));
            }

        } catch (FileNotFoundException e) {
            LOGGER.fatal("File not found");
        }



        return accounts;
    }


    public static ArrayList<Account> accountsXML(String xml) {

        ArrayList<Account> accounts = new ArrayList<>();

        ArrayList<String> nameStr = new ArrayList<>();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(new File(xml));

            NodeList listOfTransactions = document.getElementsByTagName("SupportTransaction");

            for (int i = 0; i < listOfTransactions.getLength(); i++) {

                Node node = listOfTransactions.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    String amount = element.getElementsByTagName("Value").item(0).getTextContent();
                    String from = element.getElementsByTagName("From").item(0).getTextContent();
                    String to = element.getElementsByTagName("To").item(0).getTextContent();

                    if (!nameStr.contains(from)) {
                        accounts.add(new Account(from));
                        nameStr.add(from);
                    }

                    if (!nameStr.contains(to)) {
                        accounts.add(new Account(to));
                        nameStr.add(to);
                    }

                    BigDecimal cash;

                    try {
                        cash = new BigDecimal(amount);
                    } catch (NumberFormatException nfe) {
                        String error = "Incorrect value";
                        LOGGER.error(error);
                        continue;
                    }

                    accounts.stream()
                            .filter(x -> x.getName().equals(from))
                            .forEach(x -> x.addBalance(cash));

                    accounts.stream()
                            .filter(x -> x.getName().equals(to))
                            .forEach(x -> x.subtractBalance(cash));


                }
                }
            } catch(ParserConfigurationException | SAXException | IOException e){
            LOGGER.fatal("Invalid XML file");
            }
            return accounts;
        }
    }