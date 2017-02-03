package app.com.example.althomas04.basicitalian;

/**
 * Created by al.thomas04.
 * The number class represents a number vocabulary word that the user wants to learn in the Italian language.
 * It contains both italian and the default translation.
 */
public class word {

    /**
     * Default translation is the users native language
     */
    private String mDefaultTranslation;

    private String mItalianTranslation;

    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private static final int NO_IMAGE_PROVIDED = -1;

    private int mAudioResourseId;

    public word(String defaultTranslation, String italianTranslation, int audioResourseId) {
        mDefaultTranslation = defaultTranslation;
        mItalianTranslation = italianTranslation;
        mAudioResourseId = audioResourseId;
    }

    public word(String defaultTranslation, String italianTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mItalianTranslation = italianTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourseId = audioResourceId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getItalianTranslation() {
        return mItalianTranslation;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public int getAudioResourseId() {
        return mAudioResourseId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}

