package org.foi.mpc.phases.readerphase;

import org.foi.mpc.MPCContext;
import org.foi.common.filesystem.directory.DirectoryFileUtility;
import org.foi.mpc.matches.MPCMatchFileUtility;
import org.foi.mpc.phases.PhaseFactory;
import org.foi.mpc.matches.models.MPCMatch;
import org.foi.mpc.matches.models.MPCMatchBuilder;
import org.foi.common.filesystem.directory.InMemoryDir;
import org.foi.common.filesystem.file.InMemoryFile;
import org.foi.common.filesystem.file.InMemoryObjectFileUtility;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class MPCMatchReaderPhaseTest implements MPCMatchListener {
    @Parameterized.Parameters(name = "{index}: ThreadUse({0})")
    public static Object[] data() {
       return new Object[] { true, false };
    }

    @Parameterized.Parameter
    public boolean treadUse;

    private int calledTimes;
    private List<MPCMatch> readMatches;
    private MPCMatchReaderPhase readerPhase;
    private List<File> dirs = new ArrayList<>();
    private MPCMatchFileUtility mfu;

    @Before
    public void setUp(){
        calledTimes = 0;
        readMatches = new ArrayList<>();

        readerPhase = (MPCMatchReaderPhase) new PhaseFactory(MPCContext.MATCHES_DIR).createMPCMatchReadPhase();
        readerPhase.setListener(this);
        readerPhase.setDirsToProcess(dirs);
        mfu = new MPCMatchFileUtility();
        MPCContext.MATCHES_THREAD_READ = treadUse;
    }

    @Test
    public void readerCallsListenerForEveryDirAndEveryFileInDir(){
        mfu.setObjectFileUtility(new InMemoryObjectFileUtility());
        readerPhase.setMPCMatchFileUtility(mfu);

        createInMemory3DirsWith2Files();
        readerPhase.setDirsToProcess(dirs);

        readerPhase.runPhase();

        assertEquals(6, calledTimes);
    }

    private void createInMemory3DirsWith2Files() {
        InMemoryFile file = new InMemoryFile("dummyFile");
        InMemoryFile file2 = new InMemoryFile("dummyFile2");
        InMemoryDir dir = new InMemoryDir("dir");
        InMemoryDir matchesDir = new InMemoryDir("dir"+File.separator+ MPCContext.MATCHES_DIR);
        dir.addFile(matchesDir);
        matchesDir.addFile(file);
        matchesDir.addFile(file2);
        dirs.add(dir);
        dirs.add(dir);
        dirs.add(dir);
    }

    @Test
    public void readsTwoMPCFilesAddPassesItToListener() throws IOException {
        for(int i=0;i<10;i++) {
            MPCMatch match = new MPCMatchBuilder().
                    withFileA("student1").withFileB("student2").build();
            MPCMatch match2 = new MPCMatchBuilder().
                    withFileA("student3").withFileB("student4").
                    with100Similarity().build();

            match.matchesDir.mkdirs();

            MPCMatchFileUtility mfu = new MPCMatchFileUtility();
            mfu.saveToFile(match);
            mfu.saveToFile(match2);

            dirs.add(match.matchesDir.getParentFile());
            readMatches = new ArrayList<>();
            readerPhase.runPhase();

            List<MPCMatch> matches = new ArrayList<>();
            matches.add(match);
            matches.add(match2);
            assertThat(readMatches, IsIterableContainingInAnyOrder.containsInAnyOrder(matches.toArray()));

            DirectoryFileUtility.deleteDirectoryTree(match.matchesDir.getParentFile());
            dirs.remove(match.matchesDir.getParentFile());
        }
    }

    @Override
    public void processMatch(MPCMatch match) {
        calledTimes++;
        readMatches.add(match);
    }
}
