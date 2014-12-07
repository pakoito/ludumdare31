
package net.mostlyoriginal.game.component.game;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
public class GameCard {
    public final Suit suit;

    public final Number number;

    public GameCard(Suit suit, Number number) {
        this.suit = suit;
        this.number = number;
    }

    public enum Suit {
        SPADES, CLUBS, HEARTS, DIAMONDS
    }

    public enum Number {
        TWO(new int[] {
            2
        }), THREE(new int[] {
            3
        }), FOUR(new int[] {
            4
        }), FIVE(new int[] {
            5
        }), SIX(new int[] {
            6
        }), SEVEN(new int[] {
            7
        }), EIGHT(new int[] {
            8
        }), NINE(new int[] {
            9
        }), TEN(new int[] {
            10
        }), J(new int[] {
            10
        }), Q(new int[] {
            10
        }), K(new int[] {
            10
        }), A(new int[] {
                1, 11
        });
        final int[] values;

        Number(int[] values) {
            this.values = values;
        }
    }

    public int[] getValue() {
        return number.values;
    }

    @Override
    public String toString() {
        return "GameCard{" + number + " of " + suit + "}";
    }
}
