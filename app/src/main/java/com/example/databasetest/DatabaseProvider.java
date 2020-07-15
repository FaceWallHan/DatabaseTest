package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {
    public static final int BOOK_DIR=0;
    public static final int BOOK_ITEM=1;
    public static final int CATEGORY_DIR=2;
    public static final int CATEGORY_ITEM=3;
    public static final String AUTHORITY="com.example.databasetest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY,"category/#",CATEGORY_ITEM);
    }
    public DatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows=db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                deleteRows=db.delete("Book","id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows=db.delete("Category",selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                deleteRows=db.delete("Category","id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        String type="vnd.android.cursor.";
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return type+"dir/vnd."+AUTHORITY+".book";
            case BOOK_ITEM:
                return type+"item/vnd."+AUTHORITY+".book";
            case CATEGORY_DIR:
                return type+"dir/vnd."+AUTHORITY+".category";
            case CATEGORY_ITEM:
                return type+"item/vnd."+AUTHORITY+".category";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri urlReturn=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId=db.insert("Book",null,values);
                urlReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId=db.insert("Category",null,values);
                urlReturn=Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
                break;
            default:
                break;
        }
        return urlReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper=new MyDatabaseHelper(getContext(),MyDatabaseHelper.DATABASE_NAME,null,2);
        db=dbHelper.getWritableDatabase();
        return !(db==null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                cursor=db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                cursor=db.query("Book",projection,"id=?",new String[]{bookId},null,null,sortOrder);
                break;
            case CATEGORY_DIR:
                cursor=db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                cursor=db.query("Category",projection,"id=?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }
    /**
     * uri.getPathSegments()将内容URL权限之后的部分以“/”符号进行分割，并把分割后的结果放入到一个字符串列表中
     * 第0个位置存放的就是路径，第1个位置存放的就是id
     * */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                updateRows=db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId=uri.getPathSegments().get(1);
                updateRows=db.update("Book",values,"id=?",new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows=db.update("Category",values,selection,selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId=uri.getPathSegments().get(1);
                updateRows=db.update("Category",values,"id=?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
