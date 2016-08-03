package cz.cvut.fit.vorobvla.semestralka.data;

/**
 * Created by vova on 4/6/15.
 */
public abstract class Constants {
    // database
    public static final int DB_VERSION = 6;
    public static final String DB_NAME = "database";

    // tables
    public static final String ARTICLE_TABLE = "a_table";
    public static final String FEED_TABLE = "f_table";

    // attributes
    //      article table
    public static final String ATTR_ARTICLE_TITLE = "title";
    public static final String ATTR_ARTICLE_TEXT = "text";
    public static final String ATTR_ARTICLE_DATE = "date";
    public static final String ATTR_ARTICLE_AUTHOR = "author";
    public static final String ATTR_ARTICLE_POSTED_BY = "posted_by";
    public static final String ATTR_ARTICLE_URL = "url";
    //      feed table
    public static final String ATTR_FEED_NAME = "name";
    public static final String ATTR_FEED_URL = "url";
    public static final String ATTR_ID = "_id";
}
