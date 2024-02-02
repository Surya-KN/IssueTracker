package com.suryakn.IssueTracker.service.duplicateTicket;

import lombok.SneakyThrows;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component

public class TicketIndexer {
    private final Directory indexDirectory;
    private final Analyzer analyzer;
    private IndexWriter indexWriter;


    public TicketIndexer() throws IOException {
        Path tempIndexPath = Files.createTempDirectory("luceneIndex");
        this.indexDirectory = new MMapDirectory(tempIndexPath);
        this.analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        this.indexWriter = new IndexWriter(indexDirectory, config);

    }

    public void indexTicket(Long projectId, Long ticketId, String content) throws IOException {
        Document doc = new Document();
        doc.add(new LongField("projectId", projectId, Field.Store.YES));
        doc.add(new LongField("ticketId", ticketId, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        indexWriter.addDocument(doc);
        indexWriter.commit();

    }

    public List<String> searchDuplicates(Long projectId, String content) throws IOException {
        List<String> duplicateTicketIds = new ArrayList<>();
        try (DirectoryReader reader = DirectoryReader.open(indexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new BM25Similarity());
            Query query = buildQuery(projectId, content);
            TopDocs topDocs = searcher.search(query, 10); // Adjust the number of search results as needed

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String ticketId = doc.get("ticketId");
                duplicateTicketIds.add(ticketId);
            }
        }
        return duplicateTicketIds;
    }


    @SneakyThrows
    private Query buildQuery(Long projectId, String content) {

        BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
        // Add a term query for the projectId
        TermQuery projectQuery = new TermQuery(new Term("projectId", String.valueOf(projectId)));
        queryBuilder.add(projectQuery, BooleanClause.Occur.MUST);
        // Parse the content to create a query
        QueryParser contentParser = new QueryParser("content", analyzer);
        Query contentQuery = contentParser.parse(content);
        queryBuilder.add(contentQuery, BooleanClause.Occur.MUST);

        return queryBuilder.build();
    }

    // Call this method during application shutdown to properly close resources

    public void close() throws IOException {
        indexWriter.close();
        indexDirectory.close();
    }
}
