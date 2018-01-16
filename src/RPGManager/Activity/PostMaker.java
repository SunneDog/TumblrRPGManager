package RPGManager.Activity;

import RPGManager.Main;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.tumblr.jumblr.JumblrClient;

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
        detail.put("format", "html");
        detail.put("state", "draft");
        detail.put("photos", "https://78.media.tumblr.com/0528adf0de085afa5e8b31cc58301b11/tumblr_nh8pd9UUMs1u6qecyo2_r1_500.gif");
        detail.put("caption", this.postBody);

        try {
            client.postCreate("krovscastlerpg", detail);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    private void makePostTitle() {
        this.postTitle = "Activity Check for ";
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
        postBody += "The following player(s) are over six days of inactivity,"
                    + " please make a post to the dash within 48 hours or contact the main for a hiatus.<br><br>";

        postBody += "<blockquote><b>";
        for(int i = 0; i < checker.getInactiveBlogs().size(); i++) {
            postBody += "<a class='tumblelog' spellcheck='false' ";
            postBody += "href='" + "https://" +  checker.getInactiveBlogs().get(i).getName() + ".tumblr.com/'>";
            postBody += "@" + checker.getInactiveBlogs().get(i).getName();
            postBody += "</a></p>";
            postBody += "<br>";
        }
        postBody += "</b></blockquote>";
    }
}
