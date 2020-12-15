public class Card {

    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }
    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public boolean equals(Card c) {
        if (c == null) return false;
        return this.rank.equals(c.rank) && this.suit.equals(c.suit);
    }

    public static int getRankValue(String rank, int currentHand) {
        try {
            return Integer.parseInt(rank);
        } catch (NumberFormatException e) {
            switch (rank) {
                case "T":
                case "J":
                case "Q":
                case "K":
                    return 10;
                case "A":
                    return (currentHand + 11) > 21 ? 1 : 11;
            }
        }

        return 0;
    }
}