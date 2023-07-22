package cn.wenhe9.pinda.core.log.event;

import cn.wenhe9.pinda.core.log.entity.OptLogDto;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import java.util.function.Consumer;

/**
 * @description: 操作日志监听器
 * @author: DuJinliang
 * @create: 2023/7/22
 */
public class OptLogEventListener {
    private final Consumer<OptLogDto> consumer;

    public OptLogEventListener(Consumer<OptLogDto> consumer) {
        this.consumer = consumer;
    }

    @Async
    @EventListener(OptLogEvent.class)
    public void saveOptLog(OptLogEvent optLogEvent) {
        OptLogDto optLog = (OptLogDto) optLogEvent.getSource();
        consumer.accept(optLog);
    }
}
