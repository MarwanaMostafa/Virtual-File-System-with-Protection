import java.util.ArrayList;

public class Directory {

    private String directoryPath;
    private String directoryName;
    private ArrayList<FILEE> files;
    private ArrayList<Directory> subDirectories;
    private boolean deleted = false;

    Directory() {
        this.directoryPath = "";
        this.directoryName = "";
        this.deleted = false;
        this.files = new ArrayList<FILEE>();
        this.subDirectories = new ArrayList<Directory>();
    }

    Directory(String pathName) {
        this.directoryPath = pathName;
        String[] arr = directoryPath.split("/");
        this.directoryName = arr[arr.length - 1];
        this.deleted = false;
        this.files = new ArrayList<FILEE>();
        this.subDirectories = new ArrayList<Directory>();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setFiles(ArrayList<FILEE> files) {
        this.files = files;
    }

    public void setSubDirectories(ArrayList<Directory> subDirectories) {
        this.subDirectories = subDirectories;
    }

    public void setDirectoryPath(String directoryPath) {

        this.directoryPath = directoryPath;
        String[] arr = directoryPath.split("/");
        this.directoryName = arr[arr.length - 1];

    }

    public String getDirectoryName() {
        return directoryName;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public ArrayList<FILEE> getFiles() {
        return files;
    }

    public ArrayList<Directory> getSubDirectories() {
        return subDirectories;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int searchFile(String SearchedFile) {

        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).getFileName().equals(SearchedFile)) {
                return i;
            }
        }
        return -1;
    }

    public int searchDirectory(String SearchedDirectory) {
        for (int i = 0; i < subDirectories.size(); i++) {
            if (subDirectories.get(i).directoryName.equals(SearchedDirectory)) {
                return i;
            }
        }
        return -1;
    }

    public void printDirectoryStructure(int level) {

        String s = " ";
        String path = directoryPath;
        for (int i = 0; i < path.length() - 1 ; i++)
            if (path.charAt(i) == '/')
                s += "\t";

        for (int i = 0; i < files.size() && !files.get(i).isDeleted(); i++)
            System.out.println(s + "*" + files.get(i).getFileName());

        for (int i = 0; i < subDirectories.size(); i++) {
            System.out.println(s + "*" + subDirectories.get(i).getDirectoryName());
            this.subDirectories.get(i).printDirectoryStructure(++level);
        }
    }
}
