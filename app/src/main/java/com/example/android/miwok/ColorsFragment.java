package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private View rootView;

    private ArrayList<Miwok_word> words = new ArrayList<>();
    private ListView wordListView;

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener audioDoneListener;

    private AudioManager audioMgr;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = null;

    public ColorsFragment() {
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
        words.add(new Miwok_word("wetetti", "red", R.raw.color_red, R.drawable.color_red));
        words.add(new Miwok_word("chokokki", "green", R.raw.color_green, R.drawable.color_green));
        words.add(new Miwok_word("takaakki", "brown", R.raw.color_brown, R.drawable.color_brown));
        words.add(new Miwok_word("topoppi", "gray", R.raw.color_gray, R.drawable.color_gray));
        words.add(new Miwok_word("kululli", "black", R.raw.color_black, R.drawable.color_black));
        words.add(new Miwok_word("kelelli", "white", R.raw.color_white, R.drawable.color_white));
        words.add(new Miwok_word("topiise", "dusty yellow", R.raw.color_dusty_yellow, R.drawable.color_dusty_yellow));
        words.add(new Miwok_word("chiwiite", "mustard yellow", R.raw.color_mustard_yellow, R.drawable.color_mustard_yellow));
    }


    void initView() {
        MiwokAdapter itemsAdapter = new MiwokAdapter(getActivity(), words, R.color.category_colors);

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

                // request audio focus
                if (audioMgr.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), words.get(position).getAudioResource());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(audioDoneListener);
                } else {
                    Log.e("ERROR", "Request for audio focus was denied.");
                }


            }
        });

        afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    // Pause playback because your Audio Focus was
                    // temporarily stolen, but will be back soon.
                    // i.e. for a phone call
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    // Stop playback, because you lost the Audio Focus.
                    // i.e. the user started some other playback app
                    // Remember to unregister your controls/buttons here.
                    // And release the kra — Audio Focus!
                    // You’re done.
                    releaseMediaPlayer();
                    audioMgr.abandonAudioFocus(afChangeListener);
                } else if (focusChange ==
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    // Lower the volume, because something else is also
                    // playing audio over you.
                    // i.e. for notifications or navigation directions
                    // Depending on your audio playback, you may prefer to
                    // pause playback here instead. You do you.
                    if (mediaPlayer != null) {
                        mediaPlayer.pause();
                    }
                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    // Resume playback, because you hold the Audio Focus
                    // again!
                    // i.e. the phone call ended or the nav directions
                    // are finished
                    // If you implement ducking and lower the volume, be
                    // sure to return it to normal here, as well.
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
