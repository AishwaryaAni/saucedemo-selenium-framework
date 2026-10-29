package com.qa.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Captures evidence on failure — the automated equivalent of attaching a screenshot to a bug. */
public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR = Paths.get("target", "screenshots");
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() { }

    /** @return path relative to the report folder, ready to embed in the HTML report */
    public static String capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String fileName = testName + "_" + LocalDateTime.now().format(TS) + ".png";
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(SCREENSHOT_DIR.resolve(fileName), png);
            return "../screenshots/" + fileName;
        } catch (IOException e) {
            throw new IllegalStateException("Could not save screenshot for " + testName, e);
        }
    }
}
