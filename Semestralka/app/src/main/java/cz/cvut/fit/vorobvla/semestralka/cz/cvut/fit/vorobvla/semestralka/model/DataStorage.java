package cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model;

import android.util.Log;
import android.view.LayoutInflater;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;

//import cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model.Article;

/**
 * Created by vova on 3/13/15.
 */

public class DataStorage {
//    private static ArrayList<Article> articles;
//    private static ArrayList<Article> articles;

    static {
    /*    articles = new ArrayList<>();*/
        for (int i = 0; i < 15; i++) {
            String text = "";
            for (int j = 0; j < 50; j++) {
                text += "Test article " + i + ". ";
            }
            Article.addArticle(new Article("Test Title " + i, text,
                    new Date(System.currentTimeMillis()), "Test Author", "Test User", "test.com"));
           // Log.w("Articles::: ", Article.getAllArray().toString());
        }/*
            articles.add(new Article("Test Title " + i, text,
                    new Date(System.currentTimeMillis()), "Test Author", "Test User", "test.com"));
            //articles.add(new Article("Test title " + i, "Test article " + i));
        }*/

    }

 /*   public static ArrayList<Article> getArticles() {
        return articles;
    }*/

/*   public static int getSize(){
        return articles.size();
    }

    public static Article getArticleById(int id){
        return articles.get(id);
    }*/
}
