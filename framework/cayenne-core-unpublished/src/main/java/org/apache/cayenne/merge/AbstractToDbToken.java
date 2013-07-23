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
name|merge
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|DbAdapter
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
name|log
operator|.
name|JdbcEventLogger
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbEntity
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
name|validation
operator|.
name|SimpleValidationFailure
import|;
end_import

begin_comment
comment|/**  * Common abstract superclass for all {@link MergerToken}s going from the model to the  * database.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractToDbToken
implements|implements
name|MergerToken
implements|,
name|Comparable
argument_list|<
name|MergerToken
argument_list|>
block|{
specifier|public
specifier|final
name|MergeDirection
name|getDirection
parameter_list|()
block|{
return|return
name|MergeDirection
operator|.
name|TO_DB
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
for|for
control|(
name|String
name|sql
range|:
name|createSql
argument_list|(
name|mergerContext
operator|.
name|getAdapter
argument_list|()
argument_list|)
control|)
block|{
name|executeSql
argument_list|(
name|mergerContext
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|executeSql
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
name|Statement
name|st
init|=
literal|null
decl_stmt|;
name|JdbcEventLogger
name|logger
init|=
name|mergerContext
operator|.
name|getDataNode
argument_list|()
operator|.
name|getJdbcEventLogger
argument_list|()
decl_stmt|;
try|try
block|{
name|logger
operator|.
name|log
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|conn
operator|=
name|mergerContext
operator|.
name|getDataNode
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|st
operator|=
name|conn
operator|.
name|createStatement
argument_list|()
expr_stmt|;
name|st
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|addFailure
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|sql
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|logQueryError
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|st
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|st
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
block|}
block|}
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|ts
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getTokenName
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getTokenValue
argument_list|()
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|ts
operator|.
name|append
argument_list|(
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ts
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|abstract
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
function_decl|;
specifier|abstract
specifier|static
class|class
name|Entity
extends|extends
name|AbstractToDbToken
block|{
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|public
name|Entity
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
specifier|public
name|DbEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|MergerToken
name|o
parameter_list|)
block|{
comment|// default order as tokens are created
return|return
literal|0
return|;
block|}
block|}
specifier|abstract
specifier|static
class|class
name|EntityAndColumn
extends|extends
name|Entity
block|{
specifier|private
name|DbAttribute
name|column
decl_stmt|;
specifier|public
name|EntityAndColumn
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|column
operator|=
name|column
expr_stmt|;
block|}
specifier|public
name|DbAttribute
name|getColumn
parameter_list|()
block|{
return|return
name|column
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
