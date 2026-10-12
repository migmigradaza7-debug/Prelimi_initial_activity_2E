package com.example.prelim_initialactivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menuButton).setOnClickListener(this::showMenu);
        findViewById(R.id.notificationButton).setOnClickListener(v ->
                showInfo("Notifications", "You have no new notifications."));
        findViewById(R.id.superButton).setOnClickListener(v -> {
            ScrollView scrollView = findViewById(R.id.mainScrollView);
            View quickAccess = findViewById(R.id.cardSafePlaces);
            int[] targetPosition = new int[2];
            int[] scrollPosition = new int[2];
            quickAccess.getLocationOnScreen(targetPosition);
            scrollView.getLocationOnScreen(scrollPosition);
            scrollView.smoothScrollTo(0, targetPosition[1] - scrollPosition[1]);
        });

        findViewById(R.id.cardSafePlaces).setOnClickListener(v ->
                showInfo("Safe Places", "Ask your school office or guidance counselor where the designated safe spaces are."));
        findViewById(R.id.cardTalk).setOnClickListener(v ->
                showInfo("Talk to Someone", "You deserve support. Consider reaching out to a trusted teacher, counselor, family member, or friend."));
        findViewById(R.id.cardResources).setOnClickListener(v ->
                showInfo("Resources", "For school support, contact your guidance office or student services. If you are in immediate danger, contact local emergency services."));
        findViewById(R.id.cardReport).setOnClickListener(v -> showReportDialog());

        findViewById(R.id.navHome).setOnClickListener(v ->
                Toast.makeText(this, "You are on Home", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navPlaces).setOnClickListener(v ->
                showInfo("Places", "Ask your school office for the locations of designated safe spaces."));
        findViewById(R.id.navChat).setOnClickListener(v ->
                showInfo("Chat", "This demo does not connect to a live chat. Please contact a trusted person or your school counselor."));
        findViewById(R.id.navProfile).setOnClickListener(v ->
                showInfo("Profile", "Profile settings are not available yet."));
    }

    private void showMenu(View anchor) {
        PopupMenu menu = new PopupMenu(this, anchor);
        menu.getMenu().add("Home");
        menu.getMenu().add("Safe Places");
        menu.getMenu().add("Resources");
        menu.getMenu().add("About SafeSpace");
        menu.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Safe Places":
                    showInfo(title, "Ask your school office or guidance counselor where the designated safe spaces are.");
                    break;
                case "Resources":
                    showInfo(title, "For school support, contact your guidance office or student services.");
                    break;
                case "About SafeSpace":
                    showInfo(title, "SafeSpace helps students find support and feel safer at school.");
                    break;
                default:
                    Toast.makeText(this, "You are on Home", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        menu.show();
    }

    private void showReportDialog() {
        EditText reportInput = new EditText(this);
        reportInput.setHint("Describe what happened");
        reportInput.setGravity(android.view.Gravity.TOP | android.view.Gravity.START);
        reportInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        reportInput.setMinLines(3);
        reportInput.setMaxLines(6);

        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(padding, 0, padding, 0);
        container.addView(reportInput, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Report an Issue")
                .setMessage("This demo keeps your report on this screen and does not send it to anyone.")
                .setView(container)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Review", null)
                .create();
        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String report = reportInput.getText().toString().trim();
            if (report.isEmpty()) {
                reportInput.setError("Please describe the issue");
                return;
            }
            getSharedPreferences("safespace", MODE_PRIVATE)
                    .edit()
                    .putString("last_report", report)
                    .apply();
            dialog.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("Report saved on this device")
                    .setMessage("Your report was saved locally. It has not been sent to your school or anyone else.")
                    .setPositiveButton("OK", null)
                    .show();
        }));
        dialog.show();
    }

    private void showInfo(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
