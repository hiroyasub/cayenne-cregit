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
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|OutputKeys
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
name|Result
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
name|Source
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
name|Transformer
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
name|TransformerFactory
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
name|dom
operator|.
name|DOMSource
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
name|StreamResult
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
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * A helper class to encode objects to XML.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|XMLEncoder
block|{
specifier|private
name|XMLMappingDescriptor
name|mappingDescriptor
decl_stmt|;
comment|// temp ivars that define the structure of the encoding stack.
specifier|private
name|Document
name|document
decl_stmt|;
specifier|private
name|Node
name|parent
decl_stmt|;
specifier|private
name|String
name|tagOverride
decl_stmt|;
specifier|private
name|boolean
name|inProgress
decl_stmt|;
comment|/**      * Creates new XMLEncoder.      */
specifier|public
name|XMLEncoder
parameter_list|()
block|{
block|}
comment|/**      * Creates new XMLEncoder that will use a mapping descriptor loaded via provided URL.      */
specifier|public
name|XMLEncoder
parameter_list|(
name|String
name|mappingUrl
parameter_list|)
block|{
name|this
operator|.
name|mappingDescriptor
operator|=
operator|new
name|XMLMappingDescriptor
argument_list|(
name|mappingUrl
argument_list|)
expr_stmt|;
block|}
comment|/**      * A callback for XMLSerializable objects to add a node to an encoding tree.      */
specifier|public
name|void
name|setRoot
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|setRoot
argument_list|(
name|xmlTag
argument_list|,
name|type
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * A callback method for XMLSerializable objects to encode an object property. Note      * that the object must call "setRoot" prior to encoding its properties.      *       * @param xmlTag The name of the XML element used to represent the property.      * @param value The object's property value to encode.      */
specifier|public
name|void
name|encodeProperty
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|encodeProperty
argument_list|(
name|xmlTag
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Encodes an object using "root" as a root tag.      */
specifier|public
name|String
name|encode
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|encode
argument_list|(
literal|"root"
argument_list|,
name|object
argument_list|)
return|;
block|}
comment|/**      * Encodes using provided root XML tag.      */
specifier|public
name|String
name|encode
parameter_list|(
name|String
name|rootTag
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
try|try
block|{
name|initDocument
argument_list|(
name|rootTag
argument_list|,
name|object
operator|!=
literal|null
condition|?
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|mappingDescriptor
operator|!=
literal|null
condition|)
block|{
name|mappingDescriptor
operator|.
name|getRootEntity
argument_list|()
operator|.
name|setObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|mappingDescriptor
operator|.
name|getRootEntity
argument_list|()
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|encodeProperty
argument_list|(
name|rootTag
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Collection
condition|)
block|{
return|return
name|nodeToString
argument_list|(
name|getRootNode
argument_list|(
literal|true
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|nodeToString
argument_list|(
name|getRootNode
argument_list|(
literal|false
argument_list|)
argument_list|)
return|;
block|}
block|}
finally|finally
block|{
comment|// make sure we can restart the encoder...
name|inProgress
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|/**      * Resets the encoder to process a new object tree.      */
name|void
name|initDocument
parameter_list|(
name|String
name|rootTag
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|XMLUtil
operator|.
name|newBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|document
expr_stmt|;
comment|// create a "synthetic" root
name|Element
name|root
init|=
name|push
argument_list|(
name|rootTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|root
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
name|inProgress
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Returns a root DOM node of the encoder.      */
name|Node
name|getRootNode
parameter_list|(
name|boolean
name|forceSyntheticRoot
parameter_list|)
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// if synthetic root has a single child, use child as a root
name|Node
name|root
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|forceSyntheticRoot
operator|&&
name|root
operator|.
name|getChildNodes
argument_list|()
operator|.
name|getLength
argument_list|()
operator|==
literal|1
condition|)
block|{
name|root
operator|=
name|root
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
return|return
name|root
return|;
block|}
name|String
name|nodeToString
parameter_list|(
name|Node
name|rootNode
parameter_list|)
block|{
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|rootNode
argument_list|)
decl_stmt|;
try|try
block|{
name|Transformer
name|xformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|xformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"xml"
argument_list|)
expr_stmt|;
name|xformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|xformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|xformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
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
name|CayenneRuntimeException
argument_list|(
literal|"XML transformation error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|out
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
return|;
block|}
name|void
name|setRoot
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|String
name|type
parameter_list|,
name|boolean
name|push
parameter_list|)
block|{
comment|// all public methods must implicitly init the document
if|if
condition|(
operator|!
name|inProgress
condition|)
block|{
name|initDocument
argument_list|(
name|xmlTag
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tagOverride
operator|!=
literal|null
condition|)
block|{
name|xmlTag
operator|=
name|tagOverride
expr_stmt|;
name|tagOverride
operator|=
literal|null
expr_stmt|;
block|}
comment|// new node will be a peer rather than a child of current parent.
if|if
condition|(
operator|!
name|push
condition|)
block|{
name|pop
argument_list|()
expr_stmt|;
block|}
name|Element
name|element
init|=
name|push
argument_list|(
name|xmlTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|encodeProperty
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|Object
name|value
parameter_list|,
name|boolean
name|useType
parameter_list|)
block|{
comment|// all public methods must be able to implicitly init the document
if|if
condition|(
operator|!
name|inProgress
condition|)
block|{
name|String
name|type
init|=
operator|(
name|useType
operator|&&
name|value
operator|!=
literal|null
operator|)
condition|?
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
decl_stmt|;
name|initDocument
argument_list|(
name|xmlTag
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|XMLSerializable
condition|)
block|{
name|encodeSerializable
argument_list|(
name|xmlTag
argument_list|,
operator|(
name|XMLSerializable
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|encodeCollection
argument_list|(
name|xmlTag
argument_list|,
operator|(
name|Collection
operator|)
name|value
argument_list|,
name|useType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|encodeSimple
argument_list|(
name|xmlTag
argument_list|,
name|value
argument_list|,
name|useType
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|encodeSimple
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|Object
name|object
parameter_list|,
name|boolean
name|useType
parameter_list|)
block|{
comment|// simple properties will not call setRoot, so push manually
name|Element
name|node
init|=
name|push
argument_list|(
name|xmlTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|useType
condition|)
block|{
name|node
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Dates need special handling
if|if
condition|(
name|object
operator|instanceof
name|Date
condition|)
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|XMLUtil
operator|.
name|DEFAULT_DATE_FORMAT
argument_list|)
decl_stmt|;
name|node
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createTextNode
argument_list|(
name|sdf
operator|.
name|format
argument_list|(
name|object
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|node
operator|.
name|appendChild
argument_list|(
name|document
operator|.
name|createTextNode
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pop
argument_list|()
expr_stmt|;
block|}
name|void
name|encodeSerializable
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|XMLSerializable
name|object
parameter_list|)
block|{
comment|// don't allow children to reset XML tag name ... unless they are at the root
comment|// level
if|if
condition|(
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|!=
name|parent
condition|)
block|{
name|tagOverride
operator|=
name|xmlTag
expr_stmt|;
block|}
name|object
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|tagOverride
operator|=
literal|null
expr_stmt|;
name|pop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Encodes a collection of objects, attaching them to the current root.      *       * @param xmlTag The name of the root XML element for the encoded collection.      * @param c The collection to encode.      */
name|void
name|encodeCollection
parameter_list|(
name|String
name|xmlTag
parameter_list|,
name|Collection
name|c
parameter_list|,
name|boolean
name|useType
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// encode collection without doing push/pop so that its elements are encoded
comment|// without an intermediate grouping node.
name|encodeProperty
argument_list|(
name|xmlTag
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|useType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
operator|(
operator|(
name|Element
operator|)
name|parent
operator|.
name|getLastChild
argument_list|()
operator|)
operator|.
name|setAttribute
argument_list|(
literal|"forceList"
argument_list|,
literal|"YES"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a new element, pushing it onto encoding stack.      */
specifier|private
name|Element
name|push
parameter_list|(
name|String
name|xmlTag
parameter_list|)
block|{
name|Element
name|child
init|=
name|document
operator|.
name|createElement
argument_list|(
name|xmlTag
argument_list|)
decl_stmt|;
name|this
operator|.
name|parent
operator|.
name|appendChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|child
expr_stmt|;
return|return
name|child
return|;
block|}
comment|/**      * Pops the top element from the encoding stack.      */
name|Node
name|pop
parameter_list|()
block|{
name|Node
name|old
init|=
name|parent
decl_stmt|;
name|parent
operator|=
name|parent
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
return|return
name|old
return|;
block|}
block|}
end_class

end_unit

