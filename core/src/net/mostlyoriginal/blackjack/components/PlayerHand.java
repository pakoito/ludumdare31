
package net.mostlyoriginal.blackjack.components;

import com.artemis.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Paco on 07/12/2014. See LICENSE.md
 */
public class PlayerHand extends Component {
    public final List<GameCard> hand;

    public PlayerHand() {
        this.hand = new ArrayList<GameCard>();
    }

    public PlayerHand(List<GameCard> hand) {
        this.hand = hand;
    }

    public int getCount() {
        List<GameCard> sortedHand = new ArrayList<GameCard>(hand);
        sortedHand.sort(new Comparator<GameCard>() {
            @Override
            public int compare(GameCard o1, GameCard o2) {
                int comparison = 0;
                if (o1.number.ordinal() < o2.number.ordinal()) {
                    comparison = -1;
                } else if (o1.number.ordinal() > o2.number.ordinal()) {
                    comparison = 1;
                }
                return comparison;
            }
        });
        int result = 0;
        for (GameCard card : hand) {
            int[] values = card.getValue();
            if (values.length == 2 && result + values[1] < 22) {
                result += values[1];
            } else {
                result += values[0];
            }
        }
        return result;
    }
}
