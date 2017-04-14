package pl.edu.agh.mobilne_2017.model;



public class ClosedAnswer implements Answer {

    private final boolean[] checkboxes;

    public ClosedAnswer(boolean[] checkboxes) {
        this.checkboxes = checkboxes;
    }

    public boolean[] getCheckboxes() {
        return checkboxes;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.CLOSED;
    }
}
