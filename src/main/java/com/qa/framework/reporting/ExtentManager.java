package com.qa.framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qa.framework.config.ConfigReader;

/** Builds one HTML report per run and tracks the current test node per thread. */
public final class ExtentManager {

    private static final String REPORT_PATH = "target/extent-report/index.html";
    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
    private static ExtentReports extent;

    private ExtentManager() { }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter(REPORT_PATH);
            spark.config().setDocumentTitle("SauceDemo Automation Report");
            spark.config().setReportName("UI Regression — Selenium + TestNG");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
            extent.setSystemInfo("Headless", ConfigReader.get("headless"));
            extent.setSystemInfo("Base URL", ConfigReader.get("baseUrl"));
            extent.setSystemInfo("Java", System.getProperty("java.version"));
        }
        return extent;
    }

    public static void setTest(ExtentTest test) { CURRENT_TEST.set(test); }
    public static ExtentTest getTest()          { return CURRENT_TEST.get(); }
    public static void removeTest()             { CURRENT_TEST.remove(); }
}
