package es.uam.eps.tfg.app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Random;


@Entity(tableName = "category_table")
public class Category {
    @PrimaryKey
    @ColumnInfo(name = "category_id")
    private long id;
    @ColumnInfo(name = "category_name")
    private String name;
    @ColumnInfo(name = "category_type")
    private CategoryType type;


    public Category(String name, CategoryType type) {
        // Create new random ID
        long newId = new Random().nextLong() % (1000000 - 1) + 1;
        if(newId < 0) {
            newId *= (-1);
        }

        this.id = newId;
        this.name = name;
        this.type = type;
    }


    // Getters and setters
    public long getId() { return this.id; }
    public String getName() { return this.name; }
    public CategoryType getType() { return this.type; }

    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(CategoryType type) { this.type = type; }

    // Format name
    public static String formatCategoryName(String name) {
        String clean = name.replaceAll("[^A-Za-z0-9\\s]","");
        String trimmed = clean.trim();
        String first = trimmed.substring(0,1).toUpperCase();
        String remaining = trimmed.substring(1).toLowerCase();

        return first + remaining;
    }
}
