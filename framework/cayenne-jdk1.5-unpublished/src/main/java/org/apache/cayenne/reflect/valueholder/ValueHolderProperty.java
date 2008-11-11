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
name|reflect
operator|.
name|valueholder
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
name|Accessor
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
name|BaseToOneProperty
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
name|PropertyException
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
name|PersistentObjectHolder
import|;
end_import

begin_comment
comment|/**  * Provides access to a property implemented as a {@link ValueHolder} Field. This  * implementation hides the fact of the ValueHolder existence. I.e. it never returns it  * from 'readPropertyDirectly', returning held value instead.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|ValueHolderProperty
extends|extends
name|BaseToOneProperty
block|{
name|ValueHolderProperty
parameter_list|(
name|ClassDescriptor
name|owner
parameter_list|,
name|ClassDescriptor
name|targetDescriptor
parameter_list|,
name|Accessor
name|accessor
parameter_list|,
name|String
name|reverseName
parameter_list|)
block|{
name|super
argument_list|(
name|owner
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if a property ValueHolder is not initialized or is itself a fault.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isFault
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|ValueHolder
name|holder
init|=
operator|(
name|ValueHolder
operator|)
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
decl_stmt|;
return|return
name|holder
operator|==
literal|null
operator|||
name|holder
operator|.
name|isFault
argument_list|()
return|;
block|}
specifier|public
name|void
name|invalidate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|ValueHolder
name|holder
init|=
operator|(
name|ValueHolder
operator|)
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|!=
literal|null
operator|&&
operator|!
name|holder
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|holder
operator|.
name|invalidate
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readPropertyDirectly
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
name|ValueHolder
name|holder
init|=
operator|(
name|ValueHolder
operator|)
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
decl_stmt|;
comment|// TODO: Andrus, 2/9/2006 ValueHolder will resolve an object in a call to
comment|// 'getValue'; this is inconsistent with 'readPropertyDirectly' contract
return|return
operator|(
name|holder
operator|!=
literal|null
operator|)
condition|?
name|holder
operator|.
name|getValueDirectly
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readProperty
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
return|return
name|ensureValueHolderSet
argument_list|(
name|object
argument_list|)
operator|.
name|getValue
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writePropertyDirectly
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
block|{
name|ValueHolder
name|holder
init|=
operator|(
name|ValueHolder
operator|)
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
name|holder
operator|=
name|createValueHolder
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
name|holder
operator|.
name|setValueDirectly
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
block|{
name|ensureValueHolderSet
argument_list|(
name|object
argument_list|)
operator|.
name|setValueDirectly
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Injects a ValueHolder in the object if it hasn't been done yet.      */
annotation|@
name|Override
specifier|public
name|void
name|injectValueHolder
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
name|ensureValueHolderSet
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * Checks that an object's ValueHolder field described by this property is set,      * injecting a ValueHolder if needed.      */
specifier|protected
name|ValueHolder
name|ensureValueHolderSet
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
name|ValueHolder
name|holder
init|=
operator|(
name|ValueHolder
operator|)
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|holder
operator|==
literal|null
condition|)
block|{
name|holder
operator|=
name|createValueHolder
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|accessor
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|holder
argument_list|)
expr_stmt|;
block|}
return|return
name|holder
return|;
block|}
comment|/**      * Creates a ValueHolder for an object. Default implementation requires that an object      * implements Persistent interface.      */
specifier|protected
name|ValueHolder
name|createValueHolder
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Persistent
operator|)
condition|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"ValueHolders for non-persistent objects are not supported."
argument_list|,
name|this
argument_list|,
name|object
argument_list|)
throw|;
block|}
return|return
operator|new
name|PersistentObjectHolder
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|,
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

