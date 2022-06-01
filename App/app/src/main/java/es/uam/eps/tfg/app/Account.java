package es.uam.eps.tfg.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;


@Entity(tableName = "account_table")
public class Account {
    @PrimaryKey
    @ColumnInfo(name = "account_id")
    private long id;
    @ColumnInfo(name = "account_name")
    private String name;
    @ColumnInfo(name = "account_amount")
    private double amount;

    public Account(String name, double amount) {
        // Create new random ID
        long newId = new Random().nextLong() % (1000000 - 1) + 1;
        if(newId < 0) {
            newId *= (-1);
        }

        this.id = newId;
        this.name = name;
        this.amount = amount;
    }


    // Getters and setters
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public double getAmount() { return this.amount; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAmount(double amount) { this.amount = amount; }

    // Format name
    public static String formatAccountName(String name) {
        String clean = name.replaceAll("[^A-Za-z0-9\\s]","");
        String trimmed = clean.trim();
        String first = trimmed.substring(0,1).toUpperCase();
        String remaining = trimmed.substring(1).toLowerCase();

        return first + remaining;
    }
}
