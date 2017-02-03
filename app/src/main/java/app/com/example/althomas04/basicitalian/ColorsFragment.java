package app.com.example.althomas04.basicitalian;


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
 * Created by al.thomas04.
 * A simple {@link ColorsFragment} subclass, it represents colors vocabulary that the user wants to learn in the Italian language.
 * It contains both italian and the default translation.
 */

public class ColorsFragment extends Fragment {


    public ColorsFragment() {
        // Required empty public constructor
    }

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback
                        mediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Stop playback
                        releaseMediaPlayer();
                    }
                }
            };


    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> colorsArrayList = new ArrayList<word>();
        colorsArrayList.add(new word("Red", "rosso", R.drawable.color_red, R.raw.color_red));
        colorsArrayList.add(new word("White", "bianco", R.drawable.color_white, R.raw.color_white));
        colorsArrayList.add(new word("Green", "verde", R.drawable.color_green, R.raw.color_green));
        colorsArrayList.add(new word("Blue", "azzuro/blu", R.drawable.color_blue, R.raw.color_blue));
        colorsArrayList.add(new word("Brown", "marrone", R.drawable.color_brown, R.raw.color_brown));
        colorsArrayList.add(new word("Gray", "grigio", R.drawable.color_gray, R.raw.color_gray));
        colorsArrayList.add(new word("Black", "nero", R.drawable.color_black, R.raw.color_black));
        colorsArrayList.add(new word("Yellow", "giallo", R.drawable.color_yellow, R.raw.color_yellow));
        colorsArrayList.add(new word("Orange", "arancione", R.drawable.color_orange, R.raw.color_orange));

        WordAdapter adapter = new WordAdapter(getActivity(), colorsArrayList, R.color.category_colors);
        ListView numbersListView = (ListView) rootView.findViewById(R.id.listView);
        numbersListView.setAdapter(adapter);

        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                word item = colorsArrayList.get(position);
                releaseMediaPlayer();
                int result = audioManager.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    mediaPlayer = MediaPlayer.create(getActivity(), item.getAudioResourseId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mOnCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();
            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
            //Abandons audio focus from the app when no longer needed.
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }
}
