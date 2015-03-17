package com.bigcustard.scene2dplus.textarea.command;

import com.badlogic.gdx.utils.Clipboard;

public class TestClipboard implements Clipboard {
    private String content;

    @Override
    public String getContents() {
        return content;
    }

    @Override
    public void setContents(String content) {
        this.content = content;
    }
}
