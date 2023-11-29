package sample.extend;

import com.xkcoding.justauth.AuthRequestFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sample.add.IamAccessTokenFeignApi;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class ExtendAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static String EXTEND_LOGIN_URL = "/oauth2/login-**";

    private AuthRequestFactory authRequestFactory;

    private IamAccessTokenFeignApi iamAccessTokenFeignApi;


    /**
     * 通过构造函数指定该 Filter 要拦截的 url 和 httpMethod
     */
    protected ExtendAuthenticationFilter(IamAccessTokenFeignApi iamAccessTokenFeignApi) {
        // TODO: pattern 可以抽取成配置，最后通过配置文件进行修改，这样作为共用组件只需要实现一个 default，具体值可以有调用者指定
        super(new AntPathRequestMatcher(EXTEND_LOGIN_URL, null));
        this.iamAccessTokenFeignApi = iamAccessTokenFeignApi;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.debug("从第三方拿到用户信息");
        AuthUser authUser = this.obtainAuthUser(request);
        ExtendAuthenticationToken token = new ExtendAuthenticationToken(authUser);

        Object details = this.authenticationDetailsSource.buildDetails(request);
        token.setDetails(details);

        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        return authenticationManager.authenticate(token);
    }

    /**
     * 获取 justauth 登录后的用户信息
     */
    @SneakyThrows
    protected AuthUser obtainAuthUser(HttpServletRequest request) {
        AuthIamRequest authRequest = (AuthIamRequest) authRequestFactory.get("iam");
        authRequest.setAccessTokenFeignApi(iamAccessTokenFeignApi);
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            sb.append(request.getHeader(headerNames.nextElement())).append("; ");
        }

        AuthCallback callback = AuthCallback.builder()
                .code(request.getParameter("code"))
                .auth_code(request.getParameter("auth_code"))
                .authorization_code(request.getParameter("authorization_code"))
                .oauth_token(request.getParameter("oauth_token"))
                .state(request.getParameter("state"))
                .oauth_verifier(request.getParameter("oauth_verifier"))
                .build();

        AuthResponse<AuthUser> response = authRequest.login(callback);
        // 第三方登录成功
        if (response.getCode() == AuthResponseStatus.SUCCESS.getCode()) {
            return (AuthUser) response.getData();
        }
        throw new FailedLoginException("第三方登录失败: " + response.getMsg());
    }

    public void setAuthRequestFactory(AuthRequestFactory authRequestFactory) {
        this.authRequestFactory = authRequestFactory;
    }

}
