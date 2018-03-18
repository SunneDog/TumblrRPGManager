/**
 * Drafts a post on the Main SunneBlog that @ mentions every inactive SunneBlog detected by the ActivityChecker.
 * ** MUST BE TWEAKED TO BE MORE GENERIC **
 * @author Danni Sunne / dannisunne@gmail.com
 */

package rpgmanager.activity;

import rpgmanager.Main;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;

import java.time.LocalDate;

public class PostMaker {
    ActivityChecker checker;
    JumblrClient client;
    LocalDate today;
    String postTitle, postBody;

    public PostMaker(ActivityChecker checker) {
        this.client = new JumblrClient(Main.OAUTHKEY, Main.OAUTHSECRET,Main.OAUTHTOKEN,Main.OAUTHTOKENSECRET);
        this.checker = checker;
        this.today = LocalDate.now();
        this.postBody = "";
    }

    public void makePost() {
        makePostTitle();
        makePostBody();

        Map<String, String> detail = new HashMap();
        detail.put("type", "photo");
        detail.put("filter", "html");
        detail.put("state", "draft");
        detail.put("source",
                "https://78.media.tumblr.com/0528adf0de085afa5e8b31cc58301b11/tumblr_nh8pd9UUMs1u6qecyo2_r1_500.gif");
        detail.put("caption", this.postBody);

        try {
            client.postCreate(Main.MAINBLOGNAME, detail);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private void makePostTitle() {
        this.postTitle = "activity Check for ";
        postTitle += today.toString();
        System.out.println("Post Title: " + this.postTitle + "\n");
    }

    private void makePostBody() {
        postBody = "";
        postBody += "<html><body><p>";
        postBody += "<h1>" + this.postTitle + "</h1><br><br>";
        postBody += "<b>MAKE SURE TO CHECK EVERY URL ON THIS LIST AND REMOVE ANY THAT ARE"
                    + " NOT ACTUALLY VIOLATING ACTIVITY RULES.</b>";
        postBody += "<br><br>";
        postBody += "Hello! It looks like you've gone seven days without making an RP post. You will be unfollowed in "
        + "48 hours unless you make a new post, or request a hiatus.";

        postBody += "<blockquote><b>";

        postBody += atMentionInactiveBlogs();

        postBody += "</b></blockquote>";
    }

    private String atMentionInactiveBlogs() {
        String result = "";

        for(String bn : checker.getInactiveBlogs()) {
            result += "<a class='tumblelog' spellcheck='false' ";
            result += "href='" + "https://" +  bn + ".tumblr.com/'>";
            result += "@" + bn;
            result += "</a></p>";
            result += "<br>";
        }

        return result;
    }
}
