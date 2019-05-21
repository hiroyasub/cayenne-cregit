begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|template
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Token Manager Error.  */
end_comment

begin_class
specifier|public
class|class
name|TokenMgrError
extends|extends
name|Error
block|{
comment|/**      * The version identifier for this Serializable class.      * Increment only if the<i>serialized</i> form of the      * class changes.      */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/*    * Ordinals for various reasons why an Error of this type can be thrown.    */
comment|/**      * Lexical error occurred.      */
specifier|static
specifier|final
name|int
name|LEXICAL_ERROR
init|=
literal|0
decl_stmt|;
comment|/**      * An attempt was made to create a second instance of a static token manager.      */
specifier|static
specifier|final
name|int
name|STATIC_LEXER_ERROR
init|=
literal|1
decl_stmt|;
comment|/**      * Tried to change to an invalid lexical state.      */
specifier|static
specifier|final
name|int
name|INVALID_LEXICAL_STATE
init|=
literal|2
decl_stmt|;
comment|/**      * Detected (and bailed out of) an infinite loop in the token manager.      */
specifier|static
specifier|final
name|int
name|LOOP_DETECTED
init|=
literal|3
decl_stmt|;
comment|/**      * Indicates the reason why the exception is thrown. It will have      * one of the above 4 values.      */
name|int
name|errorCode
decl_stmt|;
comment|/**      * Replaces unprintable characters by their escaped (or unicode escaped)      * equivalents in the given string      */
specifier|protected
specifier|static
name|String
name|addEscapes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|StringBuilder
name|retval
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|char
name|ch
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
condition|)
block|{
case|case
literal|0
case|:
continue|continue;
case|case
literal|'\b'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\b"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\t'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\t"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\n'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\n"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\f'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\f"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\r'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\r"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\"'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\\""
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\''
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\\'"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\\'
case|:
name|retval
operator|.
name|append
argument_list|(
literal|"\\\\"
argument_list|)
expr_stmt|;
continue|continue;
default|default:
if|if
condition|(
operator|(
name|ch
operator|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|)
operator|<
literal|0x20
operator|||
name|ch
operator|>
literal|0x7e
condition|)
block|{
name|String
name|s
init|=
literal|"0000"
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|ch
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|retval
operator|.
name|append
argument_list|(
literal|"\\u"
argument_list|)
operator|.
name|append
argument_list|(
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|,
name|s
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|.
name|append
argument_list|(
name|ch
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a detailed message for the Error when it is thrown by the      * token manager to indicate a lexical error.      * Parameters :      * EOFSeen     : indicates if EOF caused the lexical error      * curLexState : lexical state in which this error occurred      * errorLine   : line number when the error occurred      * errorColumn : column number when the error occurred      * errorAfter  : prefix that was seen before this error occurred      * curchar     : the offending character      * Note: You can customize the lexical error message by modifying this method.      */
specifier|protected
specifier|static
name|String
name|LexicalError
parameter_list|(
name|boolean
name|EOFSeen
parameter_list|,
name|int
name|lexState
parameter_list|,
name|int
name|errorLine
parameter_list|,
name|int
name|errorColumn
parameter_list|,
name|String
name|errorAfter
parameter_list|,
name|char
name|curChar
parameter_list|)
block|{
return|return
operator|(
literal|"Lexical error at line "
operator|+
name|errorLine
operator|+
literal|", column "
operator|+
name|errorColumn
operator|+
literal|".  Encountered: "
operator|+
operator|(
name|EOFSeen
condition|?
literal|"<EOF> "
else|:
operator|(
literal|"\""
operator|+
name|addEscapes
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|curChar
argument_list|)
argument_list|)
operator|+
literal|"\""
operator|)
operator|+
literal|" ("
operator|+
operator|(
name|int
operator|)
name|curChar
operator|+
literal|"), "
operator|)
operator|+
literal|"after : \""
operator|+
name|addEscapes
argument_list|(
name|errorAfter
argument_list|)
operator|+
literal|"\""
operator|)
return|;
block|}
comment|/**      * You can also modify the body of this method to customize your error messages.      * For example, cases like LOOP_DETECTED and INVALID_LEXICAL_STATE are not      * of end-users concern, so you can return something like :      *<p>      * "Internal Error : Please file a bug report .... "      *<p>      * from this method for such cases in the release version of your parser.      */
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|super
operator|.
name|getMessage
argument_list|()
return|;
block|}
comment|/*    * Constructors of various flavors follow.    */
comment|/**      * No arg constructor.      */
specifier|public
name|TokenMgrError
parameter_list|()
block|{
block|}
comment|/**      * Constructor with message and reason.      */
specifier|public
name|TokenMgrError
parameter_list|(
name|String
name|message
parameter_list|,
name|int
name|reason
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|errorCode
operator|=
name|reason
expr_stmt|;
block|}
comment|/**      * Full Constructor.      */
specifier|public
name|TokenMgrError
parameter_list|(
name|boolean
name|EOFSeen
parameter_list|,
name|int
name|lexState
parameter_list|,
name|int
name|errorLine
parameter_list|,
name|int
name|errorColumn
parameter_list|,
name|String
name|errorAfter
parameter_list|,
name|char
name|curChar
parameter_list|,
name|int
name|reason
parameter_list|)
block|{
name|this
argument_list|(
name|LexicalError
argument_list|(
name|EOFSeen
argument_list|,
name|lexState
argument_list|,
name|errorLine
argument_list|,
name|errorColumn
argument_list|,
name|errorAfter
argument_list|,
name|curChar
argument_list|)
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

