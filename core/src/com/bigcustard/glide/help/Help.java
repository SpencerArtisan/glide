package com.bigcustard.glide.help;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Help {
    public List<HelpTopic> topics() {
        HelpTopic topic1 = new HelpTopic("Show sprite", "some code");
        HelpTopic topic2 = new HelpTopic("Position sprite", "some more code");
        return ImmutableList.of(topic1, topic2);
    }
}
