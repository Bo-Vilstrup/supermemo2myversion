
package supermemo2;


public class SuperMemo2 {
    
    private static final sm2 quiz = new sm2() {{
        addCard("Question 1", "1");
        addCard("Question 2", "2");
        addCard("Question 3", "3");
        addCard("Question 4", "4");
    }};
    
    
    public static void main(String[] args) { startQuiz(); }
        
   
    public static void startQuiz() {
        
        do { // A new day of exercises
            do { // daily exercicses   
                quiz.getQuestions().getUsersAnswer();
                quiz.gradYouAnswer();
                
                quiz.cardsToString();
            
            } while (quiz.isFinishedForToDay());
            
        } while (quiz.doTomorrowsExercises(1));
    } // End of startQuiz
} // End of class
