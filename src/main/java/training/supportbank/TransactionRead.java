package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
}
