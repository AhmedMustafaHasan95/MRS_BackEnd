package com.ejada.meetingroomreservation.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedEventReq {
	private String startDate;
	private String endDate;
}
