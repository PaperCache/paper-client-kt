import kotlin.test.Test
import kotlin.test.*

class ResizeTest: PaperClientTest() {
    @Test fun resize() {
		val client = this.client
		if (client == null) fail()

		val initial_size: ULong = 40UL * 1024UL * 1024UL;
		val updated_size: ULong = 20UL * 1024UL * 1024UL;

		client.resize(initial_size)
		assertEquals(initial_size, this.get_cache_size())

		client.resize(updated_size)
		assertEquals(updated_size, this.get_cache_size())
    }

	private fun get_cache_size(): ULong {
		val client = this.client
		if (client == null) fail()

		val status = client.status()
		return status.max_size
	}
}
