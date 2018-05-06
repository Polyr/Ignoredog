package io.github.synchronousx.ignoredog.utils.request;

public class RequestCooldown {
    private static final long COOLDOWN_MILLIS = 600000L; // 10 minutes
    private final long startTime;

    public RequestCooldown() {
        this.startTime = System.currentTimeMillis();
    }

    public long getRemainingMillis() {
        final long remainingMillis = this.startTime + RequestCooldown.COOLDOWN_MILLIS - System.currentTimeMillis();
        return remainingMillis >= 0L ? remainingMillis : 0L;
    }

    public boolean hasEnded() {
        return this.getRemainingMillis() == 0L;
    }
}
