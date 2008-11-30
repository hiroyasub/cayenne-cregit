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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|exp
operator|.
name|Expression
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
name|DbAttribute
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
name|EntityResult
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * A ClassDescriptor wrapper that compiles decorated descriptor lazily on first access.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|LazyClassDescriptorDecorator
implements|implements
name|ClassDescriptor
block|{
specifier|protected
name|ClassDescriptor
name|descriptor
decl_stmt|;
specifier|protected
name|ClassDescriptorMap
name|descriptorMap
decl_stmt|;
specifier|protected
name|String
name|entityName
decl_stmt|;
specifier|public
name|LazyClassDescriptorDecorator
parameter_list|(
name|ClassDescriptorMap
name|descriptorMap
parameter_list|,
name|String
name|entityName
parameter_list|)
block|{
name|this
operator|.
name|descriptorMap
operator|=
name|descriptorMap
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
block|}
comment|/**      * Checks whether decorated descriptor is initialized, and if not, creates it using      * parent {@link ClassDescriptorMap}.      */
specifier|protected
name|void
name|checkDescriptorInitialized
parameter_list|()
block|{
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
name|descriptor
operator|=
name|descriptorMap
operator|.
name|createDescriptor
argument_list|(
name|entityName
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns underlying descriptor used to delegate all processing, resolving it if      * needed.      */
specifier|public
name|ClassDescriptor
name|getDescriptor
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
return|;
block|}
specifier|public
name|Object
name|createObject
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|createObject
argument_list|()
return|;
block|}
specifier|public
name|Property
name|getDeclaredProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getDeclaredProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
specifier|public
name|ObjEntity
name|getEntity
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getEntity
argument_list|()
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getRootDbEntities
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getRootDbEntities
argument_list|()
return|;
block|}
specifier|public
name|EntityResult
name|getEntityResult
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getEntityResult
argument_list|()
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectClass
parameter_list|()
block|{
comment|// note that we can resolve Object class without triggering descriptor resolution.
comment|// This is very helpful when compiling POJO relationships
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
name|ObjEntity
name|entity
init|=
name|descriptorMap
operator|.
name|getResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
return|return
name|entity
operator|.
name|getJavaClass
argument_list|()
return|;
block|}
block|}
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getObjectClass
argument_list|()
return|;
block|}
comment|/**      * @deprecated since 3.0. Use {@link #visitProperties(PropertyVisitor)} method      *             instead.      */
specifier|public
name|Iterator
argument_list|<
name|Property
argument_list|>
name|getProperties
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getProperties
argument_list|()
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|Property
argument_list|>
name|getIdProperties
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getIdProperties
argument_list|()
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|getDiscriminatorColumns
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getDiscriminatorColumns
argument_list|()
return|;
block|}
specifier|public
name|Expression
name|getEntityQualifier
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getEntityQualifier
argument_list|()
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|ArcProperty
argument_list|>
name|getMapArcProperties
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getMapArcProperties
argument_list|()
return|;
block|}
specifier|public
name|Property
name|getProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
specifier|public
name|ClassDescriptor
name|getSubclassDescriptor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getSubclassDescriptor
argument_list|(
name|objectClass
argument_list|)
return|;
block|}
specifier|public
name|ClassDescriptor
name|getSuperclassDescriptor
parameter_list|()
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|getSuperclassDescriptor
argument_list|()
return|;
block|}
specifier|public
name|void
name|injectValueHolders
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
name|descriptor
operator|.
name|injectValueHolders
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFault
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|isFault
argument_list|(
name|object
argument_list|)
return|;
block|}
specifier|public
name|void
name|shallowMerge
parameter_list|(
name|Object
name|from
parameter_list|,
name|Object
name|to
parameter_list|)
throws|throws
name|PropertyException
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
name|descriptor
operator|.
name|shallowMerge
argument_list|(
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitDeclaredProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|visitDeclaredProperties
argument_list|(
name|visitor
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|visitProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|visitProperties
argument_list|(
name|visitor
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|visitAllProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
name|checkDescriptorInitialized
argument_list|()
expr_stmt|;
return|return
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
name|visitor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

