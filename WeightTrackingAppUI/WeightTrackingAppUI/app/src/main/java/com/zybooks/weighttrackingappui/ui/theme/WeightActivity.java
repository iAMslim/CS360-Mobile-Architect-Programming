package com.zybooks.weighttrackingappui.ui.theme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zybooks.weighttrackingappui.R;

import java.util.ArrayList;

public class WeightActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WeightAdapter adapter;
    private ArrayList<WeightEntry> weightEntries;
    private DatabaseHelper dbHelper;
    private int userId = 1; // Replace with logged-in user ID if needed
    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    private static final float GOAL_WEIGHT = 180.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        recyclerView = findViewById(R.id.recyclerViewWeights);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        weightEntries = new ArrayList<>();

        loadWeights();

        adapter = new WeightAdapter(weightEntries,
                this::showEditDialog,
                this::confirmDelete);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAddWeight).setOnClickListener(view -> showAddDialog());

        // Request SMS permission if not already granted
        if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied. App will work without it.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadWeights() {
        weightEntries.clear();
        Cursor cursor = dbHelper.getAllWeights(userId);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("entry_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date_logged"));
                float weight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight"));
                weightEntries.add(new WeightEntry(id, date, weight));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void showAddDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputDate = new EditText(this);
        inputDate.setHint("Date (YYYY-MM-DD)");
        layout.addView(inputDate);

        EditText inputWeight = new EditText(this);
        inputWeight.setHint("Weight (lbs)");
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inputWeight);

        new AlertDialog.Builder(this)
                .setTitle("Add Weight Entry")
                .setView(layout)
                .setPositiveButton("Add", (dialog, which) -> {
                    String date = inputDate.getText().toString().trim();
                    float weight = Float.parseFloat(inputWeight.getText().toString().trim());
                    boolean added = dbHelper.insertWeight(userId, date, weight);
                    if (added) {
                        loadWeights();
                        adapter.notifyDataSetChanged();

                        if (weight <= GOAL_WEIGHT) {
                            sendGoalReachedSMS();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditDialog(WeightEntry entry) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText inputDate = new EditText(this);
        inputDate.setText(entry.getDate());
        layout.addView(inputDate);

        EditText inputWeight = new EditText(this);
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputWeight.setText(String.valueOf(entry.getWeight()));
        layout.addView(inputWeight);

        new AlertDialog.Builder(this)
                .setTitle("Update Entry")
                .setView(layout)
                .setPositiveButton("Update", (dialog, which) -> {
                    String newDate = inputDate.getText().toString().trim();
                    float newWeight = Float.parseFloat(inputWeight.getText().toString().trim());
                    boolean updated = dbHelper.updateWeight(entry.getId(), newDate, newWeight);
                    if (updated) {
                        loadWeights();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(WeightEntry entry) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    boolean deleted = dbHelper.deleteWeight(entry.getId());
                    if (deleted) {
                        loadWeights();
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendGoalReachedSMS() {
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            String phoneNumber = "5551234567";  // Replace with actual user phone later
            String message = "Congratulations! You reached your goal weight of " + GOAL_WEIGHT + " lbs!";
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Goal SMS sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SMS permission not granted. Can't send alert.", Toast.LENGTH_SHORT).show();
        }
    }
}
