package com.example.w1761097;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Random;

public class FourthActivity extends AppCompatActivity {
    private static final String LOG_TAG =
            FourthActivity.class.getSimpleName();
    //ArrayList of all the car makes in the game
    String[] carMakesList = {"audi", "bentley", "bmw", "bugatti", "dodge", "ferrari", "ford", "honda", "jaguar", "lamborghini", "lexus", "maserati", "mercedes",
            "mitsubishi", "nissan", "porsche", "suzuki", "tesla", "toyota", "volkswagen"};
    ImageView imageView1, imageView2, imageView3, correctImage;
    TextView textView_question_and_outcome, displayTimer, displayScore;
    Button nextBtn;
    //variables to hold the carMakesList ArrayList index for the generated car makes, and the one that is tested
    int correctImageId, imageId1, imageId2, imageId3;
    int clickedImageId, totalScore = 0;
    boolean timerStatus;
    ArrayList<Integer> usedImages = new ArrayList<>(); //usedImages ArrayList holds imageIds of the images generated in the previous round
    //resource names of the generated images
    String imageName1, imageName2, imageName3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identify_car_image);

        imageView1 = findViewById(R.id.randomImage1);
        imageView2 = findViewById(R.id.randomImage2);
        imageView3 = findViewById(R.id.randomImage3);
        textView_question_and_outcome = findViewById(R.id.textView_car_image_question);
        nextBtn = findViewById(R.id.button_next);
        displayScore = findViewById(R.id.display_score3);
        displayTimer = findViewById(R.id.textView_timer3);

        //usedImages is initialized with 0 as its elements
        for(int i = 0; i<3;i++){
            usedImages.add(0);
        }

        //timer status is received from MainActivity and assigned to timerStatus variable
        Bundle extras = getIntent().getExtras();
        timerStatus = extras.getBoolean("Timer status");
        Log.d(LOG_TAG, "Timer status: " + timerStatus);
        randomImageGenerator();
    }

    //method to generate a new random image
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
        String randomCarMake1 = carMakesList[randomCarMakeNum1];
        imageId1 = randomCarMakeNum1;
        int randomNumber = rand.nextInt((3-1)+1) + 1;
        Log.d("imageid1", "this is " + randomCarMake1+randomNumber);
        imageName1= randomCarMake1+randomNumber;
        int randomImageId = getResources().getIdentifier(imageName1, "drawable", this.getPackageName());
        imageView1.setImageResource(randomImageId);

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
        imageView2.setImageResource(randomImageId);

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
        imageView3.setImageResource(randomImageId);

        //usedImages ArrayList is set with the newly generated images to replace the old one
        usedImages.set(0, imageId1);
        usedImages.set(1, imageId2 );
        usedImages.set(2, imageId3);
        //the car make to be guessed is randomly selected and displayed
        correctImage();
        textView_question_and_outcome.setText(getString(R.string.identify_image_question_text, carMakesList[correctImageId]));
        textView_question_and_outcome.setTextColor(getResources().getColor(R.color.default_text_color));
        //timer starts counting down
        if(timerStatus){
            countDownTimer.start();
        }
        clickedImageId=0;
    }

    //method that randomly selects which of the three generated images the user will be guessing
    public void correctImage(){
        Random rand = new Random();
        int randomNum = rand.nextInt((3-1)+1) + 1;
        switch (randomNum){
            case 1:
                correctImageId = imageId1;
                correctImage = findViewById(R.id.randomImage1);
                break;
            case 2:
                correctImageId = imageId2;
                correctImage = findViewById(R.id.randomImage2);
                break;
            case 3:
                correctImageId = imageId3;
                correctImage = findViewById(R.id.randomImage3);
                break;
            default:
                //nothing happens
                break;
        }
    }

    //the image clicked by the user is assigned to clickedImagesId
    //checkAnswer() method is called
    public void onImage1Click(View view){
        clickedImageId = imageId1;
        checkAnswer();
    }

    public void onImage2Click(View view){
        clickedImageId = imageId2;
        checkAnswer();
    }

    public void onImage3Click(View view){
        clickedImageId = imageId3;
        checkAnswer();
    }

    public void checkAnswer(){
        //timer stops
        if(timerStatus){
            countDownTimer.cancel();
        }
        int stringId;
        int result;
        int color;
        //checks if selected image is correct
        //sets result, result text color and SnackBox message
        if(clickedImageId == correctImageId){
            result = R.string.correct_answer;
            stringId= R.string.positive_message;
            color=getResources().getColor(R.color.correct2);
            totalScore++;
        }else{
            result = R.string.wrong_answer;
            stringId= R.string.encouraging_message;
            color=getResources().getColor(R.color.wrong2);
            //setAlpha is used to change the opacity of the wrong images to 0.7
            //correct image remains the same
            imageView1.setAlpha((float) 0.7);
            imageView2.setAlpha((float) 0.7);
            imageView3.setAlpha((float) 0.7);
            correctImage.setAlpha((float) 1.0);
        }
        textView_question_and_outcome.setText(result);
        textView_question_and_outcome.setTextColor(color);
        displayScore.setText(getString(R.string.score_text, Integer.toString(totalScore)));
        Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content).getRootView(), stringId,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
        //"Next" button_high_score is enabled
        nextBtn.setEnabled(true);
    }

    //method to execute the code whenever the button_high_score is clicked
    public void buttonAction(View view){
        //values are set to default
        //new images are generated
        textView_question_and_outcome.setText("");
        imageView1.setAlpha((float)1.0);
        imageView2.setAlpha((float)1.0);
        imageView3.setAlpha((float)1.0);
        nextBtn.setEnabled(false);
        randomImageGenerator();
    }

    //checks if generated car make was generated in the previous round, using the elements in usedImages ArrayList
    //returns true if image was previously generated
    //else returns false
    public boolean imageCheck(int imageNumber){
        boolean check = false;
        for(Integer number: usedImages){
            if(number == imageNumber){
                check = true;
            }
        }
        return check;
    }

    //CountDownTimer is starts at 20s and counts down to 1
    //Time left is displayed onTick()
    //onFinish buttonAction() method is called
    /*SOURCE: https://developer.android.com/reference/android/os/CountDownTimer*/
    CountDownTimer countDownTimer = new CountDownTimer(20000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String time = Long.toString(millisUntilFinished / 1000);
            displayTimer.setText(getString(R.string.display_timer, time));
        }

        @Override
        public void onFinish() {
            checkAnswer();
        }
    };

    //saves instances of values that changes with orientation changes
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("RANDOM_IMAGE1", imageName1);
        outState.putString("RANDOM_IMAGE2", imageName2);
        outState.putString("RANDOM_IMAGE3", imageName3);

        outState.putFloat("ALPHA1", imageView1.getAlpha());
        outState.putFloat("ALPHA2", imageView2.getAlpha());
        outState.putFloat("ALPHA3", imageView3.getAlpha());

        outState.putBoolean("BUTTON_STATE", nextBtn.isEnabled());

        outState.putInt("CORRECT_IMAGE_ID1", correctImageId);
        outState.putString("QUESTION", textView_question_and_outcome.getText().toString());
        outState.putInt("QUESTION_COLOR", textView_question_and_outcome.getCurrentTextColor());

        outState.putInt("CURRENT_SCORE", totalScore);
        outState.putString("CURRENT_SCORE_TEXT", displayScore.getText().toString());

        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

    }

    //restores all saved instances after orientation change
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        imageName1 = savedInstanceState.getString("RANDOM_IMAGE1");
        imageView1.setImageResource(getResources().getIdentifier(imageName1, "drawable", this.getPackageName()));
        imageView1.setAlpha(savedInstanceState.getFloat("ALPHA1"));

        imageName2 = savedInstanceState.getString("RANDOM_IMAGE2");
        imageView2.setImageResource(getResources().getIdentifier(imageName2, "drawable", this.getPackageName()));
        imageView2.setAlpha(savedInstanceState.getFloat("ALPHA2"));

        imageName3 = savedInstanceState.getString("RANDOM_IMAGE3");
        imageView3.setImageResource(getResources().getIdentifier(imageName3, "drawable", this.getPackageName()));
        imageView3.setAlpha(savedInstanceState.getFloat("ALPHA3"));

        nextBtn.setEnabled(savedInstanceState.getBoolean("BUTTON_STATE"));

        correctImageId = savedInstanceState.getInt("CORRECT_IMAGE_ID");
        textView_question_and_outcome.setText(savedInstanceState.getString("QUESTION"));
        textView_question_and_outcome.setTextColor(savedInstanceState.getInt("QUESTION_COLOR"));
        Log.d(LOG_TAG, "Restored image are " + imageName1 + " " + imageName2 + " " + imageName3);

        totalScore = savedInstanceState.getInt("CURRENT_SCORE");
        String currentScoreText = savedInstanceState.getString("CURRENT_SCORE_TEXT");
        displayScore.setText(currentScoreText);
    }
}