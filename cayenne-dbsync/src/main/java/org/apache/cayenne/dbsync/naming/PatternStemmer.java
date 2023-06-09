begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|PatternStemmer
implements|implements
name|DbEntityNameStemmer
block|{
specifier|private
name|Pattern
name|pattern
decl_stmt|;
specifier|public
name|PatternStemmer
parameter_list|(
name|String
name|stripPattern
parameter_list|,
name|boolean
name|caseSensitive
parameter_list|)
block|{
name|int
name|flags
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|caseSensitive
condition|)
block|{
name|flags
operator|=
name|flags
operator||
name|Pattern
operator|.
name|CASE_INSENSITIVE
expr_stmt|;
block|}
name|this
operator|.
name|pattern
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|stripPattern
argument_list|,
name|flags
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|stem
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
name|Matcher
name|m
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|dbEntityName
argument_list|)
decl_stmt|;
return|return
name|m
operator|.
name|replaceAll
argument_list|(
literal|""
argument_list|)
return|;
block|}
block|}
end_class

end_unit

