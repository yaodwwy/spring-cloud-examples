package sample.extend;

import me.zhyd.oauth.config.AuthSource;

/**
 * 可以快速集成 qq 微信 等第三方平台
 */
public enum AuthCustomSource implements AuthSource {

    IAM {

        @Override
        public String authorize() {
            return "http://127.0.0.1:9000/oauth2/authorize";
        }

        @Override
        public String accessToken() {
            return "http://127.0.0.1:9000/oauth2/token";
        }

        @Override
        public String userInfo() {
            return "http://127.0.0.1:9000/userinfo";
        }

        @Override
        public String revoke() {
            return "http://127.0.0.1:9000/oauth2/revoke";
        }

    }
}
