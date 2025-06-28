import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

class Question {
    String questionText;
    List<String> options;
    int correctOption;

    public Question(String questionText, List<String> options, int correctOption) {
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }
}

class User {
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

public class OnlineExaminationSystem {
    static List<Question> questions = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static User loggedInUser;
    static boolean sessionOpen = false;
    static Timer examTimer;
    static boolean timeUp = false;
    static final int EXAM_DURATION = 120; // Exam duration in seconds (2 minutes)
    static int score = 0;

    public static void main(String[] args) {
        initializeQuestions();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOnline Examination System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select 1-3.");
            }
        }
    }

    public static void initializeQuestions() {
        // General Knowledge Questions
        List<String> options1 = new ArrayList<>();
        options1.add("Paris");
        options1.add("London");
        options1.add("Berlin");
        options1.add("Madrid");
        
        List<String> options2 = new ArrayList<>();
        options2.add("Pacific");
        options2.add("Atlantic");
        options2.add("Indian");
        options2.add("Arctic");
        
        List<String> options3 = new ArrayList<>();
        options3.add("William Shakespeare");
        options3.add("Charles Dickens");
        options3.add("J.K. Rowling");
        options3.add("George Orwell");
        
        List<String> options4 = new ArrayList<>();
        options4.add("Au");
        options4.add("Ag");
        options4.add("Fe");
        options4.add("Hg");
        
        List<String> options5 = new ArrayList<>();
        options5.add("Mount Everest");
        options5.add("K2");
        options5.add("Kangchenjunga");
        options5.add("Lhotse");
        
        List<String> options6 = new ArrayList<>();
        options6.add("Leonardo da Vinci");
        options6.add("Pablo Picasso");
        options6.add("Vincent van Gogh");
        options6.add("Michelangelo");
        
        List<String> options7 = new ArrayList<>();
        options7.add("Mercury");
        options7.add("Venus");
        options7.add("Mars");
        options7.add("Jupiter");
        
        List<String> options8 = new ArrayList<>();
        options8.add("Yen");
        options8.add("Won");
        options8.add("Dollar");
        options8.add("Yuan");
        
        List<String> options9 = new ArrayList<>();
        options9.add("Blue Whale");
        options9.add("Elephant");
        options9.add("Giraffe");
        options9.add("Polar Bear");
        
        List<String> options10 = new ArrayList<>();
        options10.add("India");
        options10.add("China");
        options10.add("USA");
        options10.add("Indonesia");

        questions.add(new Question("What is the capital of France?", options1, 0));
        questions.add(new Question("Which is the largest ocean on Earth?", options2, 0));
        questions.add(new Question("Who wrote 'Romeo and Juliet'?", options3, 0));
        questions.add(new Question("What is the chemical symbol for gold?", options4, 0));
        questions.add(new Question("Which is the highest mountain in the world?", options5, 0));
        questions.add(new Question("Who painted the Mona Lisa?", options6, 0));
        questions.add(new Question("Which planet is known as the Red Planet?", options7, 2));
        questions.add(new Question("What is the currency of Japan?", options8, 0));
        questions.add(new Question("What is the largest mammal?", options9, 0));
        questions.add(new Question("Which country has the largest population?", options10, 1));
    }

    public static void register(Scanner scanner) {
        System.out.println("\nUser Registration");
        System.out.print("Enter a username: ");
        String username = scanner.nextLine();
        
        // Check if username already exists
        for (User user : users) {
            if (user.username.equalsIgnoreCase(username)) {
                System.out.println("Username already taken. Please choose another one.");
                return;
            }
        }
        
        // Enhanced password validation
        String password;
        while (true) {
            System.out.println("\nPassword requirements:");
            System.out.println("- At least 6 characters long");
            System.out.println("- Must contain both letters and numbers");
            System.out.print("Enter a password: ");
            password = scanner.nextLine();
            
            if (password.length() < 6) {
                System.out.println("Error: Password must be at least 6 characters long. Try again.");
                continue;
            }
            
            if (!Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z]).{6,}$").matcher(password).matches()) {
                System.out.println("Error: Password must contain both letters and numbers. Try again.");
                continue;
            }
            
            break;
        }

        User newUser = new User(username, password);
        users.add(newUser);
        System.out.println("\nRegistration successful! You can now login.");
    }

    public static void login(Scanner scanner) {
        System.out.println("\nUser Login");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                loggedInUser = user;
                sessionOpen = true;
                System.out.println("\nLogin successful! Welcome, " + username + "!");
                showMainMenu(scanner);
                return;
            }
        }

        System.out.println("Invalid credentials. Please try again!");
    }

    public static void showMainMenu(Scanner scanner) {
        while (sessionOpen) {
            System.out.println("\nMain Menu");
            System.out.println("1. Start Exam");
            System.out.println("2. View Previous Score");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    startExam(scanner);
                    break;
                case 2:
                    viewPreviousScore();
                    break;
                case 3:
                    logout();
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1-3.");
            }
        }
    }

    public static void startExam(Scanner scanner) {
        System.out.println("\nExam Instructions:");
        System.out.println("- You have " + EXAM_DURATION/60 + " minutes to complete the exam");
        System.out.println("- There are " + questions.size() + " multiple-choice questions");
        System.out.println("- Each question has only one correct answer");
        System.out.println("- The exam will automatically submit when time expires\n");
        
        System.out.print("Are you ready to start? (yes/no): ");
        String ready = scanner.nextLine();
        
        if (!ready.equalsIgnoreCase("yes")) {
            System.out.println("Exam cancelled. Returning to main menu.");
            return;
        }
        
        score = 0;
        timeUp = false;
        startTimer();
        
        System.out.println("\nExam started! Good luck!\n");
        
        for (int i = 0; i < questions.size() && !timeUp; i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.questionText);
            
            for (int j = 0; j < question.options.size(); j++) {
                System.out.println((j + 1) + ") " + question.options.get(j));
            }
            
            System.out.print("Your answer (1-" + question.options.size() + "): ");
            
            try {
                int answer = Integer.parseInt(scanner.nextLine());
                if (answer == question.correctOption + 1) {
                    System.out.println("Correct!\n");
                    score++;
                } else {
                    System.out.println("Incorrect! The correct answer was: " + 
                            (question.correctOption + 1) + "\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number next time.\n");
            }
        }
        
        if (examTimer != null) {
            examTimer.cancel();
        }
        
        System.out.println("\nExam completed!");
        System.out.println("Your score: " + score + " out of " + questions.size());
        
        if (timeUp) {
            System.out.println("Note: Your exam was automatically submitted when time expired.");
        }
    }

    private static void startTimer() {
        examTimer = new Timer();
        final int[] secondsRemaining = {EXAM_DURATION};
        
        examTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondsRemaining[0]--;
                
                if (secondsRemaining[0] <= 0) {
                    timeUp = true;
                    this.cancel();
                    System.out.println("\n\nTime's up! Your exam has been automatically submitted.");
                } else if (secondsRemaining[0] % 30 == 0 || secondsRemaining[0] <= 10) {
                    System.out.println("\nTime remaining: " + 
                            formatTime(secondsRemaining[0]) + " minutes");
                }
            }
        }, 1000, 1000);
    }
    
    private static String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }

    public static void viewPreviousScore() {
        if (score == 0) {
            System.out.println("You haven't taken any exam yet.");
        } else {
            System.out.println("Your last score: " + score + " out of " + questions.size());
        }
    }

    public static void logout() {
        loggedInUser = null;
        sessionOpen = false;
        System.out.println("Successfully logged out.");
    }
}