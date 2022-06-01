package es.uam.eps.tfg.app.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.uam.eps.tfg.app.Account;
import es.uam.eps.tfg.app.Category;
import es.uam.eps.tfg.app.CategoryType;
import es.uam.eps.tfg.app.Transaction;

@Dao
public interface AppDAO {

    @Query("SELECT * FROM transaction_table ORDER BY transaction_date DESC")
    List<Transaction> getTransactions();

    @Query("SELECT * FROM account_table ORDER BY account_name ASC")
    List<Account> getAccounts();

    @Query("SELECT * FROM category_table ORDER BY category_name ASC")
    List<Category> getCategories();

    @Query("SELECT * FROM category_table WHERE category_type = :category_type ORDER BY category_name ASC")
    List<Category> getCategoriesByType(CategoryType category_type);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTransaction(Transaction transaction);

    @Update
    void updateTransaction(Transaction transaction);

    @Delete
    void deleteTransaction(Transaction transaction);

    @Query("SELECT * FROM transaction_table WHERE transaction_id = :transaction_id LIMIT 1")
    Transaction getTransactionById(Long transaction_id);

    @Query("DELETE FROM transaction_table WHERE transaction_table.account_id = :account_id")
    void deleteTransactionsMatchingAccount(Long account_id);

    @Query("DELETE FROM transaction_table WHERE transaction_table.category_id = :category_id")
    void deleteTransactionsMatchingCategory(Long category_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccount(Account account);

    @Update
    void updateAccount(Account account);

    @Delete
    void deleteAccount(Account account);

    @Query("SELECT * FROM account_table WHERE account_id = :account_id LIMIT 1")
    Account getAccountById(Long account_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM category_table WHERE category_table.category_id = :category_id LIMIT 1")
    Category getCategoryById(Long category_id);
}
