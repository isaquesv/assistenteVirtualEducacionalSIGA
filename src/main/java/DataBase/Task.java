package DataBase;

import java.util.ArrayList;

public class Task {
    public static ArrayList<Task> list = new ArrayList<>();
    
    private String title;

    public Task(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
