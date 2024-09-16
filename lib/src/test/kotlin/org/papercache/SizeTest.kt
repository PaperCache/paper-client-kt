import kotlin.test.Test
import kotlin.test.*

class SizeTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

		val response = client.size("key")

		assertFalse(response.is_ok())
		assertNull(response.data())
		assertNotNull(response.err_data())
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		assertTrue(client.set("key", "value").is_ok())
		val response = client.size("key")

		assertTrue(response.is_ok())
		assertEquals(5, response.data())
		assertNull(response.err_data())
    }
}
