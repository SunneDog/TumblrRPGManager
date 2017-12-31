package RPGManager.Activity;

import RPGManager.Main;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ActivityChecker {
    private JumblrClient client;
    private DateTimeFormatter jumblrFormat;
    private static int DAYSTILINACTIVE = 7;
    private ArrayList<Blog> checkedBlogs;
    private ArrayList<Integer> inactiveBlogIndeces;

    public ActivityChecker() {
        client = new JumblrClient(Main.OAUTHKEY, Main.OAUTHSECRET);
        client.setToken(Main.OAUTHTOKEN, Main.OAUTHTOKENSECRET);
        jumblrFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        checkedBlogs = new ArrayList<>();
        inactiveBlogIndeces = new ArrayList<>();
    }

    // "main" method. run to do the full process
    public void activityCheck() {
        setCheckedBlogs();
        System.out.println(printCheckedBlogs());

        inactiveBlogIndeces.clear();
        inactiveBlogIndeces.addAll(findInactiveBlogs());
        System.out.println(printInactiveBlogs(inactiveBlogIndeces));
    }

    private void setCheckedBlogs() {
        checkedBlogs.clear();

        Map<String, Object> params = new HashMap<>();

        for(int i = 0; i < 200; i += 20) {
            params.put("offset", i + 1);
            checkedBlogs.addAll(client.userFollowing(params));
        }
    }

    private String printCheckedBlogs() {
        String result = "List of blogs being checked:\n";
        for(Blog blog : checkedBlogs) {
            result += blog.getName() + "\n";
        }
        result += "\n";
        return result;
    }

    private ArrayList<Integer> findInactiveBlogs() {
        ArrayList<Integer> result = new ArrayList<>();

        for(Blog blog : checkedBlogs) {
            if(!isActive(blog.getName())){
                result.add(checkedBlogs.indexOf(blog));
            }
        }

        return result;
    }

    private String printInactiveBlogs(ArrayList<Integer> inactiveBlogIndeces) {
        String result = "The following blogs have not posted a new text post within the last "
                            + DAYSTILINACTIVE + " days:\n";
        for(Blog i : getInactiveBlogs()) {
            result += i.getName() + "\n";
        }
        result += "\n";
        return result;
    }

    // Tumblr is considered inactive (false) if it has not posted in seven days.
    // Can be editted by changing DAYSTILINACTIVE
    public boolean isActive(String blogName) {
        // returns false if inactive
        boolean result = false;

        Post latestPost = getPost(blogName);
        LocalDate postDate = null;

        postDate = formatDate(latestPost);

        result = postedInDateRange(postDate);

        System.out.println(blogName + " isActive{" + result + "}");

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

    private LocalDate formatDate(Post latestPost) {

        String gmtString = latestPost.getDateGMT().substring(0, 10);
        LocalDate result = LocalDate.parse(gmtString, jumblrFormat);
        // System.out.println(result);

        return result;
    }

    private boolean postedInDateRange(LocalDate postDate) {
        boolean result = false;
        LocalDate today = LocalDate.now();
        LocalDate checkDate = today.minus(DAYSTILINACTIVE, ChronoUnit.DAYS);
        // check
        if(postDate.isAfter(checkDate)) {
            result = true;
        }
        //System.out.println("postedInDateRange{" + result + "}");

        return result;
    }

    /* Getters + Setters */
    public ArrayList<Blog> getInactiveBlogs() {
        ArrayList<Blog> result = new ArrayList<>();
        int tick = 0;
        for(Integer i : inactiveBlogIndeces) {
            result.add(checkedBlogs.get(i));
            System.out.println("Added inactive blog: " + result.get(tick).getName());
            tick++;
        }
        return result;
    }
}
