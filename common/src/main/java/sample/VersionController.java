package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class VersionController {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @GetMapping("/version")
    public String version() {
        Map<String, String> map = System.getenv();
        String getenv = System.getenv("APP_VERSION");
        return StringUtils.isBlank(getenv) ? objectMapper.writeValueAsString(map) : getenv;
    }
}
