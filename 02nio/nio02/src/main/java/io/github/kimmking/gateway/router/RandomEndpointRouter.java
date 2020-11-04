package io.github.kimmking.gateway.router;

import java.util.List;
import java.util.Random;

import org.springframework.util.CollectionUtils;

/**
 * 随机路由
 * Date: 2020-11-04
 *
 * @author chenxi
 */
public class RandomEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> endpoints) {
        if (CollectionUtils.isEmpty(endpoints)) {
            return "";
        }
        int size = endpoints.size();
        int random = new Random().nextInt(size);
        int index = random % size;

        String endpoint = endpoints.get(index);
        System.out.println("随机路由，index:" + index + " endpoint:" + endpoint);
        return endpoint;
    }
}
