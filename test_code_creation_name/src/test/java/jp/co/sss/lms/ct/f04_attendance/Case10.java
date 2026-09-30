package jp.co.sss.lms.ct.f04_attendance;

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
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

	private String login = "http://localhost:8080/lms/";

	private String detail = "http://localhost:8080/lms/course/detail";

	private String attendanceDetail = "http://localhost:8080/lms/attendance/detail";

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
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		//header"勤怠"URL押下
		webDriver.findElement(By.linkText("勤怠")).click();
		//JavaScriptポップアップにて、OKボタン押下
		webDriver.switchTo().alert().accept();
		//タイトル一致テスト
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		//URL一致テスト
		assertEquals(attendanceDetail, webDriver.getCurrentUrl());
		//エビデンス取得
		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		//出勤ボタン押下
		webDriver.findElement(By.cssSelector("input[value = '出勤']")).click();
		//JavaScriptポップアップにて、OKボタン押下
		webDriver.switchTo().alert().accept();
		//出勤要素取得
		WebElement startTime = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(3)"));
		//出勤入力テスト
		assertTrue(startTime.isDisplayed());
		//5秒待機
		visibilityTimeout(By.tagName("h2"), 5);
		//画面スクロール
		scrollBy("200");
		//エビデンス取得
		getEvidence(new Object() {
		}, "");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		//画面スクロール
		scrollBy("-200");
		//退勤ボタン押下
		webDriver.findElement(By.cssSelector("input[value = '退勤']")).click();
		//JavaScriptポップアップにて、OKボタン押下
		webDriver.switchTo().alert().accept();
		//退勤要素取得
		WebElement EndTime = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(4)"));
		//退勤入力テスト
		assertTrue(EndTime.isDisplayed());
		//5秒待機
		visibilityTimeout(By.tagName("h2"), 5);
		//画面スクロール
		scrollBy("200");
		//エビデンス取得
		getEvidence(new Object() {
		}, "");
	}

}
