import java.util.ArrayList;

public class VirtualFileSystem {
    private int sizeSystem = 0;
    private int freeBlocks = 0;
    private int allocatedBlocks = 0;
    private Directory root;
    private ArrayList<Integer> freeBlocksIndex = new ArrayList<Integer>();
    private ArrayList<Block> Blocks = new ArrayList<Block>();
    private ArrayList<IndexTable> allocatedBlocksTable = new ArrayList<IndexTable>();
    private AllocationTechniques tech;
    private String technique = "";

    public VirtualFileSystem(int Size, String Technique) {

        this.sizeSystem = Size;
        this.freeBlocks = Size;
        this.allocatedBlocks = 0;
        allocatedBlocksTable = new ArrayList<IndexTable>();
        this.root = new Directory("root");
        technique = Technique;
        if (technique.equals("Linked"))
            this.tech = new Linked();
        else if (technique.equals("Indexed")) {
            this.tech = new Indexed();
        } else if (technique.equals("Contiguous"))
            this.tech = new Contiguous();

        for (int i = 0; i < sizeSystem; i++) {
            Block tempBLock = new Block(-1);
            Blocks.add(tempBLock);
            freeBlocksIndex.add(i);
        }

    }

    public String getTech() {
        return technique;
    }

    public void setAllocatedBlocks(int allocatedBlocks) {
        this.allocatedBlocks = allocatedBlocks;
    }

    public void setAllocatedBlocksTable(ArrayList<IndexTable> allocatedBlocksTable) {
        this.allocatedBlocksTable = allocatedBlocksTable;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        Blocks = blocks;
    }

    public void setFreeBlocks(int freeBlocks) {
        this.freeBlocks = freeBlocks;
    }

    public void setFreeBlocksIndex(ArrayList<Integer> freeBlocksIndex) {
        this.freeBlocksIndex = freeBlocksIndex;
    }

    public void setRoot(Directory root) {
        this.root = root;
    }

    public void setSizeSystem(int sizeSystem) {
        this.sizeSystem = sizeSystem;
    }

    public void setTechnique(AllocationTechniques technique) {
        tech = technique;
    }

    public int getAllocatedBlocks() {
        return allocatedBlocks;
    }

    public ArrayList<IndexTable> getAllocatedBlocksTable() {
        return allocatedBlocksTable;
    }

    public ArrayList<Block> getBlocks() {
        return Blocks;
    }

    public int getFreeBlocks() {
        return freeBlocks;
    }

    public ArrayList<Integer> getFreeBlocksIndex() {
        return freeBlocksIndex;
    }

    public Directory getRoot() {
        return root;
    }

    public int getSizeSystem() {
        return sizeSystem;
    }

    public AllocationTechniques getTechnique() {
        return tech;
    }

    // Full path llike: "root/file.txt "
    public int checkExistFileName(String FullPath) {
        String[] path = FullPath.split("/");
        Directory CurrentDIRECTORY = root;
        // path not contain root like this /file.txt 100 should be this root/file.txt
        if (!CurrentDIRECTORY.getDirectoryName().equals(path[0])) {
            // System.out.println("166666666666666666");

            return -1;
        }

        for (int i = 1; i < path.length - 1; i++) {
            // until reach Parent Directory
            if (CurrentDIRECTORY.searchDirectory(path[i]) != -1)
                CurrentDIRECTORY = CurrentDIRECTORY.getSubDirectories().get(CurrentDIRECTORY.searchDirectory(path[i]));
            else {
                // System.out.println("166666666666666666");
                return -1;
            }
        }
        // If FileName Exist,(We using this in delete if 1 then we can delete file)
        if (CurrentDIRECTORY.searchFile(path[path.length - 1]) != -1) {
            return 1;
        }

        return 0; // if file name not exist mean you can create file
    }

    // Full path llike: "root/file.txt "
    public int checkExistFolderName(String FullPath) {
        String[] path = FullPath.split("/");
        Directory CurrentDIRECTORY = root;
        if (!CurrentDIRECTORY.getDirectoryName().equals(path[0]))
            return -1;
        for (int i = 1; i < path.length - 1; i++) {
            // until reach Directory
            if (CurrentDIRECTORY.searchDirectory(path[i]) != -1)
                CurrentDIRECTORY = CurrentDIRECTORY.getSubDirectories().get(CurrentDIRECTORY.searchDirectory(path[i]));
            else
                return -1;
        }
        // If DirectoryName Exist,(We using this in delete if 1 then we can delete
        // Directory)
        if (CurrentDIRECTORY.searchDirectory(path[path.length - 1]) != -1)
            return 1;
        return 0; // if Directory name not exist mean you can create Directory
    }

    public boolean CreateFile(String Path) {
        int indexSizeFile = Path.lastIndexOf(" ") + 1;// why +1 because position size file after space
        int sizeNewFile = Integer.parseInt(Path.substring(indexSizeFile, Path.length()));
        if (sizeSystem <= sizeNewFile) {
            return false;
            // CreateFile root/file.txt 100
        }

        // Path new File Before Space to enter size like this "root/file.txt 100 " Path=
        // root/file.txt
        String PATHH = Path.substring(0, indexSizeFile - 1);// why -1 because we add 1 in var indexsizefile
        if (checkExistFileName(PATHH) == 0) {
            String[] paths = PATHH.split("/");
            Directory currentDirectory = root;
            for (int i = 1; i < paths.length - 1; i++)
                if (currentDirectory.searchDirectory(paths[i]) != -1)
                    currentDirectory = currentDirectory.getSubDirectories()
                            .get(currentDirectory.searchDirectory(paths[i]));

            // CreateFile root/file.txt 100

            if (currentDirectory.searchFile(paths[paths.length - 1]) != -1)// file name exist
                return false;
            if (!tech.allocate(this, PATHH, sizeNewFile))
                return false;

            currentDirectory.getFiles().add(new FILEE(PATHH, sizeNewFile));
            return true;
        }
        return false;
    }

    public boolean CreateFolder(String Path) {
        if (checkExistFolderName(Path) == 0) {
            String[] paths = Path.split("/");
            Directory currentDirectory = root;
            for (int i = 1; i < paths.length - 1; i++)
                if (currentDirectory.searchDirectory(paths[i]) != -1)
                    currentDirectory = currentDirectory.getSubDirectories()
                            .get(currentDirectory.searchDirectory(paths[i]));

            currentDirectory.getSubDirectories().add(new Directory(Path));

            return true;
        }
        return false;
    }

    public boolean DeleteFile(String path) {

        if (checkExistFileName(path) == 1) {
            String[] paths = path.split("/");
            Directory CurrentDIRECTORY = root;

            for (int i = 1; i < paths.length - 1; i++)// i =1 not 0 because 0 is root
                if (CurrentDIRECTORY.searchDirectory(paths[i]) != -1)
                    CurrentDIRECTORY = CurrentDIRECTORY.getSubDirectories()
                            .get(CurrentDIRECTORY.searchDirectory(paths[i]));

            if (!tech.deallocate(this, path))
                return false;
            int PositionFile = CurrentDIRECTORY.searchFile(paths[paths.length - 1]);
            // System.out.println(CurrentDIRECTORY.getFiles().get(PositionFile).getFileName());

            CurrentDIRECTORY.getFiles().remove(PositionFile);
            return true;
        }
        return false;
    }

    public boolean DeleteFolder(String path) {

        if (checkExistFolderName(path) == 1) {
            String[] paths = path.split("/");
            Directory CurrentDIRECTORY = root;

            for (int i = 1; i < paths.length - 1; i++)// i =1 not 0 because 0 is root
                if (CurrentDIRECTORY.searchDirectory(paths[i]) != -1)
                    CurrentDIRECTORY = CurrentDIRECTORY.getSubDirectories()
                            .get(CurrentDIRECTORY.searchDirectory(paths[i]));

            int PositionCurrentDirectory = CurrentDIRECTORY.searchDirectory(paths[paths.length - 1]);

            int NumOfFileInCurrentDir = CurrentDIRECTORY.getSubDirectories().get(PositionCurrentDirectory).getFiles()
                    .size();
            int NumOfDirInCurrentDir = CurrentDIRECTORY.getSubDirectories().get(PositionCurrentDirectory)
                    .getSubDirectories().size();

            // first delete ALL FILE IN CURRENT DIR
            for (int i = NumOfFileInCurrentDir - 1; i >= 0; i--)
                DeleteFile(CurrentDIRECTORY.getSubDirectories().get(PositionCurrentDirectory).getFiles().get(i)
                        .getFilePath());
            // THEN OPEN EACH DIR AND DELETE ALL FILES AND SO ON
            for (int i = NumOfDirInCurrentDir - 1; i >= 0; i--)
                DeleteFolder(CurrentDIRECTORY.getSubDirectories().get(PositionCurrentDirectory).getSubDirectories()
                        .get(i).getDirectoryPath());

            CurrentDIRECTORY.getSubDirectories().remove(PositionCurrentDirectory);
            return true;
        }
        return false;
    }

    public void DisplayDiskStatus() {
        System.out.println("Free Space Now IS  :" + freeBlocks + " BLocks");
        System.out.println("Allocated BLOCKS IS  :" + allocatedBlocks + " BLOCKS ");

        System.out.println("EMPTY BLOCKS IN DISK ARE :");
        for (int i = 0; i < freeBlocksIndex.size(); i++)
            if (freeBlocksIndex.get(i) != -1)
                System.out.print("Block" + freeBlocksIndex.get(i) + " IS EMPTY ");

        System.out.println();

        System.out.println("ALLOCATED BLOCK IN DISK ARE  :");
        for (int i = 0; i < Blocks.size(); i++)
            if (!Blocks.get(i).isDeallocated())
                System.out.println("Block" + Blocks.get(i).getIndexBlock() + " IS ALLOCATEDD ");

        System.out.println();
    }

    public void DisplyDiskStructure(int level) {
        System.out.println("Directory Name Is < " + root.getDirectoryName() + ">");
        root.printDirectoryStructure(level);
    }
}
