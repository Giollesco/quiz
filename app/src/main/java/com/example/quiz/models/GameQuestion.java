package com.example.quiz.models;

public class GameQuestion {
    private String question;
    private Options options;

    // Default constructor required for Firebase deserialization
    public GameQuestion() {
        // Default constructor with no parameters
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    // Inner class representing the "options" object
    public static class Options {
        private Option a;
        private Option b;
        private Option c;
        private Option d;

        public Option getA() {
            return a;
        }

        public void setA(Option a) {
            this.a = a;
        }

        public Option getB() {
            return b;
        }

        public void setB(Option b) {
            this.b = b;
        }

        public Option getC() {
            return c;
        }

        public void setC(Option c) {
            this.c = c;
        }

        public Option getD() {
            return d;
        }

        public void setD(Option d) {
            this.d = d;
        }
    }

    // Inner class representing an option
    public static class Option {
        private boolean isCorrect;
        private String text;

        public boolean isCorrect() {
            return isCorrect;
        }

        public void setCorrect(boolean correct) {
            isCorrect = correct;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
