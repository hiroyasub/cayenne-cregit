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
name|util
operator|.
name|combo
package|;
end_package

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
name|javax
operator|.
name|swing
operator|.
name|ComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComboBox
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|plaf
operator|.
name|basic
operator|.
name|BasicComboBoxEditor
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
name|CellRenderers
import|;
end_import

begin_comment
comment|/**  * CustomTypeComboBoxEditor is used as an editor of a combobox, when  * custom type (such as Entity) is to be used. BasicComboBoxEditor  * cannot be used, because it converts String values to other types  * incorrectly (in fact, only classes with valueOf(String) methods  * are supported).  *   */
end_comment

begin_class
specifier|public
class|class
name|CustomTypeComboBoxEditor
extends|extends
name|BasicComboBoxEditor
block|{
comment|/**      * 'oldValue' property is private somewhy, so we make our local      * copy      */
specifier|protected
name|Object
name|localOldValue
decl_stmt|;
comment|/**      * The combobox being edited      */
specifier|protected
specifier|final
name|JComboBox
name|combo
decl_stmt|;
comment|/**      * Whether non-present items are allowed      */
specifier|protected
specifier|final
name|boolean
name|allowsUserValues
decl_stmt|;
comment|/**      * Creates new editor      * @param combo ComboBox being edited      */
specifier|public
name|CustomTypeComboBoxEditor
parameter_list|(
name|JComboBox
name|combo
parameter_list|,
name|boolean
name|allowsUserValues
parameter_list|)
block|{
name|editor
operator|=
operator|new
name|EditorTextField
argument_list|(
name|combo
argument_list|)
expr_stmt|;
name|this
operator|.
name|combo
operator|=
name|combo
expr_stmt|;
name|this
operator|.
name|allowsUserValues
operator|=
name|allowsUserValues
expr_stmt|;
block|}
comment|/**       * Sets the item that should be edited.       *      * @param anObject the displayed value of the editor      */
annotation|@
name|Override
specifier|public
name|void
name|setItem
parameter_list|(
name|Object
name|anObject
parameter_list|)
block|{
name|localOldValue
operator|=
name|anObject
expr_stmt|;
name|super
operator|.
name|setItem
argument_list|(
name|anObject
operator|==
literal|null
condition|?
literal|null
else|:
name|CellRenderers
operator|.
name|asString
argument_list|(
name|anObject
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return edited item      */
annotation|@
name|Override
specifier|public
name|Object
name|getItem
parameter_list|()
block|{
name|Object
name|newValue
init|=
name|editor
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|localOldValue
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|localOldValue
operator|instanceof
name|String
operator|)
condition|)
block|{
comment|// The original value is not a string. Should return the value in it's
comment|// original type.
if|if
condition|(
name|newValue
operator|.
name|equals
argument_list|(
name|localOldValue
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|localOldValue
return|;
block|}
else|else
block|{
comment|// Must take the value from the editor and get the value and cast it to the new type.
name|Class
name|cls
init|=
name|localOldValue
operator|.
name|getClass
argument_list|()
decl_stmt|;
try|try
block|{
name|newValue
operator|=
name|convert
argument_list|(
operator|(
name|String
operator|)
name|newValue
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
block|}
block|}
block|}
if|if
condition|(
operator|!
name|allowsUserValues
operator|&&
name|newValue
operator|!=
literal|null
condition|)
block|{
name|boolean
name|contains
init|=
literal|false
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
name|combo
operator|.
name|getItemCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|newValue
operator|.
name|equals
argument_list|(
name|combo
operator|.
name|getItemAt
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|contains
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|contains
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|newValue
return|;
block|}
comment|/**      * Converts String value to specified type      *      * @param value String value of textfield      * @param classTo type of result item      *       * @return value of classTo type, or null if conversion is impossible      */
specifier|protected
name|Object
name|convert
parameter_list|(
name|String
name|value
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|classTo
parameter_list|)
block|{
if|if
condition|(
name|classTo
operator|==
name|String
operator|.
name|class
condition|)
block|{
return|return
name|value
return|;
block|}
comment|/*          * We still try to it in BasicComboBox's way, so that primary object          * types (such as numbers) would still be supported           */
try|try
block|{
name|Method
name|method
init|=
name|classTo
operator|.
name|getMethod
argument_list|(
literal|"valueOf"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|method
operator|.
name|invoke
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
block|}
comment|/*          * We could manually convert strings to dbentities, attrs and other, but          * in this implementation we use reverse operation instead, and convert           * combobox model's items to String.          * All string values are assumed unique is one model.          */
name|ComboBoxModel
name|model
init|=
name|combo
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
name|model
operator|.
name|getSize
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|CellRenderers
operator|.
name|asString
argument_list|(
name|model
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|model
operator|.
name|getElementAt
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
comment|//we return null, since String will not be appreciated
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

