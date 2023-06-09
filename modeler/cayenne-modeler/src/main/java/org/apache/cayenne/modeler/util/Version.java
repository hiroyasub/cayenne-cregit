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
name|modeler
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * Helper class to deal with version strings.  *   */
end_comment

begin_class
specifier|public
class|class
name|Version
implements|implements
name|Comparable
argument_list|<
name|Object
argument_list|>
block|{
specifier|protected
name|String
name|versionString
decl_stmt|;
specifier|protected
name|int
index|[]
name|versionParts
decl_stmt|;
specifier|public
name|Version
parameter_list|(
name|String
name|versionString
parameter_list|)
throws|throws
name|NumberFormatException
block|{
if|if
condition|(
name|versionString
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null version."
argument_list|)
throw|;
block|}
if|if
condition|(
name|versionString
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty version."
argument_list|)
throw|;
block|}
name|this
operator|.
name|versionString
operator|=
name|versionString
expr_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|versionString
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|versionParts
operator|=
operator|new
name|int
index|[
name|toks
operator|.
name|countTokens
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|versionParts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|versionParts
index|[
name|i
index|]
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|toks
operator|.
name|nextToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|CharSequence
condition|)
block|{
name|o
operator|=
operator|new
name|Version
argument_list|(
name|o
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|Version
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can only compare to Versions and Strings, got: "
operator|+
name|o
argument_list|)
throw|;
block|}
name|int
index|[]
name|otherVersion
init|=
operator|(
operator|(
name|Version
operator|)
name|o
operator|)
operator|.
name|versionParts
decl_stmt|;
name|int
name|len
init|=
name|Math
operator|.
name|min
argument_list|(
name|otherVersion
operator|.
name|length
argument_list|,
name|versionParts
operator|.
name|length
argument_list|)
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|int
name|delta
init|=
name|versionParts
index|[
name|i
index|]
operator|-
name|otherVersion
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|delta
operator|!=
literal|0
condition|)
block|{
return|return
name|delta
return|;
block|}
block|}
if|if
condition|(
name|versionParts
operator|.
name|length
operator|<
name|otherVersion
operator|.
name|length
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|versionParts
operator|.
name|length
operator|>
name|otherVersion
operator|.
name|length
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
specifier|public
name|String
name|getVersionString
parameter_list|()
block|{
return|return
name|versionString
return|;
block|}
block|}
end_class

end_unit

