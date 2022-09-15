package training.supportbank;

import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.*;

public class Main {

    public static void main(String[] args) {

        String csv = "/Users/tbart/Training/SupportBank-Resources/Transactions2014.csv";

        ArrayList<Account> listOfAccounts = accountsCSV(csv);

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
        }

        transactionCSV(choice, csv);


    }


    public static ArrayList<Account> accountsCSV(String path) {
        String line;
        String splitBy = ",";


        ArrayList<Account> accounts = new ArrayList<>();

        ArrayList<String> nameStr = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));

            while ((line = reader.readLine()) != null) {

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

                BigDecimal cash = new BigDecimal(transaction[4]);

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

        return false;
    }

    public static void transactionCSV(String name, String csv) {
        String line;
        String splitBy = ",";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(csv));

            while ((line = reader.readLine()) != null) {

                String[] transaction = line.split(splitBy);

                if (transaction[0].equals("Date")) {
                    continue;
                }

                if (transaction[1].equals(name) || transaction[2].equals(name)) {
                    System.out.println("Date: " + transaction[0]
                            + ",         From: " + transaction[1]
                            + ",         To: " + transaction[2]
                            + ",         Amount: £" + transaction[4]
                            + ",         Narrative: " + transaction[3]
                            );
                }

            }
        }
            catch(IOException e){
                e.printStackTrace();

            }


        }
    }