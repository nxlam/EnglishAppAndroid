package com.example.englishapp.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.englishapp.Controller.CategoryAdapter;
import com.example.englishapp.Controller.CategorySQLHelper;
import com.example.englishapp.Model.Category;
import com.example.englishapp.Model.Vocabulary;
import com.example.englishapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocaFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    View view;
    List<Category> list;
    CategoryAdapter adapter;
    CategorySQLHelper helper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VocaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VocaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VocaFragment newInstance(String param1, String param2) {
        VocaFragment fragment = new VocaFragment();
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
        view = inflater.inflate(R.layout.fragment_voca, container, false);
        helper = new CategorySQLHelper(getContext());
        initView();
        onResume();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDialog();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list = helper.getAll();
        sortList();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new CategoryAdapter(getActivity(), getContext(), this);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setData(list);
        recyclerView.setAdapter(adapter);
    }

    private void sortList(){
        Collections.sort(list, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycleView);
        fab = view.findViewById(R.id.fab);
    }

    private void AddDialog(){
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.add_category_dialog);
        //initView
        EditText eCategoryName = dialog.findViewById(R.id.eCategoryName);
        Button btAdd, btCancel;
        btAdd = dialog.findViewById(R.id.btAddCategory);
        btCancel = dialog.findViewById(R.id.btCancelCategory);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eCategoryName.getText().toString();
                long res = helper.addCategory(new Category(name));
                if (res > 0){
                    onResume();
                    dialog.dismiss();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}