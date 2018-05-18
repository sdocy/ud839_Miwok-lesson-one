package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class NumbersFragment extends Fragment {

    private View rootView;

    private ArrayList<Miwok_word> words = new ArrayList<>();
    private ListView wordListView;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener audioDoneListener;

    private AudioManager audioMgr;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.word_list, container, false);

        initWords();

        initView();

        initAudioManager();

        initListeners();

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    void initWords() {
        words.add(new Miwok_word("lutti", "one", R.raw.number_one, R.drawable.number_one));
        words.add(new Miwok_word("otiiko", "two", R.raw.number_two, R.drawable.number_two));
        words.add(new Miwok_word("tolookosu", "three", R.raw.number_three, R.drawable.number_three));
        words.add(new Miwok_word("oyyisa", "four", R.raw.number_four, R.drawable.number_four));
        words.add(new Miwok_word("massokka", "five", R.raw.number_five, R.drawable.number_five));
        words.add(new Miwok_word("temmokka", "six", R.raw.number_six, R.drawable.number_six));
        words.add(new Miwok_word("kenekaku", "seven", R.raw.number_seven, R.drawable.number_seven));
        words.add(new Miwok_word("kawinta", "eight", R.raw.number_eight, R.drawable.number_eight));
        words.add(new Miwok_word("wo'e", "nine", R.raw.number_nine, R.drawable.number_nine));
        words.add(new Miwok_word("na'aacha", "ten", R.raw.number_ten, R.drawable.number_ten));
    }


    void initView() {
        MiwokAdapter itemsAdapter = new MiwokAdapter(getActivity(), words, R.color.category_numbers);

        wordListView = (ListView) rootView.findViewById(R.id.list);
        assert(wordListView != null);

        wordListView.setAdapter(itemsAdapter);
    }

    private void initAudioManager() {
        audioMgr = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    private void initListeners() {
        audioDoneListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                releaseMediaPlayer();
            }
        };

        wordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                releaseMediaPlayer();

                mediaPlayer = MediaPlayer.create(getActivity(), words.get(position).getAudioResource());
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(audioDoneListener);
            }
        });

        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    releaseMediaPlayer();
                    audioMgr.abandonAudioFocus(afChangeListener);
                } else if (focusChange ==
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                }
            }
        };
    }


    private void releaseMediaPlayer() {
        if (mediaPlayer == null) {
            return;
        }

        mediaPlayer.release();
        mediaPlayer = null;

        // release audio focus
        audioMgr.abandonAudioFocus(afChangeListener);
    }
}
