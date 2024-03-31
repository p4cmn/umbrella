package com.artem.umbrella.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ResponseDto {
    private LocalDateTime time;
    private String message;
}
