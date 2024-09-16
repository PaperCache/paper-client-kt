import kotlin.test.Test
import kotlin.test.*

class TtlTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

		val response = client.ttl("key", 1)

		assertFalse(response.is_ok())
		assertNull(response.data())
		assertNotNull(response.err_data())
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		assertTrue(client.set("key", "value").is_ok())
		val response = client.ttl("key", 1)

		assertTrue(response.is_ok())
		assertNotNull(response.data())
		assertNull(response.err_data())
    }
}
