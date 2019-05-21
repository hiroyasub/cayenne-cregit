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
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|Persistent
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|query
operator|.
name|QueryMetadata
import|;
end_import

begin_comment
comment|/**  * A paginated list that implements a strategy for retrieval of entities with a single PK  * column. It is much more memory-efficient compared to the superclass.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|SimpleIdIncrementalFaultList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|IncrementalFaultList
argument_list|<
name|E
argument_list|>
block|{
specifier|protected
name|DbAttribute
name|pk
decl_stmt|;
name|SimpleIdIncrementalFaultList
parameter_list|(
name|DataContext
name|dataContext
parameter_list|,
name|Query
name|query
parameter_list|,
name|int
name|maxFetchSize
parameter_list|)
block|{
name|super
argument_list|(
name|dataContext
argument_list|,
name|query
argument_list|,
name|maxFetchSize
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pks
init|=
name|rootEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pks
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected a single column primary key, instead got "
operator|+
name|pks
operator|.
name|size
argument_list|()
operator|+
literal|". ObjEntity: "
operator|+
name|rootEntity
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|pk
operator|=
name|pks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
name|IncrementalFaultList
argument_list|<
name|E
argument_list|>
operator|.
name|IncrementalListHelper
name|createHelper
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
if|if
condition|(
name|metadata
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
return|return
operator|new
name|SingleIdDataRowListHelper
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|SingleIdPersistentListHelper
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Expression
name|buildIdQualifier
parameter_list|(
name|Object
name|id
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|,
name|id
argument_list|)
return|;
block|}
class|class
name|SingleIdPersistentListHelper
extends|extends
name|IncrementalFaultList
argument_list|<
name|E
argument_list|>
operator|.
name|PersistentListHelper
block|{
annotation|@
name|Override
name|boolean
name|objectsAreEqual
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
if|if
condition|(
name|objectInTheList
operator|instanceof
name|Persistent
condition|)
block|{
comment|// due to object uniquing this should be sufficient
return|return
name|object
operator|==
name|objectInTheList
return|;
block|}
else|else
block|{
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idSnapshot
init|=
name|persistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
return|return
name|idSnapshot
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|objectInTheList
operator|.
name|equals
argument_list|(
name|idSnapshot
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
name|boolean
name|replacesObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
if|if
condition|(
name|objectInTheList
operator|instanceof
name|Persistent
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idSnapshot
init|=
name|persistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
return|return
name|idSnapshot
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|objectInTheList
operator|.
name|equals
argument_list|(
name|idSnapshot
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
class|class
name|SingleIdDataRowListHelper
extends|extends
name|IncrementalFaultList
argument_list|<
name|E
argument_list|>
operator|.
name|DataRowListHelper
block|{
annotation|@
name|Override
name|boolean
name|objectsAreEqual
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
if|if
condition|(
name|objectInTheList
operator|instanceof
name|Map
condition|)
block|{
return|return
name|super
operator|.
name|objectsAreEqual
argument_list|(
name|object
argument_list|,
name|objectInTheList
argument_list|)
return|;
block|}
if|if
condition|(
name|object
operator|==
literal|null
operator|&&
name|objectInTheList
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|!=
literal|null
operator|&&
name|objectInTheList
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|objectInTheList
operator|.
name|equals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
name|boolean
name|replacesObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
if|if
condition|(
name|objectInTheList
operator|instanceof
name|Map
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|objectInTheList
operator|.
name|equals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

