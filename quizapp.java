import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* =========================
   MODEL (OOP - SRP)
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
   SERVICE (SOLID - Logic Layer)
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
                new String[]{"Object Oriented Programming", "Open Online Program", "Only One Process", "Object Option Protocol"}, 0));

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
        Question q = questions.get(currentIndex);
        if (q.isCorrect(index)) {
            score++;
        }
        currentIndex++;
    }

    public boolean hasNext() {
        return currentIndex < questions.size();
    }

    public int getScore() {
        return score;
    }

    public int getTotal() {
        return questions.size();
    }
}

/* =========================
   THREAD (Multithreading - Timer)
========================= */
class QuestionTimer extends Thread {
    private int seconds;
    private Runnable onTimeout;

    public QuestionTimer(int seconds, Runnable onTimeout) {
        this.seconds = seconds;
        this.onTimeout = onTimeout;
    }

    @Override
    public void run() {
        try {
            while (seconds > 0) {
                System.out.println("Time left: " + seconds + " sec");
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
   VIEW (Swing GUI - SRP)
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

        JPanel centerPanel = new JPanel(new GridLayout(5, 1));

        centerPanel.add(questionLabel);

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            centerPanel.add(options[i]);
        }

        add(centerPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> nextQuestion());

        loadQuestion();
    }

    private void loadQuestion() {
        Question q = service.getCurrentQuestion();

        if (q == null) {
            showResult();
            return;
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
        if (timer != null) {
            timer.interrupt();
        }

        timer = new QuestionTimer(10, () -> {
            SwingUtilities.invokeLater(() -> {
                nextQuestion();
            });
        });

        timer.start();
    }

    private void showResult() {
        JOptionPane.showMessageDialog(this,
                "Quiz Finished!\nScore: " + service.getScore()
                        + "/" + service.getTotal());
        System.exit(0);
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