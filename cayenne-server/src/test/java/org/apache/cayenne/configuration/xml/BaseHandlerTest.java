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
name|configuration
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|util
operator|.
name|Util
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
name|ContentHandler
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
name|XMLReader
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
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseHandlerTest
block|{
specifier|protected
name|void
name|parse
parameter_list|(
name|String
name|tag
parameter_list|,
name|HandlerFactory
name|factory
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|BaseHandlerTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|".xml"
argument_list|)
operator|.
name|openStream
argument_list|()
init|)
block|{
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|DefaultHandler
name|handler
init|=
operator|new
name|TestRootHandler
argument_list|(
name|parser
argument_list|,
name|tag
argument_list|,
name|factory
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
interface|interface
name|HandlerFactory
block|{
name|NamespaceAwareNestedTagHandler
name|createHandler
parameter_list|(
name|NamespaceAwareNestedTagHandler
name|parent
parameter_list|)
function_decl|;
block|}
specifier|public
specifier|static
class|class
name|TestRootHandler
extends|extends
name|NamespaceAwareNestedTagHandler
block|{
specifier|private
name|String
name|rootTag
decl_stmt|;
specifier|private
name|BaseHandlerTest
operator|.
name|HandlerFactory
name|factory
decl_stmt|;
specifier|public
name|TestRootHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|String
name|rootTag
parameter_list|,
name|BaseHandlerTest
operator|.
name|HandlerFactory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|LoaderContext
argument_list|(
name|parser
argument_list|,
operator|new
name|DefaultHandlerFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setTargetNamespace
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|rootTag
operator|=
name|rootTag
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ContentHandler
name|createChildTagHandler
parameter_list|(
name|String
name|namespaceURI
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
block|{
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|rootTag
argument_list|)
condition|)
block|{
return|return
name|factory
operator|.
name|createHandler
argument_list|(
name|this
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|createChildTagHandler
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|qName
argument_list|,
name|attributes
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

