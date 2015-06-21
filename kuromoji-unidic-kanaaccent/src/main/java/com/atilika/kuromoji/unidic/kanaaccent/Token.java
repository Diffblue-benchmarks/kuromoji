/**
 * Copyright 2010-2015 Atilika Inc. and contributors (see CONTRIBUTORS.md)
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

package com.atilika.kuromoji.unidic.kanaaccent;

import com.atilika.kuromoji.AbstractToken;
import com.atilika.kuromoji.dict.Dictionary;
import com.atilika.kuromoji.unidic.kanaaccent.dict.DictionaryField;
import com.atilika.kuromoji.viterbi.ViterbiNode;

public class Token extends AbstractToken {
    public Token(int wordId, String surfaceForm, ViterbiNode.Type type, int position, Dictionary dictionary) {
        super(wordId, surfaceForm, type, position, dictionary);
    }

    public String getPosLevel1() {
        return getFeature(DictionaryField.POS_LEVEL_1);
    }

    public String getPosLevel2() {
        return getFeature(DictionaryField.POS_LEVEL_2);
    }

    public String getPosLevel3() {
        return getFeature(DictionaryField.POS_LEVEL_3);
    }

    public String getPosLevel4() {
        return getFeature(DictionaryField.POS_LEVEL_4);
    }

    public String getConjugationForm() {
        return getFeature(DictionaryField.CONJUGATION_FORM);
    }

    public String getConjugationType() {
        return getFeature(DictionaryField.CONJUGATION_TYPE);
    }

    public String getLemmaReadingForm() {
        return getFeature(DictionaryField.LEMMA_READING_FORM);
    }

    public String getLemma() {
        return getFeature(DictionaryField.LEMMA);
    }

    public String getWrittenForm() {
        return getFeature(DictionaryField.WRITTEN_FORM);
    }

    public String getPronunciation() {
        return getFeature(DictionaryField.PRONUNCIATION);
    }

    public String getWrittenBaseForm() {
        return getFeature(DictionaryField.WRITTEN_BASE_FORM);
    }

    public String getPronunciationBaseForm() {
        return getFeature(DictionaryField.PRONUNCIATION_BASE_FORM);
    }

    public String getLanguageType() {
        return getFeature(DictionaryField.LANGUAGE_TYPE);
    }

    public String getInitialSoundAlterationType() {
        return getFeature(DictionaryField.INITIAL_SOUND_ALTERATION_TYPE);
    }

    public String getInitialSoundAlterationForm() {
        return getFeature(DictionaryField.INITIAL_SOUND_ALTERATION_FORM);
    }

    public String getFinalSoundAlterationType() {
        return getFeature(DictionaryField.FINAL_SOUND_ALTERATION_TYPE);
    }

    public String getFinalSoundAlterationForm() {
        return getFeature(DictionaryField.FINAL_SOUND_ALTERATION_FORM);
    }

    public String getKana() {
        return getFeature(DictionaryField.KANA);
    }

    public String getKanaBase() {
        return getFeature(DictionaryField.KANA_BASE);
    }

    public String getForm() {
        return getFeature(DictionaryField.FORM);
    }

    public String getFormBase() {
        return getFeature(DictionaryField.FORM_BASE);
    }

    public String getInitialConnectionType() {
        return getFeature(DictionaryField.INITIAL_CONNECTION_TYPE);
    }

    public String getFinalConnectionType() {
        return getFeature(DictionaryField.FINAL_CONNECTION_TYPE);
    }

    public String getAccentType() {
        return getFeature(DictionaryField.ACCENT_TYPE);
    }

    public String getAccentConnectionType() {
        return getFeature(DictionaryField.ACCENT_CONNECTION_TYPE);
    }

    public String getAccentModificationType() {
        return getFeature(DictionaryField.ACCENT_MODIFICATION_TYPE);
    }

}
