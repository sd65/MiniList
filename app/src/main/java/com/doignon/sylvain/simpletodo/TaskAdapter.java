package com.doignon.sylvain.simpletodo;

import android.content.Context;
import android.os.health.SystemHealthManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> tasks;
    private MyStorage storage;

    public TaskAdapter(Context context, ArrayList<Task> vtasks, MyStorage vstorage) {
        super(context, 0, vtasks);
        storage = vstorage;
        tasks = vtasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_task,parent, false);
        }

        View.OnClickListener myStatusListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                Task t = getItem(position);
                t.toggleStatus();
                v.setHovered(t.getStatus());
                notifyDataSetChanged();
            }
        };

        TaskViewHolder viewHolder = (TaskViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TaskViewHolder();
            viewHolder.row_status = (ImageView) convertView.findViewById(R.id.row_status);
            viewHolder.row_status.setOnClickListener(myStatusListener);
            viewHolder.row_label = (TextView) convertView.findViewById(R.id.row_label);
            viewHolder.row_deadline = (TextView) convertView.findViewById(R.id.row_deadline);
            viewHolder.row_priority = (RatingBar) convertView.findViewById(R.id.row_priority);
            viewHolder.row_status.setTag(position);
            convertView.setTag(viewHolder);
        }

        Task task = getItem(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        viewHolder.row_status.setHovered(task.getStatus());
        viewHolder.row_label.setText(task.getLabel());
        Date deadline = task.getDeadline();
        String sdeadline = "ASAP";
        if(deadline != null)
            sdeadline = sdf.format(deadline);
        viewHolder.row_deadline.setText(sdeadline);
        viewHolder.row_priority.setRating(task.getPriority());
        return convertView;
    }

    private class TaskViewHolder {
        public ImageView row_status;
        public TextView row_label;
        public TextView row_deadline;
        public RatingBar row_priority;
    }

    public void remove(Task toRemove){
        tasks.remove(tasks.indexOf(toRemove));
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public void notifyDataSetChanged () {
        super.notifyDataSetChanged();
        storage.saveTasks(tasks);
    }

}