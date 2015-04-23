package com.bigcustard.planet.code.command;

import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.planet.code.Game;
import com.bigcustard.planet.code.Syntax;
import com.bigcustard.planet.plugin.Plugin;
import com.bigcustard.scene2dplus.image.ImageAreaModel;
import com.bigcustard.scene2dplus.image.ImageModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class RunCommandTest {
    private RunCommand command;
    @Mock private Plugin languagePlugin;
    @Mock private Syntax syntax;
    @Mock private FileHandle gameFolder;
    @Mock private FileHandle buildFolder;
    @Mock private ImageAreaModel imageAreaModel;
    @Mock private ImageModel imageModel;
    @Mock private Game game;
    @Mock private Consumer<Game> runGame;

    @Before
    public void before() {
        initMocks(this);
        when(game.imageModel()).thenReturn(imageAreaModel);
        when(game.folder()).thenReturn(gameFolder);
        when(gameFolder.child("build")).thenReturn(buildFolder);
        command = spy(new RunCommand(game, runGame));
        doNothing().when(command).resize(any(ImageModel.class));
    }

    @Test
    public void createsBuildFolder() {
        command.execute();
        verify(buildFolder).mkdirs();
    }

    @Test
    public void resizesImages() {
        when(imageAreaModel.images()).thenReturn(Arrays.asList(imageModel));
        command.execute();
        verify(command).resize(imageModel);
    }

    @Test
    public void cannotExecuteWhenGameInvalid() {
        when(game.isValid()).thenReturn(false);
        assertThat(command.canExecute()).isFalse();
    }

    @Test
    public void canExecuteWhenGameValid() {
        when(game.isValid()).thenReturn(true);
        assertThat(command.canExecute()).isTrue();
    }

    @Test
    public void execute() {
        command.execute();
        verify(runGame).accept(game);
    }
}