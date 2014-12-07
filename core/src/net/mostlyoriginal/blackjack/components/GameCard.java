
package net.mostlyoriginal.blackjack.components;

import java.util.Random;

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

    public static final GameCard getRandomCard() {
        Random random = new Random();
        return new GameCard(Suit.fromInt(random.nextInt(Suit.values().length)),
                Number.fromInt(random.nextInt(Number.values().length)));
    }

    public enum Suit {
        SPADES, CLUBS, HEARTS, DIAMONDS;
        private static Suit fromInt(int value) {
            Suit result = SPADES;
            Suit[] suits = Suit.values();
            if (value >= 0 && value < suits.length) {
                result = suits[value];
            }
            return result;
        }
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

        private static Number fromInt(int value) {
            Number result = A;
            Number[] suits = Number.values();
            if (value >= 0 && value < suits.length) {
                result = suits[value];
            }
            return result;
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