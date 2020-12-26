package com.qigetech.mark.result.label.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by liuxuanjun on 2019-06-09
 * Project : qigetech-mark
 */
@Data
public class LabelResultDTO {


    /**
     * originId : 8
     * words : [{"word":"这是","location":1,"partOfSpeech":"n"},{"word":"一个","location":2,"partOfSpeech":"n"},{"word":"测试","location":3,"partOfSpeech":"n"},{"word":"1","location":4,"partOfSpeech":"num"}]
     */

    private Integer originId;

    private List<Word> words;

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public static class Word {
        /**
         * word : 这是
         * location : 1
         * partOfSpeech : n
         */

        private String word;
        private int location;
        private String partOfSpeech;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public String getPartOfSpeech() {
            return partOfSpeech;
        }

        public void setPartOfSpeech(String partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
        }
    }
}
