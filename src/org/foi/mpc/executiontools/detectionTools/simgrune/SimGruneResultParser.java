package org.foi.mpc.executiontools.detectionTools.simgrune;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.common.filesystem.file.TextFileUtility;

public class SimGruneResultParser {
    public class SimGruneException extends RuntimeException {
        public SimGruneException(String message) {
            super(message);
        }
    }

    private SimGruneMatchesWriter writer;
    private String line;

    public SimGruneResultParser(File matchesDir) {
        this.writer = new SimGruneMatchesWriter(matchesDir);
    }

    public void processResultFile(File resultFile) throws TextFileUtility.FileBufferedReaderException {
        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        BufferedReader reader = tfu.createBufferedReader(resultFile);
        try {
            parseAllResultFileLines(reader);
            tfu.closeBufferedReader(reader);
        } catch (IOException ex) {
            throw new TextFileUtility.FileBufferedReaderException(ex.getMessage());
        } finally {
            tfu.closeBufferedReader(reader);
        }
        writer.writeMatchesToDirectory();
    }

    private void parseAllResultFileLines(BufferedReader reader) throws IOException {
        while ((line = reader.readLine()) != null) {
            parseResultLine(line);
        }
    }

    protected void parseResultLine(String simResultLine) {
        if (simResultLine.startsWith("File")) {
            parseHeaderLine(simResultLine);
        } else if (simResultLine.startsWith("Total")) {
            parseTotalLine(simResultLine);
        } else if (simResultLine.contains(": line")) {
            parseMatchPartLine(simResultLine);
        } else if(simResultLine.contains("consists for")) {
            parsePrecentMatchPartLine(simResultLine);
        } else if (isNotLineToBeSkiped(simResultLine)) {
            throw new SimGruneException("Unknown sim result line!");
        }
    }

    private void parseHeaderLine(String headerLine) {
        Pattern pattern = Pattern.compile("^File .*/(.+): (\\d+) (tokens|words), (\\d+) lines?.*$");
        Matcher m = pattern.matcher(headerLine);
        if (m.matches()) {
            writer.addDetectedFile(getFileNameInHeader(m), getLineCountInHeader(m), getTokenCountInHeader(m));
        } else {
            throw new SimGruneException("Unknown sim result header line!");
        }
    }

    private static int getLineCountInHeader(Matcher m) throws NumberFormatException {
        return Integer.parseInt(m.group(4));
    }

    private static int getTokenCountInHeader(Matcher m) throws NumberFormatException {
        return Integer.parseInt(m.group(2));
    }

    private static String getFileNameInHeader(Matcher m) {
        return m.group(1);
    }

    private void parseTotalLine(String totalLine) {
        Pattern pattern = Pattern.compile("^Total input: (\\d+) files .* (tokens|words)$");
        Matcher m = pattern.matcher(totalLine);
        if (m.matches()) {
            checkIfDetectedFilesCountIsOk(m);
        }else{
            throw new SimGruneException("Wrong total line!");
        }
    }

    private void checkIfDetectedFilesCountIsOk(Matcher m) throws SimGruneException, NumberFormatException {
        if (writer.getDetectedFilesCount() != getFileCountForTotalLine(m)) {
            throw new SimGruneException("Total line and detectedFilesWithLines do not match up!");
        }
    }

    private static int getFileCountForTotalLine(Matcher m) throws NumberFormatException {
        return Integer.parseInt(m.group(1));
    }

    private void parseMatchPartLine(String matchPartLine) {
        Pattern pattern = Pattern.compile("^.*/(.+): line (\\d+)-(\\d+) *\\|.*/(.+): line (\\d+)-(\\d+) *\\[(\\d+)\\]$");
        Matcher m = pattern.matcher(matchPartLine);
        if (m.matches()) {
            writer.processMatchPartLine(m);
        } else {
            throw new SimGruneException("Unknown match part line wrong!");
        }
    }

    private void parsePrecentMatchPartLine(String matchPartLine) {
        Pattern pattern = Pattern.compile("^.*/(.+) consists for (\\d+) % of .*/(.+) material$");
        Matcher m = pattern.matcher(matchPartLine);
        if (m.matches()) {
            writer.processPrecentMatchPart(m);
        } else {
            throw new SimGruneException("Unknown precent match part line!");
        }
    }

    private static boolean isNotLineToBeSkiped(String simResultLine) {
        return !simResultLine.equals("");
    }

    protected String getLine() {
        return this.line;
    }    
    
    protected SimGruneMatchesWriter getWriter() {
        return this.writer;
    }
    
}
