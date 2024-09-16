import kotlin.test.Test
import kotlin.test.*

class PolicyTest: PaperClientTest() {
    @Test fun policy() {
		val client = this.client
		if (client == null) fail()

		val initial_policy = PaperPolicy.LFU
		val updated_policy = PaperPolicy.FIFO

		val initial_response = client.policy(initial_policy)
		assertTrue(initial_response.is_ok())
		assertNotNull(initial_response.data())
		assertNull(initial_response.err_data())

		assertEquals(initial_policy, this.get_cache_policy())

		val updated_response = client.policy(updated_policy)
		assertTrue(updated_response.is_ok())
		assertNotNull(updated_response.data())
		assertNull(updated_response.err_data())

		assertEquals(updated_policy, this.get_cache_policy())
    }

	private fun get_cache_policy(): PaperPolicy {
		val client = this.client
		if (client == null) fail()

		val response = client.stats()
		val stats = response.data()
		if (stats == null) fail(response.err_data())

		return stats.policy
	}
}
