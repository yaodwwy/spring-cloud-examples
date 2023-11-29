package sample.add;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import sample.model.User;

import java.net.URI;
import java.util.LinkedHashMap;

/**
 *
 */
@Tag(name = "认证服务客户端")
@FeignClient(name = "iam", configuration = {FeignClientLoggingConfiguration.class})
public interface IamAccessTokenFeignApi {

    @Operation(summary = "获取 Token")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<IamTokenResult> getToken(URI uri, @SpringQueryMap IamTokenRequest params, @RequestHeader("Authorization") String authHeader);
    @Operation(summary = "获取 UserInfo")
    @GetMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<LinkedHashMap<String, String>> getUserInfo(URI uri, @RequestHeader("Authorization") String authHeader);
}
