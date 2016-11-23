package com.doignon.sylvain.simpletodo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class MyStorage {

    private String filename = "tasks.object";
    private File file;

    public MyStorage(File filesDir) {
        file = new File(filesDir + File.separator + filename);
    }

    public ArrayList<Task> restoreTasks(){
        ObjectInputStream input;
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            input = new ObjectInputStream(new FileInputStream(file));
            tasks = (ArrayList<Task>) input.readObject();
            input.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void saveTasks (ArrayList<Task> tasks){
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(tasks);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
