package sample.add;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Adam 2023/11/3 9:29 说明: 访问 /oauth2/token 必须的参数
 * @link iam/IamApplication.http:42
 * @since 2023/11/3 9:29
 */
@Data
@AllArgsConstructor
public class IamCodeRequest {

    String response_type;
    String client_id;
    String redirect_uri;
    String scope;
    String state;
}
