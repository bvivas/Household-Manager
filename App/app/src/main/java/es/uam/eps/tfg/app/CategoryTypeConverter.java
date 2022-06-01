package es.uam.eps.tfg.app;

import androidx.room.TypeConverter;


public class CategoryTypeConverter {
    @TypeConverter
    public static String fromCategoryTypeToString(CategoryType type) {
        if (type == CategoryType.EXPENSE) {
            return App.getContext().getResources().getString(R.string.type_expense_text);
        } else if(type == CategoryType.INCOME) {
            return App.getContext().getResources().getString(R.string.type_income_text);
        }
        return null;
    }

    @TypeConverter
    public static CategoryType fromStringToCategoryType(String type) {
        if (type.equals(App.getContext().getResources().getString(R.string.type_expense_text))) {
            return CategoryType.EXPENSE;
        } else if(type.equals(App.getContext().getResources().getString(R.string.type_income_text))) {
            return CategoryType.INCOME;
        }
        return null;
    }
}
