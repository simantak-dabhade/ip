import java.util.ArrayList;
import java.util.List;

public class TaskStore {

    private List<Task> items;

    public TaskStore() {
        items = new ArrayList<>();
    }

    public void addItem(Task item) {
        items.add(item);
    }

    public List<Task> readItems() {
        return items;
    }

    public Task deleteItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.remove(index);
        }
        return null;
    }
}
