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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
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
name|map
operator|.
name|JpaEntityMap
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
name|xml
operator|.
name|XMLDecoder
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

begin_comment
comment|/**  * {@link org.apache.cayenne.jpa.map.JpaEntityMap} loader that reads mapping information  * from the XML sources compatible with the JPA ORM schema.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EntityMapXMLLoader
block|{
specifier|static
specifier|final
name|String
name|XML_CODER_MAPPING
init|=
literal|"META-INF/cayenne/orm-coder.xml"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ORM_SCHEMA
init|=
literal|"META-INF/schemas/orm_1_0.xsd"
decl_stmt|;
name|Schema
name|schema
decl_stmt|;
specifier|protected
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|public
name|EntityMapXMLLoader
parameter_list|()
block|{
name|this
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EntityMapXMLLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|,
name|boolean
name|validateDescriptors
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
comment|// TODO: andrus, 04/18/2006 - merge validation capabilities to the XMLDecoder...
if|if
condition|(
name|validateDescriptors
condition|)
block|{
name|SAXParserFactory
name|parserFactory
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|parserFactory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// note that validation requires that orm.xml declares a schema like this:
comment|//<orm xmlns="http://java.sun.com/xml/ns/persistence/orm"
comment|// xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
comment|// xsi:schemaLocation="http://java.sun.com/xml/ns/persistence/orm
comment|// http://java.sun.com/xml/ns/persistence/orm/orm_1_0.xsd">
name|URL
name|schemaURL
init|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|ORM_SCHEMA
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
try|try
block|{
name|this
operator|.
name|schema
operator|=
name|factory
operator|.
name|newSchema
argument_list|(
name|ss
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error loading ORM schema"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Loads {@link JpaEntityMap} using provided class loader to locate the XML. Returns      * null if no mapping is found.      */
specifier|public
name|JpaEntityMap
name|getEntityMap
parameter_list|(
name|URL
name|resource
parameter_list|)
block|{
comment|// TODO: andrus, 04/18/2006 XMLDecoder should support classpath locations for
comment|// mapping descriptors
name|URL
name|mappingURL
init|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|XML_CODER_MAPPING
argument_list|)
decl_stmt|;
if|if
condition|(
name|mappingURL
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No code mapping found: "
operator|+
name|XML_CODER_MAPPING
argument_list|)
throw|;
block|}
name|validate
argument_list|(
name|resource
argument_list|)
expr_stmt|;
try|try
block|{
name|Reader
name|in
init|=
operator|new
name|InputStreamReader
argument_list|(
name|resource
operator|.
name|openStream
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 04/18/2006 - an inefficiency in XMLDecoder - it
comment|// doesn't cache the mapping
name|XMLDecoder
name|decoder
init|=
operator|new
name|XMLDecoder
argument_list|()
decl_stmt|;
name|JpaEntityMap
name|entityMap
init|=
operator|(
name|JpaEntityMap
operator|)
name|decoder
operator|.
name|decode
argument_list|(
name|in
argument_list|,
name|mappingURL
operator|.
name|toExternalForm
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|entityMap
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error processing ORM mapping "
operator|+
name|resource
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|// TODO: andrus, 04/18/2006 - move schema validation to the XMLDecoder
name|void
name|validate
parameter_list|(
name|URL
name|resource
parameter_list|)
block|{
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|schema
operator|.
name|newValidator
argument_list|()
operator|.
name|validate
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|resource
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error validating ORM mapping "
operator|+
name|resource
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Error processing ORM mapping "
operator|+
name|resource
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

