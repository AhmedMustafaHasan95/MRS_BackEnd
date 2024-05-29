package com.ejada.meetingroomreservation.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomDTO {
    private long id;
    @NotBlank(message = "Room name is required")
    private String name;
    private OfficeDTO office;

}
