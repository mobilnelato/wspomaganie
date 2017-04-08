package pl.edu.agh.mobilne_2017;


import pl.edu.agh.mobilne_2017.activ.QuestionType;

public class OpenQuestion implements Question {
    private final String content;
    private final String stringAnswer;


    public OpenQuestion(String content, String stringAnswer) {
        this.content = content;
        this.stringAnswer = stringAnswer;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.OPEN;
    }

    public String getStringAnswer() {
        return stringAnswer;
    }
}
