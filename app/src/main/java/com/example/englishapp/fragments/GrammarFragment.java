package com.example.englishapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishapp.Controller.CategoryAdapter;
import com.example.englishapp.Controller.CategorySQLHelper;
import com.example.englishapp.Controller.VocabularySQLHelper;
import com.example.englishapp.Model.Category;
import com.example.englishapp.R;
import com.example.englishapp.quiz.QuizActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GrammarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GrammarFragment extends Fragment {
    View view;
    List<Category> list;
    CategorySQLHelper helper;
    VocabularySQLHelper vocabularySQLHelper;
    Spinner spCategory;
    Button btStartQuiz;
    EditText eTotal;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GrammarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GrammarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GrammarFragment newInstance(String param1, String param2) {
        GrammarFragment fragment = new GrammarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grammar, container, false);
        helper = new CategorySQLHelper(getContext());
        vocabularySQLHelper = new VocabularySQLHelper(getContext());
        initView();
        onResume();
        btStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = (Category) spCategory.getSelectedItem();
                //kiểm tra số lượng từ của quiz
                int total = vocabularySQLHelper.countVocabulary(category.getId());
                if (total < 4){
                    Toast.makeText(getContext(), "Chủ đề phải có ít nhất 4 từ", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        //kiểm tra input
                        total = Integer.parseInt(eTotal.getText().toString());
                        if (total < 4) throw new Exception();
                        else {
                            Intent intent = new Intent(getContext(), QuizActivity.class);
                            intent.putExtra("category_id", category.getId());
                            //số lượng total của quiz
                            intent.putExtra("total", total);
                            startActivity(intent);
                        }
                    } catch (Exception e){
                        Toast.makeText(getContext(), "Số câu hỏi không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        list = helper.getAll();
        sortList();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.spinner_item, list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spCategory.setAdapter(adapter);
    }

    private void initView() {
        spCategory = view.findViewById(R.id.spCategory);
        btStartQuiz = view.findViewById(R.id.btStartQuiz);
        eTotal = view.findViewById(R.id.eTotal);
    }

    private void sortList(){
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }
}