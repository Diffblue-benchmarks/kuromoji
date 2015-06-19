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
package com.atilika.kuromoji.dict;

import com.atilika.kuromoji.ResourceResolver;
import com.atilika.kuromoji.io.IntegerArrayIO;
import com.atilika.kuromoji.io.StringArrayIO;

import java.io.IOException;
import java.io.InputStream;

public class UnknownDictionary implements Dictionary {

    private final int[][] entries;

    private final int[][] costs;

    private final String[][] features;

    private final CharacterDefinitions characterDefinition;

    public UnknownDictionary(CharacterDefinitions characterDefinition,
                             int[][] entries,
                             int[][] costs,
                             String[][] features) {
        this.characterDefinition = characterDefinition;
        this.entries = entries;
        this.costs = costs;
        this.features = features;
    }

    // TODO: Only supports a single lookup now
    public int lookup(String text) {
        char character = text.charAt(0);

        if (characterDefinition.isGroup(character)[0] == 0) {
            return 1;
        }

        // Extract unknown word. Characters with the same character class are considered to be part of unknown word
        int charDefinition = characterDefinition.lookup(character)[0];
        int length = 1;

        for (int i = 1; i < text.length(); i++) {
            character = text.charAt(i);

            if (charDefinition == characterDefinition.lookup(character)[0]) {
                length++;
            } else {
                break;
            }
        }

        return length;
    }

    public int[] lookupWordIds(int categoryId) {
        // Returns an array of word ids
        return entries[categoryId];
    }

    @Override
    public int getLeftId(int wordId) {
        return costs[wordId][0];
    }

    @Override
    public int getRightId(int wordId) {
        return costs[wordId][1];
    }

    @Override
    public int getWordCost(int wordId) {
        return costs[wordId][2];
    }

    @Override
    public String getAllFeatures(int wordId) {
        return join(getAllFeaturesArray(wordId));
    }

    @Override
    public String[] getAllFeaturesArray(int wordId) {
        return features[wordId];
    }

    @Override
    public String getFeature(int wordId, int... fields) {
        String[] allFeatures = getAllFeaturesArray(wordId);
        String[] features = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {

            int featureNumber = fields[i];
            features[i] = allFeatures[featureNumber];
        }

        return join(features);
    }

    public CharacterDefinitions getCharacterDefinition() {
        return characterDefinition;
    }

    private String join(String[] values) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);

            if (i < values.length - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }

    public static UnknownDictionary newInstance(ResourceResolver resolver) throws IOException {

        InputStream charDefInput = resolver.resolve("chardef2.dat");

        int[][] definitions = IntegerArrayIO.readSparseArray2D(charDefInput);
        int[][] mappings = IntegerArrayIO.readSparseArray2D(charDefInput);

        CharacterDefinitions characterDefinition = new CharacterDefinitions(
            definitions,
            mappings
        );

        InputStream unkDefInput = resolver.resolve("unkdef2.dat");

        int[][] costs = IntegerArrayIO.readArray2D(unkDefInput);
        int[][] references = IntegerArrayIO.readArray2D(unkDefInput);
        String[][] features = StringArrayIO.readArray2D(unkDefInput);

        UnknownDictionary unknownDictionary = new UnknownDictionary(
            characterDefinition,
            references,
            costs,
            features
        );

        return unknownDictionary;
    }
}
