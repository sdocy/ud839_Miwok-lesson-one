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

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    private View rootView;

    private ArrayList<Miwok_word> words = new ArrayList<>();
    private ListView wordListView;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener audioDoneListener;

    private AudioManager audioMgr;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;

    public PhrasesFragment() {
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
        words.add(new Miwok_word("minto wuksus", "Where are you going?", R.raw.phrase_where_are_you_going));
        words.add(new Miwok_word("tinne oyaase'ne", "What is your name?", R.raw.phrase_what_is_your_name));
        words.add(new Miwok_word("oyaaset...", "My name is...", R.raw.phrase_my_name_is));
        words.add(new Miwok_word("michekses", "How are you feeling?", R.raw.phrase_how_are_you_feeling));
        words.add(new Miwok_word("kuchi achit", "I'm feeling good.", R.raw.phrase_im_feeling_good));
        words.add(new Miwok_word("eenes'aa", "Are you coming?", R.raw.phrase_are_you_coming));
        words.add(new Miwok_word("hee'eenem", "Yes, I'm coming.", R.raw.phrase_yes_im_coming));
        words.add(new Miwok_word("yoowutis", "Let's go.", R.raw.phrase_lets_go));
        words.add(new Miwok_word("enni'nem", "Come here.", R.raw.phrase_come_here));
    }


    void initView() {
        MiwokAdapter itemsAdapter = new MiwokAdapter(getActivity(), words, R.color.category_phrases);

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
