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
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	private String login = "http://localhost:8080/lms/";

	private String detail = "http://localhost:8080/lms/course/detail";

	private String sectionDetail = "http://localhost:8080/lms/section/detail";

	private String reportRegister = "http://localhost:8080/lms/report/regist";

	private String userDetail = "http://localhost:8080/lms/user/detail";

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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		int detail = webDriver.findElements(By.cssSelector(".sctionList tr")).size();
		for (int i = 0; i < detail; i++) {
			String text = webDriver.findElements(By.cssSelector(".sctionList tr")).get(i).getText();

			if (text.contains("提出済み")) {
				webDriver.findElements(By.cssSelector(".sctionList tr")).get(i)
						.findElement(By.cssSelector("input[value = '詳細']")).click();
				break;
			}
		}
		visibilityTimeout(By.id("sectionDetail"), 5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertEquals(sectionDetail, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		webDriver.findElement(By.cssSelector("input[value *= 'を確認する']")).click();
		visibilityTimeout(By.tagName("Legend"), 5);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		assertEquals(reportRegister, webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		WebElement text = webDriver.findElement(By.cssSelector("textarea"));
		text.clear();
		text.sendKeys("本日の研修内容を修正しました");
		webDriver.findElement(By.cssSelector("button[type = 'submit']")).click();
		visibilityTimeout(By.id("sectionDetail"), 5);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertEquals("http://localhost:8080/lms/section/detail?sectionId=1", webDriver.getCurrentUrl());
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		WebElement user = webDriver.findElement(By.partialLinkText("ようこそ"));
		user.click();
		assertEquals("ユーザー詳細", webDriver.getTitle());
		assertEquals(userDetail, webDriver.getCurrentUrl());

		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		//テーブル内の行数取得
		int tableCount = webDriver.findElements(By.cssSelector("table.table-hover tr")).size();
		for (int i = 0; i < tableCount; i++) {

			String detailButton = webDriver.findElements(By.cssSelector("table.table-hover tr")).get(i).getText();
			if (detailButton.contains("2022年10月1日(土)")) {
				scrollBy("800");
				webDriver.findElements(By.cssSelector("table.table-hover tr")).get(i)
						.findElement(By.cssSelector("input[value = '詳細']")).click();
				break;
			}
		}
		visibilityTimeout(By.tagName("h2"), 5);
		WebElement report = webDriver.findElement(By.cssSelector("h3 + table.table-hover"));
		assertTrue(report.getText().contains("本日の研修内容を修正しました"));
		getEvidence(new Object() {
		}, "");
	}
}
