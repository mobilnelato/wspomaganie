package pl.edu.agh.mobilne_2017.model;


public class ClosedQuestion implements Question {
    private final boolean[] checkboxes;
    private final String[] answs;
    private final String content;
    private final long id;

    public ClosedQuestion(String content, boolean[] checkboxes, String[] answs, long id) {
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
    public long getId() {
        return id;
    }

    public boolean[] getCheckboxes() {
        return checkboxes;
    }

    public String[] getAnsws() {
        return answs;
    }
}
