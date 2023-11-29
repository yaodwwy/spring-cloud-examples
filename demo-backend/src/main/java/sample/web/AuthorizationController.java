package sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {

    @GetMapping("/oauth2/authorization/iam")
    public String redirectLogin() {

        String param = "?response_type=code" +
                "&client_id=pm-client" +
                "&redirect_uri=http://127.0.0.1:8080/login/oauth2/code/pm-client" +
                "&scope=message.read+openid";
        return "redirect:http://127.0.0.1:9000/oauth2/authorize" + param;
    }

}
