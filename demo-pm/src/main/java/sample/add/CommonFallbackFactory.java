package sample.add;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 通用 Feign 接口失败回调Bean
 */

@Component("CommonFallbackFactory")
public class CommonFallbackFactory<T> implements FallbackFactory<T> {

    @Override
    public T create(Throwable cause) {
        throw new RuntimeException(cause);
    }
}
