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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|BeanInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|Introspector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Comparator
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
name|javax
operator|.
name|swing
operator|.
name|JOptionPane
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
name|CayenneRuntimeException
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
name|Application
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
name|undo
operator|.
name|CayenneTableModelUndoableEdit
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Superclass of CayenneModeler table model classes.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneTableModel
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractTableModel
block|{
specifier|protected
name|ProjectController
name|mediator
decl_stmt|;
specifier|protected
name|Object
name|eventSource
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|T
argument_list|>
name|objectList
decl_stmt|;
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CayenneTableModel
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor for CayenneTableModel.      */
specifier|public
name|CayenneTableModel
parameter_list|(
name|ProjectController
name|mediator
parameter_list|,
name|Object
name|eventSource
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|objectList
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventSource
operator|=
name|eventSource
expr_stmt|;
name|this
operator|.
name|mediator
operator|=
name|mediator
expr_stmt|;
name|this
operator|.
name|objectList
operator|=
name|objectList
expr_stmt|;
block|}
specifier|public
name|void
name|setValueAt
parameter_list|(
name|Object
name|newVal
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
block|{
try|try
block|{
name|Object
name|oldValue
init|=
name|getValueAt
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|newVal
argument_list|,
name|oldValue
argument_list|)
condition|)
block|{
name|setUpdatedValueAt
argument_list|(
name|newVal
argument_list|,
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
name|this
operator|.
name|mediator
operator|.
name|getApplication
argument_list|()
operator|.
name|getUndoManager
argument_list|()
operator|.
name|addEdit
argument_list|(
operator|new
name|CayenneTableModelUndoableEdit
argument_list|(
name|this
argument_list|,
name|oldValue
argument_list|,
name|newVal
argument_list|,
name|row
argument_list|,
name|col
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error setting table model value"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Invalid value"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets a new value after it is already checked by the superclass and it is determined      * that the value has changed.      */
specifier|public
specifier|abstract
name|void
name|setUpdatedValueAt
parameter_list|(
name|Object
name|newVal
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|col
parameter_list|)
function_decl|;
comment|/**      * Returns Java class of the internal list elements.      */
specifier|public
specifier|abstract
name|Class
argument_list|<
name|?
argument_list|>
name|getElementsClass
parameter_list|()
function_decl|;
comment|/**      * Returns the number of objects on the list.      */
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|objectList
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Returns an object used as an event source.      */
specifier|public
name|Object
name|getEventSource
parameter_list|()
block|{
return|return
name|eventSource
return|;
block|}
comment|/**      * Returns EventController object.      */
specifier|public
name|ProjectController
name|getMediator
parameter_list|()
block|{
return|return
name|mediator
return|;
block|}
comment|/**      * Returns internal object list.      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|T
argument_list|>
name|getObjectList
parameter_list|()
block|{
return|return
name|objectList
return|;
block|}
specifier|public
name|void
name|addRow
parameter_list|(
name|T
name|row
parameter_list|)
block|{
name|objectList
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|removeRow
parameter_list|(
name|T
name|row
parameter_list|)
block|{
name|objectList
operator|.
name|remove
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
comment|/**      * Moves a row up, jumping down if row is already at the top.      */
specifier|public
name|int
name|moveRowUp
parameter_list|(
name|T
name|row
parameter_list|)
block|{
name|int
name|len
init|=
name|objectList
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|2
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|ind
init|=
name|objectList
operator|.
name|indexOf
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|<=
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|neighborIndex
init|=
name|ind
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|neighborIndex
operator|<
literal|0
condition|)
block|{
name|neighborIndex
operator|=
name|len
operator|-
literal|1
expr_stmt|;
block|}
name|swapRows
argument_list|(
name|ind
argument_list|,
name|neighborIndex
argument_list|)
expr_stmt|;
return|return
name|neighborIndex
return|;
block|}
comment|/**      * Moves a row down, jumping up if row is already at the bottom.      */
specifier|public
name|int
name|moveRowDown
parameter_list|(
name|T
name|row
parameter_list|)
block|{
name|int
name|len
init|=
name|objectList
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|<
literal|2
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|ind
init|=
name|objectList
operator|.
name|indexOf
argument_list|(
name|row
argument_list|)
decl_stmt|;
comment|// not valid if it is not found or it is at the end of the list
if|if
condition|(
name|ind
operator|<
literal|0
operator|||
operator|(
name|ind
operator|+
literal|1
operator|)
operator|>=
name|len
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|neighborIndex
init|=
name|ind
operator|+
literal|1
decl_stmt|;
name|swapRows
argument_list|(
name|ind
argument_list|,
name|neighborIndex
argument_list|)
expr_stmt|;
return|return
name|neighborIndex
return|;
block|}
specifier|protected
name|void
name|swapRows
parameter_list|(
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
name|Collections
operator|.
name|swap
argument_list|(
name|objectList
argument_list|,
name|i
argument_list|,
name|j
argument_list|)
expr_stmt|;
name|fireTableDataChanged
argument_list|()
expr_stmt|;
block|}
comment|/**      * Correct errors that model has.      */
specifier|public
name|void
name|resetModel
parameter_list|()
block|{
comment|// do nothing by default
block|}
comment|/**      * @return false, if model is not valid.       */
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|protected
class|class
name|PropertyComparator
parameter_list|<
name|C
parameter_list|>
implements|implements
name|Comparator
argument_list|<
name|C
argument_list|>
block|{
name|Method
name|getter
decl_stmt|;
name|PropertyComparator
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Class
name|beanClass
parameter_list|)
block|{
try|try
block|{
name|getter
operator|=
name|findGetter
argument_list|(
name|beanClass
argument_list|,
name|propertyName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IntrospectionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Introspection error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|Method
name|findGetter
parameter_list|(
name|Class
name|beanClass
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|IntrospectionException
block|{
name|BeanInfo
name|info
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
name|PropertyDescriptor
index|[]
name|descriptors
init|=
name|info
operator|.
name|getPropertyDescriptors
argument_list|()
decl_stmt|;
for|for
control|(
name|PropertyDescriptor
name|descriptor
range|:
name|descriptors
control|)
block|{
if|if
condition|(
name|propertyName
operator|.
name|equals
argument_list|(
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|descriptor
operator|.
name|getReadMethod
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IntrospectionException
argument_list|(
literal|"No getter found for "
operator|+
name|propertyName
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|int
name|compare
parameter_list|(
name|C
name|o1
parameter_list|,
name|C
name|o2
parameter_list|)
block|{
if|if
condition|(
operator|(
name|o1
operator|==
literal|null
operator|&&
name|o2
operator|==
literal|null
operator|)
operator|||
name|o1
operator|==
name|o2
condition|)
block|{
return|return
literal|0
return|;
block|}
if|else if
condition|(
name|o1
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
if|else if
condition|(
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
try|try
block|{
name|Comparable
name|p1
init|=
operator|(
name|Comparable
operator|)
name|getter
operator|.
name|invoke
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|Comparable
name|p2
init|=
operator|(
name|Comparable
operator|)
name|getter
operator|.
name|invoke
argument_list|(
name|o2
argument_list|)
decl_stmt|;
return|return
operator|(
name|p1
operator|==
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
operator|(
name|p2
operator|==
literal|null
operator|)
condition|?
literal|1
else|:
name|p1
operator|.
name|compareTo
argument_list|(
name|p2
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error reading property."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
specifier|abstract
name|void
name|sortByColumn
parameter_list|(
name|int
name|sortCol
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|boolean
name|isColumnSortable
parameter_list|(
name|int
name|sortCol
parameter_list|)
function_decl|;
specifier|public
name|void
name|sortByElementProperty
parameter_list|(
name|String
name|string
parameter_list|,
name|boolean
name|isAscent
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|objectList
argument_list|,
operator|new
name|PropertyComparator
argument_list|<>
argument_list|(
name|string
argument_list|,
name|getElementsClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isAscent
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|objectList
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

