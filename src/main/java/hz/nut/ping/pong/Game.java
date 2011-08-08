package hz.nut.ping.pong;


public class Game {
    public enum TableSide {
        LEFT, RIGHT;
        TableSide oppSide() {
            return (this == LEFT) ? RIGHT : LEFT;
        }
    }

    public static void main(String args[]) throws InterruptedException {
        Ball ball = new Ball();
        Referee referee = new Referee(ball);
        Player p1 = new PlayerImpl.PlayerBuilder().ball(ball).referee(referee)
                .side(TableSide.LEFT).build();
        Player p2 = new PlayerImpl.PlayerBuilder().ball(ball).referee(referee)
                .side(TableSide.RIGHT).opponent(p1).build();
        p1.setOpponent(p2);
        Thread leftPlayer = new Thread(p1);
        Thread rightPlayer = new Thread(p2);
        referee.setLeft(p1, leftPlayer);
        referee.setRight(p2, rightPlayer);
        referee.startGame();
    }

}
