package com.logistics.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleTest {

    @Test
    void testCreationStandardArticle(){
        ArticleStandard articleStandard = new ArticleStandard("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1 );
        assertEquals("Art1", articleStandard.getId());
        assertEquals("Article Test", articleStandard.getName());
        assertEquals("REF-ART-111", articleStandard.getReference());
        assertEquals(3.0, articleStandard.getWeight(), 0.01);
        assertEquals(730.73, articleStandard.getVolume(), 0.01);
        assertEquals(0, articleStandard.getFragilityLevel());
    }

    @Test
    void testCreationFragileArticle(){
        ArticleFragile article = new ArticleFragile("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1, 3);
        assertEquals(3, article.getFragilityLevel());
    }


}
