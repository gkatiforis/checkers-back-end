
package com.katiforis.top10.services.impl;

import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
@Slf4j
public class WordHandler {
    private static List<String> greekCh = Arrays.asList("ΑΙ", "ΕΙ", "Η", "ΟΙ", "Υ", "Ω");

    public static String convert(String word) {
        log.debug("Start WordHandler.convert");
        word = stripAccents(word);
        for (String c : greekCh) {
            String mapped = map(c);

            if (c.equalsIgnoreCase("Υ")) {
                int i = word.indexOf(c);
                if (i == 0) {
                    StringBuilder myName = new StringBuilder(word);
                    myName.setCharAt(i, 'Ι');
                    word = myName.toString();
                }
                while (i > 0) {

                    if (word.charAt(i - 1) != 'Ο') {
                        StringBuilder myName = new StringBuilder(word);
                        myName.setCharAt(i, 'Ι');
                        word = myName.toString();
                    }
                    i = word.indexOf(c, i + 1);
                }
            } else {
                word = word.replaceAll(c, mapped);
            }

        }
        log.debug("End WordHandler.convert");
        return word;
    }

    private static String map(String str) {
        log.debug("Start WordHandler.map");
        switch (str) {
            case "ΑΙ":
                return "Ε";
            case "ΕΙ":
            case "Η":
            case "ΟΙ":
            case "Υ":
                return "Ι";
            case "Ω":
                return "Ο";
            default:
                return str;
        }
    }

    public static String stripAccents(String s) {
        log.debug("Start QuestionHandlerImpl.stripAccents");
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        log.debug("End QuestionHandlerImpl.stripAccents");
        return s.toUpperCase();
    }
}
