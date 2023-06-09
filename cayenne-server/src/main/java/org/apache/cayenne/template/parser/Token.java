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
comment|/**  * Describes the input token stream.  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|Token
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
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
comment|/**      * An integer that describes the kind of this token.  This numbering      * system is determined by JavaCCParser, and a table of these numbers is      * stored in the file ...Constants.java.      */
specifier|public
name|int
name|kind
decl_stmt|;
comment|/**      * The line number of the first character of this Token.      */
specifier|public
name|int
name|beginLine
decl_stmt|;
comment|/**      * The column number of the first character of this Token.      */
specifier|public
name|int
name|beginColumn
decl_stmt|;
comment|/**      * The line number of the last character of this Token.      */
specifier|public
name|int
name|endLine
decl_stmt|;
comment|/**      * The column number of the last character of this Token.      */
specifier|public
name|int
name|endColumn
decl_stmt|;
comment|/**      * The string image of the token.      */
specifier|public
name|String
name|image
decl_stmt|;
comment|/**      * A reference to the next regular (non-special) token from the input      * stream.  If this is the last token from the input stream, or if the      * token manager has not read tokens beyond this one, this field is      * set to null.  This is true only if this token is also a regular      * token.  Otherwise, see below for a description of the contents of      * this field.      */
specifier|public
name|Token
name|next
decl_stmt|;
comment|/**      * This field is used to access special tokens that occur prior to this      * token, but after the immediately preceding regular (non-special) token.      * If there are no such special tokens, this field is set to null.      * When there are more than one such special token, this field refers      * to the last of these special tokens, which in turn refers to the next      * previous special token through its specialToken field, and so on      * until the first special token (whose specialToken field is null).      * The next fields of special tokens refer to other special tokens that      * immediately follow it (without an intervening regular token).  If there      * is no such token, this field is null.      */
specifier|public
name|Token
name|specialToken
decl_stmt|;
comment|/**      * An optional attribute value of the Token.      * Tokens which are not used as syntactic sugar will often contain      * meaningful values that will be used later on by the compiler or      * interpreter. This attribute value is often different from the image.      * Any subclass of Token that actually wants to return a non-null value can      * override this method as appropriate.      */
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * No-argument constructor      */
specifier|public
name|Token
parameter_list|()
block|{
block|}
comment|/**      * Constructs a new token for the specified Image.      */
specifier|public
name|Token
parameter_list|(
name|int
name|kind
parameter_list|)
block|{
name|this
argument_list|(
name|kind
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs a new token for the specified Image and Kind.      */
specifier|public
name|Token
parameter_list|(
name|int
name|kind
parameter_list|,
name|String
name|image
parameter_list|)
block|{
name|this
operator|.
name|kind
operator|=
name|kind
expr_stmt|;
name|this
operator|.
name|image
operator|=
name|image
expr_stmt|;
block|}
comment|/**      * Returns the image.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|image
return|;
block|}
comment|/**      * Returns a new Token object, by default. However, if you want, you      * can create and return subclass objects based on the value of ofKind.      * Simply add the cases to the switch for all those special cases.      * For example, if you have a subclass of Token called IDToken that      * you want to create if ofKind is ID, simply add something like :      *<p>      * case MyParserConstants.ID : return new IDToken(ofKind, image);      *<p>      * to the following switch statement. Then you can cast matchedToken      * variable to the appropriate type and use sit in your lexical actions.      */
specifier|public
specifier|static
name|Token
name|newToken
parameter_list|(
name|int
name|ofKind
parameter_list|,
name|String
name|image
parameter_list|)
block|{
switch|switch
condition|(
name|ofKind
condition|)
block|{
default|default:
return|return
operator|new
name|Token
argument_list|(
name|ofKind
argument_list|,
name|image
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|Token
name|newToken
parameter_list|(
name|int
name|ofKind
parameter_list|)
block|{
return|return
name|newToken
argument_list|(
name|ofKind
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

