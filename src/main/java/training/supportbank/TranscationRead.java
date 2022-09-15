package training.supportbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TranscationRead {

    private static final Logger LOGGER = LogManager.getLogger();
    public static void transactionCSV(String name, String csv) {
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
}
