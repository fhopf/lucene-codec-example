lucene-codec-example
====================

Example code on using the new Lucene Codec API. See http://blog.florian-hopf.de/2012/12/looking-at-plaintext-lucene-index.html for details. 

To execute the test run `gradle test`. Two directories will be created in your tmp directory, e.g. /tmp/lucene-plaintext and /tmp/lucene-mixed on Linux. lucene-plaintext contains a plaintext index, lucene-mixed a mixes index with a plaintext segment and a standard segment.
