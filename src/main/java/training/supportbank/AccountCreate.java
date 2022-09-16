package training.supportbank;

import com.google.gson.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
}
