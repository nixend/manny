package com.nixend.manny.rpc.dubbo.listener;

import com.nixend.manny.common.model.DubboTag;
import com.nixend.manny.common.model.WeightTag;
import com.nixend.manny.common.model.WeightTagRoute;
import com.nixend.manny.configcenter.api.DataEvent;
import com.nixend.manny.configcenter.api.notify.TagRouteNotify;
import com.nixend.manny.rpc.dubbo.strategy.TagStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author panyox
 */
@Slf4j
public class DubboTagRouteNotify implements TagRouteNotify {

    @Override
    public void onSubscribe(WeightTagRoute tagRoute, DataEvent event) {
        log.info("tag route: {} evt: {}", tagRoute, event);
        if (tagRoute != null) {
            List<DubboTag> tagList = new ArrayList<>();
            for (WeightTag wt : tagRoute.getTags()) {
                DubboTag dt = new DubboTag();
                dt.setApplication(tagRoute.getApplication());
                dt.setName(wt.getName());
                dt.setWeight(wt.getWeight());
                dt.setCurrentWeight(0);
                log.info("dt: {}", dt);
                tagList.add(dt);
            }
            log.info("add routes {}", tagList);
            TagStrategy.getInstance().addTags(tagRoute.getApplication(), tagList);
        }
    }
}
