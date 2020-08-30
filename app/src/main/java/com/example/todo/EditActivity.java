package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    List<String> notes;

    EditText etItem;
    EditText etPriority;
    EditText mlNotes;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        etPriority = findViewById(R.id.etPriority);
        mlNotes = findViewById(R.id.mlNotes);
        btnSave = findViewById(R.id.btnSave);

        loadNotes();

        getSupportActionBar().setTitle("Edit Item");




        String keyItem = getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT);
        String keyNote = getIntent().getStringExtra(/*The final value to represent a note*/MainActivity.KEY_ITEM_NOTES);

        StringBuilder strItem = new StringBuilder(keyItem);
        String priority = "";

        if (strItem.charAt(0) == '(' && strItem.charAt(1) == '*') {
            //int itemLength = strItem.length();
            strItem.deleteCharAt(0);
            strItem.deleteCharAt(0);
            while (strItem.charAt(0) >= 48 && strItem.charAt(0) <= 57) {//0 = index
                priority += strItem.charAt(0);
                strItem.deleteCharAt(0);
            }
            strItem.deleteCharAt(0);
            strItem.deleteCharAt(0);
            strItem.deleteCharAt(0);
        }

        System.out.println("Item: " + strItem);
        System.out.println("Priority: " + priority);

        etItem.setText(strItem);
        etPriority.setText(priority);
        mlNotes.setText(keyNote);
        System.out.println(keyNote);
        System.out.println(keyItem);
        System.out.println(notes);

        //Button saves user edits
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create intent that contains modifications
                Intent intent = new Intent();
                //Pass results/edits
                if (etPriority.getText().toString().compareTo("0") == 0 || etPriority.getText().toString().compareTo("") == 0) {//0 = true
                    System.out.println("There's a 0, or empty");
                    intent.putExtra(MainActivity.KEY_ITEM_TEXT, etItem.getText().toString());
                } else {
                    intent.putExtra(MainActivity.KEY_ITEM_TEXT, ("(*" + etPriority.getText().toString() + "*) " + etItem.getText().toString()));//Adding priority
                }
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                intent.putExtra(MainActivity.KEY_ITEM_NOTES, mlNotes.getText().toString());
                //Set result of intent
                setResult(RESULT_OK, intent);
                //Finish/close activity
                saveNotes();
                finish();
            }
        });
    }

    private File getNotesFile () {
        return new File(getFilesDir(), "notes.txt");
    }

    private void loadNotes() {
        try {
            notes = new ArrayList<>(FileUtils.readLines(getNotesFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading notes", e);
            notes = new ArrayList<>();
        }
    }

    private void saveNotes () {
        try {
            FileUtils.writeLines(getNotesFile(), notes);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing note", e);
        }
    }
}