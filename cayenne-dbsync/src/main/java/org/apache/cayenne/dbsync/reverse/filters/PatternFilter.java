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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|PatternFilter
block|{
specifier|public
specifier|static
specifier|final
name|PatternFilter
name|INCLUDE_EVERYTHING
init|=
operator|new
name|PatternFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|obj
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|protected
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|)
block|{
return|return
name|res
operator|.
name|append
argument_list|(
literal|"ALL"
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|PatternFilter
name|INCLUDE_NOTHING
init|=
operator|new
name|PatternFilter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|obj
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|)
block|{
return|return
name|res
operator|.
name|append
argument_list|(
literal|"NONE"
argument_list|)
return|;
block|}
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Pattern
argument_list|>
name|PATTERN_COMPARATOR
init|=
operator|new
name|Comparator
argument_list|<
name|Pattern
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Pattern
name|o1
parameter_list|,
name|Pattern
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|!=
literal|null
operator|&&
name|o2
operator|!=
literal|null
condition|)
block|{
return|return
name|o1
operator|.
name|pattern
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|pattern
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
block|}
decl_stmt|;
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|Pattern
argument_list|>
name|includes
decl_stmt|;
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|Pattern
argument_list|>
name|excludes
decl_stmt|;
specifier|public
name|PatternFilter
parameter_list|()
block|{
name|this
operator|.
name|includes
operator|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|PATTERN_COMPARATOR
argument_list|)
expr_stmt|;
name|this
operator|.
name|excludes
operator|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|PATTERN_COMPARATOR
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SortedSet
argument_list|<
name|Pattern
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|PatternFilter
name|include
parameter_list|(
name|Pattern
name|p
parameter_list|)
block|{
name|includes
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|PatternFilter
name|exclude
parameter_list|(
name|Pattern
name|p
parameter_list|)
block|{
name|excludes
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
specifier|static
name|Pattern
name|pattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Pattern
operator|.
name|compile
argument_list|(
name|pattern
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
return|;
block|}
specifier|public
name|PatternFilter
name|include
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|include
argument_list|(
name|pattern
argument_list|(
name|p
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|PatternFilter
name|exclude
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|exclude
argument_list|(
name|pattern
argument_list|(
name|p
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|obj
parameter_list|)
block|{
name|boolean
name|include
init|=
name|includes
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
for|for
control|(
name|Pattern
name|p
range|:
name|includes
control|)
block|{
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|matcher
argument_list|(
name|obj
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|include
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|include
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Pattern
name|p
range|:
name|excludes
control|)
block|{
if|if
condition|(
name|p
operator|.
name|matcher
argument_list|(
name|obj
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|PatternFilter
name|filter
init|=
operator|(
name|PatternFilter
operator|)
name|o
decl_stmt|;
return|return
name|includes
operator|.
name|equals
argument_list|(
name|filter
operator|.
name|includes
argument_list|)
operator|&&
name|excludes
operator|.
name|equals
argument_list|(
name|filter
operator|.
name|excludes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|includes
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|protected
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|)
block|{
if|if
condition|(
name|includes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Do nothing.
block|}
if|else if
condition|(
name|includes
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|Util
operator|.
name|join
argument_list|(
name|includes
argument_list|,
literal|" OR "
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|.
name|append
argument_list|(
name|includes
operator|.
name|first
argument_list|()
operator|.
name|pattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|excludes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|" AND NOT ("
argument_list|)
operator|.
name|append
argument_list|(
name|Util
operator|.
name|join
argument_list|(
name|includes
argument_list|,
literal|" OR "
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|includes
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludes
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

