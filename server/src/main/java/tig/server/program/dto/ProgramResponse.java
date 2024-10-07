package tig.server.program.dto;

import lombok.Getter;

@Getter
public class ProgramResponse {
    private Long programId;
    private String programName;

    public ProgramResponse(Long programId, String programName) {
        this.programId = programId;
        this.programName = programName;
    }
}
