package io.hhplus.concert.domain.user;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import io.hhplus.concert.TestcontainersConfiguration;
import io.hhplus.concert.domain.concert.Concert;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Sql(statements = {
	"SET FOREIGN_KEY_CHECKS=0",
	"TRUNCATE TABLE user_points",
	"TRUNCATE TABLE user_point_histories",
	"TRUNCATE TABLE users",
	"SET FOREIGN_KEY_CHECKS=1"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPointConcurrencyIntegrationTest {
	private static final Logger log = LoggerFactory.getLogger(UserPointConcurrencyIntegrationTest.class);
	@Autowired private UserService userService;
	@Autowired private UserRepository userRepository;
	@Autowired private UserPointRepository userPointRepository;
	@Autowired private UserPointHistoryRepository userPointHistoryRepository;

	User sampleUser;
	UserPoint sampleUserPoint;
	@BeforeEach
	void setUp() {
		// truncate -> serUp -> 테스트케이스 수행 순으로 진행
		// 유저 테스트 데이터 셋팅
		sampleUser = User.of("이동우");
		userRepository.save(sampleUser);

		sampleUserPoint = UserPoint.of(sampleUser); // 초기포인트 0 포인트
		sampleUserPoint.charge(3000L);
		userPointRepository.save(sampleUserPoint);
	}

	@Test
	void 동시에_여러요청이_들어와도_포인트는_한번만_충전된다() throws InterruptedException {
		long userId = sampleUser.getId();

		int threadCount = 15;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					userService.chargePoint(new UserPointCommand.ChargePoint(userId, 1000L));
				} catch (Exception e) {
					// 중복 충전 시 예외 발생 가능 (락 획득 실패 등)
					System.out.println("예외 발생: " + e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		UserPoint result = userPointRepository.findByUserId(userId);
		assertNotNull(result);
		assertEquals(4000L, result.getPoint()); // 단 1번만 충전되었는지 확인
	}

	@Test
	void 동시에_포인트_사용요청이_들어와도_한번만_사용된다() throws InterruptedException {
		long userId = sampleUser.getId();

		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					userService.usePoint(new UserPointCommand.UsePoint(userId, 1000L));
					return true;
				} catch (Exception e) {
					System.out.println("예외 발생: " + e.getMessage());
					return false;
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();


		UserPoint result = userPointRepository.findByUserId(userId);
		assertNotNull(result);
		assertEquals(2000L, result.getPoint());
	}
}
