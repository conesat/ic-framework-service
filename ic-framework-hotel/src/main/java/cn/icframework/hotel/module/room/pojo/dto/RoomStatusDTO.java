package cn.icframework.hotel.module.room.pojo.dto;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoomStatusDTO {
    private Long hotelId;
    private Long buildingId;
    private Long floorId;
    private LocalDate startDate;
    private LocalDate endDate;
}
