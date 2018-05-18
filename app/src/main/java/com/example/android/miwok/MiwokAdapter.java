package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


// Implements a custom array adapter for out Miwok_word class
public class MiwokAdapter extends ArrayAdapter<Miwok_word> {

    private Context myContext;
    private List<Miwok_word> wordList;
    private int bgColorRes;

    MiwokAdapter(Context context, ArrayList<Miwok_word> list, int colorRes) {
        super (context, 0, list);
        myContext = context;
        wordList = list;
        bgColorRes = colorRes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(myContext).inflate(R.layout.miwok_word, parent, false);
        }

        Miwok_word currentWord = getItem(position);
        assert(currentWord != null);

        TextView miwokWord = (TextView) listItem.findViewById(R.id.wordMiwokView);
        miwokWord.setText(currentWord.miwok);
        miwokWord.setBackgroundColor(ContextCompat.getColor(getContext(), bgColorRes));

        TextView englishWord = (TextView) listItem.findViewById(R.id.wordEnglishView);
        englishWord.setText(currentWord.english);
        englishWord.setBackgroundColor(ContextCompat.getColor(getContext(), bgColorRes));

        ImageView wordImage = (ImageView) listItem.findViewById(R.id.wordImage);

        LinearLayout playIconLayout = (LinearLayout) listItem.findViewById(R.id.playIconLayout);
        playIconLayout.setBackgroundColor(ContextCompat.getColor(getContext(), bgColorRes));

        int image = currentWord.getImageResource();
        if (image != -1) {
            wordImage.setImageResource(image);
        } else {
            wordImage.setVisibility(View.GONE);
        }

        return listItem;
    }
}
