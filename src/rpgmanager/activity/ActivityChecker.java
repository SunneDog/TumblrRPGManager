/**
 * Performs an Activity Check on all blogs a user is following
 *  for a one-week activity requirement.
 * @author Danni Sunne / dannisunne@gmail.com
 */

package rpgmanager.activity;

import com.tumblr.jumblr.types.Blog;
import rpgmanager.Main;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Post;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ActivityChecker {
    private JumblrClient client;
    private DateTimeFormatter jumblrFormat;
    private final int DAYSTILINACTIVE = 7;
    private HashSet<String> checkedBlogs;

    public ActivityChecker() {
        client = new JumblrClient(Main.OAUTHKEY, Main.OAUTHSECRET);
        client.setToken(Main.OAUTHTOKEN, Main.OAUTHTOKENSECRET);
        jumblrFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        checkedBlogs = new HashSet<>();
    }

    /**
     * Runs through the processes of getting Blogs followed by the account determined by Main, checking them for
     *  whether or not they are active by comparing them to DAYSTILINACTIVE, and removes all blogs that are considered
     *  active, leaving only those who are inactive in the HashSet checkedBlogs, which can be accessed through
     *  getInactiveBlogs().
     */
    public void activityCheck() {
        setCheckedBlogs();
        printCheckedBlogs();
    }

    /**
     * Adds every blog followed by the jumblr client's account to checkedBlogs.
     */
    private void setCheckedBlogs() {
        checkedBlogs.clear();

        Map<String, Object> params = new HashMap<>();

        // FIND A BETTER WAY TO DO THIS LOOP. I HATE YOU @TUMBLR
        for(int i = 0; i < 200; i += 20) {
            params.put("offset", i);

            // cast tumblr's SunneBlog class to ours that has hashSet and equals
            // FIX THIS
            for(Blog b : client.userFollowing(params)) {
                checkedBlogs.add(b.getName());
            }
        }

        trimToInactiveBlogs();
    }

    /**
     * Used for troubleshooting,
     *  prints all of the blogs being checked, and returns the same String printed to the console.
     * @return a String containing all of the blogs being checked.
     */
    private String printCheckedBlogs() {
        String result = "List of blogs being checked:\n";

        for(String bn : checkedBlogs) {
            result += bn + "\n";
        }

        result += "\n";
        System.out.println(result);
        return result;
    }

    /**
     * Iterates through checkedBlogs and remove any Blogs for which isActive() returns true.
     *  isActive() returns true if the blog has posted within DAYSTILINACTIVE days before the system's current date.
     */
    private void trimToInactiveBlogs() {
        Iterator<String> itr = checkedBlogs.iterator();

        while(itr.hasNext()) {
            if(isActive(itr.next())) {
                itr.remove();
            }
        }
    }

    /**
     * Checks blog to determine if it is considered "active" or not.
     * @param blogName
     * @return true if blogName.tumblr.com has posted a text post within DAYSTILINACTIVE days.
     */
    private boolean isActive(String blogName) {
        // returns false if inactive
        boolean result;

        Post latestPost = getPost(blogName);
        LocalDate postDate;

        if(latestPost == null) {
            result = false;
        } else {
            postDate = formatDate(latestPost);

            result = postedInDateRange(postDate);
        }

        System.out.println(blogName + " isActive{" + result + "}");
        return result;
    }

    /**
     * Gets the most recent text post made by blogName.
     * @param blogName
     * @return most recent text Post.
     */
    private Post getPost(String blogName) {
        Post result;
        Map<String, Object> params = new HashMap<>();
        params.put("type", "text");
        params.put("limit", 1);

        /* This situation is extremely rare and only occurs when a brand
         *  new blog joins the group and has its activity
         *  checked before it has had a chance to make a single text post. */
        if(client.blogPosts(blogName, params).size() == 0) {
            System.out.println("No posts found for " + blogName);
            return null;
        } else {
            result = client.blogPosts(blogName, params).get(0);
        }

        return result;
    }

    /**
     * Accepts tumblr's GMT Date format and converts it to Java's preferred LocalDate format.
     * @param latestPost
     * @return a LocalDate based on the post's date, filtered through jumblrFormat to accommodate for the different date
     * style.
     */
    private LocalDate formatDate(Post latestPost) {
        String gmtString = latestPost.getDateGMT().substring(0, 10);
        LocalDate result = LocalDate.parse(gmtString, jumblrFormat);

        return result;
    }

    /**
     * Checks if the post's date is within DAYSTILINACTIVE days before the machine's current date.
     * @param postDate
     * @return true if postDate is within DAYSTILINACTIVE days before the machine's current date.
     */
    private boolean postedInDateRange(LocalDate postDate) {
        boolean result = false;
        LocalDate today = LocalDate.now();
        LocalDate checkDate = today.minus(DAYSTILINACTIVE, ChronoUnit.DAYS);

        if(postDate.isAfter(checkDate)) {
            result = true;
        }

        return result;
    }

    /** DO LAMBDA FOR POST MAKING HERE ?
     *
     */


    /* Getters + Setters */
    public HashSet<String> getInactiveBlogs() {
        return checkedBlogs;
    }
}
