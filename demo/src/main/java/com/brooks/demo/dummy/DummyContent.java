package com.brooks.demo.dummy;
import java.util.ArrayList;
import java.util.List;
/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent{
    private static int COUNT ;
    private static int TOTALPAGE ;

    public static List<DummyItem> generateData (int page) {
        int start = page * COUNT;
        int end = start + COUNT;
        List<DummyItem> items = new ArrayList<>();
        for (int i = start; i < end; i++) {
            items.add(createDummyItem(i));
        }
        return items;
    }
    public  void setData(int COUNT,int TOTALPAGE){
        this.COUNT=COUNT;
        this.TOTALPAGE=TOTALPAGE;
    }
    /**
     * 是否还有更多
     *
     * @param page
     * @return
     */
    public static boolean hasMore(int page) {
        return page < TOTALPAGE;
    }
    private static DummyItem createDummyItem (int text) {
        return new DummyItem ("Item " + text);
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String content;
        public DummyItem (String content) {
            this.content = content;
        }
        @Override
        public String toString() {
            return content;
        }
    }
}
