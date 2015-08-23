package com.bigcustard.scene2dplus.dialog;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.Spacer;

import java.io.FileFilter;
import java.util.Comparator;

public class FileDialog extends Dialog implements Disposable {
    private final String title;
    private final Skin skin;
    private final FileHandle baseDir;
    private final Label fileListLabel;
    private final List<FileListItem> fileList;
    protected FileHandle result;
    protected ResultListener resultListener;
    private final TextButton ok;

    private static final Comparator<FileListItem> dirListComparator = (file1, file2) -> {
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

        getButtonTable().pad(25);

        ok = new TextButton("Ok", skin);
        button(ok, true);

        getButtonTable().add(new Spacer(20));

        TextButton cancel = new TextButton("Cancel", skin);
        button(cancel, false);
        key(Keys.ENTER, true);
        key(Keys.ESCAPE, false);


        fileList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final FileListItem selected = fileList.getSelected();
                if (!selected.file.isDirectory()) {
                    result = selected.file;
                }
            }
        });
    }

    private void changeDirectory(FileHandle currentDir) {
        fileListLabel.setText(currentDir.path());
        final Array<FileListItem> items = new Array<>();
        final FileHandle[] list = currentDir.list(filter);
        for (final FileHandle handle : list) {
            items.add(new FileListItem(handle));
        }

        items.sort(dirListComparator);

        if (currentDir.file().getParentFile() != null) {
            items.insert(0, new FileListItem("..", currentDir.parent()));
        }

        fileList.setSelected(null);
        fileList.setItems(items);
        fileList.getStyle().background = skin.getDrawable("solarizedSelection");
    }

    public FileDialog setFilter(FileFilter filter) {
        this.filter = filter;
        return this;
    }

    public FileDialog setOkButtonText(String text) {
        this.ok.setText(text);
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
    public static FileDialog createLoadDialog(String title, final Skin skin, final FileHandle path) {
        return new FileDialog(title, skin, path) {
            @Override
            protected void result(Object object) {
                if (resultListener == null) return;
                final boolean success = (Boolean) object;
                resultListener.result(success, result);
            }
        }.setOkButtonText("Load");
    }

    @Override
    public void dispose() {
        fileList.clearListeners();
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