package es.uam.eps.tfg.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.app.database.AppDAO;
import es.uam.eps.tfg.app.database.AppDatabase;


public class StatsFragment extends Fragment {

    AppDatabase database = AppDatabase.getInstance(App.getContext());
    AppDAO appDAO = database.getAppDAO();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_stats,
                container,
                false);

        Button expensesButton = view.findViewById(R.id.stats_expenses_button);
        Button incomesButton = view.findViewById(R.id.stats_incomes_button);
        Button allButton = view.findViewById(R.id.stats_all_button);

        // Display expense statistics by default
        view.findViewById(R.id.bar_chart).setVisibility(View.INVISIBLE);
        setTotalAmount(view, CategoryType.EXPENSE);
        setPieChart(view, CategoryType.EXPENSE);

        // Display expense statistics
        expensesButton.setOnClickListener(b -> {
            // Set buttons colors
            b.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            incomesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            allButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            setTotalAmount(view, CategoryType.EXPENSE);
            setPieChart(view, CategoryType.EXPENSE);
            view.findViewById(R.id.bar_chart).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.pie_chart).setVisibility(View.VISIBLE);
        });

        // Display income statistics
        incomesButton.setOnClickListener(b -> {
            // Set buttons colors
            b.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            expensesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            allButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            setTotalAmount(view, CategoryType.INCOME);
            setPieChart(view, CategoryType.INCOME);
            view.findViewById(R.id.bar_chart).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.pie_chart).setVisibility(View.VISIBLE);
        });

        // Display all statistics
        allButton.setOnClickListener(b -> {
            // Set buttons colors
            b.setBackgroundColor(App.getContext().getResources().getColor(R.color.blue_highlight));
            expensesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));
            incomesButton.setBackgroundColor(App.getContext().getResources().getColor(R.color.white));

            view.findViewById(R.id.pie_chart).setVisibility(View.INVISIBLE);
            setBarChart(view);
            view.findViewById(R.id.bar_chart).setVisibility(View.VISIBLE);

            allTotalAmount(view);
        });

        return view;
    }

    public void setPieChart(View view, CategoryType type) {
        // Create the chart
        PieChart pieChart = view.findViewById(R.id.pie_chart);
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Get the transactions and the expense and income categories
        List<Transaction> transactions = appDAO.getTransactions();
        List<Category> expenseCategories = appDAO.getCategoriesByType(CategoryType.EXPENSE);
        List<Category> incomeCategories = appDAO.getCategoriesByType(CategoryType.INCOME);

        if(type == CategoryType.EXPENSE) {
            // Get the data for the expense chart
            for(Category c : expenseCategories) {
                float i = 0.0F;
                for(Transaction t : transactions) {
                    Category category = t.getCategory();
                    if(category.getName().equals(c.getName()) && t.getType() == CategoryType.EXPENSE) {
                        i++;
                    }
                }

                if(i > 0.0F) {
                    entries.add(new PieEntry(i, c.getName()));
                }
            }
        } else if(type == CategoryType.INCOME) {
            // Get the data for the income chart
            for(Category c : incomeCategories) {
                float i = 0.0F;
                for(Transaction t : transactions) {
                    Category category = t.getCategory();
                    if(category.getName().equals(c.getName()) && t.getType() == CategoryType.INCOME) {
                        i++;
                    }
                }

                if(i > 0.0F) {
                    entries.add(new PieEntry(i, c.getName()));
                }
            }
        }

        // Use percentages
        pieChart.setUsePercentValues(true);

        // Set chart data
        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(
                new int[]{
                        R.color.pie_green,
                        R.color.pie_blue,
                        R.color.pie_red,
                        R.color.pie_yellow,
                        R.color.pie_orange,
                        R.color.pie_brown,
                        R.color.pie_pink,
                        R.color.pie_aqua,
                        R.color.pie_grey,
                        R.color.pie_purple},
                App.getContext());
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

        // Set visual features of the chart
        pieChart.setEntryLabelTextSize(16f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);
        dataSet.setDrawValues(false);
        pieChart.animateY(500, Easing.EaseInOutQuad);

        // Refresh chart
        pieChart.invalidate();
    }

    public void setBarChart(View view) {
        // Create the chart
        BarChart barChart = view.findViewById(R.id.bar_chart);
        ArrayList<BarEntry> entries = new ArrayList<>();

        // Get the transactions
        List<Transaction> transactions = appDAO.getTransactions();

        double expensesAmount = 0.0F;
        double incomesAmount = 0.0F;

        for(Transaction t : transactions) {
            if(t.getType() == CategoryType.EXPENSE) {
                expensesAmount += t.getAmount();
            } else if(t.getType() == CategoryType.INCOME) {
                incomesAmount += t.getAmount();
            }
        }

        entries.add(new BarEntry(0, (float)expensesAmount));
        entries.add(new BarEntry(1, (float)incomesAmount));

        // Set chart data
        BarDataSet dataSet = new BarDataSet(entries, "Categories");
        dataSet.setColors(
                new int[]{
                        R.color.pie_red,
                        R.color.pie_green},
                App.getContext());
        dataSet.setDrawValues(false);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);

        // Set visual features of the chart
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        barChart.getLegend().setEnabled(false);
        barChart.animateY(300);
        barChart.animateX(300);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(false);

        // Hide the grid of the X axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Set the labels of the X axis
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add(App.getContext().getResources().getString(R.string.type_expense_text));
        xAxisLabel.add(App.getContext().getResources().getString(R.string.type_income_text));
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));
        xAxis.setLabelCount(2);

        // Hide the grid of the Y axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisMinimum(0f);

        // Refresh chart
        barChart.invalidate();
    }

    public void setTotalAmount(View view, CategoryType type) {
        List<Transaction> transactions = appDAO.getTransactions();

        // Summation of the amount of each transaction matching the category type
        double total_amount = 0.0F;

        for(Transaction t : transactions) {
            if(t.getType() == type) {
                total_amount += t.getAmount();
            }
        }

        // Display total amount
        @SuppressLint("DefaultLocale")
        String total_amount_format = String.format("%.2f", total_amount);
        String amount = total_amount_format + " \u20ac";
        TextView amountText = view.findViewById(R.id.stats_amount);
        amountText.setText(amount);

        if(type == CategoryType.EXPENSE) {
            amountText.setTextColor(App.getContext().getResources().getColor(R.color.red));
        } else if(type == CategoryType.INCOME) {
            amountText.setTextColor(App.getContext().getResources().getColor(R.color.green));
        }
    }

    public void allTotalAmount(View view) {
        List<Transaction> transactions = appDAO.getTransactions();

        double expensesAmount = 0.0F;
        double incomesAmount = 0.0F;
        double difference;

        for(Transaction t : transactions) {
            if(t.getType() == CategoryType.EXPENSE) {
                expensesAmount += t.getAmount();
            } else if(t.getType() == CategoryType.INCOME) {
                incomesAmount += t.getAmount();
            }
        }

        // Display the difference between expenses and incomes
        difference = incomesAmount - expensesAmount;
        @SuppressLint("DefaultLocale")
        String differenceFormat = String.format("%.2f", difference);
        String amount = differenceFormat + " \u20ac";
        TextView amountText = view.findViewById(R.id.stats_amount);
        amountText.setText(amount);

        if(difference < 0) {
            amountText.setTextColor(App.getContext().getResources().getColor(R.color.red));
        } else if(difference == 0) {
            amountText.setTextColor(App.getContext().getResources().getColor(R.color.black));
        } else {
            amountText.setTextColor(App.getContext().getResources().getColor(R.color.green));
        }
    }
}
