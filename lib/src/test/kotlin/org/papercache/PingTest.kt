import kotlin.test.Test
import kotlin.test.*

class PingTest: PaperClientTest() {
    @Test fun ping() {
		val client = this.client
		if (client == null) fail()

		val response = client.ping()

		assertTrue(response.is_ok())
		assertEquals("pong", response.data())
		assertNull(response.err_data())
    }
}
