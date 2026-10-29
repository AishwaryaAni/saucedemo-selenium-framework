package com.qa.framework.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.framework.driver.DriverFactory;
import com.qa.framework.reporting.ExtentManager;
import com.qa.framework.utils.ScreenshotUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Hooks into TestNG's lifecycle: logs each result to the Extent report
 * and attaches a screenshot automatically when a test fails.
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest test = ExtentManager.getInstance()
                .createTest(result.getMethod().getMethodName(), description == null ? "" : description)
                .assignCategory(result.getMethod().getGroups());
        ExtentManager.setTest(test);
        LOG.info("▶ START  {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().pass("Test passed");
        LOG.info("✔ PASS   {}", result.getMethod().getMethodName());
        ExtentManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String name = result.getMethod().getMethodName();
        LOG.error("✘ FAIL   {} — {}", name, result.getThrowable().getMessage());
        ExtentTest test = ExtentManager.getTest();
        test.fail(result.getThrowable());
        try {
            String path = ScreenshotUtil.capture(DriverFactory.getDriver(), name);
            test.fail("Screenshot at failure", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } catch (RuntimeException e) {
            LOG.warn("Screenshot not captured: {}", e.getMessage());
        }
        ExtentManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentManager.getTest();
        if (test != null) {
            test.skip(result.getThrowable() == null ? "Skipped" : result.getThrowable().getMessage());
        }
        LOG.warn("↷ SKIP   {}", result.getMethod().getMethodName());
        ExtentManager.removeTest();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.getInstance().flush(); // writes target/extent-report/index.html
    }
}
