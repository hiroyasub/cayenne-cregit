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
name|modeler
operator|.
name|dialog
operator|.
name|db
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|AbstractTableModel
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
name|merge
operator|.
name|MergeDirection
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
name|merge
operator|.
name|MergerToken
import|;
end_import

begin_class
specifier|public
class|class
name|MergerTokenTableModel
extends|extends
name|AbstractTableModel
block|{
specifier|public
specifier|static
specifier|final
name|int
name|COL_SELECT
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|COL_DIRECTION
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|COL_NAME
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|COL_VALUE
init|=
literal|3
decl_stmt|;
specifier|private
name|MergerTokenSelectorController
name|controller
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
decl_stmt|;
specifier|public
name|MergerTokenTableModel
parameter_list|(
name|MergerTokenSelectorController
name|controller
parameter_list|)
block|{
name|this
operator|.
name|controller
operator|=
name|controller
expr_stmt|;
name|this
operator|.
name|tokens
operator|=
name|controller
operator|.
name|getSelectableTokens
argument_list|()
expr_stmt|;
block|}
specifier|private
name|MergerTokenSelectorController
name|getController
parameter_list|()
block|{
return|return
name|controller
return|;
block|}
specifier|public
name|Class
name|getColumnClass
parameter_list|(
name|int
name|columnIndex
parameter_list|)
block|{
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|COL_SELECT
case|:
return|return
name|Boolean
operator|.
name|class
return|;
case|case
name|COL_DIRECTION
case|:
comment|// TODO: correct?
return|return
name|String
operator|.
name|class
return|;
case|case
name|COL_NAME
case|:
case|case
name|COL_VALUE
case|:
return|return
name|String
operator|.
name|class
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
literal|4
return|;
block|}
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|columnIndex
parameter_list|)
block|{
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|COL_SELECT
case|:
return|return
literal|""
return|;
case|case
name|COL_NAME
case|:
return|return
literal|"Operation"
return|;
case|case
name|COL_DIRECTION
case|:
return|return
literal|"Direction"
return|;
case|case
name|COL_VALUE
case|:
return|return
literal|""
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|tokens
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|MergerToken
name|getToken
parameter_list|(
name|int
name|rowIndex
parameter_list|)
block|{
return|return
name|tokens
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getValueAt
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
name|MergerToken
name|token
init|=
name|getToken
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|COL_SELECT
case|:
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|getController
argument_list|()
operator|.
name|isSelected
argument_list|(
name|token
argument_list|)
argument_list|)
return|;
case|case
name|COL_NAME
case|:
return|return
name|token
operator|.
name|getTokenName
argument_list|()
return|;
case|case
name|COL_DIRECTION
case|:
return|return
name|token
operator|.
name|getDirection
argument_list|()
return|;
case|case
name|COL_VALUE
case|:
return|return
name|token
operator|.
name|getTokenValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|COL_SELECT
case|:
case|case
name|COL_DIRECTION
case|:
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setValueAt
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
name|MergerToken
name|token
init|=
name|getToken
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|COL_SELECT
case|:
name|Boolean
name|val
init|=
operator|(
name|Boolean
operator|)
name|value
decl_stmt|;
name|getController
argument_list|()
operator|.
name|select
argument_list|(
name|token
argument_list|,
name|val
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|COL_DIRECTION
case|:
name|MergeDirection
name|direction
init|=
operator|(
name|MergeDirection
operator|)
name|value
decl_stmt|;
name|getController
argument_list|()
operator|.
name|setDirection
argument_list|(
name|token
argument_list|,
name|direction
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

end_unit

