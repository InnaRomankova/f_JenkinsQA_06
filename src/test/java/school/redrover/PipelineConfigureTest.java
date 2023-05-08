package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;

public class PipelineConfigureTest extends BaseTest {

    public void createPipeline() {
        WebElement createJobButton = getDriver().findElement(By.xpath("//a[@href = 'newJob']"));
        createJobButton.click();

        WebElement name = getDriver().findElement(By.id("name"));
        name.sendKeys("test-pipeline");

        WebElement newPipeline = getDriver()
                .findElement(By.xpath("//*[@id='j-add-item-type-standalone-projects']//li[2]"));
        newPipeline.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();
    }

    @Test
    public void testSetDescription() {
        String descriptionText = "This is a test description";

        createPipeline();

        WebElement descriptionField = getDriver().findElement(By.name("description"));
        descriptionField.sendKeys(descriptionText);

        WebElement saveButton = getDriver().findElement(By.name("Submit"));
        saveButton.click();

        WebElement actualDescription = getDriver().findElement(By.xpath("//*[@id='description']/div"));

        Assert.assertEquals(actualDescription.getText(), descriptionText);
    }

    @Test
    public void testDiscardOldBuildsIsCheckedEmptyDaysAndBuildsField() {
        createPipeline();

        WebElement discardOldBuildsLabel = getDriver()
                .findElement(By.xpath("//label[contains(text(),'Discard old builds')]"));
        discardOldBuildsLabel.click();

        WebElement saveButton = getDriver().findElement(By.name("Submit"));
        saveButton.click();

        WebElement configureMenu = getDriver()
                .findElement(By.xpath("//*[@href='/job/test-pipeline/configure']"));
        configureMenu.click();

        WebElement discardOldBuildsCheckbox = getDriver().findElement(By.id("cb2"));

        Assert.assertTrue(discardOldBuildsCheckbox.isSelected());
    }

    @Test
    public void testPipelineCreation() {
        final String PIPELINE_NAME = "My_pipeline";

        WebElement newItem = getDriver().findElement(By.xpath(
                "//a[@href='/view/all/newJob']"));
        newItem.click();

        getWait2().until(ExpectedConditions.visibilityOf(getDriver().findElement(By.id("name"))));

        WebElement pipelineType = getDriver().findElement(By.cssSelector(".org_jenkinsci_plugins_workflow_job_WorkflowJob"));
        pipelineType.click();

        WebElement nameField = getDriver().findElement(By.id("name"));
        nameField.sendKeys(PIPELINE_NAME);

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        getWait5().until(ExpectedConditions.elementToBeClickable(By.name("Submit")));

        WebElement enableToggles = getDriver().findElement(By.id("toggle-switch-enable-disable-project"));
        boolean isPipelineEnabled = Boolean.parseBoolean(getDriver().findElement(By.xpath("//input[@name='enable']"))
                .getAttribute("value"));
        if (isPipelineEnabled){
            enableToggles.click();
        }

        getDriver().findElement(By.name("Submit")).click();

        getWait2().until(ExpectedConditions.textToBe(By.tagName("h1"), "Pipeline " + PIPELINE_NAME));

        String disabledWarning = getDriver().findElement(By.id("enable-project")).getText();

        WebElement configurePipeline= getDriver().findElement(By.linkText("Configure"));
        configurePipeline.click();

        getWait5().until(ExpectedConditions.textToBe(By.tagName("h2"), "General"));

        boolean isPipelineEnabledAfterDisable = Boolean.parseBoolean(getDriver().findElement(
                By.xpath("//input[@name='enable']")).getAttribute("value"));

        Assert.assertTrue(disabledWarning.contains("This project is currently disabled"));
        Assert.assertFalse(isPipelineEnabledAfterDisable,"false");
    }
}