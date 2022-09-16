package training.supportbank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();


    public static void main(String[] args) {


        String file = args[0];

        String filetype = file.split("\\.")[1];

        ArrayList<Account> listOfAccounts;

        switch (filetype) {
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

            switch (filetype) {
                case "csv" :
                    TransactionRead.readCSV(choice, file);
                    break;
                case "json" :
                    TransactionRead.readJson(choice, file);
                    break;
                default:
                    LOGGER.fatal("Incorrect filetype");
            }
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