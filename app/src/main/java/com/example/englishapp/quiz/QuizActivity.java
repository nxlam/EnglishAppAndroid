package com.example.englishapp.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishapp.Controller.VocabularySQLHelper;
import com.example.englishapp.Model.Vocabulary;
import com.example.englishapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    int categoryID;
    List<Vocabulary> list;
    VocabularySQLHelper helper;
    int answered, total, trueAnswer;
    //answered: các câu đã trả lời
    // total: tổng số câu
    // trueAnswer: tổng câu đúng
    TextView tvQuestionNumber, tvVietsub;
    ImageView imgQuiz;
    Button btAnswer1, btAnswer2, btAnswer3, btAnswer4;
    TextToSpeech textToSpeech;
    int speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryID = getIntent().getIntExtra("category_id", 0);
        helper = new VocabularySQLHelper(QuizActivity.this);
        list = helper.getAll(categoryID);
        Collections.shuffle(list);
        total = list.size();
        answered = 0;
        trueAnswer = 0;
        setContentView(R.layout.activity_quiz);
        initView();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (answered == total) {
            endQuizDialog();
            textToSpeech.shutdown();
        } else {
            Log.d("answered", answered+"");
            ArrayList<Vocabulary> answeredSet = new ArrayList<>();// chứa các position đã trả lời
            answeredSet.add(list.get(answered));
            //đọc
            try {
                speech = textToSpeech.speak(answeredSet.get(0).getEngsub(), TextToSpeech.QUEUE_ADD, null);
            } catch (Exception e){
                    Toast.makeText(this, "TTS chưa hoạt động", Toast.LENGTH_SHORT).show();
                    Log.d("exception tts", e.getMessage());
                }
            Random random = new Random();
            for (int j = 0; j < 3; j++) {
                int r = random.nextInt(total);
                if (answeredSet.contains(list.get(r)) == false) {
                    answeredSet.add(list.get(r));
                } else j--;
            }
            String s = "";
            for (Vocabulary v : answeredSet) {
                s += v.getEngsub();
                s += ", ";
            }
            Log.d("item", s);
            answered++;
            setQuestion(answeredSet);
        }
    }

    private int setQuestion(ArrayList<Vocabulary> answeredSet){
        tvQuestionNumber.setText(answered+"/"+total);
        Vocabulary[] options = answeredSet.toArray(new Vocabulary[answeredSet.size()]);
        byte[] imgData = options[0].getImg();
        imgQuiz.setImageBitmap(BitmapFactory.decodeByteArray(imgData, 0, imgData.length));
        tvVietsub.setText(options[0].getVietsub());
        //Vị trí đáp án đúng
        Random random = new Random();
        int answer = random.nextInt(4)+1;
        //đặt các đáp án
        switch (answer){
            case 1:{
                btAnswer1.setText(options[0].getEngsub());
                btAnswer2.setText(options[1].getEngsub());
                btAnswer3.setText(options[2].getEngsub());
                btAnswer4.setText(options[3].getEngsub());
                break;
            }
            case 2:{
                btAnswer1.setText(options[1].getEngsub());
                btAnswer2.setText(options[0].getEngsub());
                btAnswer3.setText(options[2].getEngsub());
                btAnswer4.setText(options[3].getEngsub());
                break;
            }
            case 3:{
                btAnswer1.setText(options[1].getEngsub());
                btAnswer2.setText(options[2].getEngsub());
                btAnswer3.setText(options[0].getEngsub());
                btAnswer4.setText(options[3].getEngsub());
                break;
            }
            case 4:{
                btAnswer1.setText(options[1].getEngsub());
                btAnswer2.setText(options[2].getEngsub());
                btAnswer3.setText(options[3].getEngsub());
                btAnswer4.setText(options[0].getEngsub());
                break;
            }
        }
        //kiểm tra đáp án
        btAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer == 1){
                    trueAnswer++;
                    trueDialog();
                } else wrongDialog();
            }
        });
        btAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer == 2){
                    trueAnswer++;
                    trueDialog();
                } else wrongDialog();
            }
        });
        btAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer == 3){
                    trueAnswer++;
                    trueDialog();
                } else wrongDialog();
            }
        });
        btAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer == 4){
                    trueAnswer++;
                    trueDialog();
                } else wrongDialog();
            }
        });
        return answer;
    }

    private void initView() {
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvVietsub = findViewById(R.id.tvVietsub);
        imgQuiz = findViewById(R.id.imgQuiz);
        btAnswer1 = findViewById(R.id.btAnswer1);
        btAnswer2 = findViewById(R.id.btAnswer2);
        btAnswer3 = findViewById(R.id.btAnswer3);
        btAnswer4 = findViewById(R.id.btAnswer4);
    }

    private void trueDialog(){
        Dialog dialog = new Dialog(QuizActivity.this);
        dialog.setContentView(R.layout.true_answer_dialog);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onResume();
            }
        });
        dialog.show();
    }

    private void wrongDialog(){
        Dialog dialog = new Dialog(QuizActivity.this);
        dialog.setContentView(R.layout.wrong_answer_dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onResume();
            }
        });
        dialog.show();
    }

    private void endQuizDialog(){
        Dialog dialog = new Dialog(QuizActivity.this);
        dialog.setContentView(R.layout.end_quiz_diaglog);
        TextView tv = dialog.findViewById(R.id.tvResult);
        tv.setText(trueAnswer+"/"+total);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }
}