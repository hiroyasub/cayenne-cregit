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
name|dbsync
operator|.
name|merge
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

begin_class
class|class
name|DbAttributeDictionary
extends|extends
name|MergerDictionary
argument_list|<
name|DbAttribute
argument_list|>
block|{
specifier|private
specifier|final
name|DbEntity
name|container
decl_stmt|;
name|DbAttributeDictionary
parameter_list|(
name|DbEntity
name|container
parameter_list|)
block|{
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
block|}
annotation|@
name|Override
name|String
name|getName
parameter_list|(
name|DbAttribute
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|getAll
parameter_list|()
block|{
return|return
name|container
operator|.
name|getAttributes
argument_list|()
return|;
block|}
block|}
end_class

end_unit

