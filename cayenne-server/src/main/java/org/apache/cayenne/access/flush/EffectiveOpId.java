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
package|;
end_package

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
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * Helper value-object class that used to compare operations by "effective" id (i.e. by id snapshot,  * that will include replacement id if any).  * This class is not used directly by Cayenne, it's designed to ease custom implementations.  * @since 4.2  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|public
class|class
name|EffectiveOpId
block|{
specifier|private
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snapshot
decl_stmt|;
specifier|public
name|EffectiveOpId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|id
operator|.
name|getEntityName
argument_list|()
expr_stmt|;
name|this
operator|.
name|snapshot
operator|=
name|id
operator|.
name|getIdSnapshot
argument_list|()
expr_stmt|;
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
return|return
literal|false
return|;
if|if
condition|(
name|snapshot
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EffectiveOpId
name|that
init|=
operator|(
name|EffectiveOpId
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|entityName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|entityName
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|snapshot
operator|.
name|equals
argument_list|(
name|that
operator|.
name|snapshot
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
name|entityName
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
name|snapshot
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

