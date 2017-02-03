package app.com.example.althomas04.basicitalian;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by al.thomas04.
 */
public class WordAdapter extends ArrayAdapter<word> {

    private int mBackgroundColorResId;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context  The current context. Used to inflate the layout file.
     * @param wordList A List of word objects to display in a list
     */
    public WordAdapter(Activity context, ArrayList<word> wordList, int backgroundColorResId) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, wordList);
        mBackgroundColorResId = backgroundColorResId;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being resused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link word} object located at this position in the list
        word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView italianTextView = (TextView) listItemView.findViewById(R.id.italian_text_view);
        // Get the Version Name from the current AndroidFlavor object and set this text on the NameTextView
        italianTextView.setText(currentWord.getItalianTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        // Get the Version Number from the current AndroidFlavor object and set this text on the NumberTextView
        defaultTextView.setText(currentWord.getDefaultTranslation());

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
        // Get the image resource ID from the current word object and
        // set the image to iconView and make it visible if there is an image, and if not, remove the imageView itself.
        if (currentWord.hasImage()) {
            iconView.setImageResource(currentWord.getImageResourceId());
            iconView.setVisibility(View.VISIBLE);
        } else {
            iconView.setVisibility(View.GONE);
        }

        View textViewLayout = listItemView.findViewById(R.id.text_view_layout);
        int backgroundColor = ContextCompat.getColor(getContext(), mBackgroundColorResId);
        textViewLayout.setBackgroundColor(backgroundColor);

        // Return the whole list item layout (containing 2 TextViews and an ImageView) so that it can be shown in the ListView
        return listItemView;
    }

}
