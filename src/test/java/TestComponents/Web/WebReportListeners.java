package TestComponents.Web;

import AbstarctComponents.Web.FrameworkAssertionLibrary;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import Utils.WebExtentReporterNG;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static AbstarctComponents.Web.GenericWebPage.instanceDriver;
public class WebReportListeners extends WebBaseTest implements ITestListener{
	ExtentTest test;
	ExtentReports extent = WebExtentReporterNG.getReportObject();
	static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>(); //Thread safe
	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		test = extent.createTest(result.getMethod().getMethodName());
		extentTest.set(test);//unique thread id(ErrorValidationTest)->test
	}
	@Override
	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		assertionAdd().clear();
		extentTest.get().log(Status.PASS, "Test Passed");
	}
	@Override
	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		assertionAdd().clear();
		extentTest.get().fail(result.getThrowable());//

		try {
			instanceDriver = (WebDriver) result.getTestClass().getRealClass().getField("driver")
					.get(result.getInstance());

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String filePath = null;
		try {
			filePath = getScreenshot(result.getMethod().getMethodName(),instanceDriver);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
	}
	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		extent.flush();
	}
	public static List<FrameworkAssertionLibrary.AssertionInfo> assertionAdd(){
		List<FrameworkAssertionLibrary.AssertionInfo> allAssertions = FrameworkAssertionLibrary.getAllAssertions();
		for (FrameworkAssertionLibrary.AssertionInfo assertion : allAssertions) {
			String status = assertion.getStatus() ? "Pass" : "Fail";
			String message = "Step: " + status + " - " + assertion.getMessage();
			System.out.println(message);
			if (extentTest.get() != null) {
				if (assertion.getStatus()) {
					extentTest.get().log(Status.PASS, message);
				} else {
					extentTest.get().log(Status.FAIL, message);
				}
			}
		}
		return allAssertions;
	}
	public String getScreenshot(String testCaseName, WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot)driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File file = new File(System.getProperty("user.dir") +File.separator+ "reports" +File.separator+ testCaseName + ".png");
		FileUtils.copyFile(source, file);
		return System.getProperty("user.dir") + File.separator +"reports" + File.separator + testCaseName + ".png";
	}
}
