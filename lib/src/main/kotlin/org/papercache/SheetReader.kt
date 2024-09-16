import java.io.*
import java.net.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class SheetReader(tcp_stream: InputStream) {
	private var tcp_stream: InputStream = tcp_stream

	fun read_u8(): Byte {
		var bytes: ByteArray = ByteArray(1)
		this.tcp_stream.read(bytes)
		return bytes[0]
	}

	fun read_bool(): Boolean {
		return this.read_u8() == 33.toByte()
	}

	fun read_u32(): Long {
		var bytes: ByteArray = ByteArray(4)
		this.tcp_stream.read(bytes)

		var buf: ByteBuffer = ByteBuffer.wrap(bytes)
		buf.order(ByteOrder.LITTLE_ENDIAN)
		return buf.getInt().toLong()
	}

	fun read_u64(): Long {
		var bytes: ByteArray = ByteArray(8)
		this.tcp_stream.read(bytes)

		var buf: ByteBuffer = ByteBuffer.wrap(bytes)
		buf.order(ByteOrder.LITTLE_ENDIAN)
		return buf.getInt().toLong()
	}

	fun read_f64(): Double {
		var bytes: ByteArray = ByteArray(8)
		this.tcp_stream.read(bytes)

		var buf: ByteBuffer = ByteBuffer.wrap(bytes)
		buf.order(ByteOrder.LITTLE_ENDIAN)
		return buf.getDouble()
	}

	fun read_string(): String {
		val size: Long = this.read_u32()
		var buf: ByteArray = ByteArray(size.toInt())

		this.tcp_stream.read(buf)

		return String(buf, StandardCharsets.UTF_8)
	}
}
