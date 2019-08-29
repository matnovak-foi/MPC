package org.foi.mpc.executiontools.factories;

import org.foi.common.filesystem.file.TextFileUtility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComboTechiniquesLoader {
    private PreprocessingTechniqueFactory preprocessingTechniqueFactory;
    private File comboTechniqueLoadedFile;
    private boolean loadSuccesfull;

    public ComboTechiniquesLoader(PreprocessingTechniqueFactory preprocessingTechniqueFactory) {
        this.preprocessingTechniqueFactory = preprocessingTechniqueFactory;
    }

    public File getComboTechniqueLoadedFile() {
        return comboTechniqueLoadedFile;
    }

    public boolean isLoadSuccesfull() {
        return loadSuccesfull;
    }

    public void loadMultipleTechniquesFromText(String comboTechniques) {
        String[] techniques = comboTechniques.split("\n");
        loadSuccesfull = true;
        for(String technique : techniques) {
            technique = technique.trim();
            if(!loadTechniqueFromText(technique)){
                loadSuccesfull = false;
            }
        }
    }

    private boolean loadTechniqueFromText(String comboTechnique) {
        Pattern p = Pattern.compile("^(\\w+)=((\\w+,)+\\w+)$");
        Matcher matcher = p.matcher(comboTechnique);
        if(matcher.matches()) {
            loadTechnique(matcher);
            return true;
        } else {
            return false;
        }
    }

    private void loadTechnique(Matcher matcher) {
        String newTechniqueName = matcher.group(1);
        String[] parts = matcher.group(2).split(",");
        LinkedHashSet<String> techniqueParts = convertToSet(parts);
        preprocessingTechniqueFactory.createComboTechnique(newTechniqueName,techniqueParts);
    }

    private LinkedHashSet<String> convertToSet(String[] parts) {
        LinkedHashSet<String> techniqueParts = new LinkedHashSet<>();
        for(String part : parts){
            techniqueParts.add(part);
        }
        return techniqueParts;
    }

    public void loadMultipleTechniquesFromFile(File comboTechniquesFile) {
        this.comboTechniqueLoadedFile = comboTechniquesFile;

        TextFileUtility tfu = new TextFileUtility(StandardCharsets.UTF_8);
        String comboTechniquesAsText = "";

        try {
            comboTechniquesAsText = tfu.readFileContentToString(this.comboTechniqueLoadedFile);
        } catch (IOException e) {
            throw new TextFileUtility.FileReadWriteException(this.comboTechniqueLoadedFile.getPath());
        }

        loadMultipleTechniquesFromText(comboTechniquesAsText);
    }
}
