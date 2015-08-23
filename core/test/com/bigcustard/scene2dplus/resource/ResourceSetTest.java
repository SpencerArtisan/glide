package com.bigcustard.scene2dplus.resource;

import com.bigcustard.scene2dplus.command.CommandHistory;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ResourceSetTest {
    @Mock private Resource<String> resource;
    @Mock private Resource.Controller controller;
    @Captor private ArgumentCaptor<Runnable> removeButtonWatcher;
    private ResourceSet<String> subject;
    private CommandHistory commandHistory = new CommandHistory();
    private List<Resource<String>> resources = new ArrayList<>();

    @Before
    public void before() {
        initMocks(this);
        when(resource.controller()).thenReturn(controller);
        doNothing().when(controller).watchRemoveButton(removeButtonWatcher.capture());
        subject = new ResourceSet<>(resources, commandHistory);
    }

    @Test
    public void itShould_HaveNoResourcesInitially() {
        assertThat(subject.resources()).isEmpty();
    }

    @Test
    public void itShould_BeConstructableWithResources() {
        resources = ImmutableList.of(resource);
        subject = new ResourceSet<>(resources, commandHistory);
        assertThat(subject.resources()).hasSize(1);
    }

    @Test
    public void itShould_DisposeOfResources() {
        resources = ImmutableList.of(resource);
        subject = new ResourceSet<>(resources, commandHistory);
        subject.dispose();
        verify(resource).dispose();
    }

    @Test
    public void itShould_RemoveAResourceWhenTheRemoveButtonIsClicked() {
        resources = ImmutableList.of(resource);
        subject = new ResourceSet<>(resources, commandHistory);
        removeButtonWatcher.getValue().run();
        assertThat(subject.resources()).hasSize(0);
    }

    @Test
    public void itShould_AddAResource() {
        subject.resources().add(resource);
        assertThat(subject.resources()).hasSize(1);
    }

    @Test
    public void itShould_RemoveAResourceWhenAResourceIsAddedThenTheRemoveButtonIsClicked() {
        subject.resources().add(resource);
        removeButtonWatcher.getValue().run();
        assertThat(subject.resources()).hasSize(0);
    }
}
