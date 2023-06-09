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
name|configuration
operator|.
name|event
operator|.
name|ProcedureParameterEvent
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
name|TypesMapping
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
name|Procedure
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
name|ProcedureParameter
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
name|ProjectController
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
name|util
operator|.
name|CayenneTableModel
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
name|util
operator|.
name|ProjectUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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

begin_class
specifier|public
class|class
name|ProcedureParameterTableModel
extends|extends
name|CayenneTableModel
argument_list|<
name|ProcedureParameter
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_NUMBER
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_NAME
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_DIRECTION
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_TYPE
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_LENGTH
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PARAMETER_PRECISION
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_PARAMETER
init|=
literal|"IN"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_PARAMETER
init|=
literal|"OUT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IN_OUT_PARAMETER
init|=
literal|"INOUT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
index|[]
name|PARAMETER_DIRECTION_NAMES
init|=
block|{
literal|""
block|,
name|IN_PARAMETER
block|,
name|OUT_PARAMETER
block|,
name|IN_OUT_PARAMETER
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|PARAMETER_INDEXES
init|=
block|{
name|PARAMETER_NUMBER
block|,
name|PARAMETER_NAME
block|,
name|PARAMETER_DIRECTION
block|,
name|PARAMETER_TYPE
block|,
name|PARAMETER_LENGTH
block|,
name|PARAMETER_PRECISION
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|PARAMETER_NAMES
init|=
block|{
literal|"No."
block|,
literal|"Name"
block|,
literal|"Direction"
block|,
literal|"Type"
block|,
literal|"Max Length"
block|,
literal|"Precision"
block|}
decl_stmt|;
specifier|protected
name|Procedure
name|procedure
decl_stmt|;
specifier|public
name|ProcedureParameterTableModel
parameter_list|(
name|Procedure
name|procedure
parameter_list|,
name|ProjectController
name|mediator
parameter_list|,
name|Object
name|eventSource
parameter_list|)
block|{
name|super
argument_list|(
name|mediator
argument_list|,
name|eventSource
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|procedure
operator|.
name|getCallParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|procedure
operator|=
name|procedure
expr_stmt|;
block|}
comment|/**      * Returns procedure parameter at the specified row.      * Returns NULL if row index is outside the valid range.      */
specifier|public
name|ProcedureParameter
name|getParameter
parameter_list|(
name|int
name|row
parameter_list|)
block|{
return|return
operator|(
name|row
operator|>=
literal|0
operator|&&
name|row
operator|<
name|objectList
operator|.
name|size
argument_list|()
operator|)
condition|?
name|objectList
operator|.
name|get
argument_list|(
name|row
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|newVal
parameter_list|,
name|int
name|rowIndex
parameter_list|,
name|int
name|columnIndex
parameter_list|)
block|{
name|ProcedureParameter
name|parameter
init|=
name|getParameter
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameter
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|String
name|value
init|=
operator|(
name|String
operator|)
name|newVal
decl_stmt|;
name|ProcedureParameterEvent
name|event
init|=
operator|new
name|ProcedureParameterEvent
argument_list|(
name|eventSource
argument_list|,
name|parameter
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|PARAMETER_NAME
case|:
name|event
operator|.
name|setOldName
argument_list|(
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setParameterName
argument_list|(
name|value
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
name|fireTableCellUpdated
argument_list|(
name|rowIndex
argument_list|,
name|columnIndex
argument_list|)
expr_stmt|;
break|break;
case|case
name|PARAMETER_DIRECTION
case|:
name|setParameterDirection
argument_list|(
name|value
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
break|break;
case|case
name|PARAMETER_TYPE
case|:
name|setParameterType
argument_list|(
name|value
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
break|break;
case|case
name|PARAMETER_LENGTH
case|:
name|setMaxLength
argument_list|(
name|value
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
break|break;
case|case
name|PARAMETER_PRECISION
case|:
name|setPrecision
argument_list|(
name|value
argument_list|,
name|parameter
argument_list|)
expr_stmt|;
break|break;
block|}
name|mediator
operator|.
name|fireProcedureParameterEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setPrecision
parameter_list|(
name|String
name|newVal
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
if|if
condition|(
name|newVal
operator|==
literal|null
operator|||
name|newVal
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|parameter
operator|.
name|setPrecision
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|parameter
operator|.
name|setPrecision
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|newVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
literal|null
argument_list|,
literal|"Invalid precision ("
operator|+
name|newVal
operator|+
literal|"), only numbers are allowed."
argument_list|,
literal|"Invalid Precision Value"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setMaxLength
parameter_list|(
name|String
name|newVal
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
if|if
condition|(
name|newVal
operator|==
literal|null
operator|||
name|newVal
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|parameter
operator|.
name|setMaxLength
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|parameter
operator|.
name|setMaxLength
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|newVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|ex
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
literal|null
argument_list|,
literal|"Invalid Max Length ("
operator|+
name|newVal
operator|+
literal|"), only numbers are allowed"
argument_list|,
literal|"Invalid Maximum Length"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|setParameterType
parameter_list|(
name|String
name|newVal
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|parameter
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|newVal
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setParameterDirection
parameter_list|(
name|String
name|direction
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
if|if
condition|(
name|ProcedureParameterTableModel
operator|.
name|IN_PARAMETER
operator|.
name|equals
argument_list|(
name|direction
argument_list|)
condition|)
block|{
name|parameter
operator|.
name|setDirection
argument_list|(
name|ProcedureParameter
operator|.
name|IN_PARAMETER
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ProcedureParameterTableModel
operator|.
name|OUT_PARAMETER
operator|.
name|equals
argument_list|(
name|direction
argument_list|)
condition|)
block|{
name|parameter
operator|.
name|setDirection
argument_list|(
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ProcedureParameterTableModel
operator|.
name|IN_OUT_PARAMETER
operator|.
name|equals
argument_list|(
name|direction
argument_list|)
condition|)
block|{
name|parameter
operator|.
name|setDirection
argument_list|(
name|ProcedureParameter
operator|.
name|IN_OUT_PARAMETER
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setParameterName
parameter_list|(
name|String
name|newVal
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|ProjectUtil
operator|.
name|setProcedureParameterName
argument_list|(
name|parameter
argument_list|,
name|newVal
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Class
name|getElementsClass
parameter_list|()
block|{
return|return
name|ProcedureParameter
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getColumnCount
parameter_list|()
block|{
return|return
name|PARAMETER_INDEXES
operator|.
name|length
return|;
block|}
annotation|@
name|Override
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
name|ProcedureParameter
name|parameter
init|=
name|getParameter
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameter
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
switch|switch
condition|(
name|columnIndex
condition|)
block|{
case|case
name|PARAMETER_NUMBER
case|:
return|return
name|getParameterNumber
argument_list|(
name|rowIndex
argument_list|,
name|parameter
argument_list|)
return|;
case|case
name|PARAMETER_NAME
case|:
return|return
name|getParameterName
argument_list|(
name|parameter
argument_list|)
return|;
case|case
name|PARAMETER_DIRECTION
case|:
return|return
name|getParameterDirection
argument_list|(
name|parameter
argument_list|)
return|;
case|case
name|PARAMETER_TYPE
case|:
return|return
name|getParameterType
argument_list|(
name|parameter
argument_list|)
return|;
case|case
name|PARAMETER_LENGTH
case|:
return|return
name|getParameterLength
argument_list|(
name|parameter
argument_list|)
return|;
case|case
name|PARAMETER_PRECISION
case|:
return|return
name|getParameterPrecision
argument_list|(
name|parameter
argument_list|)
return|;
default|default :
return|return
literal|""
return|;
block|}
block|}
specifier|protected
name|String
name|getParameterNumber
parameter_list|(
name|int
name|rowIndex
parameter_list|,
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|boolean
name|hasReturnValue
init|=
name|parameter
operator|.
name|getProcedure
argument_list|()
operator|.
name|isReturningValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasReturnValue
condition|)
block|{
return|return
operator|(
name|rowIndex
operator|==
literal|0
operator|)
condition|?
literal|"R"
else|:
literal|""
operator|+
name|rowIndex
return|;
block|}
else|else
block|{
return|return
literal|""
operator|+
operator|(
name|rowIndex
operator|+
literal|1
operator|)
return|;
block|}
block|}
specifier|protected
name|String
name|getParameterPrecision
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
operator|(
name|parameter
operator|.
name|getPrecision
argument_list|()
operator|>=
literal|0
operator|)
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|parameter
operator|.
name|getPrecision
argument_list|()
argument_list|)
else|:
literal|""
return|;
block|}
specifier|protected
name|String
name|getParameterLength
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
operator|(
name|parameter
operator|.
name|getMaxLength
argument_list|()
operator|>=
literal|0
operator|)
condition|?
name|String
operator|.
name|valueOf
argument_list|(
name|parameter
operator|.
name|getMaxLength
argument_list|()
argument_list|)
else|:
literal|""
return|;
block|}
specifier|protected
name|String
name|getParameterType
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|parameter
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getParameterDirection
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
name|int
name|direction
init|=
name|parameter
operator|.
name|getDirection
argument_list|()
decl_stmt|;
return|return
name|PARAMETER_DIRECTION_NAMES
index|[
name|direction
operator|==
operator|-
literal|1
condition|?
literal|0
else|:
name|direction
index|]
return|;
block|}
specifier|protected
name|String
name|getParameterName
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|)
block|{
return|return
name|parameter
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getColumnName
parameter_list|(
name|int
name|col
parameter_list|)
block|{
return|return
name|PARAMETER_NAMES
index|[
name|col
index|]
return|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getColumnClass
parameter_list|(
name|int
name|col
parameter_list|)
block|{
return|return
name|String
operator|.
name|class
return|;
block|}
comment|/**      * Suppressed ordering operations defined in a superclass.      * Since stored procedure parameters are positional,      * no reordering is allowed.      */
specifier|public
name|void
name|orderList
parameter_list|()
block|{
comment|// NOOP
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isCellEditable
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
return|return
name|col
operator|!=
name|PARAMETER_NUMBER
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isColumnSortable
parameter_list|(
name|int
name|sortCol
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sortByColumn
parameter_list|(
name|int
name|sortCol
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

