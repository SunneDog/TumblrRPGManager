package RPGManager.Activity;

import RPGManager.Main;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.Blog;
import com.tumblr.jumblr.types.Post;

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
        detail.put("type", "text");
        detail.put("format", "html");
        detail.put("state", "draft");
        detail.put("title", this.postTitle);
        detail.put("body", this.postBody);

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
    }
}
