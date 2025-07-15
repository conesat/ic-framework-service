package cn.icframework.hotel.module.supporting;

import cn.icframework.mybatis.annotation.Id;
import cn.icframework.mybatis.annotation.Table;
import cn.icframework.mybatis.annotation.TableField;
import cn.icframework.mybatis.consts.IdType;
import cn.icframework.hotel.common.enums.SupportingTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(value = "hotel_supporting", comment = "配套设施")
public class Supporting {

    @Id(idType = IdType.AUTO)
    private Long id;

    /**
    * 名称
    */
    @TableField(value = "name", comment = "名称")
    private String name;

    /**
    * 图标文件id
    */
    @TableField(value = "icon_file_id", comment = "图标文件id")
    private Long iconFileId;

    /**
    * 图标文件地址
    */
    @TableField(value = "icon_file_url", comment = "图标文件地址")
    private String iconFileUrl;

    /**
    * 配套设施类型
    */
    @TableField(value = "supporting_type", comment = "配套设施类型")
    private SupportingTypeEnum supportingType;

    /**
    * 一般创建对象通过这个方法
    * 可以统一为对象赋初始值
    */
    public static Supporting def() {
        Supporting def = new Supporting();
        return def;
    }
}
