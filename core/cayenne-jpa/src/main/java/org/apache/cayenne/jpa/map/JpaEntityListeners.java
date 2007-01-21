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
name|jpa
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|TreeNodeChild
import|;
end_import

begin_comment
comment|/**  * A collection of entity listener descriptors.  *   * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// andrus: I'd rather we flatten this object into JpaEntity, but since we have to follow
end_comment

begin_comment
comment|// the schema structure, we need this meaningless object.
end_comment

begin_class
specifier|public
class|class
name|JpaEntityListeners
block|{
specifier|protected
name|Collection
argument_list|<
name|JpaEntityListener
argument_list|>
name|entityListeners
decl_stmt|;
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaEntityListener
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaEntityListener
argument_list|>
name|getEntityListeners
parameter_list|()
block|{
if|if
condition|(
name|entityListeners
operator|==
literal|null
condition|)
block|{
name|entityListeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaEntityListener
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|entityListeners
return|;
block|}
block|}
end_class

end_unit

