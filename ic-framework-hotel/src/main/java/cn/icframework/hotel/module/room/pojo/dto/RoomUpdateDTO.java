package cn.icframework.hotel.module.room.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoomUpdateDTO {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateStart;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateEnd;
    @Nonnull
    private Long roomId;
}
