package de.fhopf.lucene.codec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;


public class TestCodec {

    private static File plaintextDir;
    private static File mixedDir;
    
    @BeforeClass
    public static void setUpDirectories() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        plaintextDir = assureDirectoryExists(new File(tmpDir, "lucene-plaintext"));
        mixedDir = assureDirectoryExists(new File(tmpDir, "lucene-mixed"));
    }

    private static File assureDirectoryExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    
    @Test
    public void indexPlaintextDocuments() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        // recreate the index on each execution
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setCodec(new SimpleTextCodec());
        Directory luceneDir = FSDirectory.open(plaintextDir);
        try (IndexWriter writer = new IndexWriter(luceneDir, config)) {
            writer.addDocument(Arrays.asList(
                    new TextField("title", "The title of my first document", Store.YES),
                    new TextField("content", "The content of the first document", Store.NO)));

            writer.addDocument(Arrays.asList(
                    new TextField("title", "The title of the second document", Store.YES),
                    new TextField("content", "And this is the content", Store.NO)));
        }
        assertDocumentsAreThere(luceneDir, 2);
    }
    
    @Test
    public void indexMixedDocuments() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        // recreate the index on each execution
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setCodec(new SimpleTextCodec());
        Directory luceneDir = FSDirectory.open(mixedDir);
        try (IndexWriter writer = new IndexWriter(luceneDir, config)) {
            writer.addDocument(Arrays.asList(
                    new TextField("title", "The title of my first document", Store.YES),
                    new TextField("content", "The content of the first document", Store.NO)));

            writer.addDocument(Arrays.asList(
                    new TextField("title", "The title of the second document", Store.YES),
                    new TextField("content", "And this is the content", Store.NO)));
        }

        config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        try (IndexWriter writer = new IndexWriter(luceneDir, config)) {
            writer.addDocument(Arrays.asList(
                    new TextField("title", "The title of my first document", Store.YES),
                    new TextField("content", "The content of the first document", Store.NO)));
        }

        assertDocumentsAreThere(luceneDir, 3);
    }


    private void assertDocumentsAreThere(Directory dir, int amount) throws IOException {
        try (IndexReader reader = DirectoryReader.open(dir)) {
            assertEquals(amount, reader.numDocs());
        }
    }
}
