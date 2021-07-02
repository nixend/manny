package com.nixend.manny.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.nixend.manny.admin.model.dto.TagDTO;
import com.nixend.manny.admin.model.dto.TagRouteDTO;
import com.nixend.manny.admin.model.store.TagRoute;
import com.nixend.manny.admin.registry.RegistryService;
import com.nixend.manny.admin.service.RouteService;
import com.nixend.manny.admin.utils.Constants;
import com.nixend.manny.admin.utils.ConvertUtil;
import com.nixend.manny.admin.utils.RouteUtils;
import com.nixend.manny.admin.utils.YamlParser;
import com.nixend.manny.common.model.WeightTag;
import com.nixend.manny.common.model.WeightTagRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author panyox
 */
@Component
public class RouteServiceImpl implements RouteService {

    private String prefix = Constants.CONFIG_KEY;

    @Autowired
    private RegistryService dubboRegistryService;

    @Autowired
    private RegistryService adminRegistryService;

    @Override
    public void createTagRoute(TagRouteDTO tagRoute) {
        String id = ConvertUtil.getIdFromDTO(tagRoute);
        String path = getPath(id, Constants.TAG_ROUTE);
        TagRoute store = RouteUtils.convertTagroutetoStore(tagRoute);
        System.out.println(id);
        System.out.println(path);
        System.out.println(store);
        dubboRegistryService.setConfig(path, YamlParser.dumpObject(store));

        WeightTagRoute wtr = new WeightTagRoute();
        wtr.setApplication(tagRoute.getApplication());
        wtr.setEnable(tagRoute.isEnabled());
        List<WeightTag> tags = new ArrayList<>();
        for (TagDTO td : tagRoute.getTags()) {
            WeightTag wt = new WeightTag();
            wt.setName(td.getName());
            wt.setWeight(td.getWeight());
            tags.add(wt);
        }
        wtr.setTags(tags);
        String path1 = getTagPath(tagRoute.getApplication());
        System.out.println(wtr);
        System.out.println(path1);
        adminRegistryService.setConfig(path1, JSON.toJSONString(wtr));
    }

    @Override
    public void updateTagRoute(TagRouteDTO tagRoute) {
        String id = ConvertUtil.getIdFromDTO(tagRoute);
        String path = getPath(id, Constants.TAG_ROUTE);
        if (dubboRegistryService.getConfig(path) == null) {
            throw new IllegalArgumentException("can not find tagroute: " + id);
            //throw exception
        }
        TagRoute store = RouteUtils.convertTagroutetoStore(tagRoute);
        dubboRegistryService.setConfig(path, YamlParser.dumpObject(store));

    }

    @Override
    public void deleteTagRoute(String id) {
        String path = getPath(id, Constants.TAG_ROUTE);
        dubboRegistryService.deleteConfig(path);
    }

    @Override
    public void enableTagRoute(String id) {
        String path = getPath(id, Constants.TAG_ROUTE);
        String config = dubboRegistryService.getConfig(path);
        if (config != null) {
            TagRoute tagRoute = YamlParser.loadObject(config, TagRoute.class);
            tagRoute.setEnabled(true);
            dubboRegistryService.setConfig(path, YamlParser.dumpObject(tagRoute));
        }

    }

    @Override
    public void disableTagRoute(String id) {
        String path = getPath(id, Constants.TAG_ROUTE);
        String config = dubboRegistryService.getConfig(path);
        if (config != null) {
            TagRoute tagRoute = YamlParser.loadObject(config, TagRoute.class);
            tagRoute.setEnabled(false);
            dubboRegistryService.setConfig(path, YamlParser.dumpObject(tagRoute));
        }

    }

    @Override
    public TagRouteDTO findTagRoute(String id) {
        String path = getPath(id, Constants.TAG_ROUTE);
        String config = dubboRegistryService.getConfig(path);
        if (config != null) {
            TagRoute tagRoute = YamlParser.loadObject(config, TagRoute.class);
            return RouteUtils.convertTagroutetoDisplay(tagRoute);
        }
        return null;
    }

    private String getPath(String key, String type) {
        key = key.replace("/", "*");
        if (type.equals(Constants.CONDITION_ROUTE)) {
            return prefix + Constants.PATH_SEPARATOR + key + Constants.CONDITION_RULE_SUFFIX;
        } else {
            return prefix + Constants.PATH_SEPARATOR + key + Constants.TAG_RULE_SUFFIX;
        }
    }

    /**
     * /manny/dubbo/tags/demo-provider.tag-router
     *
     * @return
     */
    private String getTagPath(String key) {
        return "dubbo/tags/" + key + ".tag-router";
    }
}
