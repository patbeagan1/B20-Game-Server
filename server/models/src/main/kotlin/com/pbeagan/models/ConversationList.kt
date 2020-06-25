package com.pbeagan.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "discussion")
data class ConversationList(
    @JacksonXmlElementWrapper(useWrapping = false)
    val conversation: List<Conversation>
)

data class Conversation constructor(
    @JacksonXmlProperty(isAttribute = true) val id: String,
    @JacksonXmlProperty(isAttribute = true) val next: String?,
    val firstsay: String?,
    val say: String?,
    @JacksonXmlElementWrapper(useWrapping = false) val option: List<Option>?
) {
    data class Option(
        @JacksonXmlProperty(isAttribute = true) val id: String,
        @JacksonXmlProperty(isAttribute = true) val next: String?,
        val say: String?
    )
}