package hz.nut.ping.pong;

import hz.nut.ping.pong.Game.TableSide;

public interface Player extends Runnable {

    TableSide getTableSide();

    void hit() throws InterruptedException;

    int getScore();

    void incrementScore();

    Player getOpponent();

    void setOpponent(Player opponent);

    int getId();

    void stop();

    void serve() throws InterruptedException;

    void releaseBall();

    void introduce();

}
