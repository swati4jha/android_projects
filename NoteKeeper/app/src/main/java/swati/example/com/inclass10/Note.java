package swati.example.com.inclass10;

import java.util.HashMap;

/**
 * Created by mihai on 2/27/17.
 */

public class Note implements Comparable{
    private  String _id;
    private String subject, priority, time, status;


    HashMap<String, Integer> priorityInteger = new HashMap<String, Integer>() {{
        put("High",3);
        put("Medium",2);
        put("Low",1);
    }};
    public Note(String subject, String priority, String time, String status) {
        this.subject = subject;
        this.priority = priority;
        this.time = time;
        this.status = status;

    }

    public Note(){

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", subject='" + subject + '\'' +
                ", priority='" + priority + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if(o==null){
            return -1;
        }else{
            if (this.priorityInteger.get(this.priority)<this.priorityInteger.get((((Note)o).getPriority()))){
                return -1;
            }else if(this.priorityInteger.get(this.priority)>this.priorityInteger.get((((Note)o).getPriority()))){
                return 1;
            }else if(this.priorityInteger.get(this.priority)==this.priorityInteger.get((((Note)o).getPriority()))){
                return 0;
            }
        }
        return -1;
    }
}
