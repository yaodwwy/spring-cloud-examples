package sample.extend;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.cache.AuthStateCache;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthDefaultRequest;
import me.zhyd.oauth.utils.Base64Utils;
import me.zhyd.oauth.utils.StringUtils;
import me.zhyd.oauth.utils.UrlBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sample.add.IamAccessTokenFeignApi;
import sample.add.IamTokenRequest;
import sample.add.IamTokenResult;
import sample.model.User;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class AuthIamRequest extends AuthDefaultRequest {

    private IamAccessTokenFeignApi accessTokenFeignApi;

    public AuthIamRequest(AuthConfig config) {
        super(config, AuthCustomSource.IAM);
    }

    public AuthIamRequest(AuthConfig config, AuthStateCache authStateCache) {
        super(config, AuthCustomSource.IAM, authStateCache);
        config.setIgnoreCheckState(true);
    }

    @Override
    @SneakyThrows
    protected AuthToken getAccessToken(AuthCallback authCallback) {

        String url = UrlBuilder.fromBaseUrl(source.accessToken())
                .build();

        IamTokenRequest params = new IamTokenRequest(authCallback.getCode(), "authorization_code", config.getRedirectUri());
        String authHeader = "Basic " + Base64Utils.encode(config.getClientId() + ":" + config.getClientSecret());
        ResponseEntity<IamTokenResult> response = accessTokenFeignApi.getToken(new URI(url), params, authHeader);

        IamTokenResult tokenBody = response.getBody();
        if (tokenBody == null) {
            HttpHeaders headers = response.getHeaders();
            HttpStatus statusCode = response.getStatusCode();
            throw new AuthException(statusCode.toString() + " | " + headers);
        }
        return tokenBody.toAuthToken();
    }

    @Override
    @SneakyThrows
    protected AuthUser getUserInfo(AuthToken authToken) {
        String authHeader = "Bearer " + authToken.getAccessToken();
        ResponseEntity<LinkedHashMap<String, String>> response = accessTokenFeignApi.getUserInfo(new URI(source.userInfo()), authHeader);
        LinkedHashMap<String, String> body = response.getBody();
        if (body == null) {
            HttpHeaders headers = response.getHeaders();
            HttpStatus statusCode = response.getStatusCode();
            throw new AuthException(statusCode + " | " + headers);
        }
        String userName = body.get("sub");
        return AuthUser.builder()
                .username(userName)
                .token(authToken)
                .source(source.getName()).build();
    }

    private void checkResponse(JSONObject object) {
        // oauth/token 验证异常
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error_description"));
        }
        // user 验证异常
        if (object.containsKey("message")) {
            throw new AuthException(object.getString("message"));
        }
    }

    /**
     * 返回带{@code state}参数的授权url，授权回调时会带上这个{@code state}
     *
     * @param state state 验证授权流程的参数，可以防止csrf
     * @return 返回授权地址
     * @since 1.11.0
     */
    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", "read_user+openid")
                .build();
    }

    @Override
    public AuthResponse revoke(AuthToken authToken) {
        String body = doGetRevoke(authToken);

        JSONObject object = JSONObject.parseObject(body);
        return AuthResponse.builder()
                .code(object.getInteger("code"))
                .msg(object.getString("msg"))
                .data(object.get("data"))
                .build();
    }

}
