package wallgram.hd.wallpapers;


import android.annotation.SuppressLint;
import android.content.SearchRecentSuggestionsProvider;

@SuppressLint("Registered")
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {

    private final static String AUTHORITY = "com.akspic.MySuggestionProvider";
    private final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
