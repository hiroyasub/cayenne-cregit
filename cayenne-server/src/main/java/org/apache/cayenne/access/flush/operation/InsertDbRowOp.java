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
class|class
name|InsertDbRowOp
extends|extends
name|BaseDbRowOp
implements|implements
name|DbRowOpWithValues
block|{
specifier|protected
specifier|final
name|Values
name|values
decl_stmt|;
specifier|public
name|InsertDbRowOp
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
name|super
argument_list|(
name|object
argument_list|,
name|entity
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|values
operator|=
operator|new
name|Values
argument_list|(
name|this
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|accept
parameter_list|(
name|DbRowOpVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitInsert
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Values
name|getValues
parameter_list|()
block|{
return|return
name|values
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
comment|// TODO: here go troubles with transitivity
comment|//   insert = update, update = delete, delete != insert
comment|//   though we need this only to store in a hash map, so it should be ok...
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|DbRowOpWithValues
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isSameBatch
parameter_list|(
name|DbRowOp
name|rowOp
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|rowOp
operator|instanceof
name|InsertDbRowOp
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|rowOp
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
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
literal|"insert "
operator|+
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

