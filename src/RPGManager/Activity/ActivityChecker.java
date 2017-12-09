package RPGManager.Activity;

import com.tumblr.jumblr.JumblrClient;
import RPGManager.Main;
import com.tumblr.jumblr.types.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ActivityChecker {
    private JumblrClient client;
    private SimpleDateFormat jumblrFormat;
    private final int DAYSTILINACTIVE = 7;

    public ActivityChecker() {
        client = new JumblrClient(Main.OAUTHKEY, Main.OAUTHSECRET);
        client.setToken(Main.OAUTHTOKEN, Main.OAUTHTOKENSECRET);
        jumblrFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    // Tumblr is considered inactive (false) if it has not posted in seven days.
    // Can be editted by changing DAYSTILINACTIVE
    public boolean isActive(String blogName) {
        boolean result = false;

        Post latestPost = getPost(blogName);
        Date postDate = new Date();
        String gmtString = "";

        postDate.equals(formatDate(latestPost, gmtString));

        result = postedInDateRange(postDate);

        System.out.println("isActive{" + result + "}");

        return result;
    }

    private Post getPost(String blogName) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "text");
        params.put("limit", 1);
        params.put("filter", "text");
        List<Post> posts = client.blogPosts(blogName, params);

        return posts.get(0);
    }

    private Date formatDate(Post latestPost, String gmtString) {
        Date result = new Date();

        System.out.println(latestPost.getDateGMT());
        gmtString = latestPost.getDateGMT();
        gmtString = gmtString.substring(0, 10);
        System.out.println(gmtString);

        try {
            result = jumblrFormat.parse(gmtString);
        } catch(ParseException e) {
            e.printStackTrace();
            System.out.println("Error Parsing Date\nActivityChecker.java");
        }

        System.out.println("formatDate{" + result + "}");

        return result;
    }

    private boolean postedInDateRange(Date postDate) {
        boolean result = false;
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date checkDate = new Date(System.currentTimeMillis() - (DAYSTILINACTIVE * DAY_IN_MS));

        if(postDate.after(checkDate)) {
            result = true;
        }

        System.out.println("postedInDateRange{" + result + "}");

        return result;
    }
}
