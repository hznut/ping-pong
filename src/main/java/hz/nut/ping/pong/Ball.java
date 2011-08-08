package hz.nut.ping.pong;

public class Ball {
    private int hitCount = 0;

    public void hit() {
        hitCount++;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void resetHitCount() {
        hitCount = 0;
    }

}
