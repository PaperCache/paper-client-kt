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
			throw PaperError("Invalid Paper address.")
		}

		val parsed = paper_addr
			.removePrefix("paper://")
			.split(':')

		if (parsed.size != 2) {
			throw PaperError("Invalid Paper address.")
		}

		this.host = parsed[0]
		this.port = parsed[1].toInt()

		this.auth_token = null
		this.reconnect_attempts = 0

		try {
			this.tcp_client = Socket(this.host, this.port)
		} catch (err: Exception) {
			throw PaperError("Could not connect to PaperServer")
		}
	}

	fun disconnect() {
		try {
			this.tcp_client.close()
		} catch (err: Exception) {
			throw PaperError("Could not disconnect from PaperServer")
		}
	}

	fun ping(): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.PING.value)

		return this.process(writer)
	}

	fun version(): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.VERSION.value)

		return this.process(writer)
	}

	fun auth(token: String): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.AUTH.value)
		writer.write_string(token)

		return this.process(writer)
	}

	fun get(key: String): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.GET.value)
		writer.write_string(key)

		return this.process(writer)
	}

	fun set(key: String, value: String, ttl: Long = 0): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.SET.value)
		writer.write_string(key)
		writer.write_string(value)
		writer.write_u32(ttl)

		return this.process(writer)
	}

	fun del(key: String): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.DEL.value)
		writer.write_string(key)

		return this.process(writer)
	}

	fun has(key: String): PaperResposnse<Boolean> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.HAS.value)
		writer.write_string(key)

		return this.process_has(writer)
	}

	fun peek(key: String): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.PEEK.value)
		writer.write_string(key)

		return this.process(writer)
	}

	fun ttl(key: String, ttl: Long = 0): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.TTL.value)
		writer.write_string(key)
		writer.write_u32(ttl)

		return this.process(writer)
	}

	fun size(key: String): PaperResposnse<Long> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.SIZE.value)
		writer.write_string(key)

		return this.process_size(writer)
	}

	fun wipe(): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.WIPE.value)

		return this.process(writer)
	}

	fun resize(size: Long): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.RESIZE.value)
		writer.write_u64(size)

		return this.process(writer)
	}

	fun policy(policy: PaperPolicy): PaperResposnse<String> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.POLICY.value)
		writer.write_u8(policy.value)

		return this.process(writer)
	}

	fun stats(): PaperResposnse<PaperStats> {
		var writer: SheetWriter = SheetWriter()
		writer.write_u8(CommandByte.STATS.value)

		return this.process_stats(writer)
	}

	private fun reconnect(): Boolean {
		this.reconnect_attempts++

		if (this.reconnect_attempts > PaperClient.MAX_RECONNECT_ATTEMPTS) {
			return false
		}

		try {
			this.tcp_client = Socket(this.host, this.port)

			if (this.auth_token != null) {
				// TODO
			}

			return true
		} catch (err: Exception) {
			return false
		}
	}

	private fun process(writer: SheetWriter): PaperResposnse<String> {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()
			val data: String = reader.read_string()

			this.reconnect_attempts = 0

			return when (is_ok) {
				true -> PaperResposnse<String>(data)
				false -> PaperResposnse<String>(null, data)
			}
		} catch (err: Exception) {
			if (!this.reconnect()) {
				throw PaperError("Could not reconnect to server.")
			}

			return this.process(writer)
		}
	}

	private fun process_has(writer: SheetWriter): PaperResposnse<Boolean> {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()

			this.reconnect_attempts = 0

			return when (is_ok) {
				true -> PaperResposnse<Boolean>(reader.read_bool())
				false -> PaperResposnse<Boolean>(null, reader.read_string())
			}
		} catch (err: Exception) {
			if (!this.reconnect()) {
				throw PaperError("Could not reconnect to server.")
			}

			return this.process_has(writer)
		}
	}

	private fun process_size(writer: SheetWriter): PaperResposnse<Long> {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()

			this.reconnect_attempts = 0

			return when (is_ok) {
				true -> PaperResposnse<Long>(reader.read_u64())
				false -> PaperResposnse<Long>(null, reader.read_string())
			}
		} catch (err: Exception) {
			if (!this.reconnect()) {
				throw PaperError("Could not reconnect to server.")
			}

			return this.process_size(writer)
		}
	}

	private fun process_stats(writer: SheetWriter): PaperResposnse<PaperStats> {
		try {
			var output_stream: OutputStream = this.tcp_client.getOutputStream()
			writer.send(output_stream)

			val reader: SheetReader = SheetReader(this.tcp_client.getInputStream())

			val is_ok: Boolean = reader.read_bool()

			this.reconnect_attempts = 0

			if (!is_ok) {
				return PaperResposnse<PaperStats>(null, reader.read_string())
			}

			val max_size = reader.read_u64()
			val used_size = reader.read_u64()

			val total_gets = reader.read_u64()
			val total_sets = reader.read_u64()
			val total_dels = reader.read_u64()

			val miss_ratio = reader.read_f64()

			val policy_byte = reader.read_u8()
			val uptime = reader.read_u64()

			val stats: PaperStats = PaperStats(
				max_size,
				used_size,

				total_gets,
				total_sets,
				total_dels,

				miss_ratio,

				policy_byte,
				uptime,
			)

			return PaperResposnse<PaperStats>(stats)
		} catch (err: Exception) {
			if (!this.reconnect()) {
				throw PaperError("Could not reconnect to server.")
			}

			return this.process_stats(writer)
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

	STATS(13),
}
