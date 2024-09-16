import kotlin.test.Test
import kotlin.test.*

class GetTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

		val response = client.get("key")

		assertFalse(response.is_ok())
		assertNull(response.data())
		assertNotNull(response.err_data())
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		assertTrue(client.set("key", "value").is_ok())
		val response = client.get("key")

		assertTrue(response.is_ok())
		assertEquals("value", response.data())
		assertNull(response.err_data())
    }
}
