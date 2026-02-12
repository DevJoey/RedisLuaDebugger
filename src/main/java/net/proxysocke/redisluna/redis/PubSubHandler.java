package net.proxysocke.redisluna.redis;

import net.proxysocke.redisluna.App;

import redis.clients.jedis.JedisPubSub;

import java.util.logging.Level;

public final class PubSubHandler {

    private final App app;

    private PubSubRunnable pubSubRunnable;

    public PubSubHandler(App app) {
        this.app = app;
    }

    public void start() {
        pubSubRunnable = new PubSubRunnable(app);
        Thread subscriberThread = new Thread(pubSubRunnable);
        subscriberThread.setName("RedisLunaSubscriber");
        subscriberThread.start();
    }

    public void shutdown() {
       pubSubRunnable.cancel();
    }

    private class PubSubRunnable implements Runnable {

        private final App app;

        private boolean cancelled = false;
        private JedisPubSub pubSub;

        public PubSubRunnable(App app) {
            this.app = app;
            setupPubSub();
        }

        private void setupPubSub() {
            pubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    app.getLogger().log(Level.INFO, String.format("(RedisScript)-> %s", message));
                }
            };
        }

        @Override
        public void run() {
            if (cancelled) return;
            try {
                app.getRedisProvider().getRedisClient().subscribe(pubSub, "rl:logs");
            } catch (Exception e) {
                app.getLogger().log(Level.SEVERE, "Failed to setup subscriber.", e);
            }
        }

        public void cancel() {
            pubSub.unsubscribe();
            cancelled = true;
        }
    }
}