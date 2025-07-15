/*
 * Copyright (c) Kia Shakiba
 *
 * This source code is licensed under the GNU AGPLv3 license found in the
 * LICENSE file in the root directory of this source tree.
 */

class PaperError(val type: Type) : Exception() {
	enum class Type {
		INTERNAL,

		INVALID_ADDRESS,

		CONNECTION_REFUSED,
		MAX_CONNECTIONS_EXCEEDED,
		UNAUTHORIZED,
		DISCONNECTED,

		KEY_NOT_FOUND,

		ZERO_VALUE_SIZE,
		EXCEEDING_VALUE_SIZE,

		ZERO_CACHE_SIZE,

		UNCONFIGURED_POLICY,
		INVALID_POLICY,
	}

	companion object {
		fun from_reader(reader: SheetReader): PaperError {
			try {
				val code = reader.read_u8()

				if (code == 0.toByte()) {
					val cache_code = reader.read_u8()
					return PaperError.from_cache_code(cache_code)
				}

				return PaperError.from_code(code)
			} catch (err: Exception) {
				return PaperError(Type.INTERNAL)
			}
		}

		fun from_code(code: Byte): PaperError {
			when (code) {
				2.toByte() -> return PaperError(Type.MAX_CONNECTIONS_EXCEEDED)
				3.toByte() -> return PaperError(Type.UNAUTHORIZED)
				else -> return PaperError(Type.INTERNAL)
			}
		}

		fun from_cache_code(code: Byte): PaperError {
			when (code) {
				1.toByte() -> return PaperError(Type.KEY_NOT_FOUND)

				2.toByte() -> return PaperError(Type.ZERO_VALUE_SIZE)
				3.toByte() -> return PaperError(Type.EXCEEDING_VALUE_SIZE)

				4.toByte() -> return PaperError(Type.ZERO_CACHE_SIZE)

				5.toByte() -> return PaperError(Type.UNCONFIGURED_POLICY)
				6.toByte() -> return PaperError(Type.INVALID_POLICY)

				else -> return PaperError(Type.INTERNAL)
			}
		}
	}
}
