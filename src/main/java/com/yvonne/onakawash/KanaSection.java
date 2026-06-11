package com.yvonne.onakawash;

import java.util.List;

public class KanaSection {
    public String title;
    public String description;
    public List<KanaItem> items;

    public KanaSection(String title, String description, List<KanaItem> items){
        this.title=title;
        this.description =description;
        this.items=items;
    }
}
