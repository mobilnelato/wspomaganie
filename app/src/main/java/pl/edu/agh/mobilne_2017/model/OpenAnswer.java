package pl.edu.agh.mobilne_2017.model;



public class OpenAnswer implements Answer {

    private final String val;

    public OpenAnswer(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.OPEN;
    }
}
