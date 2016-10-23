/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package supermemo2;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

class Card {

    int interval;
    int Repetition;
    double efactor = 2.5;

    int overDueDate = 0; // should be a date (here 0 = today);
    int grad = 0;

    String Q = "";
    String A = "";

    public Card(String Q, String A) {
        this.Q = Q;
        this.A = A;
    }

    @Override
    public String toString() {
        String s = "Card{" + "interval=" + interval + ", Repetition=" + Repetition + ", efactor=" + efactor + ", overDueDate=" + overDueDate + ", grad=" + grad + ", Q=" + Q + ", A=" + A + '}';
        System.out.println(s);
        return s;
    }
} // End of class card

public class sm2 {

    private ArrayList<Card> cards = new ArrayList<>();
    private int scheduled = 0;      // numbers of cards in deck
    private int notMemorised = 0;   // start value will be the same as scheduled

    private int currentIndex = 0;   // current index in cards Arraylist
    private Card currentCard = null;

    public sm2() {
    }

    // methods -->
    public void addCard(String q, String a) {
        // Fill deck up with cards
        cards.add(new Card(q, a));
        scheduled++;
        notMemorised++;
    }

    // currentCard should be named card, and
    // nextDate should be called overDueDate and should be changed from int to time
    // in final product
    public void calcIntervalEF(int grade /*,Card card*/) {

        // SM-2:
        // EF (easiness factor) is a rating for how difficult the card is.
        // Grade: (0-2) Set reps and interval to 0, keep current EF (repeat card today)
        //        (3)   Set interval to 0, lower the EF, reps + 1 (repeat card today)
        //        (4-5) Reps + 1, interval is calculated using EF, increasing in time.
        double oldEF = currentCard.efactor;
        double newEF = 0;

        //int nextDate = new Date(today);
        int nextDate = 0;

        if (grade < 3) {
            currentCard.Repetition = 0;
            currentCard.interval = 0;

        } else {

            newEF = oldEF + (0.1 - (5 - grade) * (0.08 + (5 - grade) * 0.02));
            newEF = Math.round(newEF * 100.0)/100.0; // force java to round number down to 2 decimals
            
            if (newEF < 1.3) { // 1.3 is the minimum EF
                currentCard.efactor = 1.3;

            } else {
                currentCard.efactor = newEF;
            }

            currentCard.Repetition = currentCard.Repetition + 1;

            switch (currentCard.Repetition) {
                case 1:
                    currentCard.interval = 1;
                    break;
                case 2:
                    currentCard.interval = 6;

                    break;
                default:
                    currentCard.interval = (int) Math.round((currentCard.Repetition - 1) * currentCard.efactor);

                    break;
            }
        } // End of if-else()

        if (grade == 3) {
            currentCard.interval = 0;

        }

        //  nextDate.setDate(today.getDate() + card.interval);
        //  card.nextDate = nextDate;
        currentCard.overDueDate += currentCard.interval;

    } // end of method :: calcIntervalEF

    public sm2 getQuestions() {

        // Roted the deck
        if ((scheduled - 1) - currentIndex <= 0) {
            currentIndex = 0;
        } else {
            currentIndex++;
        }

        // pick a card
        for (int i = currentIndex; i < scheduled; i++) {

            // Get "Over Due Data" questions
            if (cards.get(i).overDueDate <= 0) {
                currentCard = cards.get(i);
                System.out.println(currentCard.Q);
                return this;
            }
            // Get cards with a wrong ansaw 
            if (cards.get(i).overDueDate <= 0 && cards.get(i).grad < 3) { // 3 or 4

                currentCard = cards.get(i);
                System.out.println(currentCard.Q);
                return this;
            }
        }
        // set to null to indicate: no more cards to test
        currentCard = null;
        return this;
    }

    public void getUsersAnswer() {

        if (currentCard == null) {
            return;
        }

        String ansaw = "";
        ansaw = getInput("Please enter you answers: ");

        if (ansaw.equalsIgnoreCase(currentCard.A)) {
            System.out.println("your ansaw is correct");
        } else {
            System.out.println("your ansaw was worng!");
        }
    }

    public sm2 gradYouAnswer() {

        if (currentCard == null) {
            return this;
        }

        int grad = Integer.parseInt(getInput("grad you answer type: (0)(1)(2)(3)(4)(5)"));

        if (0 <= grad && grad <= 5) {
            //calculate new Over Due Date
            currentCard.grad = grad;
            calcIntervalEF(grad);
            
            if (2 < grad) {
                //currentCard.overDueDate = 1;
                currentCard.grad = grad;
                calcIntervalEF(grad);
                notMemorised--;
            }
        } else {
            gradYouAnswer();
        }
        return this;
    }

    private String getInput(String msg) {

        Scanner in = new Scanner(System.in);
        System.out.print(msg);
        return in.nextLine();
    }

    public boolean isFinishedForToDay() {
        return notMemorised != 0;
    }

    public boolean doTomorrowsExercises(int day) {

        String ansaw = getInput("Do you whant to do a nother day press y for yes");

        if (ansaw.equalsIgnoreCase("y")) {
            for (Card c : cards) {
                c.overDueDate--;
                if(c.overDueDate <= 0) notMemorised++;
            }
            return true;
        }
        return false;
    }

    // util methods -->
    public void cardsToString() {
        for (Card c : cards) {
            c.toString();
        }
    }
} // End of class
