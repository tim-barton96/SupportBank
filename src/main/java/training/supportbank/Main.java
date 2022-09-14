package training.supportbank;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        String csv = "/Users/tbart/Training/SupportBank-Resources/Transactions2014.csv";

        ArrayList<Account> listOfAccounts = accountsCSV(csv);

        //ArrayList<Transcation> listOfTransactions =


        for (int i = 0; i < listOfAccounts.size(); i++) {
            String name = listOfAccounts.get(i).getName();
            System.out.println(name);
        }
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

                Float cash = Float.parseFloat(transaction[4]);


                System.out.println(cash + " " + (cash + 12));





                //accounts.stream()
                    //    .filter(x -> x.getName().equals(transaction[1]) )
                       // .forEach(x -> x.addBalance(Integer.parseInt(transaction[4])));


            }
        } catch (IOException e) {
            e.printStackTrace();

        }

        return accounts;
    }
}
