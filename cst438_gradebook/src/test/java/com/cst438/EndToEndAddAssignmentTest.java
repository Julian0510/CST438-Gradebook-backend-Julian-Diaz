package com.cst438;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@SpringBootTest
public class EndToEndAddAssignmentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "/Users/juliandiaz/Documents/chromedriver";
	public static final String URL = "http://localhost:3000";
	public static final String ALIAS_NAME = "test";
	public static final int SLEEP_DURATION = 1000;
	public static final String TEST_ASSIGNMENT_NAME = "Test Assignment";
	public static final String TEST_DUE_DATE = "2022-11-22";
	public static final int TEST_COURSE_ID = 123456;
	
	@Autowired
	CourseRepository courseRepository;

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignment() throws Exception {
		Assignment x = null;
				
		do {
			System.out.println(assignmentRepository);
        	for (Assignment assignment : assignmentRepository.findAll()) {
        		if (assignment.getName().equals(TEST_ASSIGNMENT_NAME)) {
        			x = assignment;
        			break;
        		}
        	}
        	
        	if (x != null) {
        		assignmentRepository.delete(x);
        	}
			
		} while (x != null); {
	        // browser    property name                 Java Driver Class
	        // edge       webdriver.edge.driver         EdgeDriver
	        // FireFox    webdriver.firefox.driver      FirefoxDriver
	        // IE         webdriver.ie.driver           InternetExplorerDriver
	        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
	        WebDriver driver = new ChromeDriver();
	        // Puts an Implicit wait for 10 seconds before throwing exception
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        
	        try {
	        	driver.get(URL);
	        	Thread.sleep(SLEEP_DURATION);
	        	
	        	driver.findElement(By.id("addAssignment")).click();
				Thread.sleep(SLEEP_DURATION);
				
				driver.findElement(By.name("name")).sendKeys(TEST_ASSIGNMENT_NAME);
				driver.findElement(By.name("dueDate")).sendKeys(TEST_DUE_DATE);
				driver.findElement(By.name("courseId")).sendKeys(Integer.toString(TEST_COURSE_ID));
	        	
				driver.findElement(By.id("Submit")).click();
				Thread.sleep(SLEEP_DURATION);

				String toast_text = driver.findElement(By.cssSelector(".Toastify__toast-body div:nth-child(2)")).getText();
				Thread.sleep(SLEEP_DURATION);
				
				assertEquals(toast_text, "Assignment successfully added");
				
				boolean foundAssignment = false;
				for (Assignment assignment : assignmentRepository.findAll()) {
	        		if (assignment.getName().equals(TEST_ASSIGNMENT_NAME)) {
	        			foundAssignment = true;
	        			break;
	        		}
				}
				
				assertTrue(foundAssignment, "Assignment " + TEST_ASSIGNMENT_NAME + " was not added.");
	        }catch (Exception ex) {
				
				throw ex;
	        } finally {
				
				Assignment assignment = null;
							
				for (Assignment a : assignmentRepository.findAll()) {
					System.out.println(a.getName());
	            	if (a.getName().equals(TEST_ASSIGNMENT_NAME)) {
	            		assignment = a;
	        			break;
	        		}
	            }
	            if (assignment != null)
	                assignmentRepository.delete(assignment);
	            
	            driver.quit();
			}
		}
	}
}
