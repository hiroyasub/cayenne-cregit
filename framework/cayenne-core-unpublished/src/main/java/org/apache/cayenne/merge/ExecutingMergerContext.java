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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|dba
operator|.
name|JdbcAdapter
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

begin_class
specifier|public
class|class
name|ExecutingMergerContext
implements|implements
name|MergerContext
block|{
specifier|private
name|DataMap
name|map
decl_stmt|;
specifier|private
name|DataNode
name|node
decl_stmt|;
specifier|private
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
specifier|private
name|ModelMergeDelegate
name|delegate
decl_stmt|;
specifier|public
name|ExecutingMergerContext
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
operator|new
name|DefaultModelMergeDelegate
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ExecutingMergerContext
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DataSource
name|dataSource
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|ModelMergeDelegate
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
comment|// create a fake DataNode as lots of DbAdapter/PkGenerator methods
comment|// take a DataNode instead of just a DataSource
name|this
operator|.
name|node
operator|=
operator|new
name|DataNode
argument_list|()
expr_stmt|;
name|this
operator|.
name|node
operator|.
name|setJdbcEventLogger
argument_list|(
name|adapter
operator|.
name|getJdbcEventLogger
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|this
operator|.
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
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
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
specifier|public
name|DataNode
name|getDataNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
specifier|public
name|ValidationResult
name|getValidationResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
specifier|public
name|ModelMergeDelegate
name|getModelMergeDelegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
end_class

end_unit
