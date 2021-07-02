package com.nixend.manny.rpc.dubbo.strategy;

import com.nixend.manny.common.model.DubboTag;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author panyox
 */
public class TagStrategy {

    private ConcurrentMap<String, List<DubboTag>> tagMap = new ConcurrentHashMap<>();

    private static volatile TagStrategy instance;

    public static TagStrategy getInstance() {
        if (instance == null) {
            synchronized (TagStrategy.class) {
                if (instance == null) {
                    instance = new TagStrategy();
                }
            }
        }
        return instance;
    }

    public TagStrategy() {

    }

    public void addTags(String key, List<DubboTag> tags) {
        tagMap.put(key, tags);
    }

    public void addTag(DubboTag tag) {
        if (tag == null) {
            return;
        }
        if (tagMap.containsKey(tag.getApplication())) {
            List<DubboTag> tagList = tagMap.get(tag.getApplication());
            List<DubboTag> tags = tagList.stream().filter(dt -> !dt.getName().equals(tag.getName())).collect(Collectors.toList());
            tags.add(tag);
            tagMap.put(tag.getApplication(), tags);
        } else {
            tagMap.put(tag.getApplication(), Arrays.asList(tag));
        }
    }

    public List<DubboTag> getTag(String application) {
        return tagMap.get(application);
    }

    /**
     * @param application
     * @return
     */
    public DubboTag matchBestTag(String application) {
        List<DubboTag> tags = getTag(application);
        System.out.println(tags);
        if (tags == null || tags.size() == 0) {
            return null;
        }
        int length = tags.size();
        if (length == 1) {
            return tags.get(0);
        }
        int totalWeight = 0;
        for (DubboTag tag : tags) {
            totalWeight += tag.getWeight();
            int cw = tag.getCurrentWeight() + tag.getWeight();
            tag.setCurrentWeight(cw);
        }
        DubboTag selectedTag = tags.get(0);
        for (DubboTag tag : tags) {
            if (tag.getCurrentWeight() > selectedTag.getCurrentWeight()) {
                selectedTag = tag;
            }
        }
        int scw = selectedTag.getCurrentWeight() - totalWeight;
        selectedTag.setCurrentWeight(scw);
        tagMap.put(application, tags);
        return selectedTag;
    }
}
