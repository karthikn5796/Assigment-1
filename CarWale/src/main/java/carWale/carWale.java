package carWale;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;


public class carWale {

	public static void main(String[] args) throws InterruptedException {
		ChromeDriver driver = null;
		
	try {	
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("https://www.carwale.com");
		driver.findElement(By.xpath("//div[text()='USED CARS']")).click();
		driver.findElement(By.xpath("//div[text()='Find Used Cars']")).click();
		Thread.sleep(3000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		
		WebDriverWait wt=new WebDriverWait(driver,Duration.ofSeconds(30));
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@searchtype='citySearch']")));
		
		driver.findElement(By.xpath("//input[@searchtype='citySearch']")).sendKeys("Chennai");
		driver.findElement(By.xpath("//li[@class='ui-menu-item ui-state-focus']//a[1]")).click();
	//	driver.findElement(By.xpath("//span[@id=\"closeLocIcon\"]")).click();

		driver.findElement(By.xpath("//span[text()='Choose budget']")).click();
		Thread.sleep(3000);
		
		
		driver.findElement(By.xpath("//input[@placeholder=\"Min\"]")).sendKeys("8");
		driver.findElement(By.xpath("//input[@placeholder=\"Max\"]")).sendKeys("12");
		
		driver.findElement(By.xpath("//div[text()='Go']")).click();
		Thread.sleep(3000);		
		driver.findElement(By.xpath("//li[@name='CarsWithPhotos']")).click();
		
		//wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@carfilterid='8']")));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click()",driver.findElement(By.xpath("//span[text()=' Hyundai ']/..")));
		js.executeScript("arguments[0].click()",driver.findElement(By.xpath("//span[text()='Creta']/..")));
		Select bM = new Select(driver.findElement(By.xpath("//select[@id='sort']")));
		bM.selectByVisibleText("KM: Low to High");
		List<WebElement> kms = driver.findElements(By.xpath("//span[contains(@class,'slkms')]"));
		for(WebElement e:kms) {
			System.out.println(e.getText());
		}
		boolean flg=false;
		
		for(int i=1;i<kms.size();i++) {
			if (kms.get(i).getText().compareTo(kms.get(i-1).getText())>0 || kms.get(i).getText().compareTo(kms.get(i-1).getText())==0) {
				flg=true;
			}else {
				flg=false;
				break;
			}
		}
		
		
		if(flg) {
			System.out.println("KM: Low to High Validation succesful");
		}else {
			System.out.println("KM: Low to High Validation failed!!");
		}
		for(int x=1;x<=4;x++) {
			wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='img-placer image-container__item']//span[@class='shortlist-icon--inactive shortlist'])["+x+"]")));
			js.executeScript("arguments[0].click()",driver.findElement(By.xpath("(//div[@class='img-placer image-container__item']//span[@class='shortlist-icon--inactive shortlist'])["+x+"]")));
		}
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@data-action='ShortList&CompareWindow_Click']")));
		driver.findElement(By.xpath("//li[@data-action='ShortList&CompareWindow_Click']")).click();
		
		wt.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='shortlistCardetails']//p[2]")));
		List<WebElement> price = driver.findElements(By.xpath("//div[@class='shortlistCardetails']//p[2]"));
		
		for(int p=1;p<price.size();p++) {
			WebElement oldEle=price.get(p-1);
			WebElement newEle=price.get(p);
			if(Double.parseDouble(newEle.getText().split(" ")[1])>Double.parseDouble(oldEle.getText().split(" ")[1])) {
				Actions act=new Actions(driver);
				act.dragAndDrop(newEle,oldEle).build().perform();
				price.set(p,oldEle);
				price.set(p-1, newEle);
				price.parallelStream();
			}			
		}	
		Thread.sleep(5000);		
		driver.close();
	
	}catch(Exception e) {
		System.err.println(e.getMessage());
		driver.quit();
	}
	
	}

}
