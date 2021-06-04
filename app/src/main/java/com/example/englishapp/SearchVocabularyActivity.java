package com.example.englishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.englishapp.Controller.VocabularyAdapter;
import com.example.englishapp.Controller.VocabularySQLHelper;
import com.example.englishapp.Model.Vocabulary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchVocabularyActivity extends AppCompatActivity {
    EditText eSearch;
    List<Vocabulary> list;
    ListView lvWord;
    VocabularyAdapter adapter;
    VocabularySQLHelper helper;
    int categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vocabulary);
        initView();
        helper = new VocabularySQLHelper(this);
        categoryID = getIntent().getIntExtra("category_id", 0);
        onResume();
        lvWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(SearchVocabularyActivity.this, UpDelActivity.class);
                    intent.putExtra("word", list.get(position));
//                    Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } catch (Exception e){
                    Log.d("word position", position+"");
                }
            }
        });

        eSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResume();
            }
        });
    }

    private void sortList(){
        Collections.sort(list, new Comparator<Vocabulary>() {
            @Override
            public int compare(Vocabulary o1, Vocabulary o2) {
                return o1.getEngsub().compareToIgnoreCase(o2.getEngsub());
            }
        });
    }

    @Override
    protected void onResume() {
        list = helper.searchVocabulary(categoryID, eSearch.getText().toString());
        sortList();
        adapter = new VocabularyAdapter(this, R.layout.vocabulary_card, 0, list);
        lvWord.setAdapter(adapter);
        super.onResume();
    }

    private void initView() {
        eSearch = findViewById(R.id.eSearch);
        lvWord = findViewById(R.id.lvWord);
    }
}