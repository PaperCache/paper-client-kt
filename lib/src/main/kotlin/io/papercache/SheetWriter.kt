/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import java.io.*
import java.net.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SheetWriter() {
	private var buf: ArrayList<Byte> = ArrayList()

	fun write_u8(value: Byte) {
		this.buf.add(value)
	}

	fun write_u32(value: UInt) {
		var bytes: ByteArray = ByteArray(4)

		var buf: ByteBuffer = ByteBuffer.wrap(bytes)
		buf.order(ByteOrder.LITTLE_ENDIAN)
		buf.putInt(value.toInt())

		for (b in bytes) {
			this.buf.add(b)
		}
	}

	fun write_u64(value: ULong) {
		var bytes: ByteArray = ByteArray(8)

		var buf: ByteBuffer = ByteBuffer.wrap(bytes)
		buf.order(ByteOrder.LITTLE_ENDIAN)
		buf.putLong(value.toLong())

		for (b in bytes) {
			this.buf.add(b)
		}
	}

	fun write_string(value: String) {
		this.write_u32(value.length.toUInt())

		for (b in value.toByteArray()) {
			this.buf.add(b)
		}
	}

	fun send(tcp_stream: OutputStream) {
		tcp_stream.write(this.buf.toByteArray())
	}
}
