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
name|merge
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
name|ObjEntity
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
name|EntityMergeSupport
import|;
end_import

begin_comment
comment|/**  * A {@link MergerToken} to add a {@link DbAttribute} to a {@link DbEntity}. The  * {@link EntityMergeSupport} will be used to update the mapped {@link ObjEntity}  *   */
end_comment

begin_class
specifier|public
class|class
name|AddColumnToModel
extends|extends
name|AbstractToModelToken
operator|.
name|EntityAndColumn
block|{
specifier|public
name|AddColumnToModel
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createDropColumnToDb
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
name|getEntity
argument_list|()
operator|.
name|addAttribute
argument_list|(
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
name|synchronizeWithObjEntity
argument_list|(
name|mergerContext
argument_list|,
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|mergerContext
operator|.
name|getModelMergeDelegate
argument_list|()
operator|.
name|dbAttributeAdded
argument_list|(
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Add Column"
return|;
block|}
block|}
end_class

end_unit
