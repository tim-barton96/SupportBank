package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public static ArrayList<Account> accountsJSON(String json) {

        ArrayList<Account> accounts = new ArrayList<>();

        accounts.add(new Account("tim"));

//        GsonBuilder gsonBuilder = new GsonBuilder();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        gsonBuilder.registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, jsonDeserializationContext) ->
//                LocalDate.parse(jsonElement, formatter)
//        );
//        Gson gson = gsonBuilder.create();
        return accounts;
    }
}
