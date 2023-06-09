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
name|swing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTable
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
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellRenderer
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
name|TableColumn
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
name|TableColumnModel
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
name|TableModel
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
name|Util
import|;
end_import

begin_comment
comment|/**  * A binding for a JTable.  *   */
end_comment

begin_class
specifier|public
class|class
name|TableBinding
extends|extends
name|BindingBase
block|{
comment|/**      * A variable exposed in the context of set/get cell value.      */
specifier|public
specifier|static
specifier|final
name|String
name|ITEM_VAR
init|=
literal|"item"
decl_stmt|;
specifier|protected
name|JTable
name|table
decl_stmt|;
specifier|protected
name|String
index|[]
name|headers
decl_stmt|;
specifier|protected
name|BindingExpression
index|[]
name|columns
decl_stmt|;
specifier|protected
name|boolean
index|[]
name|editableState
decl_stmt|;
specifier|protected
name|Class
index|[]
name|columnClass
decl_stmt|;
specifier|protected
name|List
name|list
decl_stmt|;
specifier|public
name|TableBinding
parameter_list|(
name|JTable
name|table
parameter_list|,
name|String
name|listBinding
parameter_list|,
name|String
index|[]
name|headers
parameter_list|,
name|BindingExpression
index|[]
name|columns
parameter_list|,
name|Class
index|[]
name|columnClass
parameter_list|,
name|boolean
index|[]
name|editableState
parameter_list|,
name|Object
index|[]
name|sampleLongValues
parameter_list|)
block|{
name|super
argument_list|(
name|listBinding
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
name|this
operator|.
name|editableState
operator|=
name|editableState
expr_stmt|;
name|this
operator|.
name|columnClass
operator|=
name|columnClass
expr_stmt|;
name|table
operator|.
name|setModel
argument_list|(
operator|new
name|BoundTableModel
argument_list|()
argument_list|)
expr_stmt|;
name|resizeColumns
argument_list|(
name|sampleLongValues
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|resizeColumns
parameter_list|(
name|Object
index|[]
name|sampleLongValues
parameter_list|)
block|{
name|TableCellRenderer
name|headerRenderer
init|=
name|table
operator|.
name|getTableHeader
argument_list|()
operator|.
name|getDefaultRenderer
argument_list|()
decl_stmt|;
name|TableColumnModel
name|columnModel
init|=
name|table
operator|.
name|getColumnModel
argument_list|()
decl_stmt|;
name|TableModel
name|tableModel
init|=
name|table
operator|.
name|getModel
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|columnModel
operator|.
name|getColumnCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|TableColumn
name|column
init|=
name|columnModel
operator|.
name|getColumn
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Component
name|header
init|=
name|headerRenderer
operator|.
name|getTableCellRendererComponent
argument_list|(
literal|null
argument_list|,
name|column
operator|.
name|getHeaderValue
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|int
name|headerWidth
init|=
name|header
operator|.
name|getPreferredSize
argument_list|()
operator|.
name|width
decl_stmt|;
if|if
condition|(
name|sampleLongValues
index|[
name|i
index|]
operator|!=
literal|null
condition|)
block|{
name|Component
name|bigCell
init|=
name|table
operator|.
name|getDefaultRenderer
argument_list|(
name|tableModel
operator|.
name|getColumnClass
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|getTableCellRendererComponent
argument_list|(
name|table
argument_list|,
name|sampleLongValues
index|[
name|i
index|]
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|int
name|cellWidth
init|=
name|bigCell
operator|.
name|getPreferredSize
argument_list|()
operator|.
name|width
decl_stmt|;
name|column
operator|.
name|setPreferredWidth
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|headerWidth
argument_list|,
name|cellWidth
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|column
operator|.
name|setPreferredWidth
argument_list|(
name|headerWidth
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|super
operator|.
name|setContext
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|updateList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|void
name|updateView
parameter_list|()
block|{
name|this
operator|.
name|list
operator|=
name|updateList
argument_list|()
expr_stmt|;
operator|(
operator|(
name|BoundTableModel
operator|)
name|table
operator|.
name|getModel
argument_list|()
operator|)
operator|.
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
name|int
name|getListSize
parameter_list|()
block|{
return|return
operator|(
name|list
operator|!=
literal|null
operator|)
condition|?
name|list
operator|.
name|size
argument_list|()
else|:
literal|0
return|;
block|}
name|List
name|updateList
parameter_list|()
block|{
if|if
condition|(
name|getContext
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|list
init|=
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|list
operator|instanceof
name|List
condition|)
block|{
return|return
operator|(
name|List
operator|)
name|list
return|;
block|}
if|if
condition|(
name|list
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|Object
index|[]
name|objects
init|=
operator|(
name|Object
index|[]
operator|)
name|list
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|objects
argument_list|)
return|;
block|}
if|if
condition|(
name|list
operator|instanceof
name|Collection
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|(
operator|(
name|Collection
operator|)
name|list
argument_list|)
return|;
block|}
throw|throw
operator|new
name|BindingException
argument_list|(
literal|"List expected, got - "
operator|+
name|list
argument_list|)
throw|;
block|}
specifier|final
class|class
name|BoundTableModel
extends|extends
name|AbstractTableModel
block|{
comment|// this map is used as "flyweight", providing on the spot context for Ognl
comment|// expression evaluation
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|listContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
block|{
name|put
argument_list|(
name|ITEM_VAR
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
name|headers
operator|.
name|length
return|;
block|}
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|getListSize
argument_list|()
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
return|return
name|editableState
index|[
name|columnIndex
index|]
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
name|Object
name|item
init|=
name|list
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
name|listContext
operator|.
name|put
argument_list|(
name|ITEM_VAR
argument_list|,
name|item
argument_list|)
expr_stmt|;
return|return
name|columns
index|[
name|columnIndex
index|]
operator|.
name|getValue
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|listContext
argument_list|)
return|;
block|}
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|column
parameter_list|)
block|{
comment|// per CAY-513 - if an empty string is passed for header, table header will
comment|// have zero height on Windows... So we have to check for this condition
return|return
name|Util
operator|.
name|isEmptyString
argument_list|(
name|headers
index|[
name|column
index|]
argument_list|)
condition|?
literal|" "
else|:
name|headers
index|[
name|column
index|]
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
return|return
name|columnClass
index|[
name|columnIndex
index|]
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
name|Object
name|item
init|=
name|list
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
name|listContext
operator|.
name|put
argument_list|(
name|ITEM_VAR
argument_list|,
name|item
argument_list|)
expr_stmt|;
name|columns
index|[
name|columnIndex
index|]
operator|.
name|setValue
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|listContext
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

