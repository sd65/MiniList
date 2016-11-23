package com.doignon.sylvain.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Task_list extends AppCompatActivity {

    private static TaskAdapter itemsAdapter;
    private Map<String, Comparator<Task>> sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

        // Storage
        MyStorage storage = new MyStorage(getFilesDir());

        // Add new task
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Task_list.this, Task_edit.class);
                startActivityForResult(i, 0);
            }
        });

        // Make the list and adapter
        ListView listTasks = (ListView) findViewById(R.id.listTasks);
        View empty = findViewById(R.id.empty);
        listTasks.setEmptyView(empty);
        ArrayList<Task> items = storage.restoreTasks();
        itemsAdapter = new TaskAdapter(this, items, storage);
        listTasks.setAdapter(itemsAdapter);

        // Sort Functions
        sortBy = new HashMap<>();
        sortBy.put("label", new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return a.getLabel().compareTo(b.getLabel());
            }
        });
        sortBy.put("deadline", new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                if(a.getDeadline() == null)
                    return -1;
                if(b.getDeadline() == null)
                    return 1;
                return a.getDeadline().compareTo(b.getDeadline());
            }
        });
        sortBy.put("priority", new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return a.getPriority().compareTo(b.getPriority());
            }
        });

        //Onclick Task
        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3)
            {
                Task t = (Task) adapter.getItemAtPosition(position);
                Intent i = new Intent(Task_list.this, Task_edit.class);
                i.putExtra("task", t);
                startActivityForResult(i, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Has created Task
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Task t = (Task) data.getSerializableExtra("task");
            itemsAdapter.add(t);
            displayToast("Task created successfully");
        }
        // Has edited Task
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.hasExtra("wantDelete")) {
                Task task = (Task) data.getSerializableExtra("task");
                System.out.println(task.getUuid());
                itemsAdapter.remove(task);
                displayToast("Task deleted successfully");
            } else {
                Task oldTask = (Task) data.getSerializableExtra("oldTask");
                Task newTask = (Task) data.getSerializableExtra("task");
                itemsAdapter.remove(oldTask);
                itemsAdapter.add(newTask);
                displayToast("Task edited successfully");
            }
        }
        itemsAdapter.notifyDataSetChanged();
    }

    private void displayToast(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_list_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_priority:
                itemsAdapter.sort(sortBy.get("priority"));
                return true;
            case R.id.sort_deadline:
                itemsAdapter.sort(sortBy.get("deadline"));
                return true;
            case R.id.sort_label:
                itemsAdapter.sort(sortBy.get("label"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
