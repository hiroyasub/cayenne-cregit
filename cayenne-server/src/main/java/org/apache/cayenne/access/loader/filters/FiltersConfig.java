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
name|access
operator|.
name|loader
operator|.
name|filters
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|List
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FiltersConfig
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|DbPath
argument_list|>
name|dbPaths
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|DbPath
argument_list|,
name|EntityFilters
argument_list|>
name|filters
decl_stmt|;
specifier|private
name|List
argument_list|<
name|DbPath
argument_list|>
name|pathsForQueries
decl_stmt|;
specifier|public
name|FiltersConfig
parameter_list|(
name|EntityFilters
modifier|...
name|filters
parameter_list|)
block|{
name|this
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|filters
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FiltersConfig
parameter_list|(
name|Collection
argument_list|<
name|EntityFilters
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|dbPaths
operator|=
operator|new
name|LinkedList
argument_list|<
name|DbPath
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|filters
operator|=
operator|new
name|HashMap
argument_list|<
name|DbPath
argument_list|,
name|EntityFilters
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|EntityFilters
name|filter
range|:
name|filters
control|)
block|{
if|if
condition|(
name|filter
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|DbPath
name|path
init|=
name|filter
operator|.
name|getDbPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|dbPaths
operator|.
name|contains
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|filters
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|this
operator|.
name|filters
operator|.
name|get
argument_list|(
name|path
argument_list|)
operator|.
name|join
argument_list|(
name|filter
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|this
operator|.
name|dbPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|this
operator|.
name|filters
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|this
operator|.
name|dbPaths
argument_list|)
expr_stmt|;
block|}
comment|/**      * Used for loading tables and procedures, it's aim avoid unnecessary queries by compacting pairs of      * (Catalog, Schema)      *      * Example:      *<ul>      *<li>"aaa", null</li>      *<li>"aaa", "11"</li>      *<li>"aa", null</li>      *<li>"aa", "a"</li>      *<li>"aa", "aa"</li>      *<li>"aa", "aa"</li>      *</ul>      *      * Should return      *<ul>      *<li>"aa", null</li>      *<li>"aaa", null</li>      *</ul>      * For more examples please see tests.      *      * @return list of pairs (Catalog, Schema) for which getTables and getProcedures should be called      *      **/
specifier|public
name|List
argument_list|<
name|DbPath
argument_list|>
name|pathsForQueries
parameter_list|()
block|{
if|if
condition|(
name|pathsForQueries
operator|!=
literal|null
condition|)
block|{
return|return
name|pathsForQueries
return|;
block|}
name|pathsForQueries
operator|=
operator|new
name|LinkedList
argument_list|<
name|DbPath
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|pathsForQueries
return|;
block|}
name|boolean
name|save
init|=
literal|true
decl_stmt|;
name|String
name|catalog
init|=
literal|null
decl_stmt|;
name|String
name|schema
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DbPath
name|path
range|:
name|dbPaths
control|)
block|{
if|if
condition|(
name|save
operator|||
name|catalog
operator|!=
literal|null
operator|&&
operator|!
name|catalog
operator|.
name|equals
argument_list|(
name|path
operator|.
name|catalog
argument_list|)
condition|)
block|{
name|catalog
operator|=
name|path
operator|.
name|catalog
expr_stmt|;
name|schema
operator|=
literal|null
expr_stmt|;
name|save
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|save
operator|||
name|schema
operator|!=
literal|null
operator|&&
operator|!
name|schema
operator|.
name|equals
argument_list|(
name|path
operator|.
name|schema
argument_list|)
condition|)
block|{
name|schema
operator|=
name|path
operator|.
name|schema
expr_stmt|;
name|save
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|save
condition|)
block|{
name|save
operator|=
literal|false
expr_stmt|;
name|pathsForQueries
operator|.
name|add
argument_list|(
operator|new
name|DbPath
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|pathsForQueries
return|;
block|}
comment|/**      * TODO comment      *      * Return filters that applicable for path (filters which path covering path passed in method)      * */
specifier|public
name|EntityFilters
name|filter
parameter_list|(
name|DbPath
name|path
parameter_list|)
block|{
name|EntityFilters
name|res
init|=
operator|new
name|EntityFilters
argument_list|(
name|path
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|DbPath
argument_list|,
name|EntityFilters
argument_list|>
name|entry
range|:
name|filters
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|isCover
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|res
operator|=
name|res
operator|.
name|join
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|Map
argument_list|<
name|DbPath
argument_list|,
name|EntityFilters
argument_list|>
name|filters
init|=
operator|(
operator|(
name|FiltersConfig
operator|)
name|o
operator|)
operator|.
name|filters
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|filters
operator|.
name|size
argument_list|()
operator|!=
name|filters
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|DbPath
argument_list|,
name|EntityFilters
argument_list|>
name|entry
range|:
name|this
operator|.
name|filters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|EntityFilters
name|f
init|=
name|filters
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
operator|||
operator|!
name|f
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
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
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|res
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|DbPath
name|dbPath
range|:
name|dbPaths
control|)
block|{
name|res
operator|.
name|append
argument_list|(
literal|"    "
argument_list|)
operator|.
name|append
argument_list|(
name|dbPath
argument_list|)
operator|.
name|append
argument_list|(
literal|" -> "
argument_list|)
operator|.
name|append
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|dbPath
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
operator|.
name|toString
argument_list|()
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
name|filters
operator|.
name|hashCode
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|DbPath
argument_list|>
name|getDbPaths
parameter_list|()
block|{
return|return
name|dbPaths
return|;
block|}
block|}
end_class

end_unit

