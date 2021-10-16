package main;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

//Class that represents the Transaction.

@Getter
@Setter
public class Transaction {

    private String merchant;
    private AtomicInteger amount;
    private Date time;

    public Transaction(String merchant, AtomicInteger amount, Date time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }
}
