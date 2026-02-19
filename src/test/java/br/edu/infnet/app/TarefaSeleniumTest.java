package br.edu.infnet.app;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TarefaSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("http://localhost:" + port + "/index.html");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    void testCriarTarefaComSucesso() {
        // Preenche o formulário de nova tarefa
        driver.findElement(By.name("categoria")).click();
        driver.findElement(By.cssSelector("option[value='programacao']")).click();
        driver.findElement(By.name("descricao")).sendKeys("Estudar Selenium");
        driver.findElement(By.id("btnNovaTarefa")).click();

        // Aguarda a tabela ser atualizada
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#task-list tr"), 0));
        List<WebElement> rows = driver.findElements(By.cssSelector("#task-list tr"));
        assertTrue(rows.get(0).getText().contains("Estudar Selenium"));
        assertTrue(rows.get(0).getText().contains("Programacao"));
    }

    @Test
    @Order(2)
    void testCriarTarefaComDescricaoVazia() {
        driver.findElement(By.cssSelector("option[value='programacao']")).click();
        driver.findElement(By.id("btnNovaTarefa")).click();

        WebElement validacaoDiv = driver.findElement(By.id("validacaoForm"));
        wait.until(ExpectedConditions.not(ExpectedConditions.attributeContains(validacaoDiv, "class", "d-none")));
        assertTrue(validacaoDiv.isDisplayed());
        assertTrue(validacaoDiv.getText().contains("A descrição não pode ser vazia."));
    }

    @Test
    @Order(3)
    void testEditarTarefa() {
        // Cria uma tarefa inicial
        driver.findElement(By.name("categoria")).click();
        driver.findElement(By.cssSelector("option[value='trabalho']")).click();
        driver.findElement(By.name("descricao")).sendKeys("Tarefa para editar");
        driver.findElement(By.id("btnNovaTarefa")).click();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#task-list tr"), 1));

        // Abre o modal de edição
        driver.findElement(By.xpath("//tr[td[contains(.,'Tarefa para editar')]]//button[@title= 'Editar']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("taskModal")));

        WebElement descricaoModal = driver.findElement(By.id("descricao"));
        descricaoModal.clear();
        descricaoModal.sendKeys("Tarefa editada");

        driver.findElement(By.id("salvarTarefa")).click();

        // Aguarda o modal fechar e verifica a alteração
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("taskModal")));
        List<WebElement> rows = driver.findElements(By.cssSelector("#task-list tr"));
        assertTrue(rows.get(1).getText().contains("Tarefa editada"));
    }

    @Test
    @Order(4)
    void testExcluirTarefa() {
        // Cria uma tarefa
        driver.findElement(By.name("categoria")).click();
        driver.findElement(By.cssSelector("option[value='pessoal']")).click();
        driver.findElement(By.name("descricao")).sendKeys("Tarefa para excluir");
        driver.findElement(By.id("btnNovaTarefa")).click();

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#task-list tr"), 2));

        // Exclui a tarefa
        driver.findElement(By.xpath("//tr[td[contains(.,'Tarefa para excluir')]]//button[@title= 'Excluir']")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();

        // Verifica que a tabela ficou vazia
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("#task-list tr"), 2));
        assertEquals(2, driver.findElements(By.cssSelector("#task-list tr")).size());
    }
}