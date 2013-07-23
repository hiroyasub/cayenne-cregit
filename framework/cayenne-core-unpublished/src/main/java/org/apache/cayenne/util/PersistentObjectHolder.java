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
name|util
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
name|FaultFailureException
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
name|ValueHolder
import|;
end_import

begin_comment
comment|/**  * A ValueHolder implementation that holds a single Persistent object related to an object  * used to initialize PersistentObjectHolder. Value is resolved on first access.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|PersistentObjectHolder
extends|extends
name|RelationshipFault
implements|implements
name|ValueHolder
block|{
specifier|protected
name|boolean
name|fault
decl_stmt|;
specifier|protected
name|Object
name|value
decl_stmt|;
comment|// exists for the benefit of manual serialization schemes such as the one in Hessian.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|PersistentObjectHolder
parameter_list|()
block|{
name|fault
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|PersistentObjectHolder
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|,
name|String
name|relationshipName
parameter_list|)
block|{
name|super
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|)
expr_stmt|;
name|fault
operator|=
operator|!
name|isTransientParent
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns true if this holder is not resolved, meaning its object is not yet known.      */
specifier|public
name|boolean
name|isFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
specifier|public
name|void
name|invalidate
parameter_list|()
block|{
name|fault
operator|=
literal|true
expr_stmt|;
name|value
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Returns a value resolving it via a query on the first call to this method.      */
specifier|public
name|Object
name|getValue
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|fault
condition|)
block|{
name|resolve
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
specifier|public
name|Object
name|getValueDirectly
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
return|return
name|value
return|;
block|}
comment|/**      * Sets an object value, marking this ValueHolder as resolved.      */
specifier|public
specifier|synchronized
name|Object
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|fault
condition|)
block|{
name|resolve
argument_list|()
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|setValueDirectly
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
name|value
operator|&&
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|.
name|propertyChanged
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldValue
operator|instanceof
name|Persistent
condition|)
block|{
name|Util
operator|.
name|unsetReverse
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
operator|(
name|Persistent
operator|)
name|oldValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|Persistent
condition|)
block|{
name|Util
operator|.
name|setReverse
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|,
operator|(
name|Persistent
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|oldValue
return|;
block|}
specifier|public
name|Object
name|setValueDirectly
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
comment|// must obtain the value from the local context
if|if
condition|(
name|value
operator|instanceof
name|Persistent
condition|)
block|{
name|value
operator|=
name|connect
argument_list|(
operator|(
name|Persistent
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
name|Object
name|oldValue
init|=
name|this
operator|.
name|value
decl_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|fault
operator|=
literal|false
expr_stmt|;
return|return
name|oldValue
return|;
block|}
comment|/**      * Returns an object that should be stored as a value in this ValueHolder, ensuring      * that it is registered with the same context.      */
specifier|protected
name|Object
name|connect
parameter_list|(
name|Persistent
name|persistent
parameter_list|)
block|{
if|if
condition|(
name|persistent
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|relationshipOwner
operator|.
name|getObjectContext
argument_list|()
operator|!=
name|persistent
operator|.
name|getObjectContext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Cannot set object as destination of relationship "
operator|+
name|relationshipName
operator|+
literal|" because it is in a different ObjectContext"
argument_list|)
throw|;
block|}
return|return
name|persistent
return|;
block|}
comment|/**      * Reads an object from the database.      */
specifier|protected
specifier|synchronized
name|void
name|resolve
parameter_list|()
block|{
if|if
condition|(
operator|!
name|fault
condition|)
block|{
return|return;
block|}
comment|// TODO: should build a HOLLOW object instead of running a query if relationship
comment|// is required and thus expected to be not null.
name|List
name|objects
init|=
name|resolveFromDB
argument_list|()
decl_stmt|;
if|if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|this
operator|.
name|value
operator|=
literal|null
expr_stmt|;
block|}
if|else if
condition|(
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|this
operator|.
name|value
operator|=
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|FaultFailureException
argument_list|(
literal|"Expected either no objects or a single object, instead fault query resolved to "
operator|+
name|objects
operator|.
name|size
argument_list|()
operator|+
literal|" objects."
argument_list|)
throw|;
block|}
name|fault
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|mergeLocalChanges
parameter_list|(
name|List
name|resolved
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit
