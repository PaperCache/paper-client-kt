import kotlin.test.Test
import kotlin.test.*

class ResizeTest: PaperClientTest() {
    @Test fun resize() {
		val client = this.client
		if (client == null) fail()

		val initial_size: Long = 40 * 1024 * 1024
		val updated_size: Long = 20 * 1024 * 1024

		val initial_response = client.resize(initial_size)
		assertTrue(initial_response.is_ok())
		assertNotNull(initial_response.data())
		assertNull(initial_response.err_data())

		assertEquals(initial_size, this.get_cache_size())

		val updated_response = client.resize(updated_size)
		assertTrue(updated_response.is_ok())
		assertNotNull(updated_response.data())
		assertNull(updated_response.err_data())

		assertEquals(updated_size, this.get_cache_size())
    }

	private fun get_cache_size(): Long {
		val client = this.client
		if (client == null) fail()

		val response = client.stats()
		val stats = response.data()
		if (stats == null) fail(response.err_data())

		return stats.max_size
	}
}
