package pl.edu.agh.mobilne_2017.model;


public class OpenQuestion implements Question {
    private final String content;
    private final String stringAnswer;
    private final int id;


    public OpenQuestion(String content, String stringAnswer, int id) {
        this.content = content;
        this.stringAnswer = stringAnswer;
        this.id = id;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.OPEN;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getStringAnswer() {
        return stringAnswer;
    }
}
