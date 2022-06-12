package com.bmp601.everylirarepoerts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


public class SpecificReportActivity extends AppCompatActivity {
    private SimpleCursorAdapter dataAdapter;
    private String kindOfReport;
    Uri reportUri;
    TextView noExpenses, reportName, year, month, totalCost, totalCostTextView, currency;
    Spinner yearsSpinner, monthsSpinner;
    Button getReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_report);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] from = new String[]{"name", "categoryName", "price", "date"};
        int[] to = new int[]{R.id.expenseItemName, R.id.expenseCategoryName, R.id.expensePrice, R.id.expenseDate};

        noExpenses = findViewById(R.id.noExpenses);
        reportName = findViewById(R.id.reportName);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        totalCost = findViewById(R.id.totalCost);
        yearsSpinner = findViewById(R.id.yearsSpinner);
        monthsSpinner = findViewById(R.id.monthsSpinner);
        getReport = findViewById(R.id.getReport);
        totalCostTextView = findViewById(R.id.totalCostTextView);
        currency = findViewById(R.id.currency);

        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            kindOfReport = bundle.getString("kindOfReport");
        }

        if (kindOfReport.trim().equalsIgnoreCase("yearlyReports")) {

            reportName.setText(R.string.yearlyReports);
            reportName.setVisibility(View.VISIBLE);

            year.setVisibility(View.VISIBLE);
            totalCost.setVisibility(View.GONE);
            totalCostTextView.setVisibility(View.GONE);
            currency.setVisibility(View.GONE);
            yearsSpinner.setVisibility(View.VISIBLE);
            getReport.setVisibility(View.VISIBLE);

            ArrayList<String> years = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 1900; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            yearsSpinner.setAdapter(adapter);
            yearsSpinner.setSelection(thisYear - 1900);

            getReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedYear = yearsSpinner.getSelectedItem().toString();

                    Log.w("HERE", selectedYear);
//                    Log.w("Search query", searchQuery);
                    String[] args = {selectedYear};

                    Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesYearTotal"), null, null, args, null);
                    c.moveToFirst();

                    if (c.getString(c.getColumnIndexOrThrow("Total")) == null)
                        totalCost.setText("0");
                    else
                        totalCost.setText(c.getString(c.getColumnIndexOrThrow("Total")));

                    Cursor cursor = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesYear"), null, null, args, null);

                    if (cursor.getCount() == 0){
                        noExpenses.setVisibility(View.VISIBLE);
                        noExpenses.setText(R.string.noExpenses);
                    } else
                        noExpenses.setVisibility(View.GONE);

                    Log.w("Cursor", String.valueOf(cursor.getCount()));


                    SimpleCursorAdapter dataAdapter;
                    dataAdapter = new SimpleCursorAdapter(SpecificReportActivity.this, R.layout.expense_info, cursor, from, to, 0);
                    ListView listView = findViewById(R.id.expensesList);
                    listView.setAdapter(dataAdapter);

                    totalCost.setVisibility(View.VISIBLE);
                    totalCostTextView.setVisibility(View.VISIBLE);
                    currency.setVisibility(View.VISIBLE);
                }
            });

        }

        if (kindOfReport.trim().equalsIgnoreCase("monthlyReports")) {

            reportName.setText(R.string.monthlyReports);
            reportName.setVisibility(View.VISIBLE);

            year.setVisibility(View.VISIBLE);
            month.setVisibility(View.VISIBLE);
            totalCost.setVisibility(View.GONE);
            totalCostTextView.setVisibility(View.GONE);
            currency.setVisibility(View.GONE);
            yearsSpinner.setVisibility(View.VISIBLE);
            monthsSpinner.setVisibility(View.VISIBLE);
            getReport.setVisibility(View.VISIBLE);

            ArrayList<String> years = new ArrayList<String>();
            ArrayList<String> months = new ArrayList<String>();
            int thisYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int i = 1900; i <= thisYear; i++) {
                years.add(Integer.toString(i));
            }
            for (int i = 1; i <= 12; i++) {
                months.add(Integer.toString(i));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearsSpinner.setAdapter(adapter);
            yearsSpinner.setSelection(thisYear - 1900);

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            monthsSpinner.setAdapter(adapter);

            getReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedYear = yearsSpinner.getSelectedItem().toString();
                    String selectedMonth = monthsSpinner.getSelectedItem().toString();

                    Log.w("HERE", selectedYear);
                    Log.w("HERE", selectedMonth);
//                    Log.w("Search query", searchQuery);

                    if (selectedMonth.length() == 1)
                        selectedMonth = '0' + selectedMonth;
                    String[] args = {selectedYear, selectedMonth};

                    Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesMonthTotal"), null, null, args, null);
                    c.moveToFirst();

                    if (c.getString(c.getColumnIndexOrThrow("Total")) == null)
                        totalCost.setText("0");
                    else
                        totalCost.setText(c.getString(c.getColumnIndexOrThrow("Total")));

                    Cursor cursor = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesMonth"), null, null, args, null);

                    if (cursor.getCount() == 0){
                        noExpenses.setVisibility(View.VISIBLE);
                        noExpenses.setText(R.string.noExpenses);
                    } else
                        noExpenses.setVisibility(View.GONE);

                    Log.w("Cursor", String.valueOf(cursor.getCount()));


                    SimpleCursorAdapter dataAdapter;
                    dataAdapter = new SimpleCursorAdapter(SpecificReportActivity.this, R.layout.expense_info, cursor, from, to, 0);
                    ListView listView = findViewById(R.id.expensesList);
                    listView.setAdapter(dataAdapter);

                    totalCost.setVisibility(View.VISIBLE);
                    totalCostTextView.setVisibility(View.VISIBLE);
                    currency.setVisibility(View.VISIBLE);
                }
            });
        }


        if (kindOfReport.trim().equalsIgnoreCase("categoryReport")) {
            year.setText(R.string.category);

            reportName.setText(R.string.categoryReport);
            reportName.setVisibility(View.VISIBLE);

            year.setVisibility(View.VISIBLE);
            totalCost.setVisibility(View.GONE);
            totalCostTextView.setVisibility(View.GONE);
            currency.setVisibility(View.GONE);
            yearsSpinner.setVisibility(View.VISIBLE);
            getReport.setVisibility(View.VISIBLE);

            ArrayList<String> categoriesList = new ArrayList<String>();
            Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderCategories/categories"), null, null, null, null);

            while (c.moveToNext()) {
                String currentCategory = c.getString(c.getColumnIndexOrThrow("categoryName"));
                categoriesList.add(currentCategory);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            yearsSpinner.setAdapter(adapter);


            getReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedCategory = yearsSpinner.getSelectedItem().toString();

                    Log.w("HERE", selectedCategory);
//                    Log.w("HERE", selectedMonth);
//                    Log.w("Search query", searchQuery);

                    String[] args = {selectedCategory};

                    Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesCategoryTotal"), null, null, args, null);
                    c.moveToFirst();

                    if (c.getString(c.getColumnIndexOrThrow("Total")) == null)
                        totalCost.setText("0");
                    else
                        totalCost.setText(c.getString(c.getColumnIndexOrThrow("Total")));

                    Cursor cursor = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesCategoryReport"), null, null, args, null);

                    if (cursor.getCount() == 0){
                        noExpenses.setVisibility(View.VISIBLE);
                        noExpenses.setText(R.string.noExpenses);
                    } else
                        noExpenses.setVisibility(View.GONE);

                    Log.w("Cursor", String.valueOf(cursor.getCount()));


                    SimpleCursorAdapter dataAdapter;
                    dataAdapter = new SimpleCursorAdapter(SpecificReportActivity.this, R.layout.expense_info, cursor, from, to, 0);
                    ListView listView = findViewById(R.id.expensesList);
                    listView.setAdapter(dataAdapter);

                    totalCost.setVisibility(View.VISIBLE);
                    totalCostTextView.setVisibility(View.VISIBLE);
                    currency.setVisibility(View.VISIBLE);
                }
            });
        }

        if (kindOfReport.trim().equalsIgnoreCase("purchasedItemsReport")) {

            reportName.setText(R.string.purchasedItemsReport);
            reportName.setVisibility(View.VISIBLE);

            reportUri = Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesPaidReport");
            Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesPaidCost"), null, null, null, null);
            c.moveToFirst();
            totalCost.setText(c.getString(c.getColumnIndexOrThrow("Total")));

            Cursor cursor = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesPaidReport"), null, null, null, null);

            if (cursor.getCount() == 0){
                noExpenses.setVisibility(View.VISIBLE);
                noExpenses.setText(R.string.noExpenses);
            } else
                noExpenses.setVisibility(View.GONE);

            SimpleCursorAdapter dataAdapter;
            dataAdapter = new SimpleCursorAdapter(this, R.layout.expense_info, cursor, from, to, 0);
            ListView listView = findViewById(R.id.expensesList);
            listView.setAdapter(dataAdapter);
        }

        if (kindOfReport.trim().equalsIgnoreCase("serviceReport")) {

            reportName.setText(R.string.servicesReport);
            reportName.setVisibility(View.VISIBLE);

            reportUri = Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesServicesReport");
            Cursor c = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesServicesCost"), null, null, null, null);
            c.moveToFirst();
            totalCost.setText(c.getString(c.getColumnIndexOrThrow("Total")));
            Cursor cursor = getContentResolver().query(Uri.parse("content://com.bmp601.everyLiraContentProviderExpenses/expensesServicesReport"), null, null, null, null);

            if (cursor.getCount() == 0){
                noExpenses.setVisibility(View.VISIBLE);
                noExpenses.setText(R.string.noExpenses);
            } else
                noExpenses.setVisibility(View.GONE);

            SimpleCursorAdapter dataAdapter;
            dataAdapter = new SimpleCursorAdapter(this, R.layout.expense_info, cursor, from, to, 0);
            ListView listView = findViewById(R.id.expensesList);
            listView.setAdapter(dataAdapter);

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}