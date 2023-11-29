package sample.add;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthToken;
import me.zhyd.oauth.model.AuthUser;

@Data
@AllArgsConstructor
public class IamTokenResult {

    private String access_token;
    private String refresh_token;
    private String scope;
    private String id_token;
    private String token_type;
    private String expires_in;
    private String error;
    private String message;

    public AuthToken toAuthToken(){
        if(this.getError() != null){
            throw new AuthException(this.getError());
        }
        if(this.getMessage() != null){
            throw new AuthException(this.getMessage());
        }
        return AuthToken.builder()
                .accessToken(this.getAccess_token())
                .refreshToken(this.getRefresh_token())
                .idToken(this.getId_token())
                .tokenType(this.getToken_type())
                .scope(this.getScope()).build();
    }
}
