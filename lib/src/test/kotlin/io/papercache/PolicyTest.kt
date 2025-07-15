/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import kotlin.test.Test
import kotlin.test.*

class PolicyTest: PaperClientTest() {
    @Test fun policy() {
		val client = this.client
		if (client == null) fail()

		val initial_policy = "lru";
		val updated_policy = "lfu";

		client.policy(initial_policy)
		assertEquals(initial_policy, this.get_cache_policy())

		client.policy(updated_policy)
		assertEquals(updated_policy, this.get_cache_policy())
    }

	private fun get_cache_policy(): String {
		val client = this.client
		if (client == null) fail()

		val status = client.status()
		return status.policy
	}
}
