/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.Test
import kotlin.test.*

class SizeTest: PaperClientTest() {
    @Test fun non_existent() {
		val client = this.client
		if (client == null) fail()

        val err = assertFailsWith<PaperError> {
            client.size("key")
        }

        assertEquals(err.type, PaperError.Type.KEY_NOT_FOUND)
    }

    @Test fun existent() {
		val client = this.client
		if (client == null) fail()

		client.set("key", "value")

		val size = client.size("key")
		assertTrue(size > 0U)
    }
}
