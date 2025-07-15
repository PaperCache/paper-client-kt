import kotlin.test.Test
import kotlin.test.*

class SetTest: PaperClientTest() {
    @Test fun no_ttl() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")
    }

    @Test fun ttl() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value", 2U)
    }

	@Test fun expiry() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value", 1U)

		val got = client.get("key")
		assertEquals("value", got)

		Thread.sleep(2000);

		val err = assertFailsWith<PaperError> {
			client.get("key")
		}

		assertEquals(err.type, PaperError.Type.KEY_NOT_FOUND)
    }
}
