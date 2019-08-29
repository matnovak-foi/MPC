package org.foi.mpc.executiontools.factories;

import org.foi.common.filesystem.file.PropertiesFileUtility;
import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.calibrator.SimilarityDetectionToolSettingFactory;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.JPlag.JPlagTextAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockOriginalAdapter;
import org.foi.mpc.executiontools.detectionTools.sherlock.SherlockTokenisedAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneAdapterSettings;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneJavaAdapter;
import org.foi.mpc.executiontools.detectionTools.simgrune.SimGruneTextAdapter;
import org.foi.mpc.executiontools.detectionTools.spector.SpectorAdapter;

import java.io.File;
import java.util.*;

public class SimilarityDetectionToolFactory implements ExecutionToolFactory {

    private static List<String> availableTools = new ArrayList<>();
    private static Map<String, Class> tools = new HashMap<>();
    private File detectionToolConfigFileName;
    private SimilarityDetectionToolSettingFactory settingFactory;

    static {
        tools.put(JPlagJavaAdapter.NAME, JPlagJavaAdapter.class);
        tools.put(JPlagTextAdapter.NAME, JPlagTextAdapter.class);
        tools.put(SherlockTokenisedAdapter.NAME, SherlockTokenisedAdapter.class);
        tools.put(SherlockOriginalAdapter.NAME, SherlockOriginalAdapter.class);
        tools.put(SimGruneTextAdapter.NAME, SimGruneTextAdapter.class);
        tools.put(SimGruneJavaAdapter.NAME, SimGruneJavaAdapter.class);
        tools.put(SpectorAdapter.NAME, SpectorAdapter.class);

        availableTools.addAll(tools.keySet());
        Collections.sort(availableTools);
    }

    private String matchesDir;

    public SimilarityDetectionToolFactory(String matchesDir) {
        this.matchesDir = matchesDir;
    }

    public SimilarityDetectionToolFactory(String matchesDir, File detectionToolConfigFileName) {
        this.matchesDir = matchesDir;
        this.detectionToolConfigFileName = detectionToolConfigFileName;
        this.settingFactory = new SimilarityDetectionToolSettingFactory(detectionToolConfigFileName);
    }

    @Override
    public SimilarityDetectionTool createTool(String selectedTool) {
        Class<SimilarityDetectionTool> toolClass = tools.get(selectedTool);

        try {
            SimilarityDetectionTool tool = toolClass.newInstance();
            tool.setMatchesDirName(matchesDir);
            if(settingFactory != null)
                tool = settingFactory.setUpConfigsIfExist(tool);
            return tool;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNameException("was '"+selectedTool+"'='"+toolClass.getName()+"'");
        }
    }

    @Override
    public List<String> getAvailableTools() {
        return availableTools;
    }

    public File getDetectionToolConfigFileName() {
        return detectionToolConfigFileName;
    }
}
