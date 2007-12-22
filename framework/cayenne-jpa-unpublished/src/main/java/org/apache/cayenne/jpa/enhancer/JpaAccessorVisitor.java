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
name|enhancer
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
name|enhancer
operator|.
name|AccessorVisitor
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
name|jpa
operator|.
name|map
operator|.
name|AccessType
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
name|jpa
operator|.
name|map
operator|.
name|JpaManagedClass
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
name|jpa
operator|.
name|map
operator|.
name|JpaPropertyDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|JpaAccessorVisitor
extends|extends
name|AccessorVisitor
block|{
specifier|private
name|JpaManagedClass
name|managedClass
decl_stmt|;
specifier|public
name|JpaAccessorVisitor
parameter_list|(
name|ClassVisitor
name|visitor
parameter_list|,
name|JpaManagedClass
name|managedClass
parameter_list|)
block|{
name|super
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|this
operator|.
name|managedClass
operator|=
name|managedClass
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isEnhancedProperty
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|managedClass
operator|.
name|getAccess
argument_list|()
operator|!=
name|AccessType
operator|.
name|PROPERTY
operator|&&
name|managedClass
operator|.
name|getAttributes
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|property
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isLazyFaulted
parameter_list|(
name|String
name|property
parameter_list|)
block|{
name|JpaPropertyDescriptor
name|propertyDescriptor
init|=
name|managedClass
operator|.
name|getClassDescriptor
argument_list|()
operator|.
name|getProperty
argument_list|(
name|property
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 10/14/2006 - this should access Jpa LAZY vs. EAGER flag
comment|// instead of using Cayenne default logic of lazy relationships
return|return
name|propertyDescriptor
operator|!=
literal|null
operator|&&
name|propertyDescriptor
operator|.
name|getTargetEntityType
argument_list|()
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

