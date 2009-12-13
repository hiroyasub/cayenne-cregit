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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Locator
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
comment|/**  * A superclass of nested tag handlers for parsing of XML documents with SAX.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|SAXNestedTagHandler
extends|extends
name|DefaultHandler
block|{
specifier|private
specifier|final
specifier|static
name|Locator
name|NOOP_LOCATOR
init|=
operator|new
name|Locator
argument_list|()
block|{
specifier|public
name|int
name|getColumnNumber
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|int
name|getLineNumber
parameter_list|()
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|public
name|String
name|getPublicId
parameter_list|()
block|{
return|return
literal|"<unknown>"
return|;
block|}
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|"<unknown>"
return|;
block|}
block|}
decl_stmt|;
specifier|protected
name|XMLReader
name|parser
decl_stmt|;
specifier|protected
name|ContentHandler
name|parentHandler
decl_stmt|;
specifier|protected
name|Locator
name|locator
decl_stmt|;
specifier|public
name|SAXNestedTagHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|SAXNestedTagHandler
name|parentHandler
parameter_list|)
block|{
name|this
operator|.
name|parentHandler
operator|=
name|parentHandler
expr_stmt|;
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
if|if
condition|(
name|parentHandler
operator|!=
literal|null
condition|)
block|{
name|locator
operator|=
name|parentHandler
operator|.
name|locator
expr_stmt|;
block|}
if|if
condition|(
name|locator
operator|==
literal|null
condition|)
block|{
name|locator
operator|=
name|NOOP_LOCATOR
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|unexpectedTagMessage
parameter_list|(
name|String
name|tagFound
parameter_list|,
name|String
modifier|...
name|tagsExpected
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|expected
init|=
name|tagsExpected
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|tagsExpected
argument_list|)
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"tag<%s> is unexpected at [%d,%d]. The following tags are allowed here: %s"
argument_list|,
name|tagFound
argument_list|,
name|locator
operator|.
name|getColumnNumber
argument_list|()
argument_list|,
name|locator
operator|.
name|getLineNumber
argument_list|()
argument_list|,
name|expected
argument_list|)
return|;
block|}
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
comment|// loose handling of unrecognized tags - just ignore them
return|return
operator|new
name|SAXNestedTagHandler
argument_list|(
name|parser
argument_list|,
name|this
argument_list|)
return|;
block|}
specifier|protected
name|void
name|stop
parameter_list|()
block|{
comment|// pop self from the handler stack
name|parser
operator|.
name|setContentHandler
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|startElement
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
throws|throws
name|SAXException
block|{
comment|// push child handler to the stack...
name|ContentHandler
name|childHandler
init|=
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
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|childHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
name|String
name|namespaceURI
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
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setDocumentLocator
parameter_list|(
name|Locator
name|locator
parameter_list|)
block|{
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
block|}
block|}
end_class

end_unit

