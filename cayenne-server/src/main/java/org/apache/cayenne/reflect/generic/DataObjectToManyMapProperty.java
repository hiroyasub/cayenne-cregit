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
name|reflect
operator|.
name|generic
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
name|map
operator|.
name|ObjRelationship
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
name|reflect
operator|.
name|ToManyMapProperty
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|DataObjectToManyMapProperty
extends|extends
name|DataObjectToManyProperty
implements|implements
name|ToManyMapProperty
block|{
specifier|private
name|Accessor
name|mapKeyAccessor
decl_stmt|;
name|DataObjectToManyMapProperty
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|ClassDescriptor
name|targetDescriptor
parameter_list|,
name|Fault
name|fault
parameter_list|,
name|Accessor
name|mapKeyAccessor
parameter_list|)
block|{
name|super
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|fault
argument_list|)
expr_stmt|;
name|this
operator|.
name|mapKeyAccessor
operator|=
name|mapKeyAccessor
expr_stmt|;
block|}
specifier|public
name|Object
name|getMapKey
parameter_list|(
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
block|{
return|return
name|mapKeyAccessor
operator|.
name|getValue
argument_list|(
name|target
argument_list|)
return|;
block|}
block|}
end_class

end_unit

