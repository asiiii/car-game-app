package com.example.w1761097;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class FifthActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            FifthActivity.class.getSimpleName();
    //ArrayList of all the car makes in the game
    String[] carMakesList = {"audi", "bentley", "bmw", "bugatti", "dodge", "ferrari", "ford", "honda", "jaguar", "lamborghini", "lexus", "maserati", "mercedes",
                        "mitsubishi", "nissan", "porsche", "suzuki", "tesla", "toyota", "volkswagen"};
    public static final int MAX_ERRORS=3;
    ArrayList<Integer> correctList = new ArrayList<>(); //holds the imageIds of the car makes guessed correctly
    ArrayList<Integer> usedImages = new ArrayList<>();//usedImages ArrayList holds imageIds of the images generated in the previous round
    TextView displayScore;
    ImageView randomImageView1, randomImageView2, randomImageView3;
    EditText userAnswer1, userAnswer2, userAnswer3;
    TextView correctAnswer1, correctAnswer2, correctAnswer3, showResult, displayTimer;
    Button submitButton;
    int imageId1, imageId2, imageId3; //variables to hold the carMakesList ArrayList index for the generated car makes
    int counter, score, totalScore = 0;
    boolean timerStatus, check1, check2, check3;
    String imageName1, imageName2, imageName3, triesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);

        displayScore = findViewById(R.id.display_score);
        randomImageView1 = findViewById(R.id.advanced_level_image1);
        randomImageView2 = findViewById(R.id.advanced_level_image2);
        randomImageView3 = findViewById(R.id.advanced_level_image3);

        userAnswer1 = findViewById(R.id.editText_entry1);
        userAnswer2 = findViewById(R.id.editText_entry2);
        userAnswer3 = findViewById(R.id.editText_entry3);

        correctAnswer1 = findViewById(R.id.advanced_level_correction1);
        correctAnswer2 = findViewById(R.id.advanced_level_correction2);
        correctAnswer3 = findViewById(R.id.advanced_level_correction3);

        submitButton = findViewById(R.id.button_advanced_level_guess);
        showResult = findViewById(R.id.show_result);

        //usedImages is initialized with 0 as its elements
        for(int i = 0; i<3;i++){
            usedImages.add(0);
            correctList.add(0);
        }

        //timer status is received from MainActivity and assigned to timerStatus variable
        displayTimer = findViewById(R.id.textView_timer4);
        Bundle extras = getIntent().getExtras();
        timerStatus = extras.getBoolean("Timer status");
        Log.d(LOG_TAG, "Timer status: " + timerStatus);
        randomImageGenerator();
    }

    public void randomImageGenerator(){
        Random rand = new Random();

        /*the car make is randomly chosen from carMakesList ArrayList at 3 instances
         * random number from 1 to 3 is generated to select which car of the car make is generated for all 3 car makes
         * the image resource files are named as car make + number
         * randomCarMake+randomNumber makes up the file name of the generated car make for each of the car makes
         * the 3 IDs if the generated image is fetched and the 3 images are is set*/
        int randomCarMakeNum1 = rand.nextInt(carMakesList.length);
        while (true){
            //checks if car make was previously generated, until a new car make is generated
            if (!imageCheck(randomCarMakeNum1)){
                break;
            }
            randomCarMakeNum1 = rand.nextInt(carMakesList.length);
        }
       String  randomCarMake1 = carMakesList[randomCarMakeNum1];
        imageId1 = randomCarMakeNum1;
        int randomNumber = rand.nextInt((3-1)+1) + 1;
        Log.d("imageid1", "this is " + randomCarMake1+randomNumber);
        imageName1 =  randomCarMake1+randomNumber;
        int randomImageId = getResources().getIdentifier(imageName1, "drawable", this.getPackageName());
        randomImageView1.setImageResource(randomImageId);

        int randomCarMakeNum2 = rand.nextInt(carMakesList.length);
        while (true){
            //checks if car make was previously generated
            // also checks if car make generated is the same as the first one
            // will keep checking until a valid car make is generated
            if (randomCarMakeNum1 != randomCarMakeNum2 && !imageCheck(randomCarMakeNum2)){
                break;
            }
            randomCarMakeNum2 = rand.nextInt(carMakesList.length);
        }

        String randomCarMake2 = carMakesList[randomCarMakeNum2];
        imageId2 = randomCarMakeNum2;
        randomNumber = rand.nextInt((3-1)+1) + 1;
        Log.d("imageid2", "this is " + randomCarMake2+randomNumber);
        imageName2 = randomCarMake2+randomNumber;
        randomImageId = getResources().getIdentifier(imageName2, "drawable" , this.getPackageName());
        randomImageView2.setImageResource(randomImageId);

        int randomCarMakeNum3 = rand.nextInt(carMakesList.length);
        while (true){
            //checks if car make was previously generated
            // also checks if car make generated is the same as the first o second ones
            // will keep checking until a valid car make is generated
            if (randomCarMakeNum1 != randomCarMakeNum3 && randomCarMakeNum2 != randomCarMakeNum3 && !imageCheck(randomCarMakeNum3)){
                break;
            }
            randomCarMakeNum3 = rand.nextInt(carMakesList.length);
        }
        String randomCarMake3 = carMakesList[randomCarMakeNum3];
        imageId3 = randomCarMakeNum3;
        randomNumber = rand.nextInt((3-1)+1) + 1;
        Log.d("imageid3", "this is " + randomCarMake3+randomNumber);
        imageName3 = randomCarMake3+randomNumber;
        randomImageId = getResources().getIdentifier(imageName3, "drawable", this.getPackageName());
        randomImageView3.setImageResource(randomImageId);

        //usedImages ArrayList is set with the newly generated images to replace the old one
        usedImages.set(0, imageId1);
        usedImages.set(1, imageId2);
        usedImages.set(2, imageId3);
        //timer starts counting down
        if(timerStatus){
            countDownTimer.start();
        }
    }

    //method that randomly selects which of the three generated images the user will be guessing
    public boolean imageCheck(int imageNumber){
        boolean check = false;
        for(Integer number: usedImages){
            if(number == imageNumber){
                check = true;
                break;
            }
        }
        return check;
    }

    //method to execute the code whenever the button_high_score is clicked or time runs out
    public void buttonAction(View view){
        score = 0;
        if(submitButton.getText().toString().equals("Submit")){
            //when the button_high_score is set to "Submit"
            //timer stops
            if(timerStatus){
                countDownTimer.cancel();
            }
            //no of attempts increases
            counter++;

            //checks each answer filled/not filled by the user using checkAnswer method()
            //if correct it is added to the correctList to a given index
            //score is incremented
            //if incorrect/empty that index is set to 0
            check1 = checkAnswer(userAnswer1, imageId1);
            if(check1){
                correctList.set(0, imageId1);
                score++;
            }else{
                correctList.set(0, 0);
            }
            check2 = checkAnswer(userAnswer2, imageId2);
            if(check2){
                correctList.set(1, imageId2);
                score++;
            }else{
                correctList.set(1, 0);
            }
            check3=checkAnswer(userAnswer3, imageId3);
            if(check3){
                correctList.set(2, imageId3);
                score++;
            }else{
                correctList.set(2, 0);
            }
            //if the no of attempts is less than or equal to 3
            if(counter <= MAX_ERRORS){
                //checks if all three entries are correct
                if(check1 && check2 && check3){
                    //timer stops
                    if(timerStatus){
                        countDownTimer.cancel();
                    }
                    //sets and displays the value of result, result text color and SnackBox message
                    int stringId = R.string.positive_message;
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.AdvancedLevelLayout), stringId,
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    showResult.setText(R.string.correct_answer);
                    showResult.setTextColor(getResources().getColor(R.color.correct));
                    totalScore = totalScore+score; //totalScore is incremented
                    displayScore.setText(getString(R.string.score_text, Integer.toString(totalScore) ));
                    //button_high_score is set to "Next" to move on to new cra makes
                    submitButton.setText(R.string.next_button_label);
                }else{
                    //timer stops
                    if(timerStatus){
                        countDownTimer.start();
                    }
                    //displays no of attempts left
                    triesLeft = Integer.toString(MAX_ERRORS - counter);
                    showResult.setText(getString(R.string.tries_left_text, triesLeft).toUpperCase());
                    showResult.setTextColor(getResources().getColor(R.color.default_text_color));
                    //checks if max no of attempts have been made
                    if(counter == MAX_ERRORS) {
                        //loop iterates through the correctList
                        //where the element value is 0, the correct car make is displayed
                        for (int i = 0; i < MAX_ERRORS; i++) {
                            if (correctList.get(i) == 0) {
                                switch (i) {
                                    case 0:
                                        correctAnswer1.setText(carMakesList[imageId1].toUpperCase());
                                        break;
                                    case 1:
                                        correctAnswer2.setText(carMakesList[imageId2].toUpperCase());
                                        break;
                                    case 2:
                                        correctAnswer3.setText(carMakesList[imageId3].toUpperCase());
                                        break;
                                    default:
                                        //nothing happens
                                        break;
                                }
                            }
                        }
                        //sets and dsiplays the value of result, result text color and SnackBox message
                        int stringId = R.string.encouraging_message;
                        showResult.setText(R.string.wrong_answer);
                        showResult.setTextColor(getResources().getColor(R.color.wrong2));
                        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content).getRootView(), stringId,
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        totalScore = totalScore+score; //totalScore is incremented by the no of correct guesses
                        displayScore.setText(getString(R.string.score_text, Integer.toString(totalScore) ));
                        //sets button_high_score to "Next" to move on to new car makes
                        submitButton.setText(R.string.next_button_label);
                        if(timerStatus){
                            countDownTimer.cancel();
                        }
                    }
                }
            }


        }else{
            //when button_high_score is "Next"
            //outcome values are set to default, button_high_score is set to "Submit", a new image is generated
            submitButton.setText(R.string.submit_button_label);
            showResult.setText("");
            correctAnswer1.setText("");
            correctAnswer2.setText("");
            correctAnswer3.setText("");
            userAnswer1.getText().clear();
            userAnswer1.setBackgroundColor(Color.TRANSPARENT);
            userAnswer1.setEnabled(true);
            userAnswer2.getText().clear();
            userAnswer2.setBackgroundColor(Color.TRANSPARENT);
            userAnswer2.setEnabled(true);
            userAnswer3.getText().clear();
            userAnswer3.setBackgroundColor(Color.TRANSPARENT);
            userAnswer3.setEnabled(true);
            counter = 0;
            randomImageGenerator();
        }
    }

    //method to check if user entry is correct
    private boolean checkAnswer(EditText userAnswer, int imageId) {
        if(userAnswer.getText().toString().toLowerCase().equals(carMakesList[imageId])){
            //if correct the EditText is disabled and background turns to green
            //returns true
            userAnswer.setEnabled(false);
            userAnswer.setBackgroundColor(getResources().getColor(R.color.correct2));
            return true;
        }else{
            //if incorrect EditText remains editable and background turns to red
            //returns false
            userAnswer.setBackgroundColor(getResources().getColor(R.color.wrong2));
            return false;
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
            buttonAction(submitButton);
        }
    };

    //saves instances of values that changes with orientation changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("RANDOM_IMAGE1", imageName1);
        outState.putString("RANDOM_IMAGE2", imageName2);
        outState.putString("RANDOM_IMAGE3", imageName3);

        outState.putInt("RANDOM_IMAGE_ID1", imageId1);
        outState.putInt("RANDOM_IMAGE_ID2", imageId2);
        outState.putInt("RANDOM_IMAGE_ID3", imageId3);

        outState.putInt("COUNTER_STATE", counter);
        outState.putInt("CURRENT_SCORE", score);
        outState.putInt("CURRENT_TOTAL_SCORE", totalScore);
        outState.putString("CURRENT_SCORE_TEXT", displayScore.getText().toString());

        outState.putString("BUTTON_STATUS", submitButton.getText().toString());

        outState.putBoolean("CHECK1", check1);
        outState.putBoolean("CHECK2", check2);
        outState.putBoolean("CHECK3", check3);

        outState.putString("RESULT", showResult.getText().toString());
        outState.getInt("RESULT_COLOR", showResult.getCurrentTextColor());

        outState.putString("CORRECT_ANSWER1", correctAnswer1.getText().toString());
        outState.putString("CORRECT_ANSWER2", correctAnswer2.getText().toString());
        outState.putString("CORRECT_ANSWER3", correctAnswer3.getText().toString());

        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    //restores all saved instances after orientation change
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        imageName1 = savedInstanceState.getString("RANDOM_IMAGE1");
        randomImageView1.setImageResource(getResources().getIdentifier(imageName1, "drawable", this.getPackageName()));
        imageName2 = savedInstanceState.getString("RANDOM_IMAGE2");
        randomImageView2.setImageResource(getResources().getIdentifier(imageName2, "drawable", this.getPackageName()));
        imageName3 = savedInstanceState.getString("RANDOM_IMAGE3");
        randomImageView3.setImageResource(getResources().getIdentifier(imageName3, "drawable", this.getPackageName()));

        imageId1 = savedInstanceState.getInt("RANDOM_IMAGE_ID1");
        imageId2 = savedInstanceState.getInt("RANDOM_IMAGE_ID2");
        imageId3 = savedInstanceState.getInt("RANDOM_IMAGE_ID3");

        Log.d(LOG_TAG, "Restored image are " + imageName1 + " " + imageName2 + " " + imageName3);

        counter = savedInstanceState.getInt("COUNTER_STATE");
        triesLeft = Integer.toString(MAX_ERRORS - counter);
        score = savedInstanceState.getInt("CURRENT_SCORE");
        totalScore = savedInstanceState.getInt("CURRENT_TOTAL_SCORE");
        String currentScoreText = savedInstanceState.getString("CURRENT_SCORE_TEXT");
        displayScore.setText(currentScoreText);

        submitButton.setText(savedInstanceState.getString("BUTTON_STATUS"));

        check1 = savedInstanceState.getBoolean("CHECK1");
        check2 = savedInstanceState.getBoolean("CHECK2");
        check3 = savedInstanceState.getBoolean("CHECK3");

        if(check1){
            userAnswer1.setBackgroundColor(getResources().getColor(R.color.correct2));
        }else{
            userAnswer1.setBackgroundColor(getResources().getColor(R.color.wrong2));
        }
        if(check2){
            userAnswer2.setBackgroundColor(getResources().getColor(R.color.correct2));
        }else{
            userAnswer2.setBackgroundColor(getResources().getColor(R.color.wrong2));
        }
        if(check3){
            userAnswer3.setBackgroundColor(getResources().getColor(R.color.correct2));
        }else{
            userAnswer3.setBackgroundColor(getResources().getColor(R.color.wrong2));
        }

        String result = savedInstanceState.getString("RESULT");
        showResult.setText(result);
        showResult.setTextColor(savedInstanceState.getInt("RESULT_COLOR"));

        String correctAnswer = savedInstanceState.getString("CORRECT_ANSWER1");
        correctAnswer1.setText(correctAnswer);
        correctAnswer = savedInstanceState.getString("CORRECT_ANSWER2");
        correctAnswer2.setText(correctAnswer);
        correctAnswer = savedInstanceState.getString("CORRECT_ANSWER3");
        correctAnswer3.setText(correctAnswer);
    }
}