package com.bigcustard.scene2dplus.dialog;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.Spacer;
import com.bigcustard.scene2dplus.button.TextButtonPlus;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import static com.bigcustard.scene2dplus.button.ErrorHandler.onChanged;
import static com.bigcustard.scene2dplus.button.ErrorHandler.onDoubleClick;
import static com.bigcustard.scene2dplus.button.ErrorHandler.tryAndRecover;

public class FileDialog extends Dialog implements Disposable {
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
    private final String title;
    private final Skin skin;
    private final FileHandle baseDir;
    private boolean directorySelector;
    private final Label fileListLabel;
    private final List<FileListItem> fileList;
    private final TextButton ok;
    protected FileHandle result;
    protected ResultListener resultListener;
    private FileFilter filter = FileDialog::isNonSystemFile;

    public FileDialog(String title, final Skin skin, FileHandle baseDir, boolean directorySelector) {
        super("", skin);
        this.title = title;
        this.skin = skin;
        this.baseDir = baseDir;
        this.directorySelector = directorySelector;

        final Table content = getContentTable();
        content.padTop(30).padLeft(40).padRight(40).padBottom(0);
        content.top().left();

        fileListLabel = new Label("", skin);
        fileListLabel.setAlignment(Align.left);
        fileListLabel.setWrap(true);

        fileList = new List<>(skin);
        fileList.getSelection().setProgrammaticChangeEvents(false);

        getButtonTable().pad(25);

        ok = new TextButtonPlus("Ok", skin);
        button(ok, true);

        getButtonTable().add(new Spacer(20));

        TextButton cancel = new TextButtonPlus("Cancel", skin);
        button(cancel, false);
        key(Keys.ENTER, true);
        key(Keys.ESCAPE, false);

        onChanged(fileList, getSkin(), () -> {
                    final FileListItem selected = fileList.getSelected();
                    if (selected.file.isDirectory() == directorySelector) {
                        result = selected.file;
                    }
                }
        );
    }

    public static FileDialog createFileLoadDialog(String title, final Skin skin, final FileHandle path) {
        return createDialog(title, skin, path, false, "Load");
    }

    public static FileDialog createDirectoryDialog(String title, final Skin skin, final FileHandle path, String okButtonText) {
        return createDialog(title, skin, path, true, okButtonText)
                .setFilter(file -> isNonSystemFile(file) && file.isDirectory());
    }

    private static FileDialog createDialog(final String title, final Skin skin, final FileHandle path, boolean directorySelector, String okButtonText) {
        return new FileDialog(title, skin, path, directorySelector) {
            @Override
            protected void result(Object object) {
                tryAndRecover(getStage(), getSkin(), () -> {
                    if (resultListener == null) return;
                    final boolean success = (Boolean) object;
                    resultListener.result(success, result);
                });
            }
        }.setOkButtonText(okButtonText);
    }

    private void changeDirectory(FileHandle currentDir) {
        fileListLabel.setText(currentDir.name());
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

        onDoubleClick(fileList, getSkin(), () -> {
                    final FileListItem selected = fileList.getSelected();
                    if (selected.file.isDirectory()) {
                        changeDirectory(selected.file);
                    }
                }
        );

        changeDirectory(baseDir);
        return super.show(stage, action);
    }

    @Override
    public void dispose() {
        fileList.clearListeners();
    }

    private static boolean isNonSystemFile(File file) {
        return !file.getName().startsWith(".");
    }

    public interface ResultListener {
        boolean result(boolean success, FileHandle result);
    }

    private static class FileListItem {
        public final FileHandle file;
        private final String label;

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