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
name|reflect
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
name|PersistentObjectMap
import|;
end_import

begin_class
class|class
name|JpaMapProperty
extends|extends
name|JpaToManyProperty
implements|implements
name|ToManyMapProperty
block|{
specifier|private
name|Accessor
name|mapKeyAccessor
decl_stmt|;
name|JpaMapProperty
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
name|Accessor
name|mapKeyAccessor
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
name|mapKeyAccessor
operator|=
name|mapKeyAccessor
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ValueHolder
name|createValueHolder
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|)
block|{
return|return
operator|new
name|PersistentObjectMap
argument_list|(
name|relationshipOwner
argument_list|,
name|getName
argument_list|()
argument_list|,
name|mapKeyAccessor
argument_list|)
return|;
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

