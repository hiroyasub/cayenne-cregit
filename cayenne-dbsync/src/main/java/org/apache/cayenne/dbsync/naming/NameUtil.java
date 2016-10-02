begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
operator|.
name|naming
package|;
end_package

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|final
class|class
name|NameUtil
block|{
specifier|static
name|String
name|uncapitalize
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|int
name|len
decl_stmt|;
if|if
condition|(
name|string
operator|==
literal|null
operator|||
operator|(
name|len
operator|=
name|string
operator|.
name|length
argument_list|()
operator|)
operator|==
literal|0
condition|)
block|{
return|return
name|string
return|;
block|}
specifier|final
name|char
name|firstChar
init|=
name|string
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|char
name|newChar
init|=
name|Character
operator|.
name|toLowerCase
argument_list|(
name|firstChar
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstChar
operator|==
name|newChar
condition|)
block|{
comment|// already capitalized
return|return
name|string
return|;
block|}
name|char
index|[]
name|newChars
init|=
operator|new
name|char
index|[
name|len
index|]
decl_stmt|;
name|newChars
index|[
literal|0
index|]
operator|=
name|newChar
expr_stmt|;
name|string
operator|.
name|getChars
argument_list|(
literal|1
argument_list|,
name|len
argument_list|,
name|newChars
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|newChars
argument_list|)
return|;
block|}
specifier|static
name|String
name|capitalize
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|int
name|len
decl_stmt|;
if|if
condition|(
name|string
operator|==
literal|null
operator|||
operator|(
name|len
operator|=
name|string
operator|.
name|length
argument_list|()
operator|)
operator|==
literal|0
condition|)
block|{
return|return
name|string
return|;
block|}
specifier|final
name|char
name|firstChar
init|=
name|string
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|char
name|newChar
init|=
name|Character
operator|.
name|toTitleCase
argument_list|(
name|firstChar
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstChar
operator|==
name|newChar
condition|)
block|{
comment|// already capitalized
return|return
name|string
return|;
block|}
name|char
index|[]
name|newChars
init|=
operator|new
name|char
index|[
name|len
index|]
decl_stmt|;
name|newChars
index|[
literal|0
index|]
operator|=
name|newChar
expr_stmt|;
name|string
operator|.
name|getChars
argument_list|(
literal|1
argument_list|,
name|len
argument_list|,
name|newChars
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|newChars
argument_list|)
return|;
block|}
block|}
end_class

end_unit

