package com.example.steve.todo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) this.findViewById(R.id.lvItems);

//        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        this.setupListViewListener();

    }

    /**
     * show the edit dialog
     **/
    private void showEditDialog(final int pos) {
        final EditText editText = new EditText(this);
        editText.setText(items.get(pos));
        new AlertDialog.Builder(this).setTitle("Please Input").setIcon(
                android.R.drawable.ic_dialog_info).setView(
                editText).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt = editText.getText().toString();
                items.set(pos, txt);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
            }
        }).setNegativeButton("Cancel", null).show();
    }

    /**
     * show the delete warning
     **/
    private void showDeleteDialog(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are u sure to delete？");
        builder.setTitle("Confirm");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        showEditDialog(position);
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showDeleteDialog(position);
                    }
                }
        );
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) this.findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (null == itemText || itemText.trim().equals("")) {
            return;
        }
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        if (!todoFile.exists()) {
            try {
                todoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
