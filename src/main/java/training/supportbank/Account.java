package training.supportbank;

public class Account {

    private String name;
    private int balance = 0;

    public Account (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getBalance() {
        return this.balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void subtractBalance(int amount) {
        this.balance -= amount;
    }

}
