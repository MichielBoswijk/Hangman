// reference package
package com.michielbowijk.hangman;

// necessary imports
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// first activity of app
// introduction screen with button to game
public class MainActivity extends Activity {

    // method called when app starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call superclass constructor (activity) and link layout file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // method called when start button is pressed
    public void startGame(View v) {
        startActivity(new Intent(getApplicationContext(), GameActivity.class));
    }
}
