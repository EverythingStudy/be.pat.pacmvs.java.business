package cn.staitech.anno.domain.vo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class NettyPortVO {
    
    @Value("${netty.port}")
    private Integer nettyPort;
    
}
