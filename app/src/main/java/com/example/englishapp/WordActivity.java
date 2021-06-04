package com.example.englishapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.englishapp.Controller.VocabularyAdapter;
import com.example.englishapp.Controller.VocabularySQLHelper;
import com.example.englishapp.Model.Vocabulary;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WordActivity extends AppCompatActivity {
    List<Vocabulary> list;
    ListView lvWord;
    FloatingActionButton fab;
    VocabularyAdapter adapter;
    VocabularySQLHelper helper;
    int categoryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        initView();
        helper = new VocabularySQLHelper(this);
        categoryID = getIntent().getIntExtra("category_id", 0);
        //Toast.makeText(WordActivity.this, categoryID+"", Toast.LENGTH_SHORT).show();
        onResume();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordActivity.this, AddWordActivity.class);
                intent.putExtra("category_id", categoryID);
                startActivity(intent);
            }
        });

        lvWord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(WordActivity.this, UpDelActivity.class);
                    intent.putExtra("word", list.get(position));
//                    Toast.makeText(getApplicationContext(), position+"", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } catch (Exception e){
                    Log.d("word position", position+"");
                }
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
        list = helper.getAll(categoryID);
        sortList();
        adapter = new VocabularyAdapter(this, R.layout.vocabulary_card, 0, list);
        lvWord.setAdapter(adapter);
        super.onResume();
    }

    private void initView() {
        lvWord = findViewById(R.id.lvWord);
        fab = findViewById(R.id.fab);
    }

    //search menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mSearch){
            Intent intent = new Intent(WordActivity.this,SearchVocabularyActivity.class);
            intent.putExtra("category_id", categoryID);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}