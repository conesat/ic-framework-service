package cn.icframework.system.module.setting.pojo.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author hzl
 * @createTime 2021-06-22  15:29:00
 * @Description
 */
@Getter
@Setter
public class ActivationInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String md5Hash;
    private String code;
    private Long outTime;
    private String grant;
    private Long generationTime;
    private String author;
    private Boolean forever;//永久激活

   public String getMd5Code(){
       return code+grant+author+outTime+generationTime;
   }
}
