package TestComponents;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        // Specify the path to your reports folder
        String reportsFolderPath = System.getProperty("user.dir") +File.separator+"reports";
        String AllureReportsFolderPath = System.getProperty("user.dir") +File.separator+"allure-results";

        // Delete everything from the reports folder
        deleteFolderContents(new File(reportsFolderPath));
        deleteFolderContents(new File(AllureReportsFolderPath));
    }

    @Override
    public void onFinish(ISuite suite) {
        // Cleanup or additional actions if needed after the suite execution
    }

    private void deleteFolderContents(File folder) {
        try {
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteFolderContents(file);
                        } else {
                            if (!file.delete()) {
                                System.err.println("Unable to delete file: " + file.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}