
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MAIN2 {

    public static void TellUser(User user) {
        System.out.println("UserName is : " + user.getUserName() + "\nPassWod Is :" + user.getPassword());
        for (int i = 0; i < user.getCapability().size(); i++)
            System.out.println(" Path is  " + user.getCapability().get(i).getPathFolder() + "\nCapability Is :"
                    + user.getCapability().get(i).getCode());
    }

    public static boolean CreateUser(String UserName, String Password, ArrayList<User> Users) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).getUserName().equals(UserName)) {
                System.out.println("UserName  : " + UserName + " Is Already Existed so Try again ");
                return false;
            }
        }
        User user = new User(UserName, Password);
        Users.add(user);
        System.out.println("Successful created");
        return true;
    }

    public static boolean GrantUser(String path, String capability, String userName, ArrayList<User> Users) {
        boolean userexist = false;
        int indexUser = 0;
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).getUserName().equals(userName)) {
                userexist = true;
                indexUser = i;
            }
        }
        if (!userexist) {
            System.out.println("UserName not exist");
            return false;
        }
        Access access = new Access(path, capability);
        // if admin change opinion and need change capability for user on specific path
        for (int i = 0; i < Users.get(indexUser).getCapability().size(); i++) {
            if (Users.get(indexUser).getCapability().get(i).pathFolder.equals(path))
                Users.get(indexUser).getCapability().remove(i);

        }
        Users.get(indexUser).getCapability().add(access);

        // System.out.println("4555555555555555 " +
        // Users.get(indexUser).getCapability().size());
        return true;

    }

    public static boolean hasAccess(User user, String PATH, String ComingFrom) {
        String[] partsOfPath = PATH.split("/");
        boolean hasACCESS = false;
        int IndexAccess = 0;
        // why !hasaccess because may be we found that user has access on this folder in
        // front user.getCapability()
        String[] AccessForPath;
        for (int i = 0; i < user.getCapability().size() && !hasACCESS; i++) {
            AccessForPath = user.getCapability().get(i).getPathFolder().split("/");
            for (int j = 0; j < AccessForPath.length; j++) {
                // note hence loop start from zero to length access not length of PATH WHICH
                // COMING FROM USER
                // because path coming from user contain command which user need do as create
                // file or folder ,delete...
                if (!AccessForPath[j].equals(partsOfPath[j])) {
                    hasACCESS = false;
                    break;
                }
                hasACCESS = true;
                IndexAccess = i;
            }
        }

        if (ComingFrom.equals("C")) {
            if (user.getCapability().get(IndexAccess).getCode().charAt(0) == '1') {
                if (!hasACCESS)
                    System.out.println("You have capability to create but not on this path ");

                return hasACCESS;
            } else {
                System.out.println("hasn't capability to Use command create");
                return false;
            }
        } else {// need delete file or folder so we call cahr at index 1 not zero because code
                // access consist of two digit first digit for create second digit for delete
            if (user.getCapability().get(IndexAccess).getCode().charAt(1) == '1') {
                if (!hasACCESS)
                    System.out.println("You have capability to delete but not on this path ");
                AccessForPath = user.getCapability().get(IndexAccess).getPathFolder().split("/");
                if (AccessForPath[AccessForPath.length - 1].equals(partsOfPath[partsOfPath.length - 1])) {
                    System.out.println("You have capability to delete  but under your this folder ");
                    return false;
                }

                return hasACCESS;
            } else {
                System.out.println("hasn't capability to Use command delete");

                return false;

            }
        }
    }

    public static int Login(String UserName, String Password, ArrayList<User> Users) {
        for (int i = 0; i < Users.size(); i++) {
            if (Users.get(i).getUserName().equals(UserName) && Users.get(i).getPassword().equals(Password)) {
                System.out.println("Successful created");
                return i;
            }
        }
        return -1;

    }

    public static void main(String[] args) throws IOException {
        DATA data = new DATA();

        VirtualFileSystem virtualFileSystem;
        ArrayList<User> users = new ArrayList<User>();
        Scanner scanner = new Scanner(System.in);
        if (data.LoadDisk() == null) {
            System.out.print("Enter disk space: ");
            int diskSize = scanner.nextInt();
            System.out.println("which Allocation Technique do you need 1-Linked 2-Indexed 3-Contiguous");
            int choice = scanner.nextInt();
            String Tech = "";
            if (choice == 1)
                Tech = "Linked";
            else if (choice == 2)
                Tech = "Indexed";
            else if (choice == 3)
                Tech = "Contiguous";
            virtualFileSystem = new VirtualFileSystem(diskSize, Tech);

        } else {
            virtualFileSystem = data.LoadDisk();
        }
        if (data.LoadUsers() == null) {
            users.add(new User("admin", "admin"));
            users.get(0).getCapability().add(new Access("root", "11"));
            System.out.println("ENTERRRRRRRRRRRRR");
        } else {
            users = data.LoadUsers();
            users = data.loadCapability(users);

        }
        // System.out.println("USER IS " + users.get(0).getUserName() + " PASSWORD IS :
        // " + users.get(0).getPassword());
        boolean flag = false;
        String UserName = "";
        String Password = "";

        User currentUser = users.get(0);
        String command = "";
        System.out.print("---");
        Scanner input = new Scanner(System.in);
        command = input.nextLine();

        while (!command.toLowerCase().equals("exit")) {

            String[] PARTS = command.split(" ");
            PARTS[0] = PARTS[0].toLowerCase();
            if (PARTS[0].equals("telluser") && PARTS.length == 1) {
                TellUser(currentUser);
            }

            ///////////////////////////////////////////////////////////////
            // 1- only the admin can use such command third condition in else if

            else if (PARTS[0].equals("cuser") && PARTS.length == 3 && currentUser.getUserName().equals("admin")) {
                CreateUser(PARTS[1], PARTS[2], users);
                // 1- No user with the same name already created :check occur in function
            }

            else if (PARTS[0].equals("grant") && PARTS.length == 4 && currentUser.getUserName().equals("admin")) {
                // 2- This command is used for folders only(1- The path already exists )
                // =>virtualFileSystem.checkExistFolderName(PARTS[2]) hence check folder not
                // file so if input was file will go to body else
                if (virtualFileSystem.checkExistFolderName(PARTS[2]) == 1 || PARTS[2].equals("root/")) {

                    GrantUser(PARTS[2], PARTS[3], PARTS[1], users);//2- The user already exists :check occur in function 
                } else {
                    System.out.println("Wrong Path ");
                }
            }
            //////////////////////////////////////////////////////////////////////////////
            else if (PARTS[0].equals("logout") && PARTS.length == 1) {
                System.out.println("\n-LogIn \n-exit ");
                String choise = scanner.nextLine();
                if (choise.toLowerCase().equals("login")) {
                    flag = false;
                    int indexUser = 0;
                    while (!flag) {
                        System.out.println("Enter UserName For log In");
                        UserName = scanner.nextLine();
                        System.out.println("Enter PassWord ");
                        Password = scanner.nextLine();
                        indexUser = Login(UserName, Password, users);
                        if (indexUser != -1)
                            flag = true;
                    }
                    currentUser = users.get(indexUser);
                } else {
                    command = "exit";
                }
            }

            else if (PARTS[0].equals("createfile") && PARTS.length == 3
                    && ((hasAccess(currentUser, PARTS[1], "C") || currentUser.getUserName().equals("admin")))) {
                if (virtualFileSystem.CreateFile(PARTS[1] + " " + PARTS[2]))
                    System.out.println(" successfully created.");
                else
                    System.out.println(" failed to be created.");
            }

            else if (PARTS[0].equals("createfolder") && PARTS.length == 2
                    && (hasAccess(currentUser, PARTS[1], "C") || currentUser.getUserName().equals("admin"))) {
                if (virtualFileSystem.CreateFolder(PARTS[1]))
                    System.out.println(" successfully created.");
                else
                    System.out.println(" failed to be created.");
            }

            else if (PARTS[0].equals("deletefile") && PARTS.length == 2
                    && (hasAccess(currentUser, PARTS[1], "D") || currentUser.getUserName().equals("admin"))) {
                if (virtualFileSystem.DeleteFile(PARTS[1]))
                    System.out.println(" successfully deleted.");
                else
                    System.out.println(" failed to be deleted.");
            }

            else if (PARTS[0].equals("deletefolder") && PARTS.length == 2
                    && (hasAccess(currentUser, PARTS[1], "D") || currentUser.getUserName().equals("admin"))) {
                if (virtualFileSystem.DeleteFolder(PARTS[1]))
                    System.out.println(" successfully deleted.");
                else
                    System.out.println(" failed to be deleted.");
            }

            else if (PARTS[0].equals("displaydiskstatus") && PARTS.length == 1) {
                virtualFileSystem.DisplayDiskStatus();
            }

            else if (PARTS[0].equals("displaydiskstructure") && PARTS.length == 1) {
                virtualFileSystem.DisplyDiskStructure(0);
            } else {
                System.out.println("INVALIDCOMMAND ENTER COMMAND AGIAN !!");
            }
            System.out.println("---");
            command = input.nextLine();
        }
        input.close();

        scanner.close();
        data.saveCapability(users);
        data.saveUsers(users);
        data.saveDisk(virtualFileSystem);
    }

}