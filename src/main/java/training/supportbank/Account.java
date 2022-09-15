package training.supportbank;

import java.math.BigDecimal;

public class Account {

    private String name;
    private BigDecimal balance = BigDecimal.valueOf(0).setScale(2);

    public Account (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void addBalance(BigDecimal amount) {
        this.balance =  this.balance.add(amount);
    }

    public void subtractBalance(BigDecimal amount) {

        this.balance = this.balance.subtract(amount);
    }

}
