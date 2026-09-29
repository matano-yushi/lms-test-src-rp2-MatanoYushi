package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.WebElement;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	private String login = "http://localhost:8080/lms/";

	private String detail = "http://localhost:8080/lms/course/detail";

	private String userDetail = "http://localhost:8080/lms/user/detail";

	private String reportRegister = "http://localhost:8080/lms/report/regist";

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

		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		assertEquals(detail, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(3)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		WebElement user = webDriver.findElement(By.partialLinkText("ようこそ"));
		user.click();
		assertEquals("ユーザー詳細", webDriver.getTitle());
		assertEquals(userDetail, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		int tableCount = webDriver.findElements(By.cssSelector("table.table-hover tr")).size();
		for (int i = 0; i < tableCount; i++) {

			String detailButton = webDriver.findElements(By.cssSelector("table.table-hover tr")).get(i).getText();
			if (detailButton.contains("2022年10月2日(日)")) {
				scrollBy("800");
				webDriver.findElements(By.cssSelector("table.table-hover tr")).get(i)
						.findElement(By.cssSelector("input[value = '修正する']")).click();
				break;
			}
		}
		visibilityTimeout(By.tagName("h2"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertEquals(reportRegister, webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習理解度：学習項目が未入力")
	void test05() {

		webDriver.findElement(By.xpath("//input[@id='intFieldName_0']")).clear();
		webDriver.findElement(By.xpath("//select[@id='intFieldValue_0']")).sendKeys("3");
		//String study = webDriver.findElement(By.xpath("//input[@id='intFieldName_0']")).getAttribute("value");
		//assertEquals("", study);
		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		visibilityTimeout(By.tagName("h2"), 5);
		String studyTest = webDriver.findElement(By.xpath("//input[@id='intFieldName_0']")).getAttribute("class");
		assertTrue(studyTest.contains("errorInput"));
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：学習理解度：理解度が未入力")
	void test06() {
		// Selectクラス(プルダウン専用クラス）を使って理解度を未選択にする
		org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
				webDriver.findElement(By.xpath("//select[@id='intFieldValue_0']")));
		select.selectByIndex(0);
		webDriver.findElement(By.xpath("//input[@id='intFieldName_0']")).sendKeys("品質管理");
		//String understandLevel = webDriver.findElement(By.xpath("//select[@id='intFieldValue_0']")).getAttribute("value");
		//assertEquals("", understandLevel);
		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		visibilityTimeout(By.tagName("h2"), 5);
		String understandLevelTest = webDriver.findElement(By.xpath("//select[@id='intFieldValue_0']"))
				.getAttribute("class");
		assertTrue(understandLevelTest.contains("errorInput"));
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度：目標の達成度が数値以外")
	void test07() {
		webDriver.findElement(By.xpath("//select[@id='intFieldValue_0']")).sendKeys("3");
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("完璧");
		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		scrollBy("-500");
		visibilityTimeout(By.tagName("h2"), 5);
		String progressGoal = webDriver.findElement(By.id("content_0")).getAttribute("class");
		assertTrue(progressGoal.contains("errorInput"));
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度：目標の達成度が範囲（１～１０）以外")
	void test08() {
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("100");
		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		scrollBy("-500");
		visibilityTimeout(By.tagName("h2"), 5);
		String progressGoal = webDriver.findElement(By.id("content_0")).getAttribute("class");
		assertTrue(progressGoal.contains("errorInput"));
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		webDriver.findElement(By.id("content_0")).clear();
		webDriver.findElement(By.id("content_0")).sendKeys("");
		webDriver.findElement(By.id("content_1")).clear();
		webDriver.findElement(By.id("content_1")).sendKeys("");
		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		scrollBy("-500");
		visibilityTimeout(By.tagName("h2"), 5);
		String progressGoal = webDriver.findElement(By.id("content_0")).getAttribute("class");
		assertTrue(progressGoal.contains("errorInput"));
		String Thoughts = webDriver.findElement(By.id("content_1")).getAttribute("class");
		assertTrue(Thoughts.contains("errorInput"));
		scrollBy("250");
		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {

		webDriver.findElement(By.id("content_0")).sendKeys("3");

		String pokemon = "や～ん".repeat(2001);

		webDriver.findElement(By.id("content_1")).clear();
		webDriver.findElement(By.id("content_1")).sendKeys(pokemon);
		webDriver.findElement(By.id("content_2")).clear();
		webDriver.findElement(By.id("content_2")).sendKeys(pokemon);

		scrollBy("500");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		scrollBy("-500");
		visibilityTimeout(By.tagName("h2"), 5);
		String Thoughts = webDriver.findElement(By.id("content_1")).getAttribute("class");
		assertTrue(Thoughts.contains("errorInput"));
		String lookingBackString = webDriver.findElement(By.id("content_2")).getAttribute("class");
		assertTrue(lookingBackString.contains("errorInput"));
		//画面縮小
		scrollBy("500");
		((org.openqa.selenium.JavascriptExecutor) webDriver)
				.executeScript("document.body.style.zoom='70%'");
		getEvidence(new Object() {
		}, "");

	}

}
