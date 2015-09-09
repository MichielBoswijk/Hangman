// reference package
package com.michielbowijk.hangman;

// necessary imports
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// class for playing the hangman game
public class GameActivity extends Activity {

    // declare private variables used in class (in multiple methods);
    private int wrongGuesses;
    private int endGameIndicator;
    private String wordToGuess;
    private String[] wordToGuessArray;
    private String[] wordState;
    private List<String> guessedCharacters;
    private int wordLength;
    private TextView gameWordView;

    // method called when app starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // call constructor of Activity class and link to layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // declare/initialize variables used in class
        wrongGuesses = 0;
        endGameIndicator = 0;
        String[] dictWords;
        Random rand = new Random();
        guessedCharacters = new ArrayList<>();
        gameWordView = (TextView) findViewById(R.id.game_word);

        // get dictionary in array and select (pseudo)random word from dict
        dictWords = getResources().getStringArray(R.array.words);
        int randomInt = rand.nextInt(dictWords.length);
        wordToGuess = dictWords[randomInt];

        // get word length and init. empty String array for word to guess and current guess state
        wordLength = wordToGuess.length();
        wordToGuessArray = new String[wordLength];
        wordState = new String[wordLength];

        // initialize current word as unknown and array for word to guess
        for (int i = 0; i < wordLength; i++) {
            wordState[i] = "?";
            wordToGuessArray[i] = Character.toString(wordToGuess.charAt(i));
        }

        // convert initial word to a string and display it
        String initialWord = createString(wordState);
        gameWordView.setText(initialWord);
    }

    // method for creating action bar with items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    // method for handling access of items in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // get clicked item id
        int id = item.getItemId();

        // handle each action bar item click individually
        if (id == R.id.action_new_game) {
            newGame();
            return true;
        } else if (id == R.id.action_how_to_play) {
            startActivity(new Intent(getApplicationContext(), HowToPlay.class));
        } else if (id == R.id.action_acknowledgements) {
            startAcknowledgements();
        }
        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------//
    // Helper methods
    //--------------------------------------------------------------------------------------------//

    // method for all events after a user clicks the 'guess' button
    public void processGuess(View v) {

        // declare/initialize variables used in method
        String userInput;
        String tempCharacter;
        boolean charInWord = false;
        EditText inputField = (EditText) findViewById(R.id.input_field);

        // get user's input
        userInput = String.valueOf(inputField.getText());

        // clear input field after button press
        inputField.setText("");

        // if character already guessed, display message
        if (guessedCharacters.contains(userInput)) {
            Toast.makeText(this, R.string.duplicate_guesses, Toast.LENGTH_LONG).show();
        }
        // if character is not a lower case letter, display message
        else if (!userInput.matches("[a-z]+")) {
            Toast.makeText(this, R.string.wrong_input, Toast.LENGTH_LONG).show();
        }
        // if input is not empty
        else if (!userInput.equals("")) {

            // update guessed characters
            guessedCharacters.add(userInput);
            updateGuessedCharacters(guessedCharacters);

            // check corresponding characters
            for (int i = 0; i < wordLength; i++) {
                tempCharacter = wordToGuessArray[i];
                if (userInput.equals(tempCharacter)) {
                    charInWord = true;
                    wordState[i] = userInput;
                }
            }
            // update view of current state of the word
            gameWordView.setText(createString(wordState));

            // process wrong guesses
            if (!charInWord) {
                wrongGuesses++;
                updateImage(wrongGuesses);
            }
            // check if word is guessed
            if (!Arrays.asList(wordState).contains("?")) {
                endGameIndicator = 1;
            }
            // check if game is ended
            checkEndGame(endGameIndicator);
        }
    }

    // method for creating string from array of strings
    public String createString(String[] wordArray) {

        // initialize empty string
        String word = "";

        // concatenate strings (single characters)
        for (String character : wordArray) {
            word += character;
        }
        return word;
    }

    // method for updating the gallows image
    public void updateImage(int n) {

        // initialize variables used in class
        ImageView gallowsImage = (ImageView) findViewById(R.id.gallow_image);
        int nthImage = 6 - n;

        // determine what image has to be drawn based on number of wrong guesses
        switch (nthImage) {
            case 6: gallowsImage.setImageResource(R.drawable.hangman6);
                break;
            case 5: gallowsImage.setImageResource(R.drawable.hangman5);
                break;
            case 4: gallowsImage.setImageResource(R.drawable.hangman4);
                break;
            case 3: gallowsImage.setImageResource(R.drawable.hangman3);
                break;
            case 2: gallowsImage.setImageResource(R.drawable.hangman2);
                break;
            case 1: gallowsImage.setImageResource(R.drawable.hangman1);
                break;
            case 0: gallowsImage.setImageResource(R.drawable.hangman0);
                endGameIndicator = 2;
                break;
        }
    }

    // method for displaying the set of guessed characters
    public void updateGuessedCharacters (List<String> chars) {

        // initialize variables used in method
        TextView guessedLettersView = (TextView) findViewById(R.id.guessed_letters);
        String tempGuessedString = "";

        // display characters with a comma, except every last character
        for (int i = 0; i < chars.size(); i++) {
            String character = chars.get(i);
            if (i < chars.size() - 1) {
                tempGuessedString += character + ", ";
            } else {
                tempGuessedString += character;
            }
        }
        // update the TextView to display guessed characters
        guessedLettersView.setText(tempGuessedString);
    }

    // method for handling end of the game
    public void checkEndGame(int indicator) {

        // initialize button object
        Button guessButton = (Button) findViewById(R.id.guess_button);

        // if game is over (indicator is 1 or 2)
        if (indicator != 0) {

            // initialize variables
            String msg = "";
            AlertDialog.Builder endgameAlert = new AlertDialog.Builder(this);

            // disable button
            guessButton.setEnabled(false);

            // display won/lose message as an alert
            if (indicator == 1) {
                msg = "Congratulations, you won! You guessed: \'" + wordToGuess + "\' correctly!";
                endgameAlert.setIcon(R.drawable.happy);
            } else if (indicator == 2) {
                msg = "Unfortunately, you lost... The word was: \'" + wordToGuess + "\'";
                endgameAlert.setIcon(R.drawable.sad);
            }
            endgameAlert.setMessage(msg);

            // create buttons for starting a new game and going back (cancelling the alert)
            endgameAlert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            endgameAlert.setNeutralButton("New Game", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newGame();
                }
            });
            endgameAlert.setTitle("Game Finished").create();
            endgameAlert.show();
        }
    }

    // method for displaying some acknowledgement information (source of icons used)
    public void startAcknowledgements() {

        // create alertDialog
        AlertDialog.Builder ackAlert = new AlertDialog.Builder(this);

        // create message for crediting the designers of the icons used
        SpannableString msg = new SpannableString("Launcher icon used designed by Vlad Marin: https://www.iconfinder.com/quizanswers\n\n" +
                        "Happy/Sad images designed by Freepik from http://www.flaticon.com");

        // make links in message appear as links (blue)
        // note: links are not clickable
        Linkify.addLinks(msg, Linkify.WEB_URLS);
        ackAlert.setMessage(msg);

        // create button to proceed
        ackAlert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ackAlert.setTitle("Acknowledgements").create();
        ackAlert.show();
    }

    // method for starting a new game
    public void newGame() {
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }
}