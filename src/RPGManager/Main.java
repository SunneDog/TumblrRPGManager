package RPGManager;

import RPGManager.Activity.ActivityChecker;

public class Main {
    public static final String OAUTHKEY = "ll6VScG2GkDnl4d9nBjyBU0uc90FSPxz3AaF74yJHxmXBJEmrC";
    public static final String OAUTHSECRET = "WaNrENiCan93RZq9kKEF03fdsQLxmTzanAPHwiomelkd4MaatR";
    public static final String OAUTHTOKEN = "kaQt8UFkwG9WoUEV93bQ0zFGUnp6Stawy8OyoG0WfCqnMHfYhv";
    public static final String OAUTHTOKENSECRET = "5GsV5pXT8TLxqe9ZGWIBdg6JUv3GV2gpbYnvTpPnEorU1kSKfA";

    public static void main(String[] args) {
        // write your code here
        ActivityChecker test = new ActivityChecker();
        test.isActive("brokenrebel.tumblr.com");
    }
}
