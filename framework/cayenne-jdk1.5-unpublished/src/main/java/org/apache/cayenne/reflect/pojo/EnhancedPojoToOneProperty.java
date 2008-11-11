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
name|pojo
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
name|Fault
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

begin_comment
comment|/**  * A property descriptor for the to-one relationship property of an enhanced pojo. Assumes  * a class in question has a field called "$cay_faultResolved_propertyName" that stores a  * boolean flag indicating whether the property in question is not yet resolved.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|EnhancedPojoToOneProperty
extends|extends
name|BaseToOneProperty
block|{
specifier|protected
name|EnhancedPojoPropertyFaultHandler
name|faultHandler
decl_stmt|;
specifier|protected
name|Fault
name|fault
decl_stmt|;
name|EnhancedPojoToOneProperty
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
parameter_list|,
name|Fault
name|fault
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
name|this
operator|.
name|faultHandler
operator|=
operator|new
name|EnhancedPojoPropertyFaultHandler
argument_list|(
name|owner
operator|.
name|getObjectClass
argument_list|()
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|fault
operator|=
name|fault
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isFault
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
return|return
name|faultHandler
operator|.
name|isFaultProperty
argument_list|(
name|source
argument_list|)
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
name|faultHandler
operator|.
name|setFaultProperty
argument_list|(
name|object
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|void
name|resolveFault
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|isFault
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|Object
name|target
init|=
name|fault
operator|.
name|resolveFault
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|,
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|writePropertyDirectly
argument_list|(
name|object
argument_list|,
literal|null
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|faultHandler
operator|.
name|setFaultProperty
argument_list|(
name|object
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
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
name|resolveFault
argument_list|(
name|object
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|readProperty
argument_list|(
name|object
argument_list|)
return|;
block|}
block|}
end_class

end_unit

