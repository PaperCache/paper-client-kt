/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.*

open class PaperClientTest(authed: Boolean = true) {
	protected val authed: Boolean = authed
	protected var client: PaperClient? = null

	@BeforeTest fun init() {
		val client = PaperClient("paper://127.0.0.1:3145")

		if (this.authed) {
			client.auth("auth_token")
			client.wipe()
		}

		this.client = client
	}

	@AfterTest fun tearDown() {
		val client = this.client
		if (client == null) fail()

		client.disconnect()
	}
}
