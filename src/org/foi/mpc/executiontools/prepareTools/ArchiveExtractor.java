package org.foi.mpc.executiontools.prepareTools;

import com.github.junrar.extract.ExtractArchive;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.directory.DirectoryTreeWalker;
import org.foi.common.filesystem.file.ZipArchive;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class ArchiveExtractor extends AssignementDirectoryIterator {
    public static final String NAME = "ArchiveExtractor";
    final ZipArchive zipArchive;

    public class ExtractException extends RuntimeException {
        public ExtractException(String message) {
            super(message);
        }
    }

    public ArchiveExtractor() {
        zipArchive = new ZipArchive(StandardCharsets.UTF_8);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void runPrepareTool(File submissionArchive) {
        extractToArchiveName(submissionArchive);
    }

    public File extractToArchiveName(File archiveFile) {
        if(!archiveFile.isDirectory()) {
            String extractDirName = DirectoryFileUtility.getFileNameWithoutExtension(archiveFile);
            File extractedDir = new File(archiveFile.getParent() + File.separator + extractDirName);
            extractedDir.mkdir();
            extractArchiveToDir(archiveFile, extractedDir);
            archiveFile.delete();//TODO should this be here?
            return extractedDir;
        } else {
            return archiveFile;
        }
    }


    private void extractArchiveToDir(File archiveFile, File extractDir) {
        if (zipArchive.isZipArchive(archiveFile)) {
            zipArchive.extractZipToDir(archiveFile, extractDir);
        } else if (isRarArchive(archiveFile)) {
            extractRarToDir(archiveFile, extractDir);
        } else {
            throw new ExtractException("Unsuported archive type " + archiveFile.getAbsolutePath());
        }
        try {
            Files.walkFileTree(extractDir.toPath(), new CheckExtractedArchive(this));
        } catch (IOException e) {
            throw new ExtractException(e.getMessage());
        }
    }

    private boolean isRarArchive(File archiveFile) {
        return archiveFile.getName().toLowerCase().endsWith(".rar");
    }

    private void extractZipToDir(File archiveFile, File extractDir) {
        zipArchive.extractZipToDir(archiveFile, extractDir);
    }

    private void extractRarToDir(File archiveFile, File extractDir) {
        ExtractArchive rarExtractor = new ExtractArchive();
        rarExtractor.extractArchive(archiveFile, extractDir);
    }

    private boolean isArchive(File file) {
        return isRarArchive(file) || zipArchive.isZipArchive(file);
    }

    private static class CheckExtractedArchive implements DirectoryTreeWalker {

        private final ArchiveExtractor archiveExtractor;

        public CheckExtractedArchive(ArchiveExtractor archiveExtractor) {
            this.archiveExtractor = archiveExtractor;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (archiveExtractor.isArchive(file.toFile())) {
                archiveExtractor.extractToArchiveName(file.toFile());
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
