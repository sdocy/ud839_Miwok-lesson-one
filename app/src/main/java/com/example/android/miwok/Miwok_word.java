package com.example.android.miwok;

// Holds all information for a miwok word
// - String for the word in Miwok
// - String for the word in English
public class Miwok_word {
    public String miwok;
    public String english;
    private int imageResource;
    private int audioResource;

    // constructor
    // String m - miwok word
    // String e - english translation
    // int audioR - audio resource id
    Miwok_word(String m, String e, int audioR) {
        miwok = m;
        english = e;
        audioResource = audioR;
        imageResource = -1;
    }

    // constructor with image
    // String m - miwok word
    // String e - english translation
    // int audioR - audio resource id
    // int imageR - image resource id
    Miwok_word(String m, String e, int audioR, int imageR) {
        miwok = m;
        english = e;
        audioResource = audioR;
        imageResource = imageR;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getAudioResource() { return audioResource; }
}
