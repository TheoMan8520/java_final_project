package app.planner;

public class Record {
    private String type;
    private String todo;
    private String note;
    private String deadline;
    private Integer id;

    public Record (Integer id, String type, String todo, String note, String deadline) {
        this.id = id;
        this.type = type;
        this.todo = todo;
        this.note = note;
        this.deadline = deadline;
    }
    public String getType() {
        return type;
    }
    public String getTodo() { return todo; }
    public String getNote() {
        return note;
    }
    public String getDeadline() {
        return deadline;
    }
    public Integer getId() {
        return id;
    }

}
