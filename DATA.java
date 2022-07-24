import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.Line;

public class DATA {
    public void SaveInnerInformation(Directory CurrentDirectory, PrintWriter WRITER) {
        WRITER.println(CurrentDirectory.getDirectoryPath() + " " + CurrentDirectory.getFiles().size() + " "
                + CurrentDirectory.getSubDirectories().size());
        for (int i = 0; i < CurrentDirectory.getFiles().size(); i++)
            WRITER.println(CurrentDirectory.getFiles().get(i).getFilePath() + " " + CurrentDirectory.getFiles().size());
        for (int i = 0; i < CurrentDirectory.getSubDirectories().size(); i++)
            SaveInnerInformation(CurrentDirectory.getSubDirectories().get(i), WRITER);
    }

    public void saveDisk(VirtualFileSystem virtualFileSystem) throws FileNotFoundException {
        File Savedfile = new File("DiskStructure.txt");
        PrintWriter WriteOnFile = null;
        WriteOnFile = new PrintWriter(Savedfile);
        WriteOnFile.println(virtualFileSystem.getTech()); //
        WriteOnFile.println(virtualFileSystem.getSizeSystem());
        WriteOnFile.println(virtualFileSystem.getFreeBlocks());
        WriteOnFile.println(virtualFileSystem.getAllocatedBlocks());

        for (int i = 0; i < virtualFileSystem.getSizeSystem(); i++)
            WriteOnFile.print(virtualFileSystem.getFreeBlocksIndex().get(i) + " ");
        WriteOnFile.println();

        WriteOnFile.println(virtualFileSystem.getAllocatedBlocksTable().size());

        for (int i = 0; i < virtualFileSystem.getAllocatedBlocksTable().size(); i++) {
            if (virtualFileSystem.getTech().equals("Indexed")) {
                WriteOnFile.println(virtualFileSystem.getAllocatedBlocksTable().get(i).getPath() + " "
                        + virtualFileSystem.getAllocatedBlocksTable().get(i).getIndex());

            } else {
                WriteOnFile.println(virtualFileSystem.getAllocatedBlocksTable().get(i).getPath() + " "
                        + virtualFileSystem.getAllocatedBlocksTable().get(i).getIndex() + " " +
                        virtualFileSystem.getAllocatedBlocksTable().get(i).getAnotherIndex());
            }
        }
        for (int i = 0; i < virtualFileSystem.getSizeSystem(); i++) {
            WriteOnFile.print(
                    virtualFileSystem.getBlocks().get(i).getIndexBlock() + " "
                            + virtualFileSystem.getBlocks().get(i).isDeallocated());
            if (virtualFileSystem.getTech().equals("Linked"))
                WriteOnFile.print(" " + virtualFileSystem.getBlocks().get(i).getAntoherIndexBlock());

            if (virtualFileSystem.getBlocks().get(i).getPointerToBlock().size() != 0)
                WriteOnFile.print(" " + virtualFileSystem.getBlocks().get(i).getPointerToBlock().size() + " ");
            for (int j = 0; j < virtualFileSystem.getBlocks().get(i).getPointerToBlock().size(); j++) {
                if (j == virtualFileSystem.getBlocks().get(i).getPointerToBlock().size() - 1)
                    WriteOnFile.print(virtualFileSystem.getBlocks().get(i).getPointerToBlock().get(j));
                else
                    WriteOnFile.print(virtualFileSystem.getBlocks().get(i).getPointerToBlock().get(j) + " ");
            }
            WriteOnFile.println();
        }
        SaveInnerInformation(virtualFileSystem.getRoot(), WriteOnFile);
        WriteOnFile.close();
    }

    public void loadInnerInformtion(Directory currentDir, Scanner reader) {
        currentDir.setDirectoryPath(reader.next());
        int NumOfFile = reader.nextInt();
        int NumOfDir = reader.nextInt();

        for (int i = 0; i < NumOfFile; i++) {
            String PathFile = reader.next();
            int Size = reader.nextInt();
            FILEE tempfile = new FILEE(PathFile, Size);
            currentDir.getFiles().add(tempfile);
        }
        for (int i = 0; i < NumOfDir; i++) {
            currentDir.getSubDirectories().add(new Directory());
            loadInnerInformtion(currentDir.getSubDirectories().get(i), reader);
        }
    }

    public VirtualFileSystem LoadDisk() {

        File informationDisk = new File("DiskStructure.txt");
        if (informationDisk.length() < 1)
            return null;
        Scanner Reader = null;
        try {
            Reader = new Scanner(informationDisk);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String TECHNIQUE = Reader.nextLine();

        int SizeSystem = (Reader.nextInt());
        VirtualFileSystem virtualFileSystem = new VirtualFileSystem(SizeSystem, TECHNIQUE);

        virtualFileSystem.setFreeBlocks(Reader.nextInt());
        virtualFileSystem.setAllocatedBlocks(Reader.nextInt());

        for (int i = 0; i < virtualFileSystem.getSizeSystem(); i++)
            virtualFileSystem.getFreeBlocksIndex().set(i, Reader.nextInt());

        int NumTables = Reader.nextInt();
        int IndexSecondBLOCK = 0;
        for (int i = 0; i < NumTables; i++) {
            String path = (Reader.next());
            int IndexBLOCK = (Reader.nextInt());
            if (!TECHNIQUE.equals("Indexed")) {
                IndexSecondBLOCK = (Reader.nextInt());
            }
            IndexTable TABLE = new IndexTable(path, IndexBLOCK);
            TABLE.setAnotherIndex(IndexSecondBLOCK);
            virtualFileSystem.getAllocatedBlocksTable().add(TABLE);
        }

        for (int i = 0; i < virtualFileSystem.getSizeSystem(); i++) {
            String line = Reader.nextLine();
            String[] index = line.split(" ");

            virtualFileSystem.getBlocks().get(i).setIndexBlock(Reader.nextInt());
            virtualFileSystem.getBlocks().get(i).setDeallocated(Reader.nextBoolean());
            if (TECHNIQUE.equals("Linked"))
                virtualFileSystem.getBlocks().get(i).setAntoherIndexBlock(Reader.nextInt());

            if (index.length > 2) {
                for (int j = 2; j < index.length; j++) {
                    int x = Integer.parseInt(index[j]);
                    virtualFileSystem.getBlocks().get(i - 1).getPointerToBlock().add(x);
                }
            }
        }
        loadInnerInformtion(virtualFileSystem.getRoot(), Reader);
        return virtualFileSystem;

    }

    public void saveUsers(ArrayList<User> users) throws FileNotFoundException {
        File Savedfile = new File("Users.txt");
        PrintWriter WriteOnFile = null;
        WriteOnFile = new PrintWriter(Savedfile);
        for (int i = 0; i < users.size(); i++)
            WriteOnFile.println(users.get(i).getUserName() + ',' + users.get(i).getPassword());
        WriteOnFile.close();
    }

    public ArrayList<User> LoadUsers() throws IOException {

        File informationUsers = new File("Users.txt");
        if (informationUsers.length() < 1)
            return null;
        Scanner Reader = null;
        try {
            Reader = new Scanner(informationUsers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Path path = Paths.get("Users.txt");

        long NumberOfUsers = Files.lines(path).count();
        String Line;
        String[] data;
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < NumberOfUsers; i++) {
            Line = Reader.nextLine();
            data = Line.split(",");
            User user = new User(data[0], data[1]);// data[0] is userName,Data[1]is Password
            users.add(user);
        }

        return users;

    }

    public void saveCapability(ArrayList<User> users) throws FileNotFoundException {
        File Savedfile = new File("capabilities.txt");
        PrintWriter WriteOnFile = null;
        WriteOnFile = new PrintWriter(Savedfile);
        for (int i = 0; i < users.size(); i++) {
            WriteOnFile.println(users.get(i).getUserName() + ',' + users.get(i).getCapability().size());

            for (int j = 0; j < users.get(i).getCapability().size(); j++) {

                WriteOnFile.println(users.get(i).getCapability().get(j).getPathFolder() + ','
                        + users.get(i).getCapability().get(j).getCode());

            }
        }
        WriteOnFile.close();
    }

    public ArrayList<User> loadCapability(ArrayList<User> users) throws FileNotFoundException {

        File informationUsers = new File("capabilities.txt");
        if (informationUsers.length() < 1)
            return null;
        Scanner Reader = null;
        try {
            Reader = new Scanner(informationUsers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String Line = "";
        String[] data = null;
        String[] Accesses = null;
        for (int i = 0; i < users.size(); i++) {
            Line = Reader.nextLine();
            data = Line.split(",");
            int IndexUSER = IndexUser(users, data[0]);
            if (IndexUSER != -1) {
                for (int j = 0; j < Integer.parseInt(data[1]); j++) {
                    Line = Reader.nextLine();
                    Accesses = Line.split(",");
                    users.get(IndexUSER).getCapability().add(new Access(Accesses[0], Accesses[1]));
                }

            }
        }

        return users;
    }

    public int IndexUser(ArrayList<User> users, String Name) {
        for (int i = 0; i < users.size(); i++)
            if (Name.equals(users.get(i).getUserName()))
                return i;
        return -1;
    }

}