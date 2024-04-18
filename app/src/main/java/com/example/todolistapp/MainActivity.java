package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {
    Button add;
    AlertDialog dialog;
    LinearLayout tasksContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=findViewById(R.id.add);
        tasksContainer=findViewById(R.id.tasks_container);
        tasksContainer.removeAllViews();

        buildDialog();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("task_preferences", Context.MODE_PRIVATE);
        int taskCount = sharedPref.getInt("task_count", 0);
        for (int i = 0; i < taskCount; i++) {
            String task = sharedPref.getString("task_" + i, "");
            addCard(task);
        }
    }
    public void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter you task")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(name.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
    }
    private void addCard(String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.name);
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksContainer.removeView(view);
                saveTasks();
            }
        });
        tasksContainer.addView(view);
        saveTasks();
    }

    private void saveTasks() {
        SharedPreferences sharedPref = getSharedPreferences("task_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < tasksContainer.getChildCount(); i++) {
            View view = tasksContainer.getChildAt(i);
            TextView nameView = view.findViewById(R.id.name);
            editor.putString("task_" + i, nameView.getText().toString());
        }
        editor.putInt("task_count", tasksContainer.getChildCount());
        editor.apply();
    }
}