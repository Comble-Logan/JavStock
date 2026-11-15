package com.logistics.model;


import com.logistics.exception.CapacityExceededException;
import com.logistics.exception.WeightExceededException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class PalletTest {

    @Test
    void testAddPackageArticleOnPallet() throws WeightExceededException, CapacityExceededException {
        Pallet pallet = new Pallet("Pal1", 15.0, 800.0);
        ArticleStandard articleStandard = new ArticleStandard("Art1", "Article Test","REF-ART-111", 3.0, 7.3, 11.0, 9.1 );
        PackageArticle packageArticle = new PackageArticle("Pkg1", articleStandard, 12, 0.1, 35.0, 35.0, 35.0);
        packageArticle.addArticle(9);
        assertEquals(0, pallet.getPackageArticles().size());
        pallet.addPackageArticle(packageArticle);
        assertEquals(1, pallet.getPackageArticles().size());
    }
}
