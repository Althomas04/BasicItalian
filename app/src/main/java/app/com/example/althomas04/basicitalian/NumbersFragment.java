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
 * A simple {@link NumbersFragment} subclass, it represents numbers vocabulary that the user wants to learn in the Italian language.
 * It contains both italian and the default translation.
 */

public class NumbersFragment extends Fragment {


    public NumbersFragment() {
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

        final ArrayList<word> numbersArrayList = new ArrayList<word>();
        numbersArrayList.add(new word("One", "uno", R.drawable.number_one, R.raw.number_one));
        numbersArrayList.add(new word("Two", "due", R.drawable.number_two, R.raw.number_two));
        numbersArrayList.add(new word("Three", "tre", R.drawable.number_three, R.raw.number_three));
        numbersArrayList.add(new word("Four", "quattro", R.drawable.number_four, R.raw.number_four));
        numbersArrayList.add(new word("Five", "cinque", R.drawable.number_five, R.raw.number_five));
        numbersArrayList.add(new word("Six", "sei", R.drawable.number_six, R.raw.number_six));
        numbersArrayList.add(new word("Seven", "sette", R.drawable.number_seven, R.raw.number_seven));
        numbersArrayList.add(new word("Eight", "otto", R.drawable.number_eight, R.raw.number_eight));
        numbersArrayList.add(new word("Nine", "nove", R.drawable.number_nine, R.raw.number_nine));
        numbersArrayList.add(new word("Ten", "dieci", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(getActivity(), numbersArrayList, R.color.category_numbers);
        ListView numbersListView = (ListView) rootView.findViewById(R.id.listView);
        numbersListView.setAdapter(adapter);

        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                word item = numbersArrayList.get(position);
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
