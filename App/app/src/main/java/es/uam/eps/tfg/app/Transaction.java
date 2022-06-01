package es.uam.eps.tfg.app;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomWarnings;

import java.util.Date;
import java.util.Random;


@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "transaction_table")
public class Transaction {
    @PrimaryKey
    @ColumnInfo(name = "transaction_id")
    private long id;
    @ColumnInfo(name = "transaction_date")
    private Date date;
    @ColumnInfo(name = "transaction_amount")
    private double amount;
    @Embedded
    private Account account;
    @ColumnInfo(name = "transaction_type")
    private CategoryType type;
    @Embedded
    private Category category;
    @ColumnInfo(name = "transaction_note")
    private String note;


    public Transaction(double amount, Account account, CategoryType type, Category category, String note) {
        // Check the provided type and the type of the provided category are the same
        if(type != category.getType()) {
            throw new IllegalArgumentException("Category and type do not match");
        }
        // Create new random ID
        long newId = new Random().nextLong() % (1000000 - 1) + 1;
        if(newId < 0) {
            newId *= (-1);
        }

        this.id = newId;
        this.date = new Date();
        this.amount = amount;
        this.account = account;
        this.type = type;
        this.category = category;
        this.note = note;
    }


    // Getters and setters
    public long getId() { return this.id; }
    public Date getDate() { return this.date; }
    public double getAmount() { return this.amount; }
    public Account getAccount() { return this.account; }
    public CategoryType getType() { return this.type; }
    public Category getCategory() { return this.category; }
    public String getNote() { return this.note; }

    public void setId(long id) { this.id = id; }
    public void setDate(Date date) { this.date = date; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setAccount(Account account) { this.account = account; }
    public void setType(CategoryType type) { this.type = type; }
    public void setCategory(Category category) { this.category = category; }
    public void setNote(String note) { this.note = note; }
}
