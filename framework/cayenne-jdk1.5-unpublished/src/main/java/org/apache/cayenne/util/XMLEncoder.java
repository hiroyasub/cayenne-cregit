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
name|Map
import|;
end_import

begin_comment
comment|/**  * A helper class to encode objects to XML.  *   * @since 1.1  */
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
name|PrintWriter
name|getPrintWriter
parameter_list|()
block|{
return|return
name|out
return|;
block|}
specifier|public
name|void
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
block|}
comment|/**      * Utility method that prints all map values, assuming they are XMLSerializable      * objects.      */
specifier|public
name|void
name|print
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
parameter_list|)
block|{
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Utility method that prints all map values, assuming they are XMLSerializable      * objects.      */
specifier|public
name|void
name|print
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|XMLSerializable
argument_list|>
name|c
parameter_list|)
block|{
for|for
control|(
name|XMLSerializable
name|value
range|:
name|c
control|)
block|{
name|value
operator|.
name|encodeAsXML
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Inserts an optional project version attribute in the output. If the project version      * is not initialized for encoder, will do nothing.      *       * @since 3.1      */
specifier|public
name|void
name|printProjectVersion
parameter_list|()
block|{
name|printAttribute
argument_list|(
literal|"project-version"
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints an XML attribute. The value is trimmed (so leading and following spaces are      * lost) and then encoded to be a proper XML attribute value. E.g. "&" becomes      * "&amp;", etc.      *       * @since 3.1      */
specifier|public
name|void
name|printAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|printAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
name|printlnAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|printAttribute
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|printAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|boolean
name|lineBreak
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|value
operator|=
name|value
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|value
operator|=
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"=\""
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|lineBreak
condition|)
block|{
name|println
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Prints a common XML element - property with name and value.      */
specifier|public
name|void
name|printProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
literal|"<property"
argument_list|)
expr_stmt|;
name|printAttribute
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|printAttribute
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
name|indentLine
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Prints a common XML element - property with name and value.      */
specifier|public
name|void
name|printProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|printProperty
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints a common XML element - property with name and value.      */
specifier|public
name|void
name|printProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|i
parameter_list|)
block|{
name|printProperty
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
expr_stmt|;
block|}
specifier|public
name|void
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
block|}
specifier|public
name|void
name|print
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|print
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|print
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|print
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
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
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
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
block|}
specifier|public
name|void
name|println
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|printIndent
argument_list|()
expr_stmt|;
name|out
operator|.
name|println
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|indentLine
operator|=
literal|true
expr_stmt|;
block|}
specifier|private
name|void
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
block|}
block|}
end_class

end_unit

