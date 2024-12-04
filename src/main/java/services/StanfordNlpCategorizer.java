package services;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

public class StanfordNlpCategorizer {

    private StanfordCoreNLP pipeline;

    public StanfordNlpCategorizer() {
        // Configure the pipeline with NER and Tokenization
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        this.pipeline = new StanfordCoreNLP(props);
    }

    // Define categories and associated keywords/entities
    private static final Map<String, List<String>> CATEGORY_KEYWORDS = new HashMap<>();

    static {
        CATEGORY_KEYWORDS.put("Technology", Arrays.asList("AI", "technology", "software", "innovation", "gadget"));
        CATEGORY_KEYWORDS.put("Health", Arrays.asList("health", "medicine", "fitness", "diet", "wellness"));
        CATEGORY_KEYWORDS.put("Sports", Arrays.asList("sports", "game", "football", "cricket", "tournament"));
        CATEGORY_KEYWORDS.put("AI", Arrays.asList("artificial intelligence", "machine learning", "neural network", "deep learning"));
    }

    public String categorize(String text) {
        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        // Extract Named Entities
        List<String> extractedKeywords = new ArrayList<>();
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (!ner.equals("O")) { // Only consider named entities
                    extractedKeywords.add(token.word().toLowerCase());
                }
            }
        }

        // Match keywords to categories
        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (extractedKeywords.contains(keyword.toLowerCase())) {
                    return entry.getKey(); // Return the matched category
                }
            }
        }
        return null; // Return null if no category matches
    }
}

