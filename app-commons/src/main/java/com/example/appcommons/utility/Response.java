package com.example.appcommons.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    private String message;
    private boolean status;
    private Object data;
    private LocalDateTime sendDateTime;


}
