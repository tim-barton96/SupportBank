package training.supportbank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
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


        String file = args[0];

        ArrayList<Account> listOfAccounts;

        switch (file.split("\\.")[1]) {
            case "csv":
                listOfAccounts = AccountCreate.accountsCSV(file);
                break;
            case "json":
                listOfAccounts = AccountCreate.accountsJSON(file);
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
            TranscationRead.transactionCSV(choice, file);
        }

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

    }