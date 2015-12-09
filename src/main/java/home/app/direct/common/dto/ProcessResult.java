package home.app.direct.common.dto;

import lombok.Data;

@Data
public class ProcessResult {
    private String identifier;
    private EventErrorStatus eventStatus;
    public ProcessResult(String identifier){this.identifier = identifier;}
    public ProcessResult(EventErrorStatus eventStatus){
        this.eventStatus = eventStatus;
    }
}
