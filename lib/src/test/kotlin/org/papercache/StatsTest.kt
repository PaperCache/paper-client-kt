import kotlin.test.Test
import kotlin.test.*

class StatsTest: PaperClientTest() {
    @Test fun stats() {
		val client = this.client
		if (client == null) fail()

		val response = client.stats()

		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())
    }
}
