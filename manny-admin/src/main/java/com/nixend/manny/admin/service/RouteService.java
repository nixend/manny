package com.nixend.manny.admin.service;

import com.nixend.manny.admin.model.dto.TagRouteDTO;

/**
 * @author panyox
 */
public interface RouteService {

    void createTagRoute(TagRouteDTO tagRoute);

    void updateTagRoute(TagRouteDTO tagRoute);

    void deleteTagRoute(String id);


    void enableTagRoute(String id);


    void disableTagRoute(String id);
    
    TagRouteDTO findTagRoute(String id);
}
