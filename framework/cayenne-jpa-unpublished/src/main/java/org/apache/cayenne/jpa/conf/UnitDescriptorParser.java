begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParserFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|JpaPersistenceProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|JpaUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
operator|.
name|JpaUnitFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * A parser of persistence descriptor files. Can be used in serial processing of multiple  * documents.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|UnitDescriptorParser
block|{
specifier|static
specifier|final
name|String
name|PERSISTENCE_SCHEMA
init|=
literal|"META-INF/schemas/persistence_1_0.xsd"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PERSISTENCE
init|=
literal|"persistence"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PERSISTENCE_UNIT
init|=
literal|"persistence-unit"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"name"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROVIDER
init|=
literal|"provider"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TRANSACTION_TYPE
init|=
literal|"transaction-type"
decl_stmt|;
specifier|static
specifier|final
name|String
name|JTA_DATASOURCE
init|=
literal|"jta-data-source"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NON_JTA_DATASOURCE
init|=
literal|"non-jta-data-source"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MAPPING_FILE
init|=
literal|"mapping-file"
decl_stmt|;
specifier|static
specifier|final
name|String
name|JAR_FILE
init|=
literal|"jar-file"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CLASS
init|=
literal|"class"
decl_stmt|;
specifier|static
specifier|final
name|String
name|EXCLUDE_UNLISTED_CLASSES
init|=
literal|"exclude-unlisted-classes"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROPERTIES
init|=
literal|"properties"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROPERTY
init|=
literal|"property"
decl_stmt|;
specifier|static
specifier|final
name|String
name|VALUE
init|=
literal|"value"
decl_stmt|;
specifier|protected
name|SAXParserFactory
name|parserFactory
decl_stmt|;
specifier|protected
name|JpaUnitFactory
name|unitFactory
decl_stmt|;
specifier|public
name|UnitDescriptorParser
parameter_list|()
throws|throws
name|SAXException
throws|,
name|ParserConfigurationException
block|{
name|this
argument_list|(
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|UnitDescriptorParser
parameter_list|(
name|JpaUnitFactory
name|unitFactory
parameter_list|,
name|boolean
name|validatesAgainstSchema
parameter_list|)
throws|throws
name|SAXException
block|{
name|this
operator|.
name|unitFactory
operator|=
name|unitFactory
expr_stmt|;
name|parserFactory
operator|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|parserFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// note that validation requires that persistence.xml declares a schema like this:
comment|//<persistence xmlns="http://java.sun.com/xml/ns/persistence"
comment|// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
comment|// xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
comment|// http://java.sun.com/xml/ns/persistence/persistence_1_0.xsd">
if|if
condition|(
name|validatesAgainstSchema
condition|)
block|{
name|URL
name|schemaURL
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|PERSISTENCE_SCHEMA
argument_list|)
decl_stmt|;
name|SchemaFactory
name|factory
init|=
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
decl_stmt|;
name|StreamSource
name|ss
init|=
operator|new
name|StreamSource
argument_list|(
name|schemaURL
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
name|Schema
name|schema
init|=
name|factory
operator|.
name|newSchema
argument_list|(
name|ss
argument_list|)
decl_stmt|;
name|parserFactory
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Loads and returns a Collection of PersistenceUnitInfos from the XML descriptor.      */
specifier|public
name|Collection
argument_list|<
name|JpaUnit
argument_list|>
name|getPersistenceUnits
parameter_list|(
name|InputSource
name|in
parameter_list|,
specifier|final
name|URL
name|persistenceUnitRootUrl
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
specifier|final
name|Collection
argument_list|<
name|JpaUnit
argument_list|>
name|unitInfos
init|=
operator|new
name|ArrayList
argument_list|<
name|JpaUnit
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// note that parser is not reused - some parser implementations blow on
comment|// parser.reset() call
name|SAXParser
name|parser
init|=
name|parserFactory
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|in
argument_list|,
operator|new
name|DefaultHandler
argument_list|()
block|{
name|JpaUnit
name|unit
decl_stmt|;
name|Properties
name|properties
decl_stmt|;
name|StringBuilder
name|charBuffer
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|error
parameter_list|(
name|SAXParseException
name|e
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
name|e
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|PERSISTENCE_UNIT
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
name|NAME
argument_list|)
decl_stmt|;
name|String
name|transactionType
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
name|TRANSACTION_TYPE
argument_list|)
decl_stmt|;
name|unit
operator|=
name|unitFactory
operator|!=
literal|null
condition|?
name|unitFactory
operator|.
name|newUnit
argument_list|()
else|:
operator|new
name|JpaUnit
argument_list|()
expr_stmt|;
name|unit
operator|.
name|setPersistenceUnitName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|unit
operator|.
name|setPersistenceUnitRootUrl
argument_list|(
name|persistenceUnitRootUrl
argument_list|)
expr_stmt|;
if|if
condition|(
name|transactionType
operator|!=
literal|null
condition|)
block|{
name|unit
operator|.
name|putProperty
argument_list|(
name|JpaPersistenceProvider
operator|.
name|TRANSACTION_TYPE_PROPERTY
argument_list|,
name|transactionType
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|PROPERTIES
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|PROPERTY
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
name|NAME
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
name|VALUE
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|EXCLUDE_UNLISTED_CLASSES
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|setExcludeUnlistedClasses
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|qName
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|PERSISTENCE_UNIT
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unitInfos
operator|.
name|add
argument_list|(
name|unit
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|PROPERTIES
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|addProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// process string values
name|String
name|string
init|=
name|resetCharBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|string
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|CLASS
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|addManagedClassName
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|PROVIDER
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|putProperty
argument_list|(
name|JpaPersistenceProvider
operator|.
name|PROVIDER_PROPERTY
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|JAR_FILE
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|addJarFileUrl
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|MAPPING_FILE
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|addMappingFileName
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|JTA_DATASOURCE
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|putProperty
argument_list|(
name|JpaPersistenceProvider
operator|.
name|JTA_DATA_SOURCE_PROPERTY
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|NON_JTA_DATASOURCE
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|putProperty
argument_list|(
name|JpaPersistenceProvider
operator|.
name|NON_JTA_DATA_SOURCE_PROPERTY
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|DESCRIPTION
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|unit
operator|.
name|setDescription
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|characters
parameter_list|(
name|char
index|[]
name|ch
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
if|if
condition|(
name|charBuffer
operator|==
literal|null
condition|)
block|{
name|charBuffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
name|charBuffer
operator|.
name|append
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
name|String
name|resetCharBuffer
parameter_list|()
block|{
if|if
condition|(
name|charBuffer
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|string
init|=
name|charBuffer
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|string
operator|=
literal|null
expr_stmt|;
block|}
name|charBuffer
operator|=
literal|null
expr_stmt|;
return|return
name|string
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|unitInfos
return|;
block|}
block|}
end_class

end_unit

