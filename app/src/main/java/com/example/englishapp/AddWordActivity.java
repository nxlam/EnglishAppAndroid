package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.englishapp.Controller.VocabularySQLHelper;
import com.example.englishapp.Model.Vocabulary;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddWordActivity extends AppCompatActivity {
    ImageView imgDemo;
    EditText eEngsub, eVietsub;
    Button btAddVocabulary, btCancelVocabulary;
    int SELECT_PICTURE = 200;
    int categoryID;

    VocabularySQLHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        initView();
        helper = new VocabularySQLHelper(this);
        categoryID = getIntent().getIntExtra("category_id", 0);
        imgDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        btAddVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] imgData = imageViewToByte();
                String engsub = eEngsub.getText().toString();
                String vietsub = eVietsub.getText().toString();
                long res = helper.addVocabulary(new Vocabulary(categoryID, engsub, vietsub, imgData));
                if (res > 0) finish();
            }
        });
        btCancelVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        imgDemo = findViewById(R.id.imgDemo);
        eEngsub = findViewById(R.id.eEngsub);
        eVietsub = findViewById(R.id.eVietsub);
        btAddVocabulary = findViewById(R.id.btAddVocabulary);
        btCancelVocabulary = findViewById(R.id.btCancelVocabulary);
    }

    //choose image
    void imageChooser() {
        Toast.makeText(getApplicationContext(), "Chọn ảnh nhỏ hơn 80KB", Toast.LENGTH_SHORT).show();
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            // Get the uri of the image from data
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgDemo.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //convert to save
    private byte[] imageViewToByte(){
        Bitmap bitmap = ((BitmapDrawable) imgDemo.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}