package com.suryakn.IssueTracker.service.duplicateTicket;

import lombok.SneakyThrows;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;

@Component
public class OpenNlpPreprocessor {
    private SentenceDetectorME sentenceDetector;
    private Tokenizer tokenizer;


    @SneakyThrows
    public OpenNlpPreprocessor(String sentenceModelPath, String tokenizerModelPath) throws IOException {

        // Load sentence detection model

        try (InputStream modelIn = new FileInputStream(sentenceModelPath)) {
            SentenceModel sentenceModel = new SentenceModel(modelIn);
            sentenceDetector = new SentenceDetectorME(sentenceModel);

        }


        // Load tokenizer model

        try (InputStream modelIn = new FileInputStream(tokenizerModelPath)) {
            TokenizerModel tokenizerModel = new TokenizerModel(modelIn);
            tokenizer = new TokenizerME(tokenizerModel);

        }

    }


    public String[] detectSentences(String text) {
        return sentenceDetector.sentDetect(text);

    }

    public String[] tokenizeText(String text) {
        return tokenizer.tokenize(text);
    }


    public Span[] tokenizeTextToSpans(String text) {
        return tokenizer.tokenizePos(text);
    }
}
