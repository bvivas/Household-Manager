package es.uam.eps.tfg.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import es.uam.eps.tfg.app.Account;
import es.uam.eps.tfg.app.Category;
import es.uam.eps.tfg.app.CategoryTypeConverter;
import es.uam.eps.tfg.app.DateConverter;
import es.uam.eps.tfg.app.Transaction;

@Database(entities = {Transaction.class, Account.class, Category.class}, version = 2, exportSchema = false)
@TypeConverters({DateConverter.class, CategoryTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract AppDAO getAppDAO();

    private static volatile AppDatabase INSTANCE = null;

    public static AppDatabase getInstance(@NotNull Context context) {
        AppDatabase instance = AppDatabase.INSTANCE;

        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "app_database"
            )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

            AppDatabase.INSTANCE = instance;
        }
        return instance;
    }
}
