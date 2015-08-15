package com.bigcustard.scene2dplus.dialog;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.bigcustard.scene2dplus.Spacer;

public class FileDialog extends Dialog {
    private final String title;
    private final Skin skin;
    private boolean fileNameEnabled;
    private final TextField fileNameInput;
    private final Label fileNameLabel;
    private final FileHandle baseDir;
    private final Label fileListLabel;
    private final List<FileListItem> fileList;

    private FileHandle currentDir;
    protected String result;

    protected ResultListener resultListener;

    private final TextButton ok;
    private final TextButton cancel;

    private static final Comparator<FileListItem> dirListComparator = new Comparator<FileListItem>() {
        @Override
        public int compare(FileListItem file1, FileListItem file2) {
            if (file1.file.isDirectory() && !file2.file.isDirectory()) {
                return -1;
            }
            if (file1.file.isDirectory() && file2.file.isDirectory()) {
                return 0;
            }
            if (!file1.file.isDirectory() && !file2.file.isDirectory()) {
                return 0;
            }
            return 1;
        }
    };

    private FileFilter filter = file -> !file.getName().startsWith(".");

    public FileDialog(String title, final Skin skin, FileHandle baseDir) {
        super("", skin);
        this.title = title;
        this.skin = skin;
        this.baseDir = baseDir;

        final Table content = getContentTable();
        content.padTop(30).padLeft(40).padRight(40).padBottom(0);
        content.top().left();

        fileListLabel = new Label("", skin);
        fileListLabel.setAlignment(Align.left);
        fileListLabel.setWrap(true);

        fileList = new List<>(skin);
        fileList.getSelection().setProgrammaticChangeEvents(false);

        fileNameInput = new TextField("", skin);
        fileNameLabel = new Label("File name:", skin);
        fileNameInput.setTextFieldListener((textField, c) -> result = textField.getText());

        getButtonTable().pad(25);

        ok = new TextButton("Ok", skin);
        button(ok, true);

        getButtonTable().add(new Spacer(20));

        cancel = new TextButton("Cancel", skin);
        button(cancel, false);
        key(Keys.ENTER, true);
        key(Keys.ESCAPE, false);


        fileList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final FileListItem selected = fileList.getSelected();
                if (!selected.file.isDirectory()) {
                    result = selected.file.name();
                    fileNameInput.setText(result);
                }
            }
        });
    }

    private void changeDirectory(FileHandle directory) {
        currentDir = directory;
        fileListLabel.setText(currentDir.path());

        final Array<FileListItem> items = new Array<>();

        final FileHandle[] list = directory.list(filter);
        for (final FileHandle handle : list) {
            items.add(new FileListItem(handle));
        }

        items.sort(dirListComparator);

        if (directory.file().getParentFile() != null) {
            items.insert(0, new FileListItem("..", directory.parent()));
        }

        fileList.setSelected(null);
        fileList.setItems(items);
        fileList.getStyle().background = skin.getDrawable("solarizedSelection");
    }

    public FileHandle getResult() {
        String path = currentDir.path() + "/";
        if (result != null && result.length() > 0) {
            path += result;
        }
        return new FileHandle(path);
    }

    public FileDialog setFilter(FileFilter filter) {
        this.filter = filter;
        return this;
    }

    public FileDialog setOkButtonText(String text) {
        this.ok.setText(text);
        return this;
    }


    public FileDialog setCancelButtonText(String text) {
        this.cancel.setText(text);
        return this;
    }

    public FileDialog setFileNameEnabled(boolean fileNameEnabled) {
        this.fileNameEnabled = fileNameEnabled;
        return this;
    }

    public FileDialog setResultListener(ResultListener result) {
        this.resultListener = result;
        return this;
    }

    @Override
    public Dialog show(Stage stage, Action action) {
        final Table content = getContentTable();
        text(title);
        content.row();
        content.add(fileListLabel).top().left().expandX().fillX().row();
        ScrollPane scrollPane = new ScrollPane(fileList, skin);
        scrollPane.setFadeScrollBars(false);
        content.add(scrollPane).size(360, 350).fill().expand().row();

        if (fileNameEnabled) {
            fileNameLabel.setWrap(true);
            content.add(fileNameLabel).fillX().expandX().row();
            content.add(fileNameInput).fillX().expandX().row();
            stage.setKeyboardFocus(fileNameInput);
        }

        fileList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final FileListItem selected = fileList.getSelected();
                if (selected.file.isDirectory()) {
                    changeDirectory(selected.file);
                }
            }
        });

        changeDirectory(baseDir);
        return super.show(stage, action);
    }

    public static FileDialog createSaveDialog(String title, final Skin skin, final FileHandle path) {
        return new FileDialog(title, skin, path) {
            @Override
            protected void result(Object object) {
                if (resultListener == null) return;
                final boolean success = (Boolean) object;
                if (!resultListener.result(success, getResult())) {
                    this.cancel();
                }
            }
        }.setFileNameEnabled(true).setOkButtonText("Save");

    }

    public static FileDialog createLoadDialog(String title, final Skin skin, final FileHandle path) {
        return new FileDialog(title, skin, path) {
            @Override
            protected void result(Object object) {
                if (resultListener == null) return;
                final boolean success = (Boolean) object;
                resultListener.result(success, getResult());
            }
        }.setFileNameEnabled(false).setOkButtonText("Load");

    }

    public static FileDialog createPickDialog(String title, final Skin skin, final FileHandle path) {
        return new FileDialog(title, skin, path) {
            @Override
            protected void result(Object object) {
                if (resultListener == null) return;
                final boolean success = (Boolean) object;
                resultListener.result(success, getResult());
            }
        }.setOkButtonText("Select");
    }

    public interface ResultListener {
        boolean result(boolean success, FileHandle result);
    }

    private static class FileListItem {
        private final String label;
        public final FileHandle file;

        public FileListItem(String label, FileHandle file) {
            this.label = label;
            this.file = file;
        }

        public FileListItem(FileHandle file) {
            this(file.name(), file);
        }

        @Override
        public String toString() {
            return label;
        }
    }
}