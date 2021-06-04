package com.example.englishapp.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.englishapp.Model.Vocabulary;
import com.example.englishapp.R;

import java.util.List;

public class VocabularyAdapter extends ArrayAdapter {
    List<Vocabulary> list;

    public VocabularyAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.vocabulary_card, null);
        ImageView img = convertView.findViewById(R.id.imgVocabulary);
        TextView tvEngsub = convertView.findViewById(R.id.tvEngsub);
        TextView tvVietsub = convertView.findViewById(R.id.tvVietsub);
        Vocabulary vocabulary = list.get(position);
        byte[] imgData = vocabulary.getImg();
        img.setImageBitmap(BitmapFactory.decodeByteArray(imgData, 0, imgData.length));
        tvEngsub.setText("Anh: " + vocabulary.getEngsub());
        tvVietsub.setText("Việt: " + vocabulary.getVietsub());
        return convertView;
    }
}
