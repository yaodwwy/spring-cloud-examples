package sample.extend;

import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.enums.AuthUserGender;
import me.zhyd.oauth.model.AuthUser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sample.model.User;

import java.io.IOException;

@Slf4j
@Service
public class ExtendUserDetailsService {

    public UserDetails loadUserByExtendKey(ExtendAuthenticationToken token) throws UsernameNotFoundException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        /*
        {"authorities":[],"details":
        {"remoteAddress":"127.0.0.1","sessionId":null},
        "authenticated":false,"extendType":null,"extendKey":null,"principal":
        {"uuid":null,"username":"user1","nickname":null,"avatar":null,"blog":null,
        "company":null,"location":null,"email":null,"remark":null,"gender":null,
        "source":"IAM","token":null,"rawUserInfo":null},"credentials":null,
        "name":"me.zhyd.oauth.model.AuthUser@4db6b2ce"}
        */
        log.debug("ExtendAuthenticationToken: {}", objectMapper.writeValueAsString(token));
        Object principal = token.getPrincipal();
        if (principal instanceof AuthUser) {
            // 测试使用 应该从User服务中取；
            return this.principalToTestUser((AuthUser) principal);
        }
        log.warn("ExtendUserDetailsService principal is not instanceof AuthUser!");
        return null;
    }

    public User principalToTestUser(AuthUser authUser) throws UsernameNotFoundException {

        AuthUserGender authUserGender = authUser.getGender();
        String genderStr = authUserGender != null ? authUserGender.getDesc() : null;

        User user = new User();
        BeanUtils.copyProperties(authUser, user);
        user.setId(authUser.getUuid());
        user.setFullname(authUser.getNickname());
        user.setGender(genderStr);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setRoles("USER");

        return user;
    }
}
