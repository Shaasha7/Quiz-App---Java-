# Quiz-App---Java-
To design and implement an Object-Oriented Quiz Application using Java.  The system allows users to answer multiple-choice questions, calculates the score, and displays the result at the end.  This project demonstrates core Object-Oriented Programming concepts such as class, object, encapsulation, constructor, 
🎯 QuizMaster App (Java Swing + OOP + Multithreading)

A fully interactive desktop Quiz Application built using Java Swing, designed with OOP principles, SOLID architecture, and multithreading (timer feature).

✨ Features
🧠 Object-Oriented Design (Model–Service–View separation)
🧩 Clean SOLID-based architecture
🖥️ Java Swing GUI (interactive UI)
⏱️ Multithreaded countdown timer per question
📊 Automatic score calculation
🔄 Auto move to next question on timeout
🎯 Simple and beginner-friendly UI
🛠️ Tech Stack
Java (JDK 8+)
Swing (GUI)
AWT (Layout Management)
Multithreading (Thread class)
🧠 Project Structure
QuizMasterApp
│
├── Question (Model)
├── QuizService (Business Logic)
├── QuestionTimer (Thread - Timer)
├── QuizAppGUI (View - Swing UI)
└── QuizMasterApp (Main Class)
🚀 How to Run

Save the file as:

QuizMasterApp.java

Compile:

javac QuizMasterApp.java

Run:

java QuizMasterApp
💡 Core Code
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* =========================
   MODEL
========================= */
class Question {
    private String question;
    private String[] options;
    private int correctIndex;

    public Question(String question, String[] options, int correctIndex) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public boolean isCorrect(int index) {
        return index == correctIndex;
    }
}

/* =========================
   SERVICE (LOGIC LAYER)
========================= */
class QuizService {
    private List<Question> questions = new ArrayList<>();
    private int score = 0;
    private int currentIndex = 0;

    public QuizService() {
        loadQuestions();
    }

    private void loadQuestions() {
        questions.add(new Question(
                "Which language runs in a browser?",
                new String[]{"Java", "C", "Python", "JavaScript"}, 3));

        questions.add(new Question(
                "What does OOP stand for?",
                new String[]{"Object Oriented Programming", "Open Online Program",
                        "Only One Process", "Object Option Protocol"}, 0));

        questions.add(new Question(
                "Which keyword is used for inheritance in Java?",
                new String[]{"this", "extends", "super", "import"}, 1));
    }

    public Question getCurrentQuestion() {
        if (currentIndex < questions.size())
            return questions.get(currentIndex);
        return null;
    }

    public void submitAnswer(int index) {
        if (index != -1 && questions.get(currentIndex).isCorrect(index)) {
            score++;
        }
        currentIndex++;
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return questions.size();
    }
}

/* =========================
   THREAD (TIMER)
========================= */
class QuestionTimer extends Thread {
    private int seconds;
    private Runnable onTimeout;

    public QuestionTimer(int seconds, Runnable onTimeout) {
        this.seconds = seconds;
        this.onTimeout = onTimeout;
    }

    public void run() {
        try {
            while (seconds > 0) {
                Thread.sleep(1000);
                seconds--;
            }
            onTimeout.run();
        } catch (InterruptedException e) {
            System.out.println("Timer stopped");
        }
    }
}

/* =========================
   GUI (VIEW)
========================= */
class QuizAppGUI extends JFrame {
    private QuizService service = new QuizService();

    private JLabel questionLabel = new JLabel();
    private JRadioButton[] options = new JRadioButton[4];
    private ButtonGroup group = new ButtonGroup();
    private JButton nextButton = new JButton("Next");

    private QuestionTimer timer;

    public QuizAppGUI() {
        setTitle("QuizMaster App");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridLayout(5, 1));
        center.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            center.add(options[i]);
        }

        add(center, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> nextQuestion());

        loadQuestion();
    }

    private void loadQuestion() {
        Question q = service.getCurrentQuestion();

        if (q == null) {
            JOptionPane.showMessageDialog(this,
                    "Quiz Finished!\nScore: " + service.getScore()
                            + "/" + service.getTotal());
            System.exit(0);
        }

        questionLabel.setText(q.getQuestion());

        String[] opts = q.getOptions();
        for (int i = 0; i < opts.length; i++) {
            options[i].setText(opts[i]);
            options[i].setSelected(false);
        }

        startTimer();
    }

    private void nextQuestion() {
        int selected = -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].isSelected()) {
                selected = i;
            }
        }

        service.submitAnswer(selected);
        loadQuestion();
    }

    private void startTimer() {
        if (timer != null) timer.interrupt();

        timer = new QuestionTimer(10, () ->
                SwingUtilities.invokeLater(this::nextQuestion));

        timer.start();
    }
}

/* =========================
   MAIN CLASS
========================= */
public class QuizMasterApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new QuizAppGUI().setVisible(true);
        });
    }
}
![1](https://github.com/user-attachments/assets/bb1ceb8b-e847-41e4-bdee-6ef023365da5)
![2](https://github.com/user-attachments/assets/ed5ff894-d9bd-4d24-aa38-6738f52d2be5)

📌 Future Improvements
🔊 Sound effects for correct/incorrect answers
🗂️ Load questions from database or JSON
🎨 Better UI styling (Dark mode / themes)
🏆 Leaderboard system
⭐ If you like this project

Give it a ⭐ on GitHub and feel free to fork it!
