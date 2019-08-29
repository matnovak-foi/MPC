package org.foi.mpc.executiontools.factories;

import org.foi.mpc.abstractfactories.ExecutionToolFactory;
import org.foi.mpc.executiontools.techniques.*;
import org.foi.mpc.executiontools.techniques.sherlock.*;

import java.io.File;
import java.util.*;

import static org.foi.common.CollectionHelper.convertSetToList;


public class PreprocessingTechniqueFactory implements ExecutionToolFactory {
    public static class TechniqueCreationParametersException extends RuntimeException {
        public TechniqueCreationParametersException(String message) {
            super(message);
        }
    }

    private static List<String> availablePPTechniques = new ArrayList<>();
    private static Map<String, Class> techniques = new HashMap<>();
    private Map<String, ComboPreprocessingTechnique> comboTechniques = new HashMap<>();
    private File templateExclusionConfigFile;

    static {
        techniques.put(NoTechniqueOriginal.NAME, NoTechniqueOriginal.class);
        techniques.put(SherlockNormalisePPTechnique.NAME, SherlockNormalisePPTechnique.class);
        techniques.put(RemoveCommentsTechnique.NAME,RemoveCommentsTechnique.class);
        techniques.put(SherlockNoCommentsPPTechnique.NAME, SherlockNoCommentsPPTechnique.class);
        techniques.put(SherlockNoCommentsNormalisedPPTechnique.NAME, SherlockNoCommentsNormalisedPPTechnique.class);
        techniques.put(SherlockNoCommentsNoWhiteSpacesPPTechnique.NAME, SherlockNoCommentsNoWhiteSpacesPPTechnique.class);
        techniques.put(CommonCodeRemoveTechnique.NAME, CommonCodeRemoveTechnique.class);
        techniques.put(SherlockNoWhiteSpacesPPTechnique.NAME, SherlockNoWhiteSpacesPPTechnique.class);
        techniques.put(TemplateExclusionTechnique.NAME, TemplateExclusionTechnique.class);

        availablePPTechniques.addAll(techniques.keySet());
        Collections.sort(availablePPTechniques);
    }

    public PreprocessingTechniqueFactory() {
    }

    public PreprocessingTechniqueFactory(File templateExclusionConfigFile) {
        this.templateExclusionConfigFile = templateExclusionConfigFile;
    }

    public File getTemplateExclusionConfigFile() {
        return templateExclusionConfigFile;
    }

    @Override
    public List<String> getAvailableTools() {
        return availablePPTechniques;
    }

    @Override
    public PreprocessingTechnique createTool(String selectedTechnique) {
        Class<PreprocessingTechnique> techniqueClass = techniques.get(selectedTechnique);

        if(techniqueClass != null) {
            PreprocessingTechnique instance = null;
            try {
                instance = techniqueClass.newInstance();
            } catch (Exception e) {
                throw new ClassNameException(e.getMessage());
            }
            instance = addParams(instance);
            return instance;
        } else {
            if(comboTechniques.containsKey(selectedTechnique))
                return comboTechniques.get(selectedTechnique).comboTechniqueInstance;
            else
                throw new ClassNameException(selectedTechnique);
        }
    }

    private PreprocessingTechnique addParams(PreprocessingTechnique instance) {
        if(instance instanceof TemplateExclusionTechnique) {
            if (templateExclusionConfigFile == null)
                throw new TechniqueCreationParametersException("templateExlusion direcory must be set to create TemplateExlusionTechnique");
            else {
                ((TemplateExclusionTechnique) instance).configure(templateExclusionConfigFile);
            }
        }
        return instance;
    }

    public void createComboTechnique(String comboTechniqueName, Set<String> newTechniqueComboParts) {
        List<PreprocessingTechnique> comboTechniqueParts = new ArrayList<>();

        for(String partName : newTechniqueComboParts)
            comboTechniqueParts.add(createTool(partName));

        PreprocessingTechnique comboTechniqueInstance = createComboTechniqueInstance(comboTechniqueName,comboTechniqueParts.toArray(new PreprocessingTechnique[newTechniqueComboParts.size()]));

        ComboPreprocessingTechnique cpt = new ComboPreprocessingTechnique();
        cpt.comboTechniqueParts.addAll(comboTechniqueParts);
        cpt.name = comboTechniqueName;
        cpt.comboTechniqueInstance = comboTechniqueInstance;

        comboTechniques.put(comboTechniqueName,cpt);
    }

    public PreprocessingTechnique createComboTechniqueInstance(String name, PreprocessingTechnique[] techniquesToCombine) {
        PreprocessingTechnique comboTechniqueInstance = new PreprocessingTechnique() {
            @Override
            public void runPreporcess(File dirToProcess) {
                for (PreprocessingTechnique technique : techniquesToCombine){
                    technique.runTool(dirToProcess);
                }
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public void runTool(File dirToProcess) {
                runPreporcess(dirToProcess);
            }
        };
        return comboTechniqueInstance;
    }

    public List<String> getAvailableComboTechniques(){
        return  convertSetToList(comboTechniques.keySet());
    }

    public ComboPreprocessingTechnique getComboTechnique(String name) {
        return comboTechniques.get(name);
    }

    public List<PreprocessingTechnique> getComboTechniqueParts(String name){
        ComboPreprocessingTechnique cpt = comboTechniques.get(name);
        return cpt.comboTechniqueParts;
    }

    public boolean containsComboTechnique(String comboTechniqueName) {
        return comboTechniques.containsKey(comboTechniqueName);
    }
}
