package com.example.w1761097;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import java.util.Arrays;
import java.util.Random;

public class SecondActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    private static final String LOG_TAG =
            SecondActivity.class.getSimpleName();
    //ArrayList of all the car makes in the game
    String[] carMakesList = {"audi", "bentley", "bmw", "bugatti", "dodge", "ferrari", "ford", "honda", "jaguar", "lamborghini", "lexus", "maserati", "mercedes",
            "mitsubishi", "nissan", "porsche", "suzuki", "tesla", "toyota", "volkswagen"};
    ImageView imageView;
    Button identifyButton;
    TextView displayResult, displayCorrectAnswer, displayTimer, displayScore;
    Spinner spinner;
    boolean timerStatus;
    int imageId; //holds the carMakesList ArrayList index for the generated car make
    int totalScore= 0;
    String itemSelected,  imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_make);

        //timer status is received from MainActivity and assigned to timerStatus variable
        displayTimer = findViewById(R.id.textView_timer1);
        Bundle extras = getIntent().getExtras();
        timerStatus = extras.getBoolean("Timer status");
        Log.d(LOG_TAG, "Timer status: " + timerStatus);

        displayScore = findViewById(R.id.display_score1);
        imageView = findViewById(R.id.image_carmake);
        identifyButton = findViewById(R.id.button_identify);
        displayResult = findViewById(R.id.display_result);
        displayCorrectAnswer =findViewById(R.id.display_correct_answer);

        //SOURCE: https://developer.android.com/codelabs/android-training-input-controls#5
        // Create the spinner.
        spinner = findViewById(R.id.label_spinner_carmake);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.carmakes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

         randomImageGenerator();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        itemSelected = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //method to generate a new random image
    public void randomImageGenerator(){
        Random rand = new Random();
        //the car make is randomly chosen from carMakesList ArrayList
        int randomCarMakeNum = rand.nextInt(carMakesList.length);
        String randomCarMake = carMakesList[randomCarMakeNum];
        imageId = randomCarMakeNum;

        //random number from 1 to 3 is generated to select which car of the car make is generated
        int randomNumber = rand.nextInt((3-1)+1) + 1;

        //the image resource files are named as car make + number
        //randomCarMake+randomNumber makes up the file name of the generated car make
        imageName= randomCarMake+randomNumber;
        Log.d(LOG_TAG, "Randomly generated image is " + imageName);

        //ID if the generated image is fetched and image is set
        int randomImageId = getResources().getIdentifier(imageName, "drawable", this.getPackageName());
        imageView.setImageResource(randomImageId);

        //timer starts counting down
        if(timerStatus){
            countDownTimer.start();
        }
    }

    //method to execute the code whenever the button_high_score is clicked or time runs out
    public void buttonAction(View view) {
        if(identifyButton.getText().toString().equals("Identify")){
            //when the button_high_score is set to "Identify"
            //timer stops
            if(timerStatus){
            countDownTimer.cancel();
            }
            //gets the item selected by the user at the moment the button_high_score is clicked
            String userGuess= itemSelected.toLowerCase();
            int chosenImageId= Arrays.asList(carMakesList).indexOf(userGuess);
            int stringId, result, color;

            //checks if user selection is correct
            //sets the value of result, result text color and  snackbox message
            if (chosenImageId == imageId){
                result = R.string.correct_answer;
                stringId= R.string.positive_message;
                color=getResources().getColor(R.color.correct);
                totalScore++; //totalScore increments
            }else{
                result = R.string.wrong_answer;
                stringId= R.string.encouraging_message;
                color=getResources().getColor(R.color.wrong);
                displayCorrectAnswer.setText(carMakesList[imageId].toUpperCase());
            }

            //sets the outcome values
            displayResult.setText(result);
            displayResult.setTextColor(color);
            displayScore.setText(getString(R.string.score_text, Integer.toString(totalScore)));
            Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content).getRootView(), stringId,
                    Snackbar.LENGTH_SHORT);
            snackbar.show();
            //button_high_score changes to "Next"  to move on to the next image
            identifyButton.setText(R.string.next_button_label);

        }else{
            //when button_high_score is "Next"
            //outcome values are set to default, button_high_score is set to "Submit", a new image is generated
            identifyButton.setText(R.string.identify_button_label);
            displayResult.setText("");
            displayCorrectAnswer.setText("");
            randomImageGenerator();
        }
    }

    //CountDownTimer is starts at 20s and counts down to 1
    //Time left is displayed onTick()
    //onFinish buttonAction() method is called
    /*SOURCE: https://developer.android.com/reference/android/os/CountDownTimer*/
    CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String time = Long.toString(millisUntilFinished / 1000);
            displayTimer.setText(getString(R.string.display_timer , time));
        }

        @Override
        public void onFinish() {
            buttonAction(findViewById(R.id.button_identify));
        }
    };


    //saves instances of values that changes with orientation changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("RANDOM_IMAGE", imageName);
        outState.putInt("RANDOM_IMAGE_ID", imageId);
        outState.putInt("CURRENT_SCORE", totalScore);
        outState.putString("CURRENT_SCORE_TEXT", displayScore.getText().toString());
        outState.putString("BUTTON_STATUS", identifyButton.getText().toString());
        outState.putString("RESULT", displayResult.getText().toString());
        outState.putInt("RESULT_COLOR", displayResult.getCurrentTextColor());
        outState.putString("CORRECT_ANSWER", displayCorrectAnswer.getText().toString());

        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    //restores all saved instances after orientation change
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        imageName = savedInstanceState.getString("RANDOM_IMAGE");
        imageView.setImageResource(getResources().getIdentifier(imageName, "drawable", this.getPackageName()));
        imageId = savedInstanceState.getInt("RANDOM_IMAGE_ID");
        Log.d(LOG_TAG, "Restored image is " + imageName);
        identifyButton.setText(savedInstanceState.getString("BUTTON_STATUS"));
        displayResult.setText(savedInstanceState.getString("RESULT"));
        displayResult.setTextColor(savedInstanceState.getInt("RESULT_COLOR"));
        displayCorrectAnswer.setText(savedInstanceState.getString("CORRECT_ANSWER"));
        totalScore = savedInstanceState.getInt("CURRENT_SCORE");
        String currentScoreText = savedInstanceState.getString("CURRENT_SCORE_TEXT");
        displayScore.setText(currentScoreText);
    }
}