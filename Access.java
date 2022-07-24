public class Access {
    String pathFolder="";
    String code="00";

    public Access(String p, String c) {
        pathFolder = p;
        code = c;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPathFolder(String pathFolder) {
        this.pathFolder = pathFolder;
    }

    public String getCode() {
        return code;
    }

    public String getPathFolder() {
        return pathFolder;
    }

}
