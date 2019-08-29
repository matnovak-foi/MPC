import org.foi.common.filesystem.directory.DirectoryFileUtility;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class deleteDir {
    public static void main(String[] args) throws IOException {
        DirectoryFileUtility.deleteDirectoryTree(new File("D:\\java\\doktorski_rad\\MPC\\templateDir"));
    }
}
