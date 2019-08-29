package org.foi.common.filesystem.file;

import org.foi.common.filesystem.directory.DirectoryTreeWalker;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class ZipArchive {

    public class ZipArchiveExtractException extends RuntimeException {
        public ZipArchiveExtractException(String message) {
            super(message);
        }
    }

    private Charset charset;

    public ZipArchive(Charset charset) {
        this.charset = charset;
    }

    public boolean isZipArchive(File archiveFile) {
        return archiveFile.getName().toLowerCase().endsWith(".zip");
    }

    public void extractZipToDir(File archiveFile, File extractDir) {
        try (FileSystem zipFileSystem = createZipFileSystem(archiveFile)) {
            Path zipRoot = zipFileSystem.getPath("/");
            Files.walkFileTree(zipRoot, new ExtractZipFileVisitor(extractDir.toPath()));
        } catch (IOException e) {
            throw new ZipArchiveExtractException(e.getMessage());
        }
    }

    private FileSystem createZipFileSystem(File zipFile) throws IOException {
        Path absPath = zipFile.toPath().toAbsolutePath().normalize();
        URI uri = URI.create("jar:file:///" + absPath.toString().replace('\\', '/').replace(" ", "%20"));
        Map<String, String> env = new HashMap<String, String>();
        env.put("create", "true");
        env.put("encoding", charset.name());
        return FileSystems.newFileSystem(uri, env);
    }

    private static class ExtractZipFileVisitor implements DirectoryTreeWalker {

        private final Path destinationDir;

        ExtractZipFileVisitor(Path destinationDir) {
            this.destinationDir = destinationDir;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path zipDir, BasicFileAttributes attrs) throws IOException {
            Path destDir = Paths.get(destinationDir.toString(), zipDir.toString());
            Files.createDirectories(destDir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path zipedFile, BasicFileAttributes attrs) throws IOException {
            Path unzipedFile = Paths.get(destinationDir.toString(), zipedFile.toString());
            Files.copy(zipedFile, unzipedFile, StandardCopyOption.REPLACE_EXISTING);
            return FileVisitResult.CONTINUE;
        }
        //TODO write to log on  visitFileFailed?
    }
}