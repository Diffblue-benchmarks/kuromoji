/**
 * Copyright 2010-2013 Atilika Inc. and contributors (see CONTRIBUTORS.md)
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
package com.atilika.kuromoji.util;

import com.atilika.kuromoji.dict.UnknownDictionary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class UnknownDictionaryBuilder {

	private String encoding = "euc-jp";

    public UnknownDictionaryBuilder(String encoding) {
		this.encoding = encoding;
	}
	
	public UnknownDictionary build(String dirname) throws IOException {
		UnknownDictionary unkDictionary = readDictionaryFile(dirname + File.separator + "unk.def");  //Should be only one file
		readCharacterDefinition(dirname + File.separator + "char.def", unkDictionary);
		return unkDictionary;
	}
	
	public UnknownDictionary readDictionaryFile(String filename)
		throws IOException {
		return readDictionaryFile(filename, encoding);
	}

	public UnknownDictionary readDictionaryFile(String filename, String encoding)
		throws IOException {
		UnknownDictionary dictionary = new UnknownDictionary();
		UnknownDictionaryEntryParser dictionaryEntryParser = new UnknownDictionaryEntryParser();
		
		FileInputStream inputStream = new FileInputStream(filename);
		InputStreamReader streamReader = new InputStreamReader(inputStream, encoding);
		LineNumberReader lineReader = new LineNumberReader(streamReader);

		String line = null;
		while ((line = lineReader.readLine()) != null) {
			dictionary.put(dictionaryEntryParser.parse(line)); // Probably we don't need to validate entry
		}

		lineReader.close();

		dictionary.generateBufferEntries();

		return dictionary;
	}
	
	public void readCharacterDefinition(String filename, UnknownDictionary dictionary) throws IOException {
		FileInputStream inputStream = new FileInputStream(filename);
		InputStreamReader streamReader = new InputStreamReader(inputStream, encoding);
		LineNumberReader lineReader = new LineNumberReader(streamReader);

		String line = null;
		
		while ((line = lineReader.readLine()) != null) {
			line = line.replaceAll("^\\s", "");
			line = line.replaceAll("\\s*#.*", "");
			line = line.replaceAll("\\s+", " ");
			
			// Skip empty line or comment line
			if(line.length() == 0) {
				continue;
			}
			
			if(line.startsWith("0x")) {	// Category mapping
				String[] values = line.split(" ", 2);	// Split only first space
				
				if(!values[0].contains("..")) {
					int cp = Integer.decode(values[0]);
					dictionary.putCharacterCategory(cp, values[1]);					
				} else {
					String[] codePoints = values[0].split("\\.\\.");
					int cpFrom = Integer.decode(codePoints[0]);
					int cpTo = Integer.decode(codePoints[1]);
					
					for(int i = cpFrom; i <= cpTo; i++){
						dictionary.putCharacterCategory(i, values[1]);					
					}
				}
			} else {	// Invoke definition
				String[] values = line.split(" "); // Consecutive space is merged above
				String characterClassName = values[0];
				int invoke = Integer.parseInt(values[1]);
				int group = Integer.parseInt(values[2]);
				int length = Integer.parseInt(values[3]);
				dictionary.putInvokeDefinition(characterClassName, invoke, group, length);
			}
		}

		lineReader.close();
	}
}
