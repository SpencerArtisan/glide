package com.bigcustard.glide.help;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.bigcustard.glide.code.Game;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Help {
    private static String EXAMPLES_FOLDER = "examples";

    public List<HelpTopic> topics() {
        List<FileHandle> examples = Arrays.asList(allExamples(examplesFolder()));
        return examples.stream().map(this::toTopic).collect(Collectors.toList());
    }

    private HelpTopic toTopic(FileHandle helpFile) {
        return new HelpTopic(helpFile.name().substring(0, helpFile.name().indexOf(".")), helpFile.readString());
    }

    private FileHandle[] allExamples(FileHandle parentFolder) {
        return parentFolder.list(file -> !file.isDirectory() && !file.getName().startsWith("."));
    }

    protected FileHandle examplesFolder() {
        return Gdx.files.internal(EXAMPLES_FOLDER);
    }

}
