import kotlin.test.Test
import kotlin.test.*

class HasTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

		val response = client.has("key")
		val data = response.data()

		assertTrue(response.is_ok())
		assertFalse(data != null && data)
		assertNull(response.err_data())
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		assertTrue(client.set("key", "value").is_ok())
		val response = client.has("key")
		val data = response.data()

		assertTrue(response.is_ok())
		assertTrue(data != null && data)
		assertNull(response.err_data())
    }
}
