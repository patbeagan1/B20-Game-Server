package services.yaml

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class XMLRepository {

    val mapper = XmlMapper.xmlBuilder()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .defaultUseWrapper(false)
        .build()
        .registerKotlinModule()

}