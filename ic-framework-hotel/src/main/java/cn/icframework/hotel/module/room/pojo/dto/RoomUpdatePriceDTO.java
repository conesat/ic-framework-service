package cn.icframework.hotel.module.room.pojo.dto;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomUpdatePriceDTO extends RoomUpdateDTO {
    @Nonnull
    private Double price;
}
