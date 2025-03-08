package com.example.WritingBuddy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.WritingBuddy.service.HuggingFaceService;
import com.example.WritingBuddy.model.WritingAnalysisResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class WritingAnalysisController {

    private final HuggingFaceService huggingFaceService;

    @Autowired
    public WritingAnalysisController(HuggingFaceService huggingFaceService) {
        this.huggingFaceService = huggingFaceService;
    }

    @PostMapping("/analyze")
    public WritingAnalysisResponse analyzeText(@RequestBody Map<String, String> request) {
        String text = request.get("text");
        WritingAnalysisResponse analysis = WritingAnalysisResponse.builder().build();
        analysis.setGrammarAnalysis(huggingFaceService.checkGrammar(text));
        analysis.setAdditionalIdeas(huggingFaceService.getAdditionalIdeas(text));
        analysis.setCounterArguments(huggingFaceService.getCounterArguments(text));
        return analysis;
    }
}

