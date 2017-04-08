package pl.edu.agh.mobilne_2017;


import pl.edu.agh.mobilne_2017.activ.QuestionType;

public class ClosedQuestion implements Question {
    private final boolean[] checkboxes;
    private final String[] answs;
    private final String content;

    public ClosedQuestion(String content, boolean[] checkboxes, String[] answs) {
        this.content = content;
        this.checkboxes = checkboxes;
        this.answs = answs;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.CLOSED;
    }

    public boolean[] getCheckboxes() {
        return checkboxes;
    }

    public String[] getAnsws() {
        return answs;
    }
}
