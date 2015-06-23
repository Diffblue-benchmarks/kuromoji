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
package com.atilika.kuromoji.compile;

import com.atilika.kuromoji.dict.AbstractDictionaryEntry;
import com.atilika.kuromoji.dict.BufferEntry;
import com.atilika.kuromoji.dict.GenericDictionaryEntry;
import com.atilika.kuromoji.util.FeatureInfoMap;
import com.atilika.kuromoji.util.StringValueMapBuffer;
import com.atilika.kuromoji.util.TokenInfoBuffer;
import com.atilika.kuromoji.util.WordIdMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public abstract class AbstractTokenInfoDictionaryCompiler<T extends AbstractDictionaryEntry> implements Compiler {

    private static final String TOKEN_INFO_DICTIONARY_FILENAME = "tid.dat";
    private static final String FEATURE_MAP_FILENAME = "tid_fet.dat";
    private static final String POS_MAP_FILENAME = "tid_pos.dat";
    private static final String TARGETMAP_FILENAME = "tid_map.dat";

    private String encoding;

    private List<BufferEntry> bufferEntries = new ArrayList<>();
    protected FeatureInfoMap posInfo = new FeatureInfoMap();
    protected FeatureInfoMap otherInfo = new FeatureInfoMap();
    private List<String> surfaces = new ArrayList<>();
    protected WordIdMap wordIdMap = new WordIdMap();

    public AbstractTokenInfoDictionaryCompiler(String encoding) {
        this.encoding = encoding;
    }

    public void analyzeTokenInfo(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, encoding));
        String line;

        while ((line = reader.readLine()) != null) {
            T entry = parse(line);

            GenericDictionaryEntry dictionaryEntry = generateGenericDictionaryEntry(entry);

            posInfo.mapFeatures(dictionaryEntry.getPosFeatures());
        }
    }

    public void readTokenInfo(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, encoding));
        String line;
        int entryCount = posInfo.getEntryCount();

        while ((line = reader.readLine()) != null) {
            T entry = parse(line);

            GenericDictionaryEntry dictionaryEntry = generateGenericDictionaryEntry(entry);

            short leftId = dictionaryEntry.getLeftId();
            short rightId = dictionaryEntry.getRightId();
            short wordCost = dictionaryEntry.getWordCost();

            List<String> allPosFeatures = dictionaryEntry.getPosFeatures();

            List<Integer> posFeatureIds = posInfo.mapFeatures(allPosFeatures);

            List<String> featureList = dictionaryEntry.getFeatures();
            List<Integer> otherFeatureIds = otherInfo.mapFeatures(featureList);

            BufferEntry bufferEntry = new BufferEntry();
            bufferEntry.tokenInfo.add(leftId);
            bufferEntry.tokenInfo.add(rightId);
            bufferEntry.tokenInfo.add(wordCost);

            if (entriesFitInAByte(entryCount)) {
                List<Byte> posFeatureIdBytes = createPosFeatureIds(posFeatureIds);
                bufferEntry.posInfo.addAll(posFeatureIdBytes);
            } else {
                for (Integer posFeatureId : posFeatureIds) {
                    bufferEntry.tokenInfo.add(posFeatureId.shortValue());
                }
            }

            bufferEntry.features.addAll(otherFeatureIds);

            bufferEntries.add(bufferEntry);
            surfaces.add(dictionaryEntry.getSurface());
        }
    }

    protected abstract GenericDictionaryEntry generateGenericDictionaryEntry(T entry);

    protected abstract T parse(String line);

    @Override
    public void compile() throws IOException {

    }

    private boolean entriesFitInAByte(int entryCount) {
        return entryCount <= 0xff;
    }

    private List<Byte> createPosFeatureIds(List<Integer> posFeatureIds) {
        List<Byte> posFeatureIdBytes = new ArrayList<>();
        for (Integer posFeatureId : posFeatureIds) {
            posFeatureIdBytes.add(posFeatureId.byteValue());
        }
        return posFeatureIdBytes;
    }


    public InputStream combinedSequentialFileInputStream(File dir) throws FileNotFoundException {
        List<FileInputStream> fileInputStreams = new ArrayList<>();
        List<File> files = getCsvFiles(dir);

        for (File file : files) {
            fileInputStreams.add(new FileInputStream(file));
        }

        return new SequenceInputStream(Collections.enumeration(fileInputStreams));
    }

    public List<File> getCsvFiles(File dir) {
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".csv");
            }
        };

        ArrayList<File> files = new ArrayList<>();
        Collections.addAll(files, dir.listFiles(filter));
        Collections.sort(files);
        return files;
    }

    public void addMapping(int sourceId, int wordId) {
        wordIdMap.addMapping(sourceId, wordId);
    }

    public List<String> getSurfaces() {
        return surfaces;
    }

    public void write(String directoryName) throws IOException {
        writeDictionary(directoryName + File.separator + TOKEN_INFO_DICTIONARY_FILENAME);
        writeMap(directoryName + File.separator + POS_MAP_FILENAME, posInfo);
        writeMap(directoryName + File.separator + FEATURE_MAP_FILENAME, otherInfo);
        writeTargetMap(directoryName + File.separator + TARGETMAP_FILENAME);
    }


    protected void writeMap(String filename, FeatureInfoMap map) throws IOException {
        TreeMap<Integer, String> features = map.invert();

        StringValueMapBuffer mapBuffer = new StringValueMapBuffer(features);
        FileOutputStream fos = new FileOutputStream(filename);
        mapBuffer.write(fos);
    }

    protected void writeDictionary(String filename) throws IOException {
        TokenInfoBuffer tokenInfoBuffer = new TokenInfoBuffer(bufferEntries);
        FileOutputStream fos = new FileOutputStream(filename);
        tokenInfoBuffer.write(fos);
    }

    protected void writeTargetMap(String filename) throws IOException {
        wordIdMap.write(new FileOutputStream(filename));
    }

}
