package com.nixend.manny.admin.controller;

import com.nixend.manny.admin.model.dto.TagRouteDTO;
import com.nixend.manny.admin.service.RouteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author panyox
 */
@RestController
@RequestMapping("tag")
public class TagRouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("create")
    public String createRule(@RequestBody TagRouteDTO routeDTO) {
        String app = routeDTO.getApplication();
        if (StringUtils.isEmpty(app)) {
            throw new IllegalArgumentException("app is Empty!");
        }
        routeService.createTagRoute(routeDTO);
        return "success";
    }
}
