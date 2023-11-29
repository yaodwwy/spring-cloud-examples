package sample;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import sample.extend.ExtendAuthenticationSecurityConfig;

/**
 * @author Adam
 */
@Order(2)
@Configuration
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] HTTP_SECURITY_IGNORES = {"/login**", "/logout**", "/sign-out**", "/oauth2/**"};
    private static final String[] WEB_SECURITY_IGNORES = {
            "/favicon.ico", "/bootstrap/**", "/static/**", "/css/**", "/images/**", "/error**",
            "/index",
            "/public/**"
    };

    private final ExtendAuthenticationSecurityConfig extendAuthenticationSecurityConfig;
//    private final SimpleUrlAuthenticationSuccessHandler simpleUrlAuthenticationSuccessHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    public WebSecurityConfig(ExtendAuthenticationSecurityConfig extendAuthenticationSecurityConfig, LogoutSuccessHandler logoutSuccessHandler) {
        this.extendAuthenticationSecurityConfig = extendAuthenticationSecurityConfig;
//        this.simpleUrlAuthenticationSuccessHandler = simpleUrlAuthenticationSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(WEB_SECURITY_IGNORES);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 增加 iframe 白名单
        http.csrf().disable().cors().disable()
                .headers()
                .addHeaderWriter(new StaticHeadersWriter(
                        "Content-Security-Policy",
                        "frame-ancestors self pm:*"))
        ;


//        simpleUrlAuthenticationSuccessHandler.setUseReferer(true);
//        simpleUrlAuthenticationSuccessHandler.setTargetUrlParameter("redirect");
//        simpleUrlAuthenticationSuccessHandler.setDefaultTargetUrl("/main");

        RequestHeaderRequestMatcher matcher = new RequestHeaderRequestMatcher("Authorization");
        NegatedRequestMatcher negatedRequestMatcher = new NegatedRequestMatcher(matcher);

        http.apply(extendAuthenticationSecurityConfig)
                .and().authorizeRequests()
                .antMatchers(HTTP_SECURITY_IGNORES).permitAll()

                // 针对不带有 Authorization Header的请求匹配器的请求进行认证
                .requestMatchers(negatedRequestMatcher).authenticated()
                // 对于其他任何请求都进行认证
                .anyRequest().authenticated()
                .and()
//                .formLogin().successHandler(simpleUrlAuthenticationSuccessHandler)
//                .and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler)
                .clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/sign-out")
//                .and()
//                .oauth2Login().loginPage("/login-iam").successHandler(simpleUrlAuthenticationSuccessHandler)
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//                .and()
//                .httpBasic()
        ;

    }

}
