import kotlin.test.Test
import kotlin.test.*

class GetTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

        val err = assertFailsWith<PaperError> {
            client.get("key")
        }

        assertEquals(err.type, PaperError.Type.KEY_NOT_FOUND)
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")
		val got = client.get("key")

		assertEquals("value", got)
    }
}
