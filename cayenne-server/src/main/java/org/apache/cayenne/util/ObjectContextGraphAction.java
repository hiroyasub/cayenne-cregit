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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ObjectContext
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
name|PersistenceState
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
name|graph
operator|.
name|ArcId
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
name|ClassDescriptor
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
name|PropertyDescriptor
import|;
end_import

begin_comment
comment|/**  * A base implementation of a helper class to handle  * {@link ObjectContext#propertyChanged(org.apache.cayenne.Persistent, String, Object, Object)}  * processing on behalf of an ObjectContext.  *   * @since 3.0  * TODO: make this non-public!   */
end_comment

begin_class
specifier|public
class|class
name|ObjectContextGraphAction
implements|implements
name|Serializable
block|{
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|ObjectContextGraphAction
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Handles property change in a Persistent object, routing to either      * {@link #handleArcPropertyChange(Persistent, ArcProperty, Object, Object)} or      * {@link #handleSimplePropertyChange(Persistent, String, Object, Object)}.      */
specifier|public
name|void
name|handlePropertyChange
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
comment|// translate ObjectContext generic property change callback to GraphManager terms
comment|// (simple properties vs. relationships)
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|PropertyDescriptor
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|instanceof
name|ArcProperty
condition|)
block|{
name|handleArcPropertyChange
argument_list|(
name|object
argument_list|,
operator|(
name|ArcProperty
operator|)
name|property
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handleSimplePropertyChange
argument_list|(
name|object
argument_list|,
name|propertyName
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
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
name|oldValue
operator|!=
name|newValue
condition|)
block|{
name|markAsDirty
argument_list|(
name|object
argument_list|)
expr_stmt|;
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
operator|new
name|ArcId
argument_list|(
name|property
argument_list|)
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
operator|new
name|ArcId
argument_list|(
name|property
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Changes object state to MODIFIED if needed, returning true if the change has      * occurred, false if not.      */
specifier|protected
name|boolean
name|markAsDirty
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
operator|==
name|PersistenceState
operator|.
name|COMMITTED
condition|)
block|{
name|object
operator|.
name|setPersistenceState
argument_list|(
name|PersistenceState
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

