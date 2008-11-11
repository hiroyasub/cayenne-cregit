begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2006 The Apache Software Foundation  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|profile
operator|.
name|cases
package|;
end_package

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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|CayenneRuntimeException
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
name|access
operator|.
name|DataContext
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
name|profile
operator|.
name|AbstractCase
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
name|profile
operator|.
name|entity
operator|.
name|Entity1
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
name|query
operator|.
name|SQLTemplate
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
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * Does a random insert/delete/update based on the current object count.  *   */
end_comment

begin_class
specifier|public
class|class
name|RandomOperationCase
extends|extends
name|AbstractCase
block|{
specifier|static
specifier|final
name|int
name|MEDIAN_COUNT
init|=
literal|500
decl_stmt|;
specifier|static
specifier|final
name|int
name|RANGE
init|=
literal|50
decl_stmt|;
specifier|protected
name|Random
name|r
init|=
operator|new
name|Random
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
specifier|protected
name|int
name|minObjects
init|=
name|MEDIAN_COUNT
operator|-
operator|(
name|RANGE
operator|/
literal|2
operator|)
decl_stmt|;
specifier|protected
name|int
name|maxObjects
init|=
name|MEDIAN_COUNT
operator|+
operator|(
name|RANGE
operator|/
literal|2
operator|)
decl_stmt|;
specifier|protected
name|void
name|doRequest
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
block|{
if|if
condition|(
name|r
operator|.
name|nextBoolean
argument_list|()
condition|)
block|{
name|doSelect
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// do update...
name|String
name|table
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
argument_list|(
name|Entity1
operator|.
name|class
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|SQLTemplate
name|count
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Entity1
operator|.
name|class
argument_list|,
literal|"SELECT #result('count(*)' 'int' 'C') FROM "
operator|+
name|table
argument_list|)
decl_stmt|;
name|count
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
name|row
init|=
operator|(
name|Map
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|count
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|c
init|=
operator|(
operator|(
name|Number
operator|)
name|row
operator|.
name|get
argument_list|(
literal|"C"
argument_list|)
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
name|minObjects
condition|)
block|{
name|doInsert
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|c
operator|>
name|maxObjects
condition|)
block|{
name|doDelete
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|r
operator|.
name|nextInt
argument_list|(
literal|3
argument_list|)
condition|)
block|{
case|case
literal|0
case|:
name|doInsert
argument_list|(
name|context
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|doUpdate
argument_list|(
name|context
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|doDelete
argument_list|(
name|context
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doInsert
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|Entity1
name|e
init|=
operator|(
name|Entity1
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Entity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|setName
argument_list|(
literal|"X"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doUpdate
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|Entity1
name|e
init|=
name|getRandomObject
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|e
operator|.
name|setName
argument_list|(
literal|"Y"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doDelete
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|Entity1
name|e
init|=
name|getRandomObject
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|deleteObject
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|doSelect
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Entity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setFetchLimit
argument_list|(
literal|300
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Entity1
name|getRandomObject
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Entity1
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|setPageSize
argument_list|(
literal|3
argument_list|)
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
literal|20
condition|;
name|i
operator|++
control|)
block|{
name|List
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
if|if
condition|(
name|allObjects
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|x
init|=
name|r
operator|.
name|nextInt
argument_list|(
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|Entity1
operator|)
name|allObjects
operator|.
name|get
argument_list|(
name|x
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
comment|// this can happen due to concurrency isses - other threads may have
comment|// deleted this page
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

