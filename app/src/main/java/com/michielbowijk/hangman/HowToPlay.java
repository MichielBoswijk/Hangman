// reference package
package com.michielbowijk.hangman;

// necessary imports
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

// screen for displaying text describing how to play the game
public class HowToPlay extends Activity {

    // method called when activity is started
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // call constructor of Activity class and link to layout file
        // note: by linking the layout, the text (string name htp) is displayed automatically
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }
}
