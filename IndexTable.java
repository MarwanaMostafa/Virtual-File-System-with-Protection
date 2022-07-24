public class IndexTable {
    int index = 0;
    String path = "";
    int AnotherIndex = 0;// represent as length to contiguous and end block to linked

    public IndexTable(String path, int index) {
        this.path = path;
        this.index = index;
    }

    public void setAnotherIndex(int IndexAnotherBLock) {
        AnotherIndex = IndexAnotherBLock;
    }

    public int getAnotherIndex() {
        return AnotherIndex;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getIndex() {
        return index;
    }

    public String getPath() {
        return path;
    }

}
