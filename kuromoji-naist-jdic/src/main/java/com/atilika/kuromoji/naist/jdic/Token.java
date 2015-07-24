/**
 * Copyright © 2010-2015 Atilika Inc. and contributors (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  A copy of the
 * License is distributed with this work in the LICENSE.md file.  You may
 * also obtain a copy of the License from
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.atilika.kuromoji.naist.jdic;

import com.atilika.kuromoji.AbstractToken;
import com.atilika.kuromoji.dict.Dictionary;
import com.atilika.kuromoji.naist.jdic.dict.DictionaryField;
import com.atilika.kuromoji.viterbi.ViterbiNode;

/**
 * NAIST-JDIC token produced by the NAIST-JDIC tokenizer with
 * various morphological features
 */
public class Token extends AbstractToken {

    public Token(int wordId,
                 String surfaceForm,
                 ViterbiNode.Type type,
                 int position,
                 Dictionary dictionary) {
        super(wordId, surfaceForm, type, position, dictionary);
    }

    /**
     * Gets the 1st level part-of-speech tag for this token (品詞細分類1)
     *
     * @return 1st level part-of-speech tag, not null
     */
    public String getPosLevel1() {
        return this.getFeature(DictionaryField.POS_LEVEL_1);
    }

    /**
     * Gets the 2nd level part-of-speech tag for this token (品詞細分類2)
     *
     * @return 2nd level part-of-speech tag, not null
     */
    public String getPosLevel2() {
        return this.getFeature(DictionaryField.POS_LEVEL_2);
    }

    /**
     * Gets the 3rd level part-of-speech tag for this token (品詞細分類3)
     *
     * @return 3rd level part-of-speech tag, not null
     */
    public String getPosLevel3() {
        return this.getFeature(DictionaryField.POS_LEVEL_3);
    }

    /**
     * Gets the 4th level part-of-speech tag for this token (品詞細分類4)
     *
     * @return 4th level part-of-speech tag, not null
     */
    public String getPosLevel4() {
        return this.getFeature(DictionaryField.POS_LEVEL_4);
    }

    /**
     * Gets the pronunciation for this token (発音)
     *
     * @return pronunciation, not null
     */
    public String getPronunciation() {
        return this.getFeature(DictionaryField.PRONUNCIATION);
    }

    /**
     * Gets the conjugation type for this token (活用型), if applicable
     * <p>
     * If this token does not have a conjugation type, return *
     *
     * @return conjugation type, not null
     */
    public String getConjugationType() {
        return this.getFeature(DictionaryField.CONJUGATION_TYPE);
    }

    /**
     * Gets the conjugation form for this token (活用形), if applicable
     * <p>
     * If this token does not have a conjugation form, return *
     *
     * @return conjugation form, not null
     */
    public String getConjugationForm() {
        return this.getFeature(DictionaryField.CONJUGATION_FORM);
    }

    /**
     * Gets the base form (also called dictionary form) for this token (基本形)
     *
     * @return base form, not null
     */
    public String getBaseForm() {
        return this.getFeature(DictionaryField.BASE_FORM);
    }

    /**
     * Gets the reading for this token (読み) in katakana script
     *
     * @return reading, not null
     */
    public String getReading() {
        return this.getFeature(DictionaryField.READING);
    }

    /**
     * Return the transcription variation for this token
     *
     * @return trascription variation, not null
     */
    public String getTranscriptionVariation() {
        return this.getFeature(DictionaryField.TRANSCRIPTION_VARIATION);
    }

    /**
     * Gets the compound word information for this token (複合語出力)
     *
     * @return the token's compound word information, not null
     */
    public String getCompoundInformation() {
        return this.getFeature(DictionaryField.COMPOUND_INFORMATION);
    }
}
