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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|Deque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
import|;
end_import

begin_comment
comment|/**  *<p>  * A helper class to encode objects to XML.  *</p>  * Usage:<pre>{@code  *      XMLEncoder encoder = new XMLEncoder(writer);  *      encoder  *          .start("tag").attribute("name", "tag_name_attribute")  *          .start("nested_tag").attribute("name", "nested_tag_name).cdata("tag text element").end()  *          .end();  * }</pre>  *  * @since 1.1  * @since 4.1 API is greatly reworked to be more usable  */
end_comment

begin_class
specifier|public
class|class
name|XMLEncoder
block|{
specifier|protected
name|String
name|projectVersion
decl_stmt|;
specifier|protected
name|String
name|indent
decl_stmt|;
specifier|protected
name|PrintWriter
name|out
decl_stmt|;
specifier|protected
name|boolean
name|indentLine
decl_stmt|;
specifier|protected
name|int
name|indentTimes
decl_stmt|;
specifier|protected
name|boolean
name|tagOpened
decl_stmt|;
specifier|protected
name|boolean
name|cdata
decl_stmt|;
specifier|protected
name|int
name|currentTagLevel
decl_stmt|;
specifier|protected
name|int
name|lastTagLevel
decl_stmt|;
specifier|protected
name|Deque
argument_list|<
name|String
argument_list|>
name|openTags
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|XMLEncoder
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
block|{
name|this
argument_list|(
name|out
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XMLEncoder
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|String
name|indent
parameter_list|)
block|{
name|this
argument_list|(
name|out
argument_list|,
name|indent
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|XMLEncoder
parameter_list|(
name|PrintWriter
name|out
parameter_list|,
name|String
name|indent
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
name|this
operator|.
name|indent
operator|=
name|indent
expr_stmt|;
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|projectVersion
operator|=
name|projectVersion
expr_stmt|;
block|}
specifier|public
name|XMLEncoder
name|indent
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|indentTimes
operator|+=
name|i
expr_stmt|;
if|if
condition|(
name|indentTimes
operator|<
literal|0
condition|)
block|{
name|indentTimes
operator|=
literal|0
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|XMLEncoder
name|print
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|text
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|XMLEncoder
name|println
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|indentLine
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|XMLEncoder
name|println
parameter_list|()
block|{
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|indentLine
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|private
name|XMLEncoder
name|printIndent
parameter_list|()
block|{
if|if
condition|(
name|indentLine
condition|)
block|{
name|indentLine
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|indentTimes
operator|>
literal|0
operator|&&
name|indent
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|indentTimes
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|print
argument_list|(
name|indent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param tag to start      * @return this      */
specifier|public
name|XMLEncoder
name|start
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
if|if
condition|(
name|tagOpened
condition|)
block|{
name|println
argument_list|(
literal|">"
argument_list|)
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|printIndent
argument_list|()
operator|.
name|print
argument_list|(
literal|"<"
argument_list|)
operator|.
name|print
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|lastTagLevel
operator|=
operator|++
name|currentTagLevel
expr_stmt|;
name|tagOpened
operator|=
literal|true
expr_stmt|;
name|openTags
operator|.
name|push
argument_list|(
name|tag
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * This method will track presence of nested tags and print closure accordingly      *      * @since 4.1      * @return this      */
specifier|public
name|XMLEncoder
name|end
parameter_list|()
block|{
name|tagOpened
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|lastTagLevel
operator|==
name|currentTagLevel
operator|--
operator|&&
operator|!
name|cdata
condition|)
block|{
name|openTags
operator|.
name|pop
argument_list|()
expr_stmt|;
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|cdata
condition|)
block|{
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
operator|.
name|printIndent
argument_list|()
expr_stmt|;
block|}
name|cdata
operator|=
literal|false
expr_stmt|;
name|print
argument_list|(
literal|"</"
argument_list|)
operator|.
name|print
argument_list|(
name|openTags
operator|.
name|pop
argument_list|()
argument_list|)
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param name of the attribute      * @param value of the attribute      * @return this      */
specifier|public
name|XMLEncoder
name|attribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|attribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @param name of the attribute      * @param value of the attribute      * @param newLine should this attribute be printed on new line      * @return this      */
specifier|public
name|XMLEncoder
name|attribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|newLine
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|newLine
condition|)
block|{
name|indent
argument_list|(
literal|1
argument_list|)
operator|.
name|println
argument_list|()
operator|.
name|printIndent
argument_list|()
expr_stmt|;
block|}
name|print
argument_list|(
literal|" "
argument_list|)
operator|.
name|print
argument_list|(
name|name
argument_list|)
operator|.
name|print
argument_list|(
literal|"=\""
argument_list|)
operator|.
name|print
argument_list|(
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|value
argument_list|)
argument_list|)
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|newLine
condition|)
block|{
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param name of the attribute      * @param value of the attribute      * @return this      */
specifier|public
name|XMLEncoder
name|attribute
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|value
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|attribute
argument_list|(
name|name
argument_list|,
literal|"true"
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @param name of the attribute      * @param value of the attribute      * @return this      */
specifier|public
name|XMLEncoder
name|attribute
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|attribute
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @param data char data      * @return this      */
specifier|public
name|XMLEncoder
name|cdata
parameter_list|(
name|String
name|data
parameter_list|)
block|{
return|return
name|cdata
argument_list|(
name|data
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * @since 4.1      * @param data char data      * @param escape does this data need to be enclosed into&lt;![CDATA[ ... ]]&gt;      * @return this      */
specifier|public
name|XMLEncoder
name|cdata
parameter_list|(
name|String
name|data
parameter_list|,
name|boolean
name|escape
parameter_list|)
block|{
if|if
condition|(
name|tagOpened
condition|)
block|{
name|print
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
block|}
name|cdata
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|escape
condition|)
block|{
name|print
argument_list|(
literal|"<![CDATA["
argument_list|)
expr_stmt|;
block|}
name|print
argument_list|(
name|data
argument_list|)
expr_stmt|;
if|if
condition|(
name|escape
condition|)
block|{
name|print
argument_list|(
literal|"]]>"
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param object nested object to serialize      * @param delegate visitor      * @return this      */
specifier|public
name|XMLEncoder
name|nested
parameter_list|(
name|XMLSerializable
name|object
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
name|object
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param collection of nested objects      * @param delegate visitor      * @return this      */
specifier|public
name|XMLEncoder
name|nested
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|XMLSerializable
argument_list|>
name|collection
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
for|for
control|(
name|XMLSerializable
name|value
range|:
name|collection
control|)
block|{
name|value
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * @since 4.1      * @param map of nested objects      * @param delegate visitor      * @return this      */
specifier|public
name|XMLEncoder
name|nested
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|XMLSerializable
argument_list|>
name|map
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
for|for
control|(
name|XMLSerializable
name|value
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
name|value
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Prints a common XML element - property with name and value.      * @since 4.1      */
specifier|public
name|XMLEncoder
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|this
return|;
block|}
name|start
argument_list|(
literal|"property"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|indentLine
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Prints a common XML element - property with name and value.      * @since 4.1      */
specifier|public
name|XMLEncoder
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
if|if
condition|(
operator|!
name|b
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|property
argument_list|(
name|name
argument_list|,
literal|"true"
argument_list|)
return|;
block|}
comment|/**      * Prints a common XML element - property with name and value.      * @since 4.1      */
specifier|public
name|XMLEncoder
name|property
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|property
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Prints common XML element - tag with name and text value (&lt;tag>value&lt;/tag>)      * If value is empty, nothing will be printed.      * @since 4.1      */
specifier|public
name|XMLEncoder
name|simpleTag
parameter_list|(
name|String
name|tag
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|start
argument_list|(
name|tag
argument_list|)
operator|.
name|cdata
argument_list|(
name|value
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Inserts an optional project version attribute in the output. If the project version      * is not initialized for encoder, will do nothing.      *      * @since 4.1      */
specifier|public
name|XMLEncoder
name|projectVersion
parameter_list|()
block|{
return|return
name|attribute
argument_list|(
literal|"project-version"
argument_list|,
name|projectVersion
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

