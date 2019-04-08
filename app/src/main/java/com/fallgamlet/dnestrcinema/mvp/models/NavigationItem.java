package com.fallgamlet.dnestrcinema.mvp.models;

import androidx.annotation.NonNull;

/**
 * Created by fallgamlet on 15.07.17.
 */

public class NavigationItem implements Comparable {

    private int id;
    private int titleId;
    private int iconResId;


    public NavigationItem(int id) {
        this.id = id;
    }


    public int getId() {
        return this.id;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }


    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += 31*id;

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NavigationItem)) {
            return false;
        }

        NavigationItem item = (NavigationItem) obj;
        return this.id == item.id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (!(o instanceof NavigationItem)) {
            return -1;
        }

        NavigationItem item = (NavigationItem) o;
        return this.id - item.id;
    }


    public interface NavigationId {
        static final int TODAY = 1;
        static final int SOON = 2;
        static final int LOGIN = 3;
        static final int TICKETS = 4;
        static final int DETAIL = 5;
        static final int NEWS = 6;
        static final int ABOUT = 7;
    }
}
