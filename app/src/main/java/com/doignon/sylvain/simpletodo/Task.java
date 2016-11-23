package com.doignon.sylvain.simpletodo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


public class Task implements Serializable {

    private String label;
    private boolean status;
    private Integer priority;
    private Date deadline;
    private UUID uuid;


    public Task (String vlabel, boolean vstatus, Integer vpriority, Date vdeadline) {
        uuid = UUID.randomUUID();
        label = vlabel;
        status = vstatus;
        priority = vpriority;
        deadline = vdeadline;
    }

    public String getLabel() {
        return label;
    }

    public boolean getStatus() {
        return status;
    }

    public Integer getPriority() {
        return priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void toggleStatus() {
        status = !status;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if ( obj != null && obj.getClass().equals(this.getClass())) {
            return ((Task)obj).getUuid().equals(uuid);
        } else {
            return false;
        }
    }

}
