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
operator|.
name|token
operator|.
name|model
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergerContext
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SetGeneratedFlagToModel
extends|extends
name|AbstractToModelToken
operator|.
name|EntityAndColumn
block|{
specifier|private
specifier|final
name|boolean
name|isGenerated
decl_stmt|;
specifier|public
name|SetGeneratedFlagToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|,
name|boolean
name|isGenerated
parameter_list|)
block|{
comment|// drop generated attribute must go first
name|super
argument_list|(
literal|"Set Is Generated"
argument_list|,
name|isGenerated
condition|?
literal|111
else|:
literal|109
argument_list|,
name|entity
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|this
operator|.
name|isGenerated
operator|=
name|isGenerated
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createSetGeneratedFlagToDb
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|,
operator|!
name|isGenerated
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|context
parameter_list|)
block|{
name|getColumn
argument_list|()
operator|.
name|setGenerated
argument_list|(
name|isGenerated
argument_list|)
expr_stmt|;
name|context
operator|.
name|getDelegate
argument_list|()
operator|.
name|dbAttributeModified
argument_list|(
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

