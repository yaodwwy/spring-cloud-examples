package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sample.model.User;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class VersionController {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @GetMapping("/version")
    public String version(HttpServletRequest request) {
        HttpSession session = request.getSession();
        boolean aNew = session.isNew();
        Object time = session.getAttribute("time");
        if(time != null){
            return objectMapper.writeValueAsString(time);
        }else {
            session.setAttribute("time", LocalDateTime.now()+"-" + aNew);
            return objectMapper.writeValueAsString(time);
        }


//        Map<String, String> map = System.getenv();
//        String getenv = System.getenv("APP_VERSION");
//        return StringUtils.isBlank(getenv) ? objectMapper.writeValueAsString(map) : getenv;
    }
}
