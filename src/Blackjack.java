import java.util.*;

public class Blackjack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player, dealer;
    private List<Card> deck;
    private final Scanner sc;
    private boolean hideDealerHand;
    private boolean isSoft;

    public Blackjack() {
        this.player = new Player();
        this.dealer = new Player();

        this.sc = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Type \"buy-in\" followed by the desired amount to buy-in for chips (e.g. \"buy-in 100\").");
        System.out.println("Type \"bet\" followed by the chip wager amount to start a round (e.g. \"bet 25\").");
        System.out.println("Type \"X\" to exit the game.");

        player.setChips(0);
        while (true) {
            String command;
            int amt = -1;
            do {
                try {
                    System.out.print("\n:: ");
                    command = sc.next().toLowerCase();
                    if (command.equals(("x"))) exit();
                    amt = sc.nextInt();
                    if (amt < 0 || (!command.equals("buy-in") && !command.equals("bet"))) {
                        System.out.println("Invalid input. Please enter a command according to the description above.");
                        continue;
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a command according to the description above.");
                }
                sc.nextLine();
            } while (true);

            if (command.toUpperCase().equals("X")) {
                System.out.println("Thanks for playing!");
                break;
            }
            else if (command.equals("bet") && (amt < 1 || amt > 25)) {
                System.out.println("You must wager at least 1 chip, but no more than 25 chips.");
                continue;
            }

            switch (command) {
                case "buy-in":
                    player.addChips(amt);
                    showChips();
                    break;
                case "bet":
                    shuffleAndDeal();
                    if (player.getHandValue() == 21) {
                        end("BLACKJACK", amt);
                        continue;
                    }
                    if (player.hasAce()) isSoft = true;
                    hideDealerHand = true;
                    bet(amt);
                    break;
            }
        }
    }

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck

        while (player.getHand().size() < 2) {
            player.takeCard(deck.remove(0));
            dealer.takeCard(deck.remove(0));
        }
    }

    private void initializeDeck() {
        deck = new ArrayList<>(52);
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private void bet(int amt) {
        System.out.println("Type \"hit\" to draw another card or \"stand\" to pass.");
        while (true) {
            display();
            String choice = sc.next().toLowerCase();
            if (choice.equals("hit")) {
                player.takeCard(deck.remove(0));

                if (player.getHandValue() > 21) {
                    if (player.hasAce() && (player.getHandValue() - 10) < 21) {
                        isSoft = false;
                        continue;
                    } else {
                        hideDealerHand = false;
                        end("PBUST", amt);
                        break;
                    }
                }
                if (player.getHandValue() == 21) {
                    hideDealerHand = false;
                    checkDealer(amt);
                    break;
                }
            }
            else if (choice.equals("stand")) {
                hideDealerHand = false;
                checkDealer(amt);
                break;
            }
        }
    }

    private void checkDealer(int amt) {
        while (dealer.getHandValue() < 17) {
            if (dealer.getHandValue() >= 17) {
                if (player.getHandValue() < dealer.getHandValue()) {
                    end("PBUST", amt);
                    return;
                }
                else if (player.getHandValue() == dealer.getHandValue()) {
                    end("PUSH", amt);
                    return;
                }
                else if (player.getHandValue() > dealer.getHandValue()) {
                    end("WIN", amt);
                    return;
                }
            }
            dealer.takeCard(deck.remove(0));
        }
        if (dealer.getHandValue() > 21) {
            end("DBUST", amt);
        }
        else if (player.getHandValue() < dealer.getHandValue()) {
            end("PBUST", amt);
        }
        else if (player.getHandValue() == dealer.getHandValue()) {
            end("PUSH", amt);
        }
        else if (player.getHandValue() > dealer.getHandValue()) {
            end("WIN", amt);
        }
    }

    private void end(String result, int amt) {
        display();
        switch (result) {
            case "BLACKJACK":
                System.out.println("Blackjack!");
                System.out.println("You received: " + (int) (amt * 1.5) + " chips");
                player.addChips((int) (amt * 1.5));
                break;
            case "WIN":
                System.out.println("You win!");
                System.out.println("You received: " + amt + " chips");
                player.addChips(amt);
                break;
            case "PUSH":
                System.out.println("Push!");
                System.out.println("You got your wager back.");
                break;
            case "PBUST":
                System.out.println("Bust!");
                System.out.println("You lost: " + (amt * -1) + " chips");
                player.removeChips(amt);
                break;
            case "DBUST":
                System.out.println("Dealer bust!");
                System.out.println("You received: " + amt + " chips");
                player.addChips(amt);
                break;
        }
        System.out.println("\nPlay again, or type \"X\" to quit.");
        showChips();

        isSoft = false;
        player.getHand().clear();
        dealer.getHand().clear();
        initializeDeck();
    }

    private void display() {
        if (isSoft) {
            System.out.println("YOUR HAND: " + player.getHand() + "  |  Value: Soft " + player.getHandValue());
        } else {
            System.out.println("YOUR HAND: " + player.getHand() + "  |  Value: " + player.getHandValue(true));
        }

        if (hideDealerHand) {
            System.out.println("DEALER HAND: [" + dealer.getHand().get(0) + ", **]");
        } else {
            System.out.println("DEALER HAND: " + dealer.getHand() + "  |  Value: " + dealer.getHandValue());
        }
        System.out.println("Cards remaining: " + deck.size());
        System.out.print("\n:: ");
    }

    private void showChips() {
        System.out.println("Chip balance: " + player.getChips());
    }

    private void exit() {
        System.out.println("Thanks for playing!");
        System.exit(1);
    }

    public static void main(String[] args) {
        System.out.println("#################################################################");
        System.out.println("#                                                               #");
        System.out.println("# A command line rendition of BLACKJACK, the classic card game. #");
        System.out.println("#                                                               #");
        System.out.println("#################################################################");

        new Blackjack().run();
    }
}
