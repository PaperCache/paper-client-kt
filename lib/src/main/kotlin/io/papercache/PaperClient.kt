/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

import java.io.*
import java.net.*

class PaperClient(paper_addr: String) {
	companion object {
		private const val MAX_RECONNECT_ATTEMPTS: Short = 3
	}

	private val host: String
	private val port: Int

	private val auth_token: String?
	private var reconnect_attempts: Short

	private var tcp_client: Socket

	init {
		if (!paper_addr.startsWith("paper://")) {
			throw PaperError(PaperError.Type.INVALID_ADDRESS)
		}

		val parsed = paper_addr
			.removePrefix("paper://")
			.split(':')

		if (parsed.size != 2) {
			throw PaperError(PaperError.Type.INVALID_ADDRESS)
		}

		this.host = parsed[0]
		this.port = parsed[1].toInt()

		this.auth_token = null
		this.reconnect_attempts = 0

		try {
			this.tcp_client = Socket(this.host, this.port)
			this.handshake();
		} catch (err: Exception) {
			throw PaperError(PaperError.Type.CONNECTION_REFUSED)
		}
	}

	fun disconnect() {
		try {
			this.tcp_client.close()
		} catch (err: Exception) {
			throw PaperError(PaperError.Type.INTERNAL)
		}
	}

	fun ping(): String {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.PING.value)

		return this.process_data(writer)
	}

	fun version(): String {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.VERSION.value)

		return this.process_data(writer)
	}

	fun auth(token: String) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.AUTH.value)
		writer.write_string(token)

		return this.process(writer)
	}

	fun get(key: String): String {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.GET.value)
		writer.write_string(key)

		return this.process_data(writer)
	}

	fun set(key: String, value: String, ttl: UInt = 0U) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.SET.value)
		writer.write_string(key)
		writer.write_string(value)
		writer.write_u32(ttl)

		return this.process(writer)
	}

	fun del(key: String) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.DEL.value)
		writer.write_string(key)

		return this.process(writer)
	}

	fun has(key: String): Boolean {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.HAS.value)
		writer.write_string(key)

		return this.process_has(writer)
	}

	fun peek(key: String): String {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.PEEK.value)
		writer.write_string(key)

		return this.process_data(writer)
	}

	fun ttl(key: String, ttl: UInt = 0U) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.TTL.value)
		writer.write_string(key)
		writer.write_u32(ttl)

		return this.process(writer)
	}

	fun size(key: String): UInt {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.SIZE.value)
		writer.write_string(key)

		return this.process_size(writer)
	}

	fun wipe() {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.WIPE.value)

		return this.process(writer)
	}

	fun resize(size: ULong) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.RESIZE.value)
		writer.write_u64(size)

		return this.process(writer)
	}

	fun policy(policy: String) {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.POLICY.value)
		writer.write_string(policy)

		return this.process(writer)
	}

	fun status(): PaperStatus {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.STATUS.value)

		return this.process_status(writer)
	}

	private fun handshake(): Boolean {
		try {
			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())
			return reader.read_bool()
		} catch (err: Exception) {
			throw PaperError(PaperError.Type.CONNECTION_REFUSED)
		}
	}

	private fun reconnect(): Boolean {
		this.reconnect_attempts++

		if (this.reconnect_attempts > PaperClient.MAX_RECONNECT_ATTEMPTS) {
			return false
		}

		try {
			this.tcp_client = Socket(this.host, this.port)

			if (this.auth_token != null) {
				this.auth(this.auth_token);
			}

			return true
		} catch (err: Exception) {
			return false
		}
	}

	private fun process(writer: SheetWriter) {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			if (!is_ok) throw PaperError.from_reader(reader)

			this.reconnect_attempts = 0
		} catch (err: IOException) {
			if (!this.reconnect()) {
				throw PaperError(PaperError.Type.DISCONNECTED)
			}

			this.process(writer)
		}
	}

	private fun process_data(writer: SheetWriter): String {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			if (!is_ok) throw PaperError.from_reader(reader)

			val data: String = reader.read_string()
			this.reconnect_attempts = 0

			return data
		} catch (err: IOException) {
			if (!this.reconnect()) {
				throw PaperError(PaperError.Type.DISCONNECTED)
			}

			return this.process_data(writer)
		}
	}

	private fun process_has(writer: SheetWriter): Boolean {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			if (!is_ok) throw PaperError.from_reader(reader)

			val has = reader.read_bool()
			this.reconnect_attempts = 0

			return has
		} catch (err: IOException) {
			if (!this.reconnect()) {
				throw PaperError(PaperError.Type.DISCONNECTED)
			}

			return this.process_has(writer)
		}
	}

	private fun process_size(writer: SheetWriter): UInt {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			if (!is_ok) throw PaperError.from_reader(reader)

			val size = reader.read_u32()
			this.reconnect_attempts = 0

			return size
		} catch (err: IOException) {
			if (!this.reconnect()) {
				throw PaperError(PaperError.Type.DISCONNECTED)
			}

			return this.process_size(writer)
		}
	}

	private fun process_status(writer: SheetWriter): PaperStatus {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			if (!is_ok) throw PaperError.from_reader(reader)

			this.reconnect_attempts = 0

			val pid = reader.read_u32()

			val max_size = reader.read_u64()
			val used_size = reader.read_u64()
			val num_objects = reader.read_u64()

			val rss = reader.read_u64()
			val hwm = reader.read_u64()

			val total_gets = reader.read_u64()
			val total_sets = reader.read_u64()
			val total_dels = reader.read_u64()

			val miss_ratio = reader.read_f64()

			val num_policies = reader.read_u32().toInt()
			var policies: Array<String> = Array(num_policies) { "" }

			for (i in 0..num_policies - 1) {
				policies[i] = reader.read_string()
			}

			val policy = reader.read_string()
			val is_auto_policy = reader.read_bool()

			val uptime = reader.read_u64()

			val status: PaperStatus = PaperStatus(
				pid,
				
				max_size,
				used_size,
				num_objects,

				rss,
				hwm,

				total_gets,
				total_sets,
				total_dels,

				miss_ratio,

				policies,
				policy,
				is_auto_policy,
				
				uptime,
			)

			return status
		} catch (err: IOException) {
			if (!this.reconnect()) {
				throw PaperError(PaperError.Type.DISCONNECTED)
			}

			return this.process_status(writer)
		}
	}
}

enum class CommandByte(val value: Byte) {
	PING(0),
	VERSION(1),

	AUTH(2),

	GET(3),
	SET(4),
	DEL(5),

	HAS(6),
	PEEK(7),
	TTL(8),
	SIZE(9),

	WIPE(10),

	RESIZE(11),
	POLICY(12),

	STATUS(13),
}
