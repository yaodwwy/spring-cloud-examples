package sample;

import com.xkcoding.justauth.AuthRequestFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sample.model.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("")
public class RestAuthController {

    private final AuthRequestFactory factory;

    public RestAuthController(AuthRequestFactory factory) {
        this.factory = factory;
    }

    @GetMapping("open")
    @ResponseBody
    public List<String> list() {
        return factory.oauthList();
    }

    /**
     * 登录
     * @param source IAM/aliyun
     * @author Adam 2023/11/7 10:20 说明:
     */
    @SneakyThrows
    @ResponseBody
    @GetMapping("login-{source}")
    public void login(@PathVariable String source, HttpServletResponse response) {
        AuthRequest authRequest = factory.get(source);
        String URL = authRequest.authorize(AuthStateUtils.createState());
        response.sendRedirect(URL);
    }

    /**
     *
     * @param source IAM/aliyun
     * @param token
     * @param response
     * @author Adam 2023/11/7 10:20 说明:
     */
    @SneakyThrows
    @ResponseBody
    @GetMapping("logout-{source}")
    public void loggedOut(@PathVariable String source, String token, HttpServletResponse response) {
        AuthRequest authRequest = factory.get(source);
        AuthResponse revoke = authRequest.revoke(AuthToken.builder()
                .accessToken(token).build());
        if (revoke.getCode() == AuthResponseStatus.SUCCESS.getCode()) {
            String url = authRequest.authorize(AuthStateUtils.createState());
            response.sendRedirect(url);
        }
        throw new InternalAuthenticationServiceException(revoke.getMsg());
    }

    @ResponseBody
    @GetMapping(value = {"data"})
    public String user(Principal user) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(user);
    }

    @GetMapping(value = {"/"})
    public String index() {
        return "index";
    }

}
