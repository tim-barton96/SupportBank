package training.supportbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();


    public static void main(String[] args) {


        String csv = "/Users/tbart/Training/SupportBank-Resources/DodgyTransactions2015.csv";

        ArrayList<Account> listOfAccounts;

        switch (csv.split("\\.")[1]) {
            case "csv":
                listOfAccounts = accountsCSV(csv);
                break;

            default:
                LOGGER.fatal("Incorrect filetype");
                return;

        }


        Scanner scanner = new Scanner(System.in);

        String choice;

        do {
            System.out.println("Please type 'All' or an account name.");
            choice = scanner.nextLine();
        } while (!checkInput(choice, listOfAccounts));

        if (choice.equals("All") || choice.equals("all")) {
            for (Account account : listOfAccounts) {
                String name = account.getName();
                BigDecimal balance = account.getBalance();
                System.out.println(name + " £" + balance);
            }
        } else {
            transactionCSV(choice, csv);
        }

    }


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

    public static boolean checkInput(String input, ArrayList<Account> list) {

        if (input.equals("All") || input.equals("all")) {
            return true;
        }

        for (Account account: list) {
            if (account.getName().equals(input)) {
                return true;
            }
        }

        LOGGER.error("Incorrect input");
        return false;
    }

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