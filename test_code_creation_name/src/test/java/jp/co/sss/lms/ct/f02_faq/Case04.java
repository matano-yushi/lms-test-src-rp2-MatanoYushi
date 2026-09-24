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
 * ケース04
 * @author 俣野宥士
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	private String detail = "http://localhost:8080/lms/course/detail";

	private String login = "http://localhost:8080/lms/";

	private String faq = "http://localhost:8080/lms/faq";

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
		assertEquals("ログイン | LMS", webDriver.getTitle());
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
		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		String originalWindowString = webDriver.getWindowHandle();
		webDriver.findElement(By.linkText("よくある質問")).sendKeys(org.openqa.selenium.Keys.ENTER);

		//現在ブラウザで開いているすべてのタブ（IDのリスト）を1つずつ取り出して
		//windowHandle に代入しながらループ処理
		for (String windowHandle : webDriver.getWindowHandles()) {
			if (!originalWindowString.contentEquals(windowHandle)) {
				//switchTo：操作対象の「切り替え機能」
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}
		assertEquals(faq, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");
	}

}
