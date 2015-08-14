# README [![Build Status](https://travis-ci.org/atilika/kuromoji.svg?branch=master)](https://travis-ci.org/atilika/kuromoji)

## Building

In order to build Kuromoji from source, please run the following command:

``` shell
    $ mvn clean package
```

This will download all source dictionary data and build Kuromoji with all dictionaries. The following Kuromoji jars will then be available:

```
kuromoji-core/target/kuromoji-core-0.9-SNAPSHOT.jar
kuromoji-unidic/target/kuromoji-ipadic-0.9-SNAPSHOT.jar
kuromoji-unidic/target/kuromoji-unidic-0.9-SNAPSHOT.jar
kuromoji-unidic-kanaaccent/target/kuromoji-unidic-kanaaccent-0.9-SNAPSHOT.jar
kuromoji-naist-jdic/target/kuromoji-naist-jdic-0.9-SNAPSHOT.jar
kuromoji-jumandic/target/kuromoji-jumandic-0.9-SNAPSHOT.jar
```

The following additional build options are available:

* `-DskipCompileDictionary`  Do not recompile the dictionaries
* `-DskipDownloadDictionary` Do not download source dictionaries
* `-DbenchmarkTokenizers` Profile each tokenizer during the package phase using content from Japanese Wikipedia
* `-DskipDownloadWikipedia` Prevent the compressed version of the Japanese Wikipedia (~765 MB) from being downloaded during profiling, i.e. if it has already been downloaded.

## Using Kuromoji

To use Kuromoji, you add `kuromoji-core-0.9-SNAPSHOT.jar` and your chosen dictionary's jar to your project. For many projects, the IPADIC variant `kuromoji-ipadic-0.9-SNAPSHOT.jar` should suffice.

The following code example demonstrates how to use the Kuromoji tokenizer:

```java
package com.atilika.kuromoji.example;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import java.util.List;

public class KuromojiExample {
    public static void main(String[] args) {
        Tokenizer tokenizer = new Tokenizer() ;
        List<Token> tokens = tokenizer.tokenize("お寿司が食べたい。");
        for (Token token : tokens) {
            System.out.println(token.getSurfaceForm() + "\t" + token.getAllFeatures());
        }
    }
}
```

When running this program, you will get the following output

```
お　　　接頭詞,名詞接続,*,*,*,*,お,オ,オ
寿司　　名詞,一般,*,*,*,*,寿司,スシ,スシ
が　　　助詞,格助詞,一般,*,*,*,が,ガ,ガ
食べ　　動詞,自立,*,*,一段,連用形,食べる,タベ,タベ
たい　　助動詞,*,*,*,特殊・タイ,基本形,たい,タイ,タイ
。　　　記号,句点,*,*,*,*,。,。,。
```

## Contributing

Please open up issues if you have a feature request.  We also welcome contributions through pull requests.

You will retain copyright to your own contributions, but need to license them using the Apache License, Version 2.0.
All contributors will be mentioned in the `CONTRIBUTORS.md` file.

# About us

We are a small team of experienced software engineers based in Tokyo who offers technologies and good advice in the field of search, natural language processing and big data analytics.

Please feel free to contact us at kuromoji@atilika.com if you have any questions or need help.
