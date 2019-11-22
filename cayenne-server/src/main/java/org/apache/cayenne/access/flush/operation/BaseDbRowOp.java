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
operator|.
name|flush
operator|.
name|operation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseDbRowOp
implements|implements
name|DbRowOp
block|{
specifier|protected
specifier|final
name|Persistent
name|object
decl_stmt|;
specifier|protected
specifier|final
name|DbEntity
name|entity
decl_stmt|;
comment|// Can be ObjEntity id or a DB row id for flattened rows
specifier|protected
name|ObjectId
name|changeId
decl_stmt|;
specifier|protected
name|BaseDbRowOp
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|ObjectId
name|id
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|this
operator|.
name|entity
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DbEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectId
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
annotation|@
name|Override
specifier|public
name|Persistent
name|getObject
parameter_list|()
block|{
return|return
name|object
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
return|return
literal|true
return|;
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|DbRowOp
operator|)
condition|)
return|return
literal|false
return|;
name|DbRowOp
name|other
init|=
operator|(
name|DbRowOp
operator|)
name|o
decl_stmt|;
return|return
name|changeId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|getChangeId
argument_list|()
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
name|changeId
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|changeId
return|;
block|}
block|}
end_class

end_unit

