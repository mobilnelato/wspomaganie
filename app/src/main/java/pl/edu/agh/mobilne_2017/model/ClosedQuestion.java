package pl.edu.agh.mobilne_2017.model;


public class ClosedQuestion implements Question {
    private final boolean[] checkboxes;
    private final String[] answs;
    private final String content;
    private final int id;

    public ClosedQuestion(String content, boolean[] checkboxes, String[] answs, int id) {
        this.content = content;
        this.checkboxes = checkboxes;
        this.answs = answs;
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.CLOSED;
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean[] getCheckboxes() {
        return checkboxes;
    }

    public String[] getAnsws() {
        return answs;
    }
}
