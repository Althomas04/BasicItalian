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
 * A simple {@link PhrasesFragment} subclass, it represents phrases that the user wants to learn in the Italian language.
 * It contains both Italian and the default translation.
 */

public class PhrasesFragment extends Fragment {

    public PhrasesFragment() {
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

        final ArrayList<word> phrasesArrayList = new ArrayList<word>();
        phrasesArrayList.add(new word("Where are you going?", "Dove vai?", R.raw.phrase_where_are_you_going));
        phrasesArrayList.add(new word("What is your name?", "Come ti chiami?", R.raw.phrase_what_is_your_name));
        phrasesArrayList.add(new word("My name is...", "Mi chiamo...", R.raw.phrase_my_name_is));
        phrasesArrayList.add(new word("How are you feeling?", "Come stai?", R.raw.phrase_how_are_you_feeling));
        phrasesArrayList.add(new word("I’m feeling good.", "Io sto bene", R.raw.phrase_im_feeling_good));
        phrasesArrayList.add(new word("I'm feeling bad/not well.", "Io sto male", R.raw.phrase_im_feeling_bad));
        phrasesArrayList.add(new word("Thank you.", "Grazie", R.raw.phrase_thank_you));
        phrasesArrayList.add(new word("You're welcome.", "Prego", R.raw.phrase_you_are_welcome));
        phrasesArrayList.add(new word("Please.", "Per favore", R.raw.phrase_please));
        phrasesArrayList.add(new word("Yes.", "Si", R.raw.phrase_yes));
        phrasesArrayList.add(new word("No.", "No", R.raw.phrase_no));
        phrasesArrayList.add(new word("Excuse me.", "Mi scusi", R.raw.phrase_excuse_me));

        WordAdapter adapter = new WordAdapter(getActivity(), phrasesArrayList, R.color.category_phrases);
        ListView numbersListView = (ListView) rootView.findViewById(R.id.listView);
        numbersListView.setAdapter(adapter);

        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                word item = phrasesArrayList.get(position);
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
