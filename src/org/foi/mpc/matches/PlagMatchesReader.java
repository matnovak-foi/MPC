package org.foi.mpc.matches;

import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.common.filesystem.file.TextFileUtility;
import org.foi.mpc.MPCContext;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlagMatchesReader {
    public File getDir() {
        return dir;
    }

    public class ProblemSavingPlagMatchFile extends RuntimeException {}
    public class ProblemSavingProcessedMatchFile extends RuntimeException {}

    private List<MPCPlagMatch> matches = new ArrayList<>();
    private int plagMatchCount = 0;
    private TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
    private File processedFile;
    private File plagFile;
    private File dir;

    public static PlagMatchesReader createPlagMatchReader(File sourceDir, File workingDir, File selectedInputDir) {
        File dir = DirectoryFileUtility.getRelativizedDir(sourceDir,selectedInputDir,new File(workingDir+File.separator+MPCContext.ANALYSIS_DIR));

        return new PlagMatchesReader(dir);
    }

    public PlagMatchesReader(File dir) {
        processedFile = new File(dir+File.separator+MPCContext.PROCESSED_MATCH_FILE);
        plagFile =new File(dir+File.separator+MPCContext.PLAG_MATCH_FILE);
        this.dir = dir;
        read();
    }

    public void read() {
        matches.clear();
        plagMatchCount = 0;
        readPlagMatches();
        readProceessedMatches();
    }

    private void readProceessedMatches() {
        readMatches(readAnalysisFile(processedFile), false);
    }


    protected void readPlagMatches() {
        readMatches(readAnalysisFile(plagFile), true);
    }

    private String readAnalysisFile(File plagFile) {
        String content = "";
        try {
            content = tfu.readFileContentToString(plagFile);
        } catch (Exception e) {
            if (MPCContext.CONSOLE_PRINT)
                System.err.println(plagFile + " does not exist!");
            return "";
        }
        return content;
    }

    private void readMatches(String content, boolean plagiarized) {
        String[] lines = content.split("\\r?\\n");
        for(String line : lines) {
            String[] files = line.split("[|]");
            if (files.length == 2) {
                MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(files[0], files[1]);
                mpcPlagMatch.plagiarized = plagiarized;
                if(!matches.contains(mpcPlagMatch)) {
                    matches.add(mpcPlagMatch);
                    if (plagiarized)
                        plagMatchCount++;
                }

            }
        }
    }

    public int matchesCount() {
        return matches.size();
    }

    public int plagMatchesCount() {
        //for(MPCPlagMatch match : matches)
        //{
        //    if(match.plagiarized){
        //        plagMatchCount++;
        //    }
        //}
        return plagMatchCount;
    }

    public boolean containsPlagPair(String file1, String file2) {
        if(containsPair(file1,file2)){
            return matches.get(matches.indexOf(createMPCPlagMatch(file1, file2))).plagiarized;
        }
        return false;
    }

    public List<MPCPlagMatch> getMatches() {
        return matches;
    }

    public boolean containsPair(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(file1, file2);
        return matches.contains(mpcPlagMatch);
    }

    private MPCPlagMatch createMPCPlagMatch(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = new MPCPlagMatch();
        mpcPlagMatch.fileA = file1;
        mpcPlagMatch.fileB = file2;
        return mpcPlagMatch;
    }

    public void addPlagiarizedMatch(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(file1, file2);
        mpcPlagMatch.plagiarized = true;
        if(!containsPlagPair(file1,file2)) {
            matches.remove(mpcPlagMatch);
            matches.add(mpcPlagMatch);
            plagMatchCount++;
            savePlagMatchesToFile();
        }
    }

    public void addProcessedMatch(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(file1, file2);
        mpcPlagMatch.plagiarized = false;
        if(!containsPair(file1,file2)) {
            matches.add(mpcPlagMatch);
            saveProcessedMatchesToFile();
        }
    }

    public void removeProcessedMatch(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(file1, file2);
        if(!containsPlagPair(file1,file2)) {
            matches.remove(mpcPlagMatch);
            saveProcessedMatchesToFile();
        }
    }

    public void removePlagMatch(String file1, String file2) {
        MPCPlagMatch mpcPlagMatch = createMPCPlagMatch(file1, file2);
        if(containsPair(file1, file2)) {
            matches.remove(mpcPlagMatch);
            plagMatchCount--;
            savePlagMatchesToFile();
            addProcessedMatch(file1, file2);
        }
    }

    private void savePlagMatchesToFile() {
        String content = "";
        for(MPCPlagMatch mpcPlagMatch : matches)
            if(mpcPlagMatch.plagiarized)
                content += forSave(mpcPlagMatch);

        try {
            resaveMatches(plagFile,content);
        } catch (Exception e) {
            throw new ProblemSavingPlagMatchFile();
        }
    }

    private void saveProcessedMatchesToFile(){
        String content = "";
        for(MPCPlagMatch mpcPlagMatch : matches){
            if(!mpcPlagMatch.plagiarized){
                content += forSave(mpcPlagMatch);
            }
        }

        try {
            resaveMatches(processedFile,content);
        } catch (Exception e) {
            throw new ProblemSavingProcessedMatchFile();
        }

    }

    private void resaveMatches(File file, String content) throws IOException {
        file.delete();
        tfu.createFileWithText(file,content);
    }

    private String forSave(MPCPlagMatch mpcPlagMatch){
        return mpcPlagMatch.fileA+"|"+mpcPlagMatch.fileB+"\n";
    }
}
