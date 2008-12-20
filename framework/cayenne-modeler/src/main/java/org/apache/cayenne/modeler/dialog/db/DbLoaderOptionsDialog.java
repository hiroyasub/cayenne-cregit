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
name|awt
operator|.
name|BorderLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
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
name|JCheckBox
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
name|JDialog
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
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
name|JPanel
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
name|ChangeEvent
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
name|ChangeListener
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
name|DbLoader
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
name|naming
operator|.
name|NamingStrategy
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
name|ClassLoadingService
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
name|ModelerPreferences
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
name|CayenneDialog
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

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|builder
operator|.
name|DefaultFormBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jgoodies
operator|.
name|forms
operator|.
name|layout
operator|.
name|FormLayout
import|;
end_import

begin_comment
comment|/**  * Dialog for selecting database reverse-engineering parameters.  */
end_comment

begin_class
specifier|public
class|class
name|DbLoaderOptionsDialog
extends|extends
name|CayenneDialog
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbLoaderOptionsDialog
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Preference to store latest strategies      */
specifier|private
specifier|static
specifier|final
name|String
name|STRATEGIES_PREFERENCE
init|=
literal|"recent.preferences"
decl_stmt|;
comment|/**      * Naming strategies to appear in combobox by default      */
specifier|private
specifier|static
specifier|final
name|Vector
argument_list|<
name|String
argument_list|>
name|PREDEFINED_STRATEGIES
init|=
operator|new
name|Vector
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|PREDEFINED_STRATEGIES
operator|.
name|add
argument_list|(
literal|"org.apache.cayenne.map.naming.BasicNamingStrategy"
argument_list|)
expr_stmt|;
name|PREDEFINED_STRATEGIES
operator|.
name|add
argument_list|(
literal|"org.apache.cayenne.modeler.util.SmartNamingStrategy"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CANCEL
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|SELECT
init|=
literal|1
decl_stmt|;
specifier|protected
name|JLabel
name|schemaLabel
decl_stmt|;
specifier|protected
name|JComboBox
name|schemaSelector
decl_stmt|;
specifier|protected
name|JTextField
name|tableNamePatternField
decl_stmt|;
specifier|protected
name|JCheckBox
name|loadProcedures
decl_stmt|;
specifier|protected
name|JTextField
name|procNamePatternField
decl_stmt|;
specifier|protected
name|JLabel
name|procedureLabel
decl_stmt|;
specifier|protected
name|JButton
name|selectButton
decl_stmt|;
specifier|protected
name|JButton
name|cancelButton
decl_stmt|;
comment|/**      * Combobox for naming strategy      */
specifier|protected
name|JComboBox
name|strategyCombo
decl_stmt|;
specifier|protected
name|NamingStrategy
name|strategy
decl_stmt|;
specifier|protected
name|int
name|choice
decl_stmt|;
comment|/**      * Creates and initializes new ChooseSchemaDialog.      */
specifier|public
name|DbLoaderOptionsDialog
parameter_list|(
name|Collection
name|schemas
parameter_list|,
name|String
name|dbUserName
parameter_list|,
name|boolean
name|loadProcedures
parameter_list|)
block|{
name|super
argument_list|(
name|Application
operator|.
name|getFrame
argument_list|()
argument_list|,
literal|"Reengineer DB Schema: Select Options"
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
name|initFromModel
argument_list|(
name|schemas
argument_list|,
name|dbUserName
argument_list|,
name|loadProcedures
argument_list|)
expr_stmt|;
name|pack
argument_list|()
expr_stmt|;
name|setDefaultCloseOperation
argument_list|(
name|JDialog
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|centerWindow
argument_list|()
expr_stmt|;
block|}
comment|/** Sets up the graphical components. */
specifier|protected
name|void
name|init
parameter_list|()
block|{
comment|// create widgets...
name|selectButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Continue"
argument_list|)
expr_stmt|;
name|cancelButton
operator|=
operator|new
name|JButton
argument_list|(
literal|"Cancel"
argument_list|)
expr_stmt|;
name|schemaSelector
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|tableNamePatternField
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|procNamePatternField
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|loadProcedures
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|strategyCombo
operator|=
operator|new
name|JComboBox
argument_list|()
expr_stmt|;
name|strategyCombo
operator|.
name|setEditable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// assemble
name|FormLayout
name|layout
init|=
operator|new
name|FormLayout
argument_list|(
literal|"right:pref, 3dlu, fill:max(170dlu;pref):grow"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|builder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|layout
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|schemaLabel
operator|=
name|builder
operator|.
name|append
argument_list|(
literal|"Select Schema:"
argument_list|,
name|schemaSelector
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Table Name Pattern:"
argument_list|,
name|tableNamePatternField
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Load Procedures:"
argument_list|,
name|loadProcedures
argument_list|)
expr_stmt|;
name|procedureLabel
operator|=
name|builder
operator|.
name|append
argument_list|(
literal|"Procedure Name Pattern:"
argument_list|,
name|procNamePatternField
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Naming Strategy:"
argument_list|,
name|strategyCombo
argument_list|)
expr_stmt|;
name|JPanel
name|buttons
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|RIGHT
argument_list|)
argument_list|)
decl_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|cancelButton
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|selectButton
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|setLayout
argument_list|(
operator|new
name|BorderLayout
argument_list|()
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|builder
operator|.
name|getPanel
argument_list|()
argument_list|,
name|BorderLayout
operator|.
name|CENTER
argument_list|)
expr_stmt|;
name|getContentPane
argument_list|()
operator|.
name|add
argument_list|(
name|buttons
argument_list|,
name|BorderLayout
operator|.
name|SOUTH
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|selectButton
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
name|processSelect
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|cancelButton
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
name|processCancel
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|loadProcedures
operator|.
name|addChangeListener
argument_list|(
operator|new
name|ChangeListener
argument_list|()
block|{
specifier|public
name|void
name|stateChanged
parameter_list|(
name|ChangeEvent
name|e
parameter_list|)
block|{
name|procNamePatternField
operator|.
name|setEnabled
argument_list|(
name|loadProcedures
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
name|procedureLabel
operator|.
name|setEnabled
argument_list|(
name|loadProcedures
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initFromModel
parameter_list|(
name|Collection
name|schemas
parameter_list|,
name|String
name|dbUserName
parameter_list|,
name|boolean
name|shouldLoadProcedures
parameter_list|)
block|{
name|this
operator|.
name|choice
operator|=
name|CANCEL
expr_stmt|;
name|this
operator|.
name|tableNamePatternField
operator|.
name|setText
argument_list|(
name|DbLoader
operator|.
name|WILDCARD
argument_list|)
expr_stmt|;
name|this
operator|.
name|loadProcedures
operator|.
name|setSelected
argument_list|(
name|shouldLoadProcedures
argument_list|)
expr_stmt|;
name|this
operator|.
name|procNamePatternField
operator|.
name|setText
argument_list|(
name|DbLoader
operator|.
name|WILDCARD
argument_list|)
expr_stmt|;
name|this
operator|.
name|procNamePatternField
operator|.
name|setEnabled
argument_list|(
name|shouldLoadProcedures
argument_list|)
expr_stmt|;
name|this
operator|.
name|procedureLabel
operator|.
name|setEnabled
argument_list|(
name|shouldLoadProcedures
argument_list|)
expr_stmt|;
name|ModelerPreferences
name|pref
init|=
name|ModelerPreferences
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
name|Vector
argument_list|<
name|?
argument_list|>
name|arr
init|=
name|pref
operator|.
name|getVector
argument_list|(
name|STRATEGIES_PREFERENCE
argument_list|,
name|PREDEFINED_STRATEGIES
argument_list|)
decl_stmt|;
name|strategyCombo
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|arr
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|showSchemaSelector
init|=
name|schemas
operator|!=
literal|null
operator|&&
operator|!
name|schemas
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|schemaSelector
operator|.
name|setVisible
argument_list|(
name|showSchemaSelector
argument_list|)
expr_stmt|;
name|schemaLabel
operator|.
name|setVisible
argument_list|(
name|showSchemaSelector
argument_list|)
expr_stmt|;
if|if
condition|(
name|showSchemaSelector
condition|)
block|{
name|schemaSelector
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|(
name|schemas
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// select schema belonging to the user
if|if
condition|(
name|dbUserName
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|schemas
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|schema
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbUserName
operator|.
name|equalsIgnoreCase
argument_list|(
name|schema
argument_list|)
condition|)
block|{
name|schemaSelector
operator|.
name|setSelectedItem
argument_list|(
name|schema
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|int
name|getChoice
parameter_list|()
block|{
return|return
name|choice
return|;
block|}
specifier|private
name|void
name|processSelect
parameter_list|()
block|{
try|try
block|{
name|ClassLoadingService
name|classLoader
init|=
name|Application
operator|.
name|getInstance
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
decl_stmt|;
name|String
name|strategyClass
init|=
operator|(
name|String
operator|)
name|strategyCombo
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|this
operator|.
name|strategy
operator|=
operator|(
name|NamingStrategy
operator|)
name|classLoader
operator|.
name|loadClass
argument_list|(
name|strategyClass
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|/**              * Be user-friendly and update preferences with specified strategy              */
name|ModelerPreferences
name|pref
init|=
name|ModelerPreferences
operator|.
name|getPreferences
argument_list|()
decl_stmt|;
name|Vector
name|arr
init|=
name|pref
operator|.
name|getVector
argument_list|(
name|STRATEGIES_PREFERENCE
argument_list|,
name|PREDEFINED_STRATEGIES
argument_list|)
decl_stmt|;
comment|//move to top
name|arr
operator|.
name|remove
argument_list|(
name|strategyClass
argument_list|)
expr_stmt|;
name|arr
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|strategyClass
argument_list|)
expr_stmt|;
name|pref
operator|.
name|setProperty
argument_list|(
name|STRATEGIES_PREFERENCE
argument_list|,
name|arr
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logObj
operator|.
name|error
argument_list|(
literal|"Error in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|th
argument_list|)
expr_stmt|;
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
argument_list|,
literal|"Naming Strategy Initialization Error: "
operator|+
name|th
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|"Naming Strategy Initialization Error"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
return|return;
block|}
name|choice
operator|=
name|SELECT
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processCancel
parameter_list|()
block|{
name|choice
operator|=
name|CANCEL
expr_stmt|;
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns selected schema.      */
specifier|public
name|String
name|getSelectedSchema
parameter_list|()
block|{
name|String
name|schema
init|=
operator|(
name|String
operator|)
name|schemaSelector
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
return|return
literal|""
operator|.
name|equals
argument_list|(
name|schema
argument_list|)
condition|?
literal|null
else|:
name|schema
return|;
block|}
comment|/**      * Returns the tableNamePattern.      */
specifier|public
name|String
name|getTableNamePattern
parameter_list|()
block|{
return|return
literal|""
operator|.
name|equals
argument_list|(
name|tableNamePatternField
operator|.
name|getText
argument_list|()
argument_list|)
condition|?
literal|null
else|:
name|tableNamePatternField
operator|.
name|getText
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isLoadingProcedures
parameter_list|()
block|{
return|return
name|loadProcedures
operator|.
name|isSelected
argument_list|()
return|;
block|}
comment|/**      * Returns the procedure name pattern.      */
specifier|public
name|String
name|getProcedureNamePattern
parameter_list|()
block|{
return|return
literal|""
operator|.
name|equals
argument_list|(
name|procNamePatternField
operator|.
name|getText
argument_list|()
argument_list|)
condition|?
literal|null
else|:
name|procNamePatternField
operator|.
name|getText
argument_list|()
return|;
block|}
comment|/**      * Returns configured naming strategy      */
specifier|public
name|NamingStrategy
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|strategy
return|;
block|}
block|}
end_class

end_unit

