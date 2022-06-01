package es.uam.eps.tfg.app;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;

public class App extends Application {

    private static App app = null;
    private final Executor executor = Executors.newSingleThreadExecutor();

    // Starting default accounts
    private static final Account acc1 = new Account("Bank", 3000.0);
    private static final Account acc2 = new Account("Cash", 250.5);
    private static final Account acc3 = new Account("Savings", 1078.5);
    private static final List<Account> accounts = new ArrayList<>();

    // Starting default categories
    private static final Category c1 = new Category("Restaurants", CategoryType.EXPENSE);
    private static final Category c2 = new Category("Household", CategoryType.EXPENSE);
    private static final Category c3 = new Category("Car", CategoryType.EXPENSE);
    private static final Category c4 = new Category("Shopping", CategoryType.EXPENSE);
    private static final Category c5 = new Category("Travel", CategoryType.EXPENSE);
    private static final Category c6 = new Category("Work", CategoryType.INCOME);
    private static final Category c7 = new Category("Gifts", CategoryType.INCOME);
    private static final List<Category> categories = new ArrayList<>();


    public App() {
        accounts.add(acc1);
        accounts.add(acc2);
        accounts.add(acc3);

        categories.add(c1);
        categories.add(c2);
        categories.add(c3);
        categories.add(c4);
        categories.add(c5);
        categories.add(c6);
        categories.add(c7);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        final AppDatabase appDatabase = AppDatabase.getInstance(this);
        AppDAO appDAO = appDatabase.getAppDAO();

        // Set default starting accounts and categories
        executor.execute(() -> populateDB(appDAO));
    }

    public static Context getContext() {
        return app.getApplicationContext();
    }

    private void populateDB(AppDAO appDAO) {
        List<Account> accountsDB = appDAO.getAccounts();
        List<Category> categoriesDB = appDAO.getCategories();

        for(Account a : accounts) {
            boolean repeated = false;
            for(Account aDB : accountsDB) {
                if(a.getName().equals(aDB.getName())) {
                    repeated = true;
                    break;
                }
            }
            if(!repeated) {
                appDAO.addAccount(a);
            }
        }

        for(Category c : categories) {
            boolean repeated = false;
            for(Category cDB : categoriesDB) {
                if(c.getName().equals(cDB.getName()) && c.getType() == cDB.getType()) {
                    repeated = true;
                    break;
                }
            }
            if(!repeated) {
                appDAO.addCategory(c);
            }
        }
    }
}
