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
name|CayenneException
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
comment|/**  * Used as an observer for DataContext commit operations.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|DataDomainFlushObserver
implements|implements
name|OperationObserver
block|{
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
specifier|public
name|void
name|nextGeneratedDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
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
name|keysIterator
operator|.
name|dataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error reading primary key"
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
literal|"Generated keys only supported for InsertBatchQuery, instead got "
operator|+
name|query
argument_list|)
throw|;
block|}
name|BatchQuery
name|batch
init|=
operator|(
name|BatchQuery
operator|)
name|query
decl_stmt|;
name|ObjectId
name|id
init|=
name|batch
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
operator|||
operator|!
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
comment|// why would this happen?
return|return;
block|}
if|if
condition|(
name|keys
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"One and only one PK row is expected, instead got "
operator|+
name|keys
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|DataRow
name|key
init|=
name|keys
operator|.
name|get
argument_list|(
literal|0
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
comment|// determine DbAttribute name...
comment|// As of now (01/2005) all tested drivers don't provide decent descriptors of
comment|// identity result sets, so a data row will contain garbage labels. Also most
comment|// DBs only support one autogenerated key per table... So here we will have to
comment|// infer the key name and currently will only support a single column...
if|if
condition|(
name|key
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only a single column autogenerated PK is supported. "
operator|+
literal|"Generated key: "
operator|+
name|key
argument_list|)
throw|;
block|}
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
comment|// batch can have generated attributes that are not PKs, e.g. columns with
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
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// I guess we should override any existing value,
comment|// as generated key is the latest thing that exists in the DB.
name|id
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
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|DataRow
argument_list|>
name|dataRows
parameter_list|)
block|{
block|}
specifier|public
name|void
name|nextDataRows
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

