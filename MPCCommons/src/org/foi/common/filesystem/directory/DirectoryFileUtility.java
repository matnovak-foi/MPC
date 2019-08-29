package org.foi.common.filesystem.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.foi.common.MPCexcpetions.IsNullException;

public class DirectoryFileUtility {


    public static class DirDoesNotExistException extends RuntimeException {
        public DirDoesNotExistException(String message) {
            super(message);
        }
    }

    public static class DirIsEmptyException extends RuntimeException {
    }

    public static class DirAleradyExistsException extends RuntimeException {
        public DirAleradyExistsException(String message) {
            super(message);
        }
    }

    public static class DirCopyException extends RuntimeException {
        public DirCopyException(String message) {
            super(message);
        }
    }

    public static class FileCopyException extends RuntimeException {
        public FileCopyException(String message) {
            super(message);
        }
    }

    public static File getRelativizedDir(File originalBasePath, File originalFullPath, File newDirBasePath) {
        Path relDir = originalBasePath.toPath().relativize(originalFullPath.toPath());
        File dir = new File(newDirBasePath + File.separator + relDir.toFile().getPath());
        return dir;
    }

    private CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING};

    public void copyDirectoryTree(File fromDirectory, File toDirectory) throws IOException {
        copyDirectoryTree(fromDirectory, toDirectory, new ArrayList<>());
    }

    public void copyDirectoryTree(File fromDirectory, File toDirectory, List<String> excludeDirsFromCopy) throws IOException {
        checkIfDirExists(fromDirectory);
        checkIfDirExists(toDirectory);

        DirectoryTreeCopier treeCopier = new DirectoryTreeCopier(fromDirectory, toDirectory);
        treeCopier.setExcludeDirs(excludeDirsFromCopy);
        Files.walkFileTree(fromDirectory.toPath(), treeCopier);
    }

    public File createSubDir(File parentDir, String name) {
        File file = new File(parentDir.getPath()+File.separator+name);
        if (file.exists()) {
            throw new DirectoryFileUtility.DirAleradyExistsException(file.getAbsolutePath());
        }
        file.mkdir();
        return file;
    }

    public static void deleteDirectoryTree(File rootDirectory) throws IOException {
        if (rootDirectory == null) {
            throw new IsNullException();
        }
        Files.walkFileTree(rootDirectory.toPath(), new DirectoryTreeDeleter());
    }

    public static boolean isNotEmptyDirectory(File rootDirectory) {
        return !isEmptyDirectory(rootDirectory);
    }

    public static boolean isEmptyDirectory(File rootDirectory) {
        if (rootDirectory == null) {
            throw new IsNullException();
        }

        if (rootDirectory.exists()) {
            return rootDirectory.listFiles().length == 0;
        } else {
            return true;
        }
    }

    public static void checkIfDirExists(File dir) {
        if (dir == null) {
            throw new IsNullException();
        } else if (!dir.exists()) {
            throw new DirDoesNotExistException(dir.getAbsolutePath());
        }
    }

    public static Path createTargetPath(Path sourceRootDir, Path targetRootDir, Path fullSourcePath) {
        Path relativeSorucePath = sourceRootDir.relativize(fullSourcePath);
        return targetRootDir.resolve(relativeSorucePath);
    }

    public static String getFileNameWithoutExtension(File file) {
        int pos = file.getName().toString().lastIndexOf(".");
        return file.getName().substring(0, pos);
    }

    public static String getFileNameWithoutExtension(String fileName) {
        int pos = fileName.toString().lastIndexOf(".");
        return fileName.substring(0, pos);
    }

    public File copyFile(File file, File newFile) {
        Path copyPath = Paths.get(newFile.getPath(),file.getName());
        try {
            Files.copy(file.toPath(),copyPath,copyOptions);
        } catch (IOException e) {
            throw new DirectoryFileUtility.FileCopyException(file+" to "+copyPath+" Error: "+e.getMessage());
        }
        return  copyPath.toFile();
    }

    public static String getDirListFromFolder(File dir){
        String dirList = "";
        for(File item : dir.listFiles()){
            dirList += item.getName()+",";
        }
        return dirList.substring(0,dirList.length()-1);
    }


    private static class DirectoryTreeDeleter implements DirectoryTreeWalker {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            file.toFile().delete();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            dir.toFile().delete();
            return FileVisitResult.CONTINUE;
        }
    }

    private class DirectoryTreeCopier implements DirectoryTreeWalker {

        private Path sourceRootDir;
        private Path targetRootDir;
        private List<String> excludeDir;

        public DirectoryTreeCopier(File fromDirectory, File toDirectory) {
            this.sourceRootDir = fromDirectory.toPath();
            this.targetRootDir = toDirectory.toPath();
        }

        public void setExcludeDirs(List<String> excludeDir){
            this.excludeDir = excludeDir;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Path targetFile = createTargetPath(sourceRootDir,targetRootDir,file);
            CopyOption[] copyOptions = new CopyOption[]{StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING};
            Files.copy(file, targetFile, copyOptions);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if(!excludeDir.contains(dir.toFile().getName())) {
                Path targetDir = createTargetPath(sourceRootDir, targetRootDir, dir);
                targetDir.toFile().mkdir();
                return FileVisitResult.CONTINUE;
            }
            else {
                return FileVisitResult.SKIP_SUBTREE;
            }
        }
    }
}
