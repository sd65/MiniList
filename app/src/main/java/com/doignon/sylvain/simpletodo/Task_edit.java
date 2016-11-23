package com.doignon.sylvain.simpletodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.doignon.sylvain.simpletodo.databinding.TaskEditBinding;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Task_edit extends AppCompatActivity {

    private TaskEditBinding binding;
    private SimpleDateFormat sdf;
    private Task oldTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.task_edit);

        // Create the date formatter
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        // Popup Calendar
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                binding.deadline.setText(sdf.format(myCalendar.getTime()));
            }

        };
        binding.deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        Task_edit.this,
                        date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        // Minimum Rating Bar
        binding.priority.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 1)
                    binding.priority.setRating(1);
            }
        });

        // Fill up if any
        if (getIntent().hasExtra("task")) {
            setTitle("Edit a task");
            Task t = (Task) getIntent().getSerializableExtra("task");
            oldTask = t;
            binding.label.setText(t.getLabel());
            if (t.getDeadline() != null)
                binding.deadline.setText(sdf.format(t.getDeadline()));
            binding.status.setChecked(t.getStatus());
            binding.priority.setRating(t.getPriority());
        } else
            setTitle("Add a new task");
    }

    public void onAddItem(View v)  {
        String label  = binding.label.getText().toString();
        if (label.isEmpty()) {
            String message = "Please enter at least a label !";
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
            return;
        }
        Boolean status = binding.status.isChecked();
        Integer priority  = Math.round(binding.priority.getRating());
        String sdeadline = binding.deadline.getText().toString();
        Date deadline = null;
        if(!sdeadline.isEmpty()) {
            try {
                deadline = sdf.parse(sdeadline);
            } catch (ParseException e) {}
        }
        Task t =  new Task(label, status, priority, deadline);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task", t);
        if (oldTask != null)
            resultIntent.putExtra("oldTask", oldTask);
        setResult(Activity.RESULT_OK, resultIntent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_edit_menu, menu);
        // Hide delete task if creating a task
        if (!getIntent().hasExtra("task")) {
            MenuItem item = menu.findItem(R.id.delete);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                this.finish();
                return true;
            case R.id.delete:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task", oldTask);
                resultIntent.putExtra("wantDelete", true);
                setResult(Activity.RESULT_OK, resultIntent);
                this.finish();
                return true;
            case R.id.reset:
                binding.label.getText().clear();
                binding.deadline.getText().clear();
                binding.status.setChecked(false);
                binding.priority.setRating(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
