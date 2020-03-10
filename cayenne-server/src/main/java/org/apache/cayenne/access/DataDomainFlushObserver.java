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
name|access
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
name|DataRow
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
name|ObjectId
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
name|ResultIterator
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
name|query
operator|.
name|BatchQuery
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
name|InsertBatchQuery
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
name|Query
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Used as an observer for DataContext commit operations.  *   * @since 1.2  * @deprecated since 4.2 as part of deprecated {@link LegacyDataDomainFlushAction}  */
end_comment

begin_class
annotation|@
name|Deprecated
class|class
name|DataDomainFlushObserver
implements|implements
name|OperationObserver
block|{
comment|/**      * @since 3.1      */
specifier|private
name|JdbcEventLogger
name|logger
decl_stmt|;
name|DataDomainFlushObserver
parameter_list|(
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextQueryException
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Raising from query exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Raising from underlyingQueryEngine exception."
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
throw|;
block|}
comment|/**      * Processes generated keys.      *       * @since 1.2      */
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|void
name|nextGeneratedRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|keysIterator
parameter_list|,
name|List
argument_list|<
name|ObjectId
argument_list|>
name|idsToUpdate
parameter_list|)
block|{
comment|// read and close the iterator before doing anything else
name|List
argument_list|<
name|DataRow
argument_list|>
name|keys
decl_stmt|;
try|try
block|{
name|keys
operator|=
operator|(
name|List
argument_list|<
name|DataRow
argument_list|>
operator|)
name|keysIterator
operator|.
name|allRows
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|keysIterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|query
operator|instanceof
name|InsertBatchQuery
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Generated keys only supported for InsertBatchQuery, instead got %s"
argument_list|,
name|query
argument_list|)
throw|;
block|}
if|if
condition|(
name|keys
operator|.
name|size
argument_list|()
operator|!=
name|idsToUpdate
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Mismatching number of generated PKs: expected %d, instead got %d"
argument_list|,
name|idsToUpdate
operator|.
name|size
argument_list|()
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|keys
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|DataRow
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// empty key?
if|if
condition|(
name|key
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Empty key generated."
argument_list|)
throw|;
block|}
name|ObjectId
name|idToUpdate
init|=
name|idsToUpdate
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|idToUpdate
operator|==
literal|null
operator|||
operator|!
name|idToUpdate
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
comment|// why would this happen?
return|return;
block|}
name|BatchQuery
name|batch
init|=
operator|(
name|BatchQuery
operator|)
name|query
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|batch
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getGeneratedAttributes
argument_list|()
control|)
block|{
comment|// batch can have generated attributes that are not PKs, e.g.
comment|// columns with
comment|// DB DEFAULT values. Ignore those.
if|if
condition|(
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|key
operator|.
name|get
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// As of now (01/2005) many tested drivers don't provide decent
comment|// descriptors of
comment|// identity result sets, so a data row may contain garbage labels.
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|value
operator|=
name|key
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
comment|// Log the generated PK
name|logger
operator|.
name|logGeneratedKey
argument_list|(
name|attribute
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// I guess we should override any existing value,
comment|// as generated key is the latest thing that exists in the DB.
name|idToUpdate
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|public
name|void
name|setJdbcEventLogger
parameter_list|(
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
specifier|public
name|JdbcEventLogger
name|getJdbcEventLogger
parameter_list|()
block|{
return|return
name|this
operator|.
name|logger
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextBatchCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
index|[]
name|resultCount
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
name|resultCount
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|dataRows
parameter_list|)
block|{
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"'nextDataRows(Query,ResultIterator)' is unsupported (and unexpected) on commit."
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

