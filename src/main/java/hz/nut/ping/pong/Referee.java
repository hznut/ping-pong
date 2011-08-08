package hz.nut.ping.pong;

import hz.nut.ping.pong.Game.TableSide;

import java.util.HashMap;

public class Referee {
    private HashMap<TableSide, Player> playerMap = new HashMap<TableSide, Player>();
    private HashMap<TableSide, Thread> threadMap = new HashMap<TableSide, Thread>();
    private final Ball ball;

    public Referee(Ball ball) {
        this.ball = ball;
    }

    public void setLeft(Player leftPlayer, Thread leftPlayerThread) {
        assert (TableSide.LEFT == leftPlayer.getTableSide());
        playerMap.put(TableSide.LEFT, leftPlayer);
        threadMap.put(TableSide.LEFT, leftPlayerThread);
    }

    public void setRight(Player rightPlayer, Thread rightPlayerThread) {
        assert (TableSide.RIGHT == rightPlayer.getTableSide());
        playerMap.put(TableSide.RIGHT, rightPlayer);
        threadMap.put(TableSide.RIGHT, rightPlayerThread);
    }

    public void notifyLostRally(Player rallyLosingPlayer) {
        Player rallyWinningPlayer = rallyLosingPlayer.getOpponent();
        rallyWinningPlayer.incrementScore();
        declareScore();
        if (rallyWinningPlayer.getScore() == 11) {
            stopPlayers();
            String s = String.format("Player %d Wins!!",
                    rallyWinningPlayer.getId());
            System.out.println("***********************");
            System.out.println(s);
            System.out.println("***********************");
        } else {
            // rallyLosingPlayer.releaseBall();
            // rallyWinningPlayer.serve();
            ball.resetHitCount();
        }
    }

    public void declareScore() {
        Player left = playerMap.get(TableSide.LEFT);
        Player right = playerMap.get(TableSide.RIGHT);
        String score = String.format(
                "Left Player(%d) : %d\tRight Player(%d) : %d", left.getId(),
                left.getScore(), right.getId(), right.getScore());
        System.out.println("-------------------------------------------");
        System.out.println(score);
        System.out.println("-------------------------------------------");
    }

    public boolean ready() {
        return (playerMap.size() == 2 && threadMap.size() == 2);
    }

    public void startGame() throws InterruptedException {
        TableSide tossWinSide = toss();
        Player servingPlayer = playerMap.get(tossWinSide);
        Player otherPlayer = servingPlayer.getOpponent();
        servingPlayer.introduce();
        otherPlayer.introduce();
        // otherPlayer.releaseBall();
        // servingPlayer.serve();
        System.out.println("Player " + servingPlayer.getId() + " wins toss.");
        threadMap.get(tossWinSide).start();
        Thread.sleep(200);
        threadMap.get(tossWinSide.oppSide()).start();
    }

    private TableSide toss() {
        return ((int) (Math.random() * 10) % 2 == 1) ? TableSide.LEFT
                : TableSide.RIGHT;
    }

    private void stopPlayers() {
        threadMap.get(TableSide.LEFT).interrupt();
        threadMap.get(TableSide.RIGHT).interrupt();
    }
}
