import kotlin.test.Test
import kotlin.test.*

class PaperPoolTest {
    @Test fun pool() {
		val pool = PaperPool("paper://127.0.0.1:3145", 2)

		for (i in 0..10) {
			val lockable_client = pool.client()
			val client = lockable_client.lock()

			val response = client.ping()

			assertTrue(response.is_ok())
			assertEquals("pong", response.data())
			assertNull(response.err_data())

			lockable_client.unlock()
		}
    }

	@Test fun auth() {
		val pool = PaperPool("paper://127.0.0.1:3145", 2)

		assertFalse(this.can_set(pool))
		pool.auth("auth_token")
		assertTrue(this.can_set(pool))
	}

	private fun can_set(pool: PaperPool): Boolean {
		val lockable_client: LockableClient = pool.client()
		val client = lockable_client.lock()

		val response = client.set("key", "value")

		lockable_client.unlock()

		return response.is_ok()
	}
}
