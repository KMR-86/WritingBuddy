package com.example.WritingBuddy.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WritingAnalysisResponse {
    private String grammarAnalysis;
    private String additionalIdeas;
    private String counterArguments;
}
