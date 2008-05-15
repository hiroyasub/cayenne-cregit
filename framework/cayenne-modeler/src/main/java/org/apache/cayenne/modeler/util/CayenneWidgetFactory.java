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
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Insets
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
name|javax
operator|.
name|swing
operator|.
name|DefaultComboBoxModel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JButton
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
name|JComponent
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
name|SwingConstants
import|;
end_import

begin_comment
comment|/**  * Utility class to create standard Swing widgets following default look-and-feel of  * CayenneModeler.  *   * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// TODO: (Andrus) investigate performance impact of substituting
end_comment

begin_comment
comment|// constructors for all new widgets with cloning the prototype
end_comment

begin_class
specifier|public
class|class
name|CayenneWidgetFactory
block|{
comment|/**      * Not intended for instantiation.      */
specifier|protected
name|CayenneWidgetFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a new JComboBox with a collection of model objects.      */
specifier|public
specifier|static
name|JComboBox
name|createComboBox
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
block|{
return|return
name|createComboBox
argument_list|(
name|model
operator|.
name|toArray
argument_list|()
argument_list|,
name|sort
argument_list|)
return|;
block|}
comment|/**      * Creates a new JComboBox with an array of model objects.      */
specifier|public
specifier|static
name|JComboBox
name|createComboBox
parameter_list|(
name|Object
index|[]
name|model
parameter_list|,
name|boolean
name|sort
parameter_list|)
block|{
name|JComboBox
name|comboBox
init|=
name|CayenneWidgetFactory
operator|.
name|createComboBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|sort
condition|)
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
name|comboBox
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
comment|/**      * Creates a new JComboBox.      */
specifier|public
specifier|static
name|JComboBox
name|createComboBox
parameter_list|()
block|{
name|JComboBox
name|comboBox
init|=
operator|new
name|JComboBox
argument_list|()
decl_stmt|;
name|initFormWidget
argument_list|(
name|comboBox
argument_list|)
expr_stmt|;
name|comboBox
operator|.
name|setBackground
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
return|return
name|comboBox
return|;
block|}
comment|/**      * Creates a new JTextField with a default columns count of 20.      */
specifier|public
specifier|static
name|JTextField
name|createTextField
parameter_list|()
block|{
return|return
name|createTextField
argument_list|(
literal|20
argument_list|)
return|;
block|}
comment|/**      * Creates a new JTextField with a specified columns count.      */
specifier|public
specifier|static
name|JTextField
name|createTextField
parameter_list|(
name|int
name|columns
parameter_list|)
block|{
specifier|final
name|JTextField
name|textField
init|=
operator|new
name|JTextField
argument_list|(
name|columns
argument_list|)
decl_stmt|;
name|initFormWidget
argument_list|(
name|textField
argument_list|)
expr_stmt|;
name|initTextField
argument_list|(
name|textField
argument_list|)
expr_stmt|;
return|return
name|textField
return|;
block|}
specifier|protected
specifier|static
name|void
name|initTextField
parameter_list|(
specifier|final
name|JTextField
name|textField
parameter_list|)
block|{
comment|// config focus
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
comment|// transfer focus
name|textField
operator|.
name|transferFocus
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initializes a "form" element with a standard font and height.      */
specifier|protected
specifier|static
name|void
name|initFormWidget
parameter_list|(
name|JComponent
name|component
parameter_list|)
block|{
name|component
operator|.
name|setFont
argument_list|(
name|component
operator|.
name|getFont
argument_list|()
operator|.
name|deriveFont
argument_list|(
name|Font
operator|.
name|PLAIN
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
comment|/*          * Dimension size = component.getPreferredSize(); if (size == null) { size = new          * Dimension(); } size.setSize(size.getWidth(), 20);          * component.setPreferredSize(size);          */
block|}
comment|/**      * Creates a borderless button that can be used as a clickable label.      */
specifier|public
specifier|static
name|JButton
name|createLabelButton
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|JButton
name|but
init|=
name|createButton
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|but
operator|.
name|setBorderPainted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|but
operator|.
name|setHorizontalAlignment
argument_list|(
name|SwingConstants
operator|.
name|LEFT
argument_list|)
expr_stmt|;
name|but
operator|.
name|setFocusPainted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|but
operator|.
name|setMargin
argument_list|(
operator|new
name|Insets
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|but
operator|.
name|setBorder
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|but
return|;
block|}
comment|/**      * Creates a normal button.      */
specifier|public
specifier|static
name|JButton
name|createButton
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|JButton
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
end_class

end_unit

