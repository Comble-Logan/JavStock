package com.logistics.model;

import com.logistics.exception.CapacityExceededException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PackageArticleTest {

    @Test
    void testUpdateQuantity() throws CapacityExceededException {
        ArticleStandard articleStandard = new ArticleStandard("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1 );
        PackageArticle packageArticle = new PackageArticle("Pkg1", articleStandard, 12, 0.1, 35.0, 35.0, 35.0);
        packageArticle.addArticle(6);

        assertEquals(6, packageArticle.getQuantity());
    }

    @Test
    void testUpdateQuantityThrowException() {
        ArticleStandard articleStandard = new ArticleStandard("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1 );
        PackageArticle packageArticle = new PackageArticle("Pkg1", articleStandard, 12, 0.1, 35.0, 35.0, 35.0);
        assertThrows(CapacityExceededException.class, () -> packageArticle.addArticle(13));
    }
}
