package lebron.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The main JavaFX window for the LeBron chatbot.
 * 
 * This creates a chat-like GUI interface that connects to the actual
 * LeBron chatbot logic for processing tasks and commands.
 */
public class MainWindow extends Application {
    
    private TextArea chatHistory;
    private TextField userInput;
    private Button sendButton;
    private GuiLebron lebron;

    @Override
    public void start(Stage stage) {
        // Create the main root with background
        StackPane backgroundPane = new StackPane();
        
        // Load and set LeBron background image
        try {
            Image lebronImage = new Image(getClass().getResourceAsStream("/images/lebron.png"));
            BackgroundImage backgroundImage = new BackgroundImage(
                lebronImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
            );
            backgroundPane.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("Could not load LeBron image, using default background");
        }
        
        // Create header with title and GOAT badge
        HBox header = createHeader();
        
        // Create the chat history area (read-only)
        chatHistory = new TextArea();
        chatHistory.setEditable(false);
        chatHistory.setWrapText(true);
        chatHistory.getStyleClass().add("chat-history");
        
        // Initialize the LeBron chatbot with GUI interface
        lebron = new GuiLebron("./data/lebron_data.txt", chatHistory);
        
        // Create scroll pane for chat history
        ScrollPane scrollPane = new ScrollPane(chatHistory);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.getStyleClass().add("scroll-pane");

        // Create user input field with basketball-themed prompt
        userInput = new TextField();
        userInput.setPromptText("🏀 What's your next move, champ? (e.g., 'todo dominate the court', 'list')");
        userInput.getStyleClass().add("text-field");
        
        // Create send button with GOAT styling
        sendButton = new Button("🏆 SEND");
        sendButton.getStyleClass().add("send-button");
        
        // Add hover animations to send button
        addButtonAnimations(sendButton);
        
        // Create input area (text field + button)
        HBox inputArea = new HBox(15);
        inputArea.getChildren().addAll(userInput, sendButton);
        inputArea.getStyleClass().add("input-area");
        inputArea.setAlignment(Pos.CENTER);
        HBox.setHgrow(userInput, Priority.ALWAYS);
        
        // Create main content container
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().addAll(header, scrollPane, inputArea);
        mainContent.getStyleClass().add("main-container");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        // Add main content to background pane
        backgroundPane.getChildren().add(mainContent);
        
        // Set up event handlers
        sendButton.setOnAction(e -> handleUserInputWithAnimation());
        userInput.setOnAction(e -> handleUserInputWithAnimation());
        
        // Create the scene with CSS styling
        Scene scene = new Scene(backgroundPane, 800, 700);
        scene.getStylesheets().add(getClass().getResource("/lebron-theme.css").toExternalForm());
        
        // Set up the stage (window)
        stage.setTitle("🏀 LeBron Task Manager - The GOAT of Productivity 🐐");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(600);
        stage.setMinHeight(500);
        
        // Add initial welcome animation
        addWelcomeAnimation(mainContent);
        
        stage.show();
        
        // Handle window close event
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        
        // Focus on input field
        Platform.runLater(() -> userInput.requestFocus());
        
        // Add welcome message
        chatHistory.appendText("🏀 Welcome to LeBron Task Manager! 🏀\n");
        chatHistory.appendText("🐐 Ready to be the GOAT of productivity? Let's get started! 🐐\n");
        chatHistory.appendText("💪 Type 'list' to see your tasks or 'help' for commands 💪\n\n");
    }
    
    /**
     * Creates the header with title and GOAT badge.
     */
    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header");
        
        // Create title
        Label title = new Label("🏀 LeBron Task Manager 🏀");
        title.getStyleClass().add("header-title");
        
        // Create GOAT badge
        Label goatBadge = new Label("🐐 GOAT 🐐");
        goatBadge.getStyleClass().add("goat-badge");
        
        header.getChildren().addAll(title, goatBadge);
        return header;
    }
    
    /**
     * Adds animations to buttons for better UX.
     */
    private void addButtonAnimations(Button button) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), button);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), button);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        
        button.setOnMouseEntered(e -> scaleUp.play());
        button.setOnMouseExited(e -> scaleDown.play());
    }
    
    /**
     * Adds welcome animation to the main content.
     */
    private void addWelcomeAnimation(VBox mainContent) {
        mainContent.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), mainContent);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
    
    /**
     * Handles user input with basketball-themed messages and animations.
     */
    private void handleUserInputWithAnimation() {
        String input = userInput.getText().trim();
        if (!input.isEmpty()) {
            // Add user input to chat with basketball flair
            chatHistory.appendText("🏀 You: " + input + "\n");
            
            // Add some basketball motivation
            addMotivationalMessage(input);
            
            // Process the command through LeBron
            boolean shouldExit = lebron.processCommand(input);
            
            // Clear input field with animation
            userInput.clear();
            
            // If user said bye, close the application after a short delay
            if (shouldExit) {
                chatHistory.appendText("🐐 Thanks for being legendary! Keep dominating! 🏆\n");
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(2000); // Give user time to see goodbye message
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    Platform.exit();
                    System.exit(0);
                });
            }
        }
    }
    
    /**
     * Adds motivational basketball messages based on user input.
     */
    private void addMotivationalMessage(String input) {
        String[] motivationalMessages = {
            "💪 That's the GOAT mentality! 💪",
            "🏆 Championship mindset! 🏆",
            "🔥 Clutch performance! 🔥",
            "👑 King James approves! 👑",
            "⚡ Unstoppable! ⚡",
            "🚀 Taking it to the next level! 🚀"
        };
        
        // Show motivational message for certain commands
        if (input.toLowerCase().contains("todo") || 
            input.toLowerCase().contains("deadline") || 
            input.toLowerCase().contains("event")) {
            
            int randomIndex = (int) (Math.random() * motivationalMessages.length);
            // Add a subtle delay for the motivational message
            Platform.runLater(() -> {
                try {
                    Thread.sleep(200);
                    chatHistory.appendText("   " + motivationalMessages[randomIndex] + "\n");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
    
    /**
     * Original handleUserInput method for compatibility.
     */
    private void handleUserInput() {
        handleUserInputWithAnimation();
    }

    public static void main(String[] args) {
        launch(args);
    }
}