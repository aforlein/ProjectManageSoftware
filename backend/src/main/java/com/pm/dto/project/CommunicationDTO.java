package com.pm.dto.project;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CommunicationDTO {
    private LocalDate communicationDate;
    private String type;
    private String participant;
    private String content;
    private String notes;
}
