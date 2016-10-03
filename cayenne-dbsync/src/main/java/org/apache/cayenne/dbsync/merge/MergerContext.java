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
name|dbsync
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
name|access
operator|.
name|DataNode
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
name|dba
operator|.
name|DbAdapter
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
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|naming
operator|.
name|ObjectNameGenerator
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
name|DataMap
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * An object passed as an argument to {@link MergerToken#execute(MergerContext)}s that a  * {@link MergerToken} can do its work.  */
end_comment

begin_class
specifier|public
class|class
name|MergerContext
block|{
specifier|private
name|DataMap
name|dataMap
decl_stmt|;
specifier|private
name|DataNode
name|dataNode
decl_stmt|;
specifier|private
name|ValidationResult
name|validationResult
decl_stmt|;
specifier|private
name|ModelMergeDelegate
name|delegate
decl_stmt|;
specifier|private
name|EntityMergeSupport
name|entityMergeSupport
decl_stmt|;
specifier|protected
name|MergerContext
parameter_list|()
block|{
block|}
comment|/**      * @since 4.0      */
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
operator|new
name|Builder
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
comment|/**      * @deprecated since 4.0 use {@link #getDataNode()} and its {@link DataNode#getAdapter()} method.      */
annotation|@
name|Deprecated
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|getDataNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|EntityMergeSupport
name|getEntityMergeSupport
parameter_list|()
block|{
return|return
name|entityMergeSupport
return|;
block|}
comment|/**      * Returns the DataMap that is the target of a the merge operation.      *      * @return the DataMap that is the target of a the merge operation.      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
specifier|public
name|DataNode
name|getDataNode
parameter_list|()
block|{
return|return
name|dataNode
return|;
block|}
specifier|public
name|ValidationResult
name|getValidationResult
parameter_list|()
block|{
return|return
name|validationResult
return|;
block|}
comment|/**      * Returns a callback object that is invoked as the merge proceeds through tokens, modifying the DataMap.      *      * @return a callback object that is invoked as the merge proceeds through tokens, modifying the DataMap.      * @since 4.0      */
specifier|public
name|ModelMergeDelegate
name|getDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|/**      * @deprecated since 4.0 in favor of {@link #getDelegate()}.      */
annotation|@
name|Deprecated
specifier|public
name|ModelMergeDelegate
name|getModelMergeDelegate
parameter_list|()
block|{
return|return
name|getDelegate
argument_list|()
return|;
block|}
specifier|public
specifier|static
class|class
name|Builder
block|{
specifier|private
name|MergerContext
name|context
decl_stmt|;
specifier|private
name|Builder
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
operator|new
name|MergerContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|dataMap
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|validationResult
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
block|}
specifier|public
name|MergerContext
name|build
parameter_list|()
block|{
comment|// init missing defaults ...
if|if
condition|(
name|context
operator|.
name|entityMergeSupport
operator|==
literal|null
condition|)
block|{
name|nameGenerator
argument_list|(
operator|new
name|DefaultObjectNameGenerator
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|delegate
operator|==
literal|null
condition|)
block|{
name|delegate
argument_list|(
operator|new
name|DefaultModelMergeDelegate
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|dataNode
operator|==
literal|null
condition|)
block|{
name|dataNode
argument_list|(
operator|new
name|DataNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|public
name|Builder
name|delegate
parameter_list|(
name|ModelMergeDelegate
name|delegate
parameter_list|)
block|{
name|context
operator|.
name|delegate
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|nameGenerator
parameter_list|(
name|ObjectNameGenerator
name|nameGenerator
parameter_list|)
block|{
comment|// should the last argument also be a part of the builder?
name|context
operator|.
name|entityMergeSupport
operator|=
operator|new
name|EntityMergeSupport
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|nameGenerator
argument_list|)
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|dataNode
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
name|this
operator|.
name|context
operator|.
name|dataNode
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|Builder
name|syntheticDataNode
parameter_list|(
name|DataSource
name|dataSource
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|DataNode
name|dataNode
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|dataNode
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
return|return
name|dataNode
argument_list|(
name|dataNode
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

