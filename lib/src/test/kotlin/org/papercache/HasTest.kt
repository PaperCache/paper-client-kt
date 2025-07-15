import kotlin.test.Test
import kotlin.test.*

class HasTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

		val has = client.has("key")
		assertFalse(has)
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")

		val has = client.has("key")
		assertTrue(has)
    }
}
