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
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

	private String login = "http://localhost:8080/lms/";

	private String detail = "http://localhost:8080/lms/course/detail";

	private String attendanceDetail = "http://localhost:8080/lms/attendance/detail";

	private String attendanceUpdate = "http://localhost:8080/lms/attendance/update";

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		//"勤怠情報を直接編集する"URL押下
		webDriver.findElement(By.linkText("勤怠情報を直接編集する")).click();
		//タイトル一致テスト
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		//URL一致テスト
		assertEquals(attendanceUpdate, webDriver.getCurrentUrl());
		//エビデンス取得
		getEvidence(new Object() {
		}, "");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		//出勤（時）クリック
		webDriver.findElement(By.id("startHour1")).click();
		//出勤（時）入力
		webDriver.findElement(By.cssSelector("#startHour1 option[value='9']")).click();

		//出勤（分）クリック
		webDriver.findElement(By.id("startMinute1")).click();
		//出勤（分）入力
		webDriver.findElement(By.cssSelector("#startMinute1 option[value='0']")).click();

		//退勤（時）クリック
		webDriver.findElement(By.id("endHour1")).click();
		//退勤（時）入力
		webDriver.findElement(By.cssSelector("#endHour1 option[value='18']")).click();

		//退勤（分）クリック
		webDriver.findElement(By.id("endMinute1")).click();
		//退勤（分）入力
		webDriver.findElement(By.cssSelector("#endMinute1 option[value='0']")).click();

		//中抜け時間クリック
		webDriver.findElement(By.cssSelector("select[name = 'attendanceList[1].blankTime']")).click();
		//中抜け時間入力
		webDriver.findElement(By.cssSelector("select[name = 'attendanceList[1].blankTime'] option[value ='60']"))
				.click();
		//備考クリア
		webDriver.findElement(By.cssSelector("Input[name = 'attendanceList[1].note']")).clear();
		//備考入力
		webDriver.findElement(By.cssSelector("Input[name = 'attendanceList[1].note']")).sendKeys("備考");
		//画面スクロール
		scrollBy("500");
		//更新ボタンクリック
		webDriver.findElement(By.cssSelector("Input[name = 'complete']")).click();
		//JavaScriptポップアップにて、OKボタン押下
		webDriver.switchTo().alert().accept();
		//タイトル一致テスト
		assertEquals("勤怠情報変更｜LMS", webDriver.getTitle());
		//出勤要素取得
		WebElement startTime = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(3)"));
		//出勤入力テスト
		assertTrue(startTime.isDisplayed());
		//退勤要素取得
		WebElement EndTime = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(4)"));
		//退勤入力テスト
		assertTrue(EndTime.isDisplayed());
		//中抜け時間要素取得
		WebElement blankTimeValue = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(5)"));
		//中抜け時間テスト
		assertTrue(blankTimeValue.isDisplayed());
		//中抜け時間要素取得
		WebElement note = webDriver.findElement(By.cssSelector("tbody tr.info td:nth-of-type(7)"));
		//中抜け時間テスト
		assertTrue(note.isDisplayed());
		//画面スクロール
		scrollBy("500");
		//エビデンス取得
		getEvidence(new Object() {
		}, "");
	}

}
