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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
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
name|reverse
operator|.
name|dbimport
operator|.
name|FilterContainer
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
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeColumn
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
name|reverse
operator|.
name|dbimport
operator|.
name|IncludeTable
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
name|modeler
operator|.
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|DbImportTreeNode
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
name|modeler
operator|.
name|dialog
operator|.
name|db
operator|.
name|load
operator|.
name|TransferableNode
import|;
end_import

begin_class
specifier|public
class|class
name|PrintTablesBiFunction
implements|implements
name|BiFunction
argument_list|<
name|FilterContainer
argument_list|,
name|DbImportTreeNode
argument_list|,
name|Void
argument_list|>
block|{
specifier|private
name|DbImportTree
name|dbImportTree
decl_stmt|;
specifier|public
name|PrintTablesBiFunction
parameter_list|(
name|DbImportTree
name|dbImportTree
parameter_list|)
block|{
name|this
operator|.
name|dbImportTree
operator|=
name|dbImportTree
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Void
name|apply
parameter_list|(
name|FilterContainer
name|filterContainer
parameter_list|,
name|DbImportTreeNode
name|root
parameter_list|)
block|{
name|DbImportModel
name|model
init|=
operator|(
name|DbImportModel
operator|)
name|dbImportTree
operator|.
name|getModel
argument_list|()
decl_stmt|;
name|boolean
name|isTransferable
init|=
name|dbImportTree
operator|.
name|isTransferable
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|.
name|getChildCount
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|root
operator|.
name|removeAllChildren
argument_list|()
expr_stmt|;
block|}
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|includeTables
init|=
name|filterContainer
operator|.
name|getIncludeTables
argument_list|()
decl_stmt|;
for|for
control|(
name|IncludeTable
name|includeTable
range|:
name|includeTables
control|)
block|{
name|DbImportTreeNode
name|node
init|=
operator|!
name|isTransferable
condition|?
operator|new
name|DbImportTreeNode
argument_list|(
name|includeTable
argument_list|)
else|:
operator|new
name|TransferableNode
argument_list|(
name|includeTable
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTransferable
operator|&&
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|dbImportTree
operator|.
name|printParams
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|IncludeColumn
argument_list|(
literal|"Loading..."
argument_list|)
argument_list|)
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|root
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|dbImportTree
operator|.
name|packColumns
argument_list|(
name|includeTable
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
name|model
operator|.
name|reload
argument_list|(
name|root
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

