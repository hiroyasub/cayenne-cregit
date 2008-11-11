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
name|graph
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A base implementation of GraphManager that stores graph nodes keyed by their ids.  *<h3>Tracking Object Changes</h3>  *<p>  * Registered objects may choose to notify GraphMap of their changes by using callback  * methods defined in GraphChangeHandler interface. GraphMap itself implements as noops,  * leaving it up to subclasses to handle object updates.  *</p>  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|GraphMap
implements|implements
name|GraphManager
block|{
specifier|protected
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|nodes
decl_stmt|;
comment|/**      * Creates a new GraphMap.      */
specifier|public
name|GraphMap
parameter_list|()
block|{
name|this
operator|.
name|nodes
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|// *** GraphMap methods
comment|/**      * Returns an immutable collection of registered nodes.      */
specifier|public
name|Collection
argument_list|<
name|Object
argument_list|>
name|registeredNodes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|nodes
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|Object
name|getNode
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
return|return
name|nodes
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
return|;
block|}
specifier|public
specifier|synchronized
name|void
name|registerNode
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|nodeObject
parameter_list|)
block|{
name|nodes
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|nodeObject
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|Object
name|unregisterNode
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
return|return
name|nodes
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
return|;
block|}
comment|// *** methods for tracking local changes declared in GraphChangeHandler interface
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
comment|// noop
block|}
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

