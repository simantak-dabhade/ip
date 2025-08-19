import java.util.ArrayList;
import java.util.List;

public class ItemStore {

    private List<String> items;

    public ItemStore() {
        items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public List<String> readItems() {
        return items;
    }
}
