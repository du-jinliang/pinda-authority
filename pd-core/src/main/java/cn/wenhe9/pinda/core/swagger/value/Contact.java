package cn.wenhe9.pinda.core.swagger.value;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 联系人
 * @author: DuJinliang
 * @create: 2023/7/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    /**
     * 联系人
     **/
    private String name = "wenhe9";
    /**
     * 联系人url
     **/
    private String url = "";
    /**
     * 联系人email
     **/
    private String email = "";
}
