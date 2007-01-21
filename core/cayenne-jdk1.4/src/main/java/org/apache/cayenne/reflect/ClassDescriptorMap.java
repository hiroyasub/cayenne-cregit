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
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|map
operator|.
name|EntityResolver
import|;
end_import

begin_comment
comment|/**  * An object that holds class descriptors for mapped entities, compiling new descriptors  * on demand using an internal chain of descriptor factories.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ClassDescriptorMap
block|{
specifier|protected
name|EntityResolver
name|resolver
decl_stmt|;
specifier|protected
name|Map
name|descriptors
decl_stmt|;
specifier|protected
name|List
name|factories
decl_stmt|;
specifier|public
name|ClassDescriptorMap
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|descriptors
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|factories
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EntityResolver
name|getResolver
parameter_list|()
block|{
return|return
name|resolver
return|;
block|}
comment|/**      * Adds a factory to the descriptor factory chain.      */
specifier|public
name|void
name|addFactory
parameter_list|(
name|ClassDescriptorFactory
name|factory
parameter_list|)
block|{
name|factories
operator|.
name|add
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeFactory
parameter_list|(
name|ClassDescriptorFactory
name|factory
parameter_list|)
block|{
name|factories
operator|.
name|remove
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearFactories
parameter_list|()
block|{
name|factories
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|clearDescriptors
parameter_list|()
block|{
name|descriptors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Removes cached descriptor if any for the given entity.      */
specifier|public
name|void
name|removeDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|descriptors
operator|.
name|remove
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Caches descriptor definition.      */
specifier|public
name|void
name|addDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
name|removeDescriptor
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|descriptors
operator|.
name|put
argument_list|(
name|entityName
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ClassDescriptor
name|getDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|ClassDescriptor
name|cached
init|=
operator|(
name|ClassDescriptor
operator|)
name|descriptors
operator|.
name|get
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cached
operator|!=
literal|null
condition|)
block|{
return|return
name|cached
return|;
block|}
return|return
name|createProxyDescriptor
argument_list|(
name|entityName
argument_list|)
return|;
block|}
comment|/**      * Creates a descriptor wrapper that will compile the underlying descriptor on demand.      * Using proxy indirection is needed to compile relationships of descriptors to other      * descriptors that are not compiled yet.      */
specifier|protected
name|ClassDescriptor
name|createProxyDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
operator|new
name|LazyClassDescriptorDecorator
argument_list|(
name|this
argument_list|,
name|entityName
argument_list|)
decl_stmt|;
name|addDescriptor
argument_list|(
name|entityName
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
return|return
name|descriptor
return|;
block|}
comment|/**      * Creates a new descriptor.      */
specifier|protected
name|ClassDescriptor
name|createDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
comment|// scan the factory chain until some factory returns a non-null descriptor;
comment|// scanning is done in reverse order so that the factories added last take higher
comment|// precedence...
name|ListIterator
name|it
init|=
name|factories
operator|.
name|listIterator
argument_list|(
name|factories
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|ClassDescriptorFactory
name|factory
init|=
operator|(
name|ClassDescriptorFactory
operator|)
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|factory
operator|.
name|getDescriptor
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
return|return
name|descriptor
return|;
block|}
block|}
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Failed to create descriptor for entity: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

