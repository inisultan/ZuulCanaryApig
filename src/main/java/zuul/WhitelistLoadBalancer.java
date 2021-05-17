package zuul;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

public class WhitelistLoadBalancer extends AbstractLoadBalancerRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistLoadBalancer.class);

	@Override
	public Server choose(Object key) {
		LOGGER.info("Load balancer key : {}", key);
		return choose(getLoadBalancer(), key);

	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		// TODO Auto-generated method stub

	}

	public Server choose(ILoadBalancer lb, Object key) {
		if (lb == null) {
			LOGGER.warn("no load balancer");
			return null;
		}

		Server server = null;
		int count = 0;
		while (server == null && count++ < 10) {
			List<Server> reachableServers = lb.getReachableServers();
			List<Server> allServers = lb.getAllServers();
			int upCount = reachableServers.size();
			int serverCount = allServers.size();

			if ((upCount == 0) || (serverCount == 0)) {
				LOGGER.warn("No up servers available from load balancer: " + lb);
				return null;
			}
			
			server = allServers.stream()
					.filter(s -> String.valueOf(s.getPort()).equals(String.valueOf(key)))
					.findFirst()
					.get();
			
			if (server.isAlive() && (server.isReadyToServe())) {
                return server;
            }
			
			// Next.
			server = null;
		}

		if (count >= 10) {
			LOGGER.warn("No available alive servers after 10 tries from load balancer: "
					+ lb);
		}
		return server;
	}

}
