package com.bigcustard.glide.help;

public class HelpTopic {
    private String text;
    private String exampleCode;

    public HelpTopic(String text, String exampleCode) {
        this.text = text;
        this.exampleCode = exampleCode;
    }

    public String getText() {
        return text;
    }

    public String getExampleCode() {
        return exampleCode;
    }
}
