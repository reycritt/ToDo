package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final String KEY_ITEM_NOTES = "item_notes";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    List<String> notes;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    //When activity is created
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        //etItem.setText("Some text");//EditText will start with text inside
        //items = new ArrayList<>();
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                items.remove(position);
                /*
                //Remove the notes?
                notes.remove(position);
                 */
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void OnItemClicked(int position) {
                Log.d("MainActivity", "Single click at position " + position);
                //Create new EditActivity (constructor contains the current activity, and the class to go to); when a new intent is made in EditActivity, this declaration us remembered in the new Intent in EditActivity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //Pass date to EditActivity
                intent.putExtra(KEY_ITEM_TEXT, items.get(position));
                /*
                //Might need to duplicate notes here

                 */
                //intent.putExtra(KEY_ITEM_NOTES, notes.get(position));
                intent.putExtra(KEY_ITEM_POSITION, position);
                //Display EditActivity
                startActivityForResult(intent, EDIT_TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, notes, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();

                if (todoItem.compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "Do not leave item blank!", Toast.LENGTH_SHORT).show();
                } else {
                    //Add item to model
                    items.add(todoItem);
                    //Collections.sort(items);
                    //Notify adapted of new item insertion in the last position
                    itemsAdapter.notifyItemInserted(items.size() - 1);

                    etItem.setText("");
                    Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
            }
        });
    }

    //Handle result of activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Unsure if notes goes here

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract original position of edited item from key position
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //Update model at right position with new item text
            items.set(position, itemText);
            //Notify adapter
            itemsAdapter.notifyItemChanged(position);
            //Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getItemsFile() {
        //Returns a file named "data.txt"
        return new File(getFilesDir(), "items.txt");
    }



    /*
    Loads items by reading every line of data file
     */
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getItemsFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }



    /*
    Saves items by writing them into data file
     */
    private void saveItems () {
        try {
            FileUtils.writeLines(getItemsFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing item", e);
        }
    }



}