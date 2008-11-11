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
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|InputVerifier
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextArea
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|DocumentEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|DocumentListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|JTextComponent
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
name|validator
operator|.
name|ValidatorDialog
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
name|ValidationException
import|;
end_import

begin_comment
comment|/**  * A validating adapter for JTextComponent. Implement {@link #updateModel(String)}to  * initialize model on text change.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|TextAdapter
block|{
specifier|protected
name|Color
name|defaultBGColor
decl_stmt|;
specifier|protected
name|Color
name|errorColor
decl_stmt|;
specifier|protected
name|JTextComponent
name|textComponent
decl_stmt|;
specifier|protected
name|String
name|defaultToolTip
decl_stmt|;
specifier|protected
name|boolean
name|modelUpdateDisabled
decl_stmt|;
specifier|public
name|TextAdapter
parameter_list|(
name|JTextField
name|textField
parameter_list|)
block|{
name|this
argument_list|(
name|textField
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TextAdapter
parameter_list|(
name|JTextField
name|textField
parameter_list|,
name|boolean
name|checkOnFocusLost
parameter_list|,
name|boolean
name|checkOnTyping
parameter_list|,
name|boolean
name|checkOnEnter
parameter_list|)
block|{
name|this
argument_list|(
name|textField
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|checkOnEnter
condition|)
block|{
name|textField
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|e
parameter_list|)
block|{
name|updateModel
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|TextAdapter
parameter_list|(
name|JTextArea
name|textField
parameter_list|)
block|{
name|this
argument_list|(
name|textField
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TextAdapter
parameter_list|(
name|JTextComponent
name|textComponent
parameter_list|,
name|boolean
name|checkOnFocusLost
parameter_list|,
name|boolean
name|checkOnTyping
parameter_list|)
block|{
name|this
operator|.
name|errorColor
operator|=
name|ValidatorDialog
operator|.
name|WARNING_COLOR
expr_stmt|;
name|this
operator|.
name|defaultBGColor
operator|=
name|textComponent
operator|.
name|getBackground
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultToolTip
operator|=
name|textComponent
operator|.
name|getToolTipText
argument_list|()
expr_stmt|;
name|this
operator|.
name|textComponent
operator|=
name|textComponent
expr_stmt|;
if|if
condition|(
name|checkOnFocusLost
condition|)
block|{
name|textComponent
operator|.
name|setInputVerifier
argument_list|(
operator|new
name|InputVerifier
argument_list|()
block|{
specifier|public
name|boolean
name|verify
parameter_list|(
name|JComponent
name|c
parameter_list|)
block|{
name|updateModel
argument_list|()
expr_stmt|;
comment|// release focus after coloring the field...
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checkOnTyping
condition|)
block|{
name|textComponent
operator|.
name|getDocument
argument_list|()
operator|.
name|addDocumentListener
argument_list|(
operator|new
name|DocumentListener
argument_list|()
block|{
specifier|public
name|void
name|insertUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|verifyTextChange
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|changedUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|verifyTextChange
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeUpdate
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
name|verifyTextChange
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|void
name|verifyTextChange
parameter_list|(
name|DocumentEvent
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|modelUpdateDisabled
condition|)
block|{
name|updateModel
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Updates bound model with document text.      */
specifier|protected
specifier|abstract
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**      * Returns internal text component.      */
specifier|public
name|JTextComponent
name|getComponent
parameter_list|()
block|{
return|return
name|textComponent
return|;
block|}
comment|/**      * Sets the text of the underlying text field.      */
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|modelUpdateDisabled
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|clear
argument_list|()
expr_stmt|;
name|textComponent
operator|.
name|setText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|modelUpdateDisabled
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|updateModel
parameter_list|()
block|{
try|try
block|{
name|updateModel
argument_list|(
name|textComponent
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|vex
parameter_list|)
block|{
name|textComponent
operator|.
name|setBackground
argument_list|(
name|errorColor
argument_list|)
expr_stmt|;
name|textComponent
operator|.
name|setToolTipText
argument_list|(
name|vex
operator|.
name|getUnlabeledMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|clear
parameter_list|()
block|{
name|textComponent
operator|.
name|setBackground
argument_list|(
name|defaultBGColor
argument_list|)
expr_stmt|;
name|textComponent
operator|.
name|setToolTipText
argument_list|(
name|defaultToolTip
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

