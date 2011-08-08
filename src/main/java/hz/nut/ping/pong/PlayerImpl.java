package hz.nut.ping.pong;

import hz.nut.ping.pong.Game.TableSide;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerImpl implements Player {
    private Ball ball;
    private static AtomicInteger instances = new AtomicInteger();
    private final int id;
    private int score = 0;
    private Player opponent;
    private Referee referee;
    public TableSide tableSide;

    private PlayerImpl() {
        this.id = instances.getAndIncrement() + 10;
    }

    public static class PlayerBuilder {
        private Ball ball;
        private Player opponent;
        private Referee referee;
        public TableSide tableSide;

        public PlayerBuilder ball(Ball ball) {
            this.ball = ball;
            return this;
        }

        public PlayerBuilder opponent(Player opponent) {
            this.opponent = opponent;
            return this;
        }

        public PlayerBuilder side(TableSide tableSide) {
            this.tableSide = tableSide;
            return this;
        }

        public PlayerBuilder referee(Referee referee) {
            this.referee = referee;
            return this;
        }

        public Player build() {
            if (ball == null) {
                throw new IllegalStateException("Ball not set.");
            }
            if (referee == null) {
                throw new IllegalStateException("Referee not set.");
            }
            if (tableSide == null) {
                throw new IllegalStateException("Table Side not set.");
            }
            PlayerImpl player = new PlayerImpl();
            player.ball = this.ball;
            player.opponent = this.opponent;
            player.referee = this.referee;
            player.tableSide = this.tableSide;
            return player;
        }
    }

    public int getId() {
        return id;
    }

    public void hit() throws InterruptedException {
        if (tableSide == TableSide.LEFT) {
            System.out.println("\t--->");
        } else {
            System.out.println("\t<---");
        }
        ball.hit();
        Thread.sleep(500);
        ball.notify();
        ball.wait();
    }

    public void run() {
        if (opponent == null) {
            throw new IllegalStateException("No opponent set for player " + id);
        }

        while (true) {
            try {
                synchronized (ball) {
                    if (ball.getHitCount() == 0) {
                        serve();
                    }

                    int nonce = (int) (Math.random() * 10);
                    if (nonce == 3 || nonce == 8) {
                        if (tableSide == TableSide.LEFT) {
                            System.out.println("  X\t\t");
                        } else {
                            System.out.println("\t\t  X");
                        }
                        referee.notifyLostRally(this);
                        ball.notify();
                        ball.wait();
                        continue;
                    }
                    hit();
                }
            } catch (InterruptedException e) {
                System.out.println("Player " + id + " has stopped playing.");
                break;
            }
        }
    }

    public void introduce() {
        System.out.println("Player " + id + " is playing on " + tableSide
                + " of table.");
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public TableSide getTableSide() {
        return tableSide;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        assert (tableSide != opponent.getTableSide());
        this.opponent = opponent;
    }

    public void stop() {

    }

    public void celebrate() {
        if (tableSide == TableSide.LEFT) {
            System.out.println("WIN!WIN!");
        } else {
            System.out.println("\t\tWIN!WIN!");
        }

    }

    public void serve() throws InterruptedException {
        if (tableSide == TableSide.LEFT) {
            System.out.println(" Serves");
        } else {
            System.out.println("\t\tServes");
        }
    }

    public void releaseBall() {

    }
}
