package cz.cvut.fit.vorobvla.semestralka.cz.cvut.fit.vorobvla.semestralka.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by vova on 3/15/15.
 */
public class Article {
    private String title;
    private String text;
    private Date date;
    private String author;
    private String postedBy;
    private String link;
    private static ArrayList<Article> all;

    static {
        all = new ArrayList<>();
    }

    public Article(String title, String text, Date date, String author, String postedBy, String link) {
        if (title == null){
            return;
        }
        this.title = title;
        this.text = text;
        this.date = date;
        this.author = author;
        this.postedBy = postedBy;
        this.link = link;
//        addArticle(this);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public String getLink(){
        return link;
    }



    /**
     *
     * @param article
     * @throws RuntimeException if failure while adding an article occurs
     */
    public static void addArticle(Article article) throws RuntimeException{
        if (!all.add(article)){
            throw new RuntimeException("Failure while adding article");
        };
    }

    /**
     *
     * @param article
     * @throws RuntimeException if failure while removing an article occurs
     */
    public static void removeArticle(Article article) throws RuntimeException{
        if (!all.remove(article)){
            throw new RuntimeException("Failure while removing article");
        };
    }

    public static Article[] getAllArray(){
        return Arrays.copyOf(all.toArray(), all.size(), Article[].class);
    }

}

