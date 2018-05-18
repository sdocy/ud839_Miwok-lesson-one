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
public class FamilyFragment extends Fragment {

    private View rootView;

    private ArrayList<Miwok_word> words = new ArrayList<>();
    private ListView wordListView;

    private MediaPlayer mediaPlayer = null;
    private MediaPlayer.OnCompletionListener audioDoneListener;

    private AudioManager audioMgr;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;

    public FamilyFragment() {
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
        words.add(new Miwok_word("epe", "father", R.raw.family_father, R.drawable.family_father));
        words.add(new Miwok_word("eta", "mother", R.raw.family_mother, R.drawable.family_mother));
        words.add(new Miwok_word("angsi", "son", R.raw.family_son, R.drawable.family_son));
        words.add(new Miwok_word("tune", "daughter", R.raw.family_daughter, R.drawable.family_daughter));
        words.add(new Miwok_word("taachi", "older brother", R.raw.family_older_brother, R.drawable.family_older_brother));
        words.add(new Miwok_word("chalitti", "younger brother", R.raw.family_younger_brother, R.drawable.family_younger_brother));
        words.add(new Miwok_word("tete", "older sister", R.raw.family_older_sister, R.drawable.family_older_sister));
        words.add(new Miwok_word("kolliti", "younger sister", R.raw.family_younger_sister, R.drawable.family_younger_sister));
        words.add(new Miwok_word("ama", "grandmother", R.raw.family_grandmother, R.drawable.family_grandmother));
        words.add(new Miwok_word("paapa", "grandfather", R.raw.family_grandfather, R.drawable.family_grandfather));
    }


    void initView() {
        MiwokAdapter itemsAdapter = new MiwokAdapter(getActivity(), words, R.color.category_family);

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
