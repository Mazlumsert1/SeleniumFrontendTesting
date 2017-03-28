import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by mazlumsert on 28/03/2017.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeleniumTest {

    private static final int WAIT_MAX = 4;
    // WebDriver API
    static WebDriver driver;


    @Before
    public void setUp() throws Exception {

        System.setProperty( "webdriver.chrome.driver",
                "/Users/mazlumsert/Desktop/SeleniumFrontendTesting/drivers/chromedriver" );


        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    public void test1(){
        ( new WebDriverWait( driver, WAIT_MAX ) ).until( new ExpectedCondition<Boolean>()
        {
            // Via the apply method
            public Boolean apply(WebDriver webdriver)
            {
                int tableLength = webdriver.findElement( By.tagName( "tbody" ) )
                        .findElements( By.tagName( "tr" ) )
                        .size();

                assertThat( tableLength, is( 5 ) );

                return true;
            }
        } );
    }

    @Test
    public void test2() throws Exception
    {
        driver.findElement( By.id( "filter" ) ).sendKeys( "2002" );

        int tableLength = driver.findElement( By.tagName( "tbody" ) )
                .findElements( By.tagName( "tr" ) )
                .size();

        assertThat( tableLength, is( 2 ) );
    }

    @Test
    public void test3() throws Exception
    {
        driver.findElement( By.id( "filter" ) ).clear();

        int tableLength = driver.findElement( By.tagName( "tbody" ) )
                .findElements( By.tagName( "tr" ) )
                .size();

        assertThat( tableLength, is( 5 ) );
    }


    @Test
    public void test4() throws Exception
    {
        driver.findElement( By.id( "h_year" ) ).click();

        List<WebElement> allRows = driver.findElements(By.xpath(".//tbody/tr"));

        int last = allRows.size()-1;

        String firstRow = allRows.get(0).findElement(By.tagName("td")).getText();
        String lastRow  = allRows.get(last).findElement(By.tagName("td")).getText();

        assertThat( firstRow, is( "938" ) );
        assertThat( lastRow, is( "940" ) );
    }

    @Test
    public void test5() throws Exception
    {
        List<WebElement> rows = driver.findElement( By.tagName( "tbody" ) )
                .findElements( By.tagName( "tr" ) );

        for( WebElement row : rows )
        {
            List<WebElement> columns = row.findElements( By.tagName( "td" ) );

            if( columns.get( 0 ).getText().equals( "938" ) )
            {
                columns.get( columns.size() - 1 )
                        .findElements( By.tagName( "a" ) )
                        .get( 0 )
                        .click();

                WebElement descField = driver.findElement( By.id( "description" ) );
                descField.clear();
                descField.sendKeys( "Cool car" );
                driver.findElement( By.id( "save" ) ).click();

                assertThat( columns.get( 5 ).getText(), is( "Cool car" ) );

                break;
            }
        }
   }

    @Test
    public void test6() throws Exception
    {
        driver.findElement( By.id( "new" ) ).click();
        driver.findElement( By.id( "save" ) ).click();

        String errorMessage = driver.findElement( By.id( "submiterr" ) ).getText();

        int tableSize = driver.findElement( By.tagName( "tbody" ) )
                .findElements( By.tagName( "tr" ) )
                .size();

        assertThat( errorMessage, is( "All fields are required" ) );
        assertThat( tableSize, is( 5 ) );
    }
    
    @Test
    public void test7() throws Exception
    {
        driver.findElement( By.id( "new" ) ).click();

        driver.findElement( By.id( "year" ) ).sendKeys( "2008" );
        driver.findElement( By.id( "registered" ) ).sendKeys( "2002-5-5" );
        driver.findElement( By.id( "make" ) ).sendKeys( "Kia" );
        driver.findElement( By.id( "model" ) ).sendKeys( "Rio" );
        driver.findElement( By.id( "description" ) ).sendKeys( "As new" );
        driver.findElement( By.id( "price" ) ).sendKeys( "31000" );

        driver.findElement( By.id( "save" ) ).click();

        int tableSize = driver.findElement( By.tagName( "tbody" ) )
                .findElements( By.tagName( "tr" ) )
                .size();

        assertThat( tableSize, is( 6 ) );
    }

}