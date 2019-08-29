package org.foi.mpc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String comboTechniquesProperties = sc.getRealPath("/WEB-INF") + File.separator + sc.getInitParameter("comboTechniques");
        String detectionToolsProperties = sc.getRealPath("/WEB-INF") + File.separator + sc.getInitParameter("detectionTools");
        String templateExclusionProperties = sc.getRealPath("/WEB-INF") + File.separator + sc.getInitParameter("templateExlusion");

        System.out.println(comboTechniquesProperties);
        System.out.println(detectionToolsProperties);
        System.out.println(templateExclusionProperties);

        MPCContext.TEMPLATE_EXCLUSION_PROPERTIES_FILE = templateExclusionProperties;
        MPCContext.DETECTION_TOOLS_PROPERITES_FILE = detectionToolsProperties;
        MPCContext.COMBO_TECHNIQUES_PROPERTIES_FILE = comboTechniquesProperties;
        MPCContext.DEFAULT_SOURCE_DIR = sc.getInitParameter("defaultSourceDir");
        MPCContext.DEFAULT_WORKING_DIR = sc.getInitParameter("defaultWorkingDir");
        MPCContext.DEFAULT_INPUT_DIR_PATH = sc.getInitParameter("defaultInputDirPath");
        MPCContext.DEFAULT_SOURCE_DIR_SOCO = sc.getInitParameter("defaultSourceDirSOCO");
        MPCContext.DEFAULT_WORKING_DIR_SOCO = sc.getInitParameter("defaultWorkingDirSOCO");
        MPCContext.DEFAULT_INPUT_DIR_PATH_SOCO = sc.getInitParameter("defaultInputDirPathSOCO");
        MPCContext.DEFAULT_INPUT_DIR_DEPTH = Integer.parseInt(sc.getInitParameter("defaultInputDirDepth"));
        MPCContext.DEFAULT_USE_SOCO_SUMMARY_REPORT = Boolean.parseBoolean(sc.getInitParameter("useDefaultSOCOInSummaryReport"));
        MPCContext.DEFAULT_USE_SOCO_STATISTIC_REPORT = Boolean.parseBoolean(sc.getInitParameter("useDefaultSOCOInStatisticReport"));
        MPCContext.ANALISYS_MODE = Boolean.parseBoolean(sc.getInitParameter("analysisMode"));
        MPCContext.CONSOLE_PRINT = Boolean.parseBoolean(sc.getInitParameter("consolePrint"));
        MPCContext.MATCHES_THREAD_READ = Boolean.parseBoolean(sc.getInitParameter("matchesThreadRead"));
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
