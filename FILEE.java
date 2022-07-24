import java.util.ArrayList;

public class FILEE {

    private String filePath;
    private String fileName;
    private ArrayList<Integer> allocatedBlocks = new ArrayList<Integer>();
    private boolean deleted;
    private int fileSize;

    FILEE() {

        this.fileName = "";
        this.filePath = "";
        this.fileSize = 0;
        this.deleted = false;
        allocatedBlocks = null;
    }

    FILEE(String path, int size) {

        this.filePath = path;
        this.deleted = false;
        this.fileSize = size;

        String[] arr = filePath.split("/");
        fileName = arr[arr.length - 1];
    }

    public void setAllocatedBlocks(ArrayList<Integer> allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public ArrayList<Integer> getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
