// Did not want to do this
// Basically makes a mutable string, so that stuff works this is so cursed
public class Cache {
    private String c;
    public Cache() {
        c = "";
    }

    public int length() {
        return c.length();
    }

    public void addToCache(int i) {
        c = c + i;
    }

    public String getCache() {
        return c;
    }

    public boolean contains(int i) {
        return c.contains(""+i);
    }

    public String toString() {
        return getCache();
    }

    public void clear() {
        c = "";
    }
}
