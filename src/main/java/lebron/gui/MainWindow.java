package lebron.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        // Create the chat history area (read-only)
        chatHistory = new TextArea();
        chatHistory.setEditable(false);
        chatHistory.setWrapText(true);
        chatHistory.setPrefRowCount(10);
        
        // Initialize the LeBron chatbot with GUI interface
        lebron = new GuiLebron("./data/lebron_data.txt", chatHistory);
        
        // Create scroll pane for chat history
        ScrollPane scrollPane = new ScrollPane(chatHistory);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Create user input field
        userInput = new TextField();
        userInput.setPromptText("Type your command here (e.g., 'todo buy groceries', 'list', 'bye')");
        
        // Create send button
        sendButton = new Button("Send");
        
        // Create input area (text field + button)
        HBox inputArea = new HBox(10);
        inputArea.getChildren().addAll(userInput, sendButton);
        HBox.setHgrow(userInput, Priority.ALWAYS); // Make text field expand
        
        // Create main layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(scrollPane, inputArea);
        VBox.setVgrow(scrollPane, Priority.ALWAYS); // Make chat area expand
        
        // Set up event handlers
        sendButton.setOnAction(e -> handleUserInput());
        userInput.setOnAction(e -> handleUserInput()); // Handle Enter key
        
        // Create the scene
        Scene scene = new Scene(root, 600, 400);
        
        // Set up the stage (window)
        stage.setTitle("LeBron - Task Manager");
        stage.setScene(scene);
        stage.show();
        
        // Handle window close event
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        
        // Focus on input field
        userInput.requestFocus();
    }
    
    /**
     * Handles user input by processing it through the LeBron chatbot logic.
     */
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (!input.isEmpty()) {
            // Add user input to chat
            chatHistory.appendText("You: " + input + "\n");
            
            // Process the command through LeBron
            boolean shouldExit = lebron.processCommand(input);
            
            // Clear input field
            userInput.clear();
            
            // If user said bye, close the application after a short delay
            if (shouldExit) {
                Platform.runLater(() -> {
                    try {
                        Thread.sleep(1000); // Give user time to see goodbye message
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    Platform.exit();
                    System.exit(0);
                });
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}