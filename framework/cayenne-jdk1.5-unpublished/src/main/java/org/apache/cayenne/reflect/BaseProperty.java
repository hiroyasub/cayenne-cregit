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
package|;
end_package

begin_comment
comment|/**  * An abstract property descriptor that delegates property access to an {@link Accessor}.  * Used as a superclass for other implementations.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseProperty
implements|implements
name|Property
block|{
specifier|protected
name|ClassDescriptor
name|owner
decl_stmt|;
specifier|protected
name|Accessor
name|accessor
decl_stmt|;
comment|// name is derived from accessor, cached here for performance
specifier|final
name|String
name|name
decl_stmt|;
specifier|public
name|BaseProperty
parameter_list|(
name|ClassDescriptor
name|owner
parameter_list|,
name|Accessor
name|accessor
parameter_list|)
block|{
if|if
condition|(
name|accessor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null accessor"
argument_list|)
throw|;
block|}
name|this
operator|.
name|accessor
operator|=
name|accessor
expr_stmt|;
name|this
operator|.
name|owner
operator|=
name|owner
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|accessor
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
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
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
return|;
block|}
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
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
specifier|abstract
name|boolean
name|visit
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Does nothing.      */
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
comment|// noop
block|}
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
return|return
name|accessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
return|;
block|}
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
name|accessor
operator|.
name|setValue
argument_list|(
name|object
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'@'
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

