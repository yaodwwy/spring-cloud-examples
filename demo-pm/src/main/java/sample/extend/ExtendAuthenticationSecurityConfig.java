package sample.extend;

import com.xkcoding.justauth.AuthRequestFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sample.add.IamAccessTokenFeignApi;

@Configuration
public class ExtendAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final AuthRequestFactory authRequestFactory;
    private final ExtendUserDetailsService extendUserDetailsService;
//    private final AuthenticationSuccessHandler successHandler;
    private final IamAccessTokenFeignApi iamAccessTokenFeignApi;

    public ExtendAuthenticationSecurityConfig(AuthRequestFactory authRequestFactory, ExtendUserDetailsService extendUserDetailsService, IamAccessTokenFeignApi iamAccessTokenFeignApi) {
        this.authRequestFactory = authRequestFactory;
        this.extendUserDetailsService = extendUserDetailsService;
        this.iamAccessTokenFeignApi = iamAccessTokenFeignApi;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        // 1. 初始化 ExtendAuthenticationFilter
        ExtendAuthenticationFilter filter = new ExtendAuthenticationFilter(iamAccessTokenFeignApi);
        filter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
//        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthRequestFactory(authRequestFactory);

        // 2. 初始化 ExtendAuthenticationProvider
        ExtendAuthenticationProvider provider = new ExtendAuthenticationProvider();
        provider.setExtendUserDetailsService(extendUserDetailsService);

        // 3. 将设置完毕的 Filter 与 Provider 添加到配置中，将自定义的 Filter 加到 UsernamePasswordAuthenticationFilter 之前
        builder
                .authenticationProvider(provider)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
