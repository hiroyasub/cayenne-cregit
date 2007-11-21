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
name|reflect
operator|.
name|ArcProperty
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
name|reflect
operator|.
name|AttributeProperty
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
name|reflect
operator|.
name|PropertyVisitor
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
name|reflect
operator|.
name|ToManyProperty
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
name|reflect
operator|.
name|ToOneProperty
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
name|ObjectContextGraphAction
import|;
end_import

begin_comment
comment|/**  * An action object that processes graph change calls from Persistent object. It handles  * GraphManager notifications and bi-directional graph consistency.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|CayenneContextGraphAction
extends|extends
name|ObjectContextGraphAction
block|{
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
name|arcChangeInProcess
decl_stmt|;
name|CayenneContextGraphAction
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|arcChangeInProcess
operator|=
operator|new
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|arcChangeInProcess
operator|.
name|set
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|handleArcPropertyChange
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|ArcProperty
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
if|if
condition|(
name|isArchChangeInProcess
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// prevent reverse actions down the stack
name|setArcChangeInProcess
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|oldValue
operator|instanceof
name|Persistent
condition|)
block|{
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|arcDeleted
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
operator|(
operator|(
name|Persistent
operator|)
name|oldValue
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|unsetReverse
argument_list|(
name|property
argument_list|,
name|object
argument_list|,
operator|(
name|Persistent
operator|)
name|oldValue
argument_list|)
expr_stmt|;
name|markAsDirty
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newValue
operator|instanceof
name|Persistent
condition|)
block|{
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|arcCreated
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
operator|(
operator|(
name|Persistent
operator|)
name|newValue
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setReverse
argument_list|(
name|property
argument_list|,
name|object
argument_list|,
operator|(
name|Persistent
operator|)
name|newValue
argument_list|)
expr_stmt|;
name|markAsDirty
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|setArcChangeInProcess
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|handleSimplePropertyChange
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|nodePropertyChanged
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
name|markAsDirty
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if the current thread is in the process of changing the arc property.      * This method is used to prevent cycles when setting reverse relationships.      */
name|boolean
name|isArchChangeInProcess
parameter_list|()
block|{
return|return
name|arcChangeInProcess
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Sets the flag indicating whether the current thread is in the process of changing      * the arc property. This method is used to prevent cycles when setting reverse      * relationships.      */
name|void
name|setArcChangeInProcess
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|arcChangeInProcess
operator|.
name|set
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setReverse
parameter_list|(
name|ArcProperty
name|property
parameter_list|,
specifier|final
name|Persistent
name|sourceObject
parameter_list|,
specifier|final
name|Persistent
name|targetObject
parameter_list|)
block|{
name|ArcProperty
name|reverseArc
init|=
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseArc
operator|!=
literal|null
condition|)
block|{
name|reverseArc
operator|.
name|visit
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|addTarget
argument_list|(
name|targetObject
argument_list|,
name|sourceObject
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|property
operator|.
name|setTarget
argument_list|(
name|targetObject
argument_list|,
name|sourceObject
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|arcCreated
argument_list|(
name|targetObject
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sourceObject
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|reverseArc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|markAsDirty
argument_list|(
name|targetObject
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|unsetReverse
parameter_list|(
name|ArcProperty
name|property
parameter_list|,
name|Persistent
name|sourceObject
parameter_list|,
name|Persistent
name|targetObject
parameter_list|)
block|{
name|ArcProperty
name|reverseArc
init|=
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseArc
operator|!=
literal|null
condition|)
block|{
name|reverseArc
operator|.
name|writePropertyDirectly
argument_list|(
name|targetObject
argument_list|,
name|sourceObject
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|arcDeleted
argument_list|(
name|targetObject
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|sourceObject
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|reverseArc
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|markAsDirty
argument_list|(
name|targetObject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

