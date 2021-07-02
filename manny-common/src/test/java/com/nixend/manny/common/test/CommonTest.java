package com.nixend.manny.common.test;

import java.util.Arrays;
import java.util.List;

/**
 * @author panyox
 */
public class CommonTest {

    public static void main(String[] args) {
        List<Server> servers = Arrays.asList(new Server("a", 4, 0), new Server("b", 2, 0), new Server("c", 1, 0), new Server("d", 0, 0));
        for (int i = 0; i < 10; i++) {
            Server server = getServer(servers);
            System.out.println("server: " + server.getName());
        }
    }

    private static Server getServer(List<Server> servers) {
        int totalWeight = 0;
        for (Server server : servers) {
            totalWeight += server.getWeight();
            int cw = server.getCurrentWeight() + server.getWeight();
            server.setCurrentWeight(cw);
        }
        Server selectServer = servers.get(0);
        for (Server server : servers) {
            if (server.getCurrentWeight() > selectServer.getCurrentWeight()) {
                selectServer = server;
            }
        }
        int scw = selectServer.getCurrentWeight() - totalWeight;
        selectServer.setCurrentWeight(scw);
        return selectServer;
    }
}
