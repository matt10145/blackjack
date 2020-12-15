import java.util.*;

public class Player {

    private final List<Card> hand;
    private int chips;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public void takeCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getChips() { return chips; }
    public void setChips(int chips) { this.chips = chips; }
    public void addChips(int chips) { this.chips += chips; }
    public void removeChips(int chips) { this.chips -= chips; }

    public int getHandValue() {
        int sum = 0;
        for (Card card : hand) {
            int prev = sum;
            sum += Card.getRankValue(card.getRank(), prev);
        }

        return sum;
    }

    public int getHandValue(boolean isSoft) {
        int sum = 0;
        for (Card card : hand) {
            int prev = sum;
            sum += Card.getRankValue(card.getRank(), prev);
        }

        if (sum > 21 && hasAce()) {
            return sum - 10;
        }

        return sum;
    }

    public boolean hasAce() {
        for (Card card : hand) {
            if (card.getRank().equals("A")) {
                return true;
            }
        }

        return false;
    }
}
