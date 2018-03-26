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
comment|/**  * TableFilter contain at least one IncludeTable always.  */
end_comment

begin_class
specifier|public
class|class
name|TableFilter
block|{
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|IncludeTableFilter
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
comment|/**      * Includes can contain only one include table      *      * @param includes      * @param excludes      */
specifier|public
name|TableFilter
parameter_list|(
name|SortedSet
argument_list|<
name|IncludeTableFilter
argument_list|>
name|includes
parameter_list|,
name|SortedSet
argument_list|<
name|Pattern
argument_list|>
name|excludes
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TableFilter should contain at least one IncludeTableFilter always "
operator|+
literal|"and it is builder responsibility. If you need table filter without includes, use EmptyTableFilter"
argument_list|)
throw|;
block|}
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
name|this
operator|.
name|excludes
operator|=
name|excludes
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIncludeTable
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|PatternFilter
name|columnFilter
init|=
name|getIncludeTableColumnFilter
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
return|return
name|columnFilter
operator|!=
literal|null
return|;
block|}
comment|/**      * Return filter for columns in case we should take this table      *      * @param tableName      * @return      */
specifier|public
name|PatternFilter
name|getIncludeTableColumnFilter
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|IncludeTableFilter
name|include
init|=
literal|null
decl_stmt|;
for|for
control|(
name|IncludeTableFilter
name|p
range|:
name|includes
control|)
block|{
if|if
condition|(
name|p
operator|.
name|pattern
operator|==
literal|null
operator|||
name|p
operator|.
name|pattern
operator|.
name|matcher
argument_list|(
name|tableName
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
name|include
operator|=
name|p
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|include
operator|==
literal|null
condition|)
block|{
return|return
literal|null
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
name|tableName
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
name|include
operator|.
name|columnsFilter
return|;
block|}
specifier|public
name|SortedSet
argument_list|<
name|IncludeTableFilter
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
specifier|static
name|TableFilter
name|include
parameter_list|(
name|String
name|tablePattern
parameter_list|)
block|{
name|TreeSet
argument_list|<
name|IncludeTableFilter
argument_list|>
name|includes
init|=
operator|new
name|TreeSet
argument_list|<
name|IncludeTableFilter
argument_list|>
argument_list|()
decl_stmt|;
name|includes
operator|.
name|add
argument_list|(
operator|new
name|IncludeTableFilter
argument_list|(
name|tablePattern
operator|==
literal|null
condition|?
literal|null
else|:
name|tablePattern
operator|.
name|replaceAll
argument_list|(
literal|"%"
argument_list|,
literal|".*"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|TableFilter
argument_list|(
name|includes
argument_list|,
operator|new
name|TreeSet
argument_list|<
name|Pattern
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|TableFilter
name|everything
parameter_list|()
block|{
name|TreeSet
argument_list|<
name|IncludeTableFilter
argument_list|>
name|includes
init|=
operator|new
name|TreeSet
argument_list|<
name|IncludeTableFilter
argument_list|>
argument_list|()
decl_stmt|;
name|includes
operator|.
name|add
argument_list|(
operator|new
name|IncludeTableFilter
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|TableFilter
argument_list|(
name|includes
argument_list|,
operator|new
name|TreeSet
argument_list|<
name|Pattern
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|res
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"Tables: "
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|IncludeTableFilter
name|include
range|:
name|includes
control|)
block|{
name|include
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
operator|+
literal|"  "
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
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"  "
argument_list|)
operator|.
name|append
argument_list|(
name|Util
operator|.
name|join
argument_list|(
name|excludes
argument_list|,
literal|" OR "
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|res
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
operator|!
operator|(
name|o
operator|instanceof
name|TableFilter
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|TableFilter
name|that
init|=
operator|(
name|TableFilter
operator|)
name|o
decl_stmt|;
return|return
name|excludes
operator|.
name|equals
argument_list|(
name|that
operator|.
name|excludes
argument_list|)
operator|&&
name|includes
operator|.
name|equals
argument_list|(
name|that
operator|.
name|includes
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
name|int
name|result
init|=
name|includes
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|excludes
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

