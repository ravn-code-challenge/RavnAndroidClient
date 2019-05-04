package com.application.ravnandroidclient.client;

import java.util.ArrayList;

public class GiphyList {

    public static final String ID_STRING = "id";
    public static final String TYPE_STRING = "type";
    public static final String TITLE_STRING = "title";
    public static final String SRC_STRING = "src";
    public static final String AUTHOR_STRING = "author";
    public static final String DATE_STRING = "date";
    public static final String VIEW_COUNT_STRING = "viewCount";

    private static final String ASCENDING_STRING = "ascending";
    private static final String DESCENDING_STRING = "descending";

    public enum SortType {
        ASCENDING,
        DESCENDING
    }

    public enum SortField {
        ID,
        TYPE,
        TITLE,
        SRC,
        AUTHOR,
        DATE,
        VIEW_COUNT,
    }

    public String sortField;
    public String sortType;
    public ArrayList<GiphyModel> models;


    public void setSortType(SortType newSortType) {
        switch (newSortType) {
            case ASCENDING:
                sortType = ASCENDING_STRING;
                break;
            case DESCENDING:
                sortType = DESCENDING_STRING;
                break;
        }
    }

    public SortType getSortType() {
        if(sortType.toLowerCase().equals(ASCENDING_STRING)) {
            return SortType.ASCENDING;
        }
        else {
            return SortType.DESCENDING;
        }
    }

    public String setSortField(SortField newSortField) {
        switch (newSortField) {
            case ID:
                sortField = ID_STRING;
                break;
            case TYPE:
                sortField = TYPE_STRING;
                break;
            case SRC:
                sortField = SRC_STRING;
                break;
            case TITLE:
                sortField = TITLE_STRING;
                break;
            case AUTHOR:
                sortField = AUTHOR_STRING;
                break;
            case DATE:
                sortField = DATE_STRING;
                break;
            case VIEW_COUNT:
                sortField = VIEW_COUNT_STRING;
                break;
        }
        return null;
    }


    public SortField getSortField() {
        if(sortField.equals(ID_STRING)) {
            return SortField.ID;
        }
        else if(sortField.equals(TYPE_STRING)) {
            return SortField.TYPE;
        }
        else if(sortField.equals(TITLE_STRING)) {
            return SortField.TITLE;
        }
        else if(sortField.equals(SRC_STRING)) {
            return SortField.SRC;
        }
        else if(sortField.equals(AUTHOR_STRING)) {
            return SortField.AUTHOR;
        }
        else if(sortField.equals(DATE_STRING)) {
            return SortField.DATE;
        }
        else if(sortField.equals(VIEW_COUNT_STRING)) {
            return SortField.VIEW_COUNT;
        }
        else {
            return SortField.ID;
        }
    }

}
