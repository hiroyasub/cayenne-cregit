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
name|java
operator|.
name|util
operator|.
name|Vector
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
name|ReverseEngineering
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
name|util
operator|.
name|NameGeneratorPreferences
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
name|TextAdapter
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
name|combo
operator|.
name|AutoCompletion
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
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringConfigPanel
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DATA_FIELDS_LAYOUT
init|=
literal|"right:pref, 3dlu, fill:235dlu"
decl_stmt|;
specifier|private
name|JComboBox
argument_list|<
name|String
argument_list|>
name|strategyCombo
decl_stmt|;
specifier|private
name|TextAdapter
name|meaningfulPk
decl_stmt|;
specifier|private
name|TextAdapter
name|stripFromTableNames
decl_stmt|;
specifier|private
name|JCheckBox
name|skipRelationshipsLoading
decl_stmt|;
specifier|private
name|JCheckBox
name|skipPrimaryKeyLoading
decl_stmt|;
specifier|private
name|JCheckBox
name|forceDataMapCatalog
decl_stmt|;
specifier|private
name|JCheckBox
name|forceDataMapSchema
decl_stmt|;
specifier|private
name|JCheckBox
name|usePrimitives
decl_stmt|;
specifier|private
name|JCheckBox
name|useJava7Types
decl_stmt|;
specifier|private
name|TextAdapter
name|tableTypes
decl_stmt|;
specifier|private
name|ProjectController
name|projectController
decl_stmt|;
specifier|private
name|DbImportView
name|dbImportView
decl_stmt|;
name|ReverseEngineeringConfigPanel
parameter_list|(
name|ProjectController
name|projectController
parameter_list|,
name|DbImportView
name|dbImportView
parameter_list|)
block|{
name|this
operator|.
name|projectController
operator|=
name|projectController
expr_stmt|;
name|this
operator|.
name|dbImportView
operator|=
name|dbImportView
expr_stmt|;
name|initFormElements
argument_list|()
expr_stmt|;
name|initListeners
argument_list|()
expr_stmt|;
name|buildView
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|buildView
parameter_list|()
block|{
name|FormLayout
name|panelLayout
init|=
operator|new
name|FormLayout
argument_list|(
name|DATA_FIELDS_LAYOUT
argument_list|)
decl_stmt|;
name|DefaultFormBuilder
name|panelBuilder
init|=
operator|new
name|DefaultFormBuilder
argument_list|(
name|panelLayout
argument_list|)
decl_stmt|;
name|panelBuilder
operator|.
name|setDefaultDialogBorder
argument_list|()
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Tables with Meaningful PK Pattern:"
argument_list|,
name|meaningfulPk
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Strip from table names:"
argument_list|,
name|stripFromTableNames
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Skip relationships loading:"
argument_list|,
name|skipRelationshipsLoading
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Skip primary key loading:"
argument_list|,
name|skipPrimaryKeyLoading
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Force datamap catalog:"
argument_list|,
name|forceDataMapCatalog
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Force datamap schema:"
argument_list|,
name|forceDataMapSchema
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Use Java primitive types:"
argument_list|,
name|usePrimitives
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Use java.util.Date type:"
argument_list|,
name|useJava7Types
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Naming strategy:"
argument_list|,
name|strategyCombo
argument_list|)
expr_stmt|;
name|panelBuilder
operator|.
name|append
argument_list|(
literal|"Table types:"
argument_list|,
name|tableTypes
operator|.
name|getComponent
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|panelBuilder
operator|.
name|getPanel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|fillCheckboxes
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|skipRelationshipsLoading
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|getSkipRelationshipsLoading
argument_list|()
argument_list|)
expr_stmt|;
name|skipPrimaryKeyLoading
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|getSkipPrimaryKeyLoading
argument_list|()
argument_list|)
expr_stmt|;
name|forceDataMapCatalog
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|isForceDataMapCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|forceDataMapSchema
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|isForceDataMapSchema
argument_list|()
argument_list|)
expr_stmt|;
name|usePrimitives
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|isUsePrimitives
argument_list|()
argument_list|)
expr_stmt|;
name|useJava7Types
operator|.
name|setSelected
argument_list|(
name|reverseEngineering
operator|.
name|isUseJava7Types
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|void
name|initializeTextFields
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|meaningfulPk
operator|.
name|setText
argument_list|(
name|reverseEngineering
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|stripFromTableNames
operator|.
name|setText
argument_list|(
name|reverseEngineering
operator|.
name|getStripFromTableNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ReverseEngineering
name|getReverseEngineeringBySelectedMap
parameter_list|()
block|{
name|DataMap
name|dataMap
init|=
name|projectController
operator|.
name|getCurrentDataMap
argument_list|()
decl_stmt|;
return|return
name|projectController
operator|.
name|getApplication
argument_list|()
operator|.
name|getMetaData
argument_list|()
operator|.
name|get
argument_list|(
name|dataMap
argument_list|,
name|ReverseEngineering
operator|.
name|class
argument_list|)
return|;
block|}
name|void
name|initStrategy
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|)
block|{
name|Vector
argument_list|<
name|String
argument_list|>
name|arr
init|=
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|getLastUsedStrategies
argument_list|()
decl_stmt|;
name|strategyCombo
operator|.
name|setModel
argument_list|(
operator|new
name|DefaultComboBoxModel
argument_list|<>
argument_list|(
name|arr
argument_list|)
argument_list|)
expr_stmt|;
name|strategyCombo
operator|.
name|setSelectedItem
argument_list|(
name|reverseEngineering
operator|.
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initFormElements
parameter_list|()
block|{
name|strategyCombo
operator|=
name|Application
operator|.
name|getWidgetFactory
argument_list|()
operator|.
name|createComboBox
argument_list|()
expr_stmt|;
name|AutoCompletion
operator|.
name|enable
argument_list|(
name|strategyCombo
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|strategyCombo
operator|.
name|setToolTipText
argument_list|(
literal|"Naming strategy to use"
argument_list|)
expr_stmt|;
name|JTextField
name|meaningfulPkField
init|=
operator|new
name|JTextField
argument_list|()
decl_stmt|;
name|meaningfulPkField
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Regular expression to filter tables with meaningful primary keys.<br>"
operator|+
literal|"Multiple expressions divided by comma can be used.<br>"
operator|+
literal|"Example:<b>^table1|^table2|^prefix.*|table_name</b></html>"
argument_list|)
expr_stmt|;
name|meaningfulPk
operator|=
operator|new
name|TextAdapter
argument_list|(
name|meaningfulPkField
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setMeaningfulPkTables
argument_list|(
name|text
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|JTextField
name|stripFromTableNamesField
init|=
operator|new
name|JTextField
argument_list|()
decl_stmt|;
name|stripFromTableNamesField
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Regex that matches the part of the table name that needs to be stripped off "
operator|+
literal|"when generating ObjEntity name</html>"
argument_list|)
expr_stmt|;
name|stripFromTableNames
operator|=
operator|new
name|TextAdapter
argument_list|(
name|stripFromTableNamesField
argument_list|)
block|{
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setStripFromTableNames
argument_list|(
name|text
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
name|JTextField
name|tableTypesField
init|=
operator|new
name|JTextField
argument_list|()
decl_stmt|;
name|tableTypesField
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Default types to import is TABLE and VIEW."
argument_list|)
expr_stmt|;
name|tableTypes
operator|=
operator|new
name|TextAdapter
argument_list|(
name|tableTypesField
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|updateModel
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ValidationException
block|{
name|ReverseEngineering
name|reverseEngineering
init|=
name|getReverseEngineeringBySelectedMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|tableTypesFromReverseEngineering
init|=
name|reverseEngineering
operator|.
name|getTableTypes
argument_list|()
decl_stmt|;
name|tableTypes
operator|.
name|setText
argument_list|(
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|tableTypesFromReverseEngineering
argument_list|)
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
literal|"Table types field can't be empty."
argument_list|,
literal|"Error setting table types"
argument_list|,
name|JOptionPane
operator|.
name|ERROR_MESSAGE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reverseEngineering
operator|.
name|getTableTypesCollection
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|String
index|[]
name|types
init|=
name|text
operator|.
name|split
argument_list|(
literal|"\\s*,\\s*"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|type
range|:
name|types
control|)
block|{
if|if
condition|(
operator|!
name|type
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|reverseEngineering
operator|.
name|addTableType
argument_list|(
name|type
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
expr_stmt|;
name|skipRelationshipsLoading
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|skipRelationshipsLoading
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Whether to load relationships.</html>"
argument_list|)
expr_stmt|;
name|skipPrimaryKeyLoading
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|skipPrimaryKeyLoading
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Whether to load primary keys.</html>"
argument_list|)
expr_stmt|;
name|forceDataMapCatalog
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|forceDataMapCatalog
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Automatically tagging each DbEntity with the actual DB catalog/schema"
operator|+
literal|"(default behavior) may sometimes be undesirable.<br>  If this is the case then setting<b>forceDataMapCatalog</b> "
operator|+
literal|"to<b>true</b> will set DbEntity catalog to one in the DataMap.</html>"
argument_list|)
expr_stmt|;
name|forceDataMapSchema
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|forceDataMapSchema
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Automatically tagging each DbEntity with the actual DB catalog/schema "
operator|+
literal|"(default behavior) may sometimes be undesirable.<br> If this is the case then setting<b>forceDataMapSchema</b> "
operator|+
literal|"to<b>true</b> will set DbEntity schema to one in the DataMap.</html>"
argument_list|)
expr_stmt|;
name|useJava7Types
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|useJava7Types
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Use<b>java.util.Date</b> for all columns with<i>DATE/TIME/TIMESTAMP</i> types.<br>"
operator|+
literal|"By default<b>java.time.*</b> types will be used.</html>"
argument_list|)
expr_stmt|;
name|usePrimitives
operator|=
operator|new
name|JCheckBox
argument_list|()
expr_stmt|;
name|usePrimitives
operator|.
name|setToolTipText
argument_list|(
literal|"<html>Use primitive types (e.g. int) or Object types (e.g. java.lang.Integer)</html>"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initListeners
parameter_list|()
block|{
name|skipRelationshipsLoading
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setSkipRelationshipsLoading
argument_list|(
name|skipRelationshipsLoading
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|skipPrimaryKeyLoading
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setSkipPrimaryKeyLoading
argument_list|(
name|skipPrimaryKeyLoading
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|forceDataMapCatalog
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setForceDataMapCatalog
argument_list|(
name|forceDataMapCatalog
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|forceDataMapSchema
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setForceDataMapSchema
argument_list|(
name|forceDataMapSchema
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|usePrimitives
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setUsePrimitives
argument_list|(
name|usePrimitives
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|useJava7Types
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setUseJava7Types
argument_list|(
name|useJava7Types
operator|.
name|isSelected
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|strategyCombo
operator|.
name|addActionListener
argument_list|(
name|e
lambda|->
block|{
name|String
name|strategy
init|=
operator|(
name|String
operator|)
name|ReverseEngineeringConfigPanel
operator|.
name|this
operator|.
name|getStrategyCombo
argument_list|()
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
name|checkStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|getReverseEngineeringBySelectedMap
argument_list|()
operator|.
name|setNamingStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
name|NameGeneratorPreferences
operator|.
name|getInstance
argument_list|()
operator|.
name|addToLastUsedStrategies
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dbImportView
operator|.
name|isInitFromModel
argument_list|()
condition|)
block|{
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkStrategy
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|this
argument_list|,
name|strategy
operator|+
literal|" not found. Please, add naming strategy to classpath."
argument_list|,
literal|"Error"
argument_list|,
name|JOptionPane
operator|.
name|WARNING_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
name|JComboBox
argument_list|<
name|String
argument_list|>
name|getStrategyCombo
parameter_list|()
block|{
return|return
name|strategyCombo
return|;
block|}
name|TextAdapter
name|getMeaningfulPk
parameter_list|()
block|{
return|return
name|meaningfulPk
return|;
block|}
name|TextAdapter
name|getStripFromTableNames
parameter_list|()
block|{
return|return
name|stripFromTableNames
return|;
block|}
name|JCheckBox
name|getSkipRelationshipsLoading
parameter_list|()
block|{
return|return
name|skipRelationshipsLoading
return|;
block|}
name|JCheckBox
name|getSkipPrimaryKeyLoading
parameter_list|()
block|{
return|return
name|skipPrimaryKeyLoading
return|;
block|}
name|JCheckBox
name|getForceDataMapCatalog
parameter_list|()
block|{
return|return
name|forceDataMapCatalog
return|;
block|}
name|JCheckBox
name|getForceDataMapSchema
parameter_list|()
block|{
return|return
name|forceDataMapSchema
return|;
block|}
name|JCheckBox
name|getUsePrimitives
parameter_list|()
block|{
return|return
name|usePrimitives
return|;
block|}
name|JCheckBox
name|getUseJava7Types
parameter_list|()
block|{
return|return
name|useJava7Types
return|;
block|}
name|TextAdapter
name|getTableTypes
parameter_list|()
block|{
return|return
name|tableTypes
return|;
block|}
block|}
end_class

end_unit

