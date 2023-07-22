package cn.wenhe9.pinda.core.log.event;

import cn.wenhe9.pinda.core.log.entity.OptLogDto;
import org.springframework.context.ApplicationEvent;

/**
 * @description: 操作日志事件
 * @author: DuJinliang
 * @create: 2023/7/22
 */
public class OptLogEvent extends ApplicationEvent {
    public OptLogEvent(OptLogDto optLog) {
        super(optLog);
    }
}
