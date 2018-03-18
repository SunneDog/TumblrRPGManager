package rpgmanager;

import rpgmanager.activity.*;

public class Main {
    public static final String OAUTHKEY = "ll6VScG2GkDnl4d9nBjyBU0uc90FSPxz3AaF74yJHxmXBJEmrC";
    public static final String OAUTHSECRET = "WaNrENiCan93RZq9kKEF03fdsQLxmTzanAPHwiomelkd4MaatR";
    public static final String OAUTHTOKEN = "KlAoERry19aU5vC8ksrAB1rbgwqKaMpgdkYteibx5whOTsEvjH";
    public static final String OAUTHTOKENSECRET = "E5n8wysFc2t7oR6ctDXwiw9kzi4tjettEWrTtql3dMqhzYG4k3";
    public static final String MAINBLOGNAME = "krovscastlerpg";

    public static void main(String[] args) {
        // write your code here
        ActivityChecker checker = new ActivityChecker();
        PostMaker poster = new PostMaker(checker);
        checker.activityCheck();
        poster.makePost();
    }
}
