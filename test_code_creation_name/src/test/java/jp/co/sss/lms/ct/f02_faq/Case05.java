package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

	private String detail = "http://localhost:8080/lms/course/detail";

	private String login = "http://localhost:8080/lms/";

	private String faq = "http://localhost:8080/lms/faq";

	private String help = "http://localhost:8080/lms/help";

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		goTo(login);
		assertEquals(login, webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		webDriver.findElement(By.name("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.name("password")).sendKeys("Pomupomupurin416");

		webDriver.findElement(By.className("btn-primary")).click();
		visibilityTimeout(By.cssSelector("button.navbar-btn"), 5);
		assertEquals(detail, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		webDriver.findElement(By.className("dropdown-toggle")).click();
		webDriver.findElement(By.linkText("ヘルプ")).click();
		assertEquals(help, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		String originalWindowString = webDriver.getWindowHandle();
		webDriver.findElement(By.linkText("よくある質問")).click();
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!originalWindowString.contentEquals(windowHandle)) {

				webDriver.switchTo().window(windowHandle);
				break;
			}
		}
		assertEquals(faq, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {

		webDriver.findElement(By.id("form")).sendKeys("事業所");
		webDriver.findElement(By.cssSelector("input[value='検索']")).click();
		visibilityTimeout(By.className("table-hover"), 5);
		getEvidence(new Object() {
		}, "01");
		webDriver.findElement(By.tagName("body")).sendKeys(org.openqa.selenium.Keys.END);
		assertTrue(webDriver.findElement(By.className("table-hover")).getText().contains("事業所"));
		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {

		webDriver.findElement(By.cssSelector("input[value='クリア']")).click();
		assertEquals("", webDriver.findElement(By.id("form")).getAttribute("value"));
		getEvidence(new Object() {
		}, "");

	}

}
