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
name|conf
package|;
end_package

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
comment|/**  * The common superclass for all SAX event handlers used to parse the configuration file.  * Each method just throws an exception, so subclasses should override what they can  * handle. Each type of XML element (map, node, etc.) has a specific subclass. In the  * constructor, this class takes over the handling of SAX events from the parent handler  * and returns control back to the parent in the endElement method.  *</p>  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|AbstractHandler
extends|extends
name|DefaultHandler
block|{
comment|/** Current parser. */
specifier|protected
name|XMLReader
name|parser
decl_stmt|;
comment|/**      * Previous handler for the document. When the next element is finished, control      * returns to this handler.      */
specifier|protected
name|ContentHandler
name|parentHandler
decl_stmt|;
comment|/**      * Creates a handler and sets the parser to use it for the current element.      *       * @param parser Currently used XML parser. Must not be<code>null</code>.      * @param parentHandler The handler which should be restored to the parser at the end      *            of the element. Must not be<code>null</code>.      */
specifier|public
name|AbstractHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|ContentHandler
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
comment|// Start handling SAX events
name|parser
operator|.
name|setContentHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/** Returns currently used XMLReader. */
specifier|public
name|XMLReader
name|getParser
parameter_list|()
block|{
return|return
name|parser
return|;
block|}
comment|/**      * Handles the start of an element. This base implementation just throws an exception.      *       * @exception SAXException if this method is not overridden, or in case of error in an      *                overridden version      */
specifier|public
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
name|atts
parameter_list|)
throws|throws
name|SAXException
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": unexpected element \""
operator|+
name|localName
operator|+
literal|"\""
argument_list|)
throw|;
block|}
comment|/**      * Handles text within an element. This base implementation just throws an exception.      *       * @param buf A character array of the text within the element. Will not be      *<code>null</code>.      * @param start The start element in the array.      * @param count The number of characters to read from the array.      * @exception SAXException if this method is not overridden, or in case of error in an      *                overridden version      */
specifier|public
name|void
name|characters
parameter_list|(
name|char
index|[]
name|buf
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|SAXException
block|{
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|buf
argument_list|,
name|start
argument_list|,
name|count
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|SAXException
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": unexpected text \""
operator|+
name|s
operator|+
literal|"\""
argument_list|)
throw|;
block|}
block|}
comment|/**      * Called when this element and all elements nested into it have been handled.      */
specifier|protected
name|void
name|finished
parameter_list|()
block|{
block|}
comment|/**      * Handles the end of an element. Any required clean-up is performed by the finished()      * method and then the original handler is restored to the parser.      *       * @see #finished()      */
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
name|finished
argument_list|()
expr_stmt|;
comment|// Let parent resume handling SAX events
name|parser
operator|.
name|setContentHandler
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

