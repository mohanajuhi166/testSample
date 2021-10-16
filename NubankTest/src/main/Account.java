package main;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.CopyOnWriteArrayList;

//Class that represents the account.

@Getter
@Setter
@NoArgsConstructor
public class Account {

    @SerializedName(value = "activeCard", alternate = {"active-card"})
    private boolean activeCard;
    @SerializedName(value = "availableLimit", alternate = {"available-limit"})
    private int availableLimit;
    //The transient is included in order to avoid this attribute to persist in the JSON parse from GSON.
    private transient CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();

    public Account(boolean activeCard, int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
        transactions = new CopyOnWriteArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

}
