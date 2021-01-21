package com.marzec.cheatday.common

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.marzec.cheatday.model.domain.CurrentUserProto
import java.io.InputStream
import java.io.OutputStream

object CurrentUserProtoSerializer : Serializer<CurrentUserProto> {
    override val defaultValue: CurrentUserProto = CurrentUserProto.getDefaultInstance()

    override fun readFrom(input: InputStream): CurrentUserProto {
        try {
            return CurrentUserProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override fun writeTo(
        t: CurrentUserProto,
        output: OutputStream
    ) = t.writeTo(output)
}
