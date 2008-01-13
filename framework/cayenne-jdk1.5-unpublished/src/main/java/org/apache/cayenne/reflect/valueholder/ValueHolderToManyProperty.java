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
name|BaseToManyProperty
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
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|abstract
class|class
name|ValueHolderToManyProperty
extends|extends
name|BaseToManyProperty
block|{
name|ValueHolderToManyProperty
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
annotation|@
name|Override
specifier|protected
specifier|abstract
name|ValueHolder
name|createCollectionValueHolder
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
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
name|Object
name|target
init|=
name|accessor
operator|.
name|getValue
argument_list|(
name|source
argument_list|)
decl_stmt|;
return|return
name|target
operator|==
literal|null
operator|||
name|target
operator|instanceof
name|Fault
operator|||
operator|(
operator|(
name|ValueHolder
operator|)
name|target
operator|)
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
name|list
init|=
operator|(
name|ValueHolder
operator|)
name|readPropertyDirectly
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|invalidate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

