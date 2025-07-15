/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.Test
import kotlin.test.*

class AuthTest: PaperClientTest(false) {
    @Test fun incorrect() {
		val client = this.client
		if (client == null) fail()

		val set_err = assertFailsWith<PaperError> {
			client.set("key", "value")
		}

		assertEquals(set_err.type, PaperError.Type.UNAUTHORIZED)

		val auth_err = assertFailsWith<PaperError> {
			client.auth("incorrect_auth_token")
		}

		assertEquals(auth_err.type, PaperError.Type.UNAUTHORIZED)
    }

	@Test fun correct() {
		val client = this.client
		if (client == null) fail()

		val set_err = assertFailsWith<PaperError> {
			client.set("key", "value")
		}

		assertEquals(set_err.type, PaperError.Type.UNAUTHORIZED)

		client.auth("auth_token")
		client.set("key", "value")
    }
}
