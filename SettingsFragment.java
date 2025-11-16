package com.example.preferencesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

    EditText etName, etEmail, etPassword;
    Spinner spinnerTheme;
    Switch switchNotifications;
    Button btnSave, btnReset;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        etName = v.findViewById(R.id.etName);
        etEmail = v.findViewById(R.id.etEmail);
        etPassword = v.findViewById(R.id.etPassword);
        spinnerTheme = v.findViewById(R.id.spinnerTheme);
        switchNotifications = v.findViewById(R.id.switchNotifications);
        btnSave = v.findViewById(R.id.btnSave);
        btnReset = v.findViewById(R.id.btnReset);

        prefs = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();

        // Load saved preferences
        etName.setText(prefs.getString("username", ""));
        etEmail.setText(prefs.getString("email", ""));
        etPassword.setText(prefs.getString("password", ""));
        switchNotifications.setChecked(prefs.getBoolean("notifications", false));

        String savedTheme = prefs.getString("theme", "Light");
        ArrayAdapter adapter = (ArrayAdapter) spinnerTheme.getAdapter();
        spinnerTheme.setSelection(adapter.getPosition(savedTheme));

        applyTheme(savedTheme, v);

        btnSave.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save all preferences
            editor.putString("username", name);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.putString("theme", spinnerTheme.getSelectedItem().toString());
            editor.putBoolean("notifications", switchNotifications.isChecked());
            editor.apply();

            Toast.makeText(getActivity(), "Preferences Saved", Toast.LENGTH_SHORT).show();

            applyTheme(spinnerTheme.getSelectedItem().toString(), v);
        });

        btnReset.setOnClickListener(view -> {
            editor.clear();
            editor.apply();

            etName.setText("");
            etEmail.setText("");
            etPassword.setText("");
            spinnerTheme.setSelection(0);
            switchNotifications.setChecked(false);

            Toast.makeText(getActivity(), "Preferences Reset", Toast.LENGTH_SHORT).show();
            applyTheme("Light", v);
        });

        return v;
    }

    private void applyTheme(String theme, View root) {
        if (root == null) return;

        switch (theme) {
            case "Light":
                root.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case "Dark":
                root.setBackgroundColor(Color.parseColor("#696969")); // Dim gray
                break;
            case "Blue":
                root.setBackgroundColor(Color.parseColor("#ADD8E6")); // Light blue
                break;
            case "Green":
                root.setBackgroundColor(Color.parseColor("#90EE90")); // Light green
                break;
        }
    }
}
