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
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JFileChooser
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
name|WindowConstants
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
name|DbGenerator
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
name|DbAdapter
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
name|dialog
operator|.
name|ValidationResultBrowser
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
name|pref
operator|.
name|DBConnectionInfo
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
name|pref
operator|.
name|DBGeneratorDefaults
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
name|CayenneController
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
name|swing
operator|.
name|BindingBuilder
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
name|swing
operator|.
name|ObjectBinding
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
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DBGeneratorOptions
extends|extends
name|CayenneController
block|{
specifier|protected
name|DBGeneratorOptionsView
name|view
decl_stmt|;
specifier|protected
name|ObjectBinding
index|[]
name|optionBindings
decl_stmt|;
specifier|protected
name|ObjectBinding
name|sqlBinding
decl_stmt|;
specifier|protected
name|DBConnectionInfo
name|connectionInfo
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|DBGeneratorDefaults
name|generatorDefaults
decl_stmt|;
specifier|protected
name|DbGenerator
name|generator
decl_stmt|;
specifier|protected
name|String
name|textForSQL
decl_stmt|;
specifier|protected
name|TableSelectorController
name|tables
decl_stmt|;
specifier|public
name|DBGeneratorOptions
parameter_list|(
name|ProjectController
name|parent
parameter_list|,
name|String
name|title
parameter_list|,
name|DBConnectionInfo
name|connectionInfo
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|tables
operator|=
operator|new
name|TableSelectorController
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|DBGeneratorOptionsView
argument_list|(
name|tables
operator|.
name|getView
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionInfo
operator|=
name|connectionInfo
expr_stmt|;
name|this
operator|.
name|generatorDefaults
operator|=
operator|(
name|DBGeneratorDefaults
operator|)
name|parent
operator|.
name|getPreferenceDomainForProject
argument_list|()
operator|.
name|getDetail
argument_list|(
literal|"DbGenerator"
argument_list|,
name|DBGeneratorDefaults
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|initController
argument_list|()
expr_stmt|;
name|tables
operator|.
name|updateTables
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|prepareGenerator
argument_list|()
expr_stmt|;
name|generatorDefaults
operator|.
name|configureGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
name|createSQL
argument_list|()
expr_stmt|;
name|refreshView
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Component
name|getView
parameter_list|()
block|{
return|return
name|view
return|;
block|}
specifier|public
name|DBGeneratorDefaults
name|getGeneratorDefaults
parameter_list|()
block|{
return|return
name|generatorDefaults
return|;
block|}
specifier|public
name|String
name|getTextForSQL
parameter_list|()
block|{
return|return
name|textForSQL
return|;
block|}
specifier|protected
name|void
name|initController
parameter_list|()
block|{
name|BindingBuilder
name|builder
init|=
operator|new
name|BindingBuilder
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getBindingFactory
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|sqlBinding
operator|=
name|builder
operator|.
name|bindToTextArea
argument_list|(
name|view
operator|.
name|getSql
argument_list|()
argument_list|,
literal|"textForSQL"
argument_list|)
expr_stmt|;
name|optionBindings
operator|=
operator|new
name|ObjectBinding
index|[
literal|5
index|]
expr_stmt|;
name|optionBindings
index|[
literal|0
index|]
operator|=
name|builder
operator|.
name|bindToStateChangeAndAction
argument_list|(
name|view
operator|.
name|getCreateFK
argument_list|()
argument_list|,
literal|"generatorDefaults.createFK"
argument_list|,
literal|"refreshSQLAction()"
argument_list|)
expr_stmt|;
name|optionBindings
index|[
literal|1
index|]
operator|=
name|builder
operator|.
name|bindToStateChangeAndAction
argument_list|(
name|view
operator|.
name|getCreatePK
argument_list|()
argument_list|,
literal|"generatorDefaults.createPK"
argument_list|,
literal|"refreshSQLAction()"
argument_list|)
expr_stmt|;
name|optionBindings
index|[
literal|2
index|]
operator|=
name|builder
operator|.
name|bindToStateChangeAndAction
argument_list|(
name|view
operator|.
name|getCreateTables
argument_list|()
argument_list|,
literal|"generatorDefaults.createTables"
argument_list|,
literal|"refreshSQLAction()"
argument_list|)
expr_stmt|;
name|optionBindings
index|[
literal|3
index|]
operator|=
name|builder
operator|.
name|bindToStateChangeAndAction
argument_list|(
name|view
operator|.
name|getDropPK
argument_list|()
argument_list|,
literal|"generatorDefaults.dropPK"
argument_list|,
literal|"refreshSQLAction()"
argument_list|)
expr_stmt|;
name|optionBindings
index|[
literal|4
index|]
operator|=
name|builder
operator|.
name|bindToStateChangeAndAction
argument_list|(
name|view
operator|.
name|getDropTables
argument_list|()
argument_list|,
literal|"generatorDefaults.dropTables"
argument_list|,
literal|"refreshSQLAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getGenerateButton
argument_list|()
argument_list|,
literal|"generateSchemaAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getSaveSqlButton
argument_list|()
argument_list|,
literal|"storeSQLAction()"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|bindToAction
argument_list|(
name|view
operator|.
name|getCancelButton
argument_list|()
argument_list|,
literal|"closeAction()"
argument_list|)
expr_stmt|;
comment|// refresh SQL if different tables were selected
name|view
operator|.
name|getTabs
argument_list|()
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
if|if
condition|(
name|view
operator|.
name|getTabs
argument_list|()
operator|.
name|getSelectedIndex
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// this assumes that some tables where checked/unchecked... not very
comment|// efficient
name|refreshGeneratorAction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates new internal DbGenerator instance.      */
specifier|protected
name|void
name|prepareGenerator
parameter_list|()
block|{
try|try
block|{
name|DbAdapter
name|adapter
init|=
name|connectionInfo
operator|.
name|makeAdapter
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|generator
operator|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|dataMap
argument_list|,
name|tables
operator|.
name|getExcludedTables
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"Error loading adapter"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns SQL statements generated for selected schema generation options.      */
specifier|protected
name|void
name|createSQL
parameter_list|()
block|{
comment|// convert them to string representation for display
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|Iterator
name|it
init|=
name|generator
operator|.
name|configuredStatements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|batchTerminator
init|=
name|generator
operator|.
name|getAdapter
argument_list|()
operator|.
name|getBatchTerminator
argument_list|()
decl_stmt|;
name|String
name|lineEnd
init|=
operator|(
name|batchTerminator
operator|!=
literal|null
operator|)
condition|?
literal|"\n"
operator|+
name|batchTerminator
operator|+
literal|"\n\n"
else|:
literal|"\n\n"
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|lineEnd
argument_list|)
expr_stmt|;
block|}
name|textForSQL
operator|=
name|buf
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|refreshView
parameter_list|()
block|{
for|for
control|(
name|ObjectBinding
name|optionBinding
range|:
name|optionBindings
control|)
block|{
name|optionBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
name|sqlBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
comment|// ===============
comment|//    Actions
comment|// ===============
comment|/**      * Starts options dialog.      */
specifier|public
name|void
name|startupAction
parameter_list|()
block|{
name|view
operator|.
name|pack
argument_list|()
expr_stmt|;
name|view
operator|.
name|setDefaultCloseOperation
argument_list|(
name|WindowConstants
operator|.
name|DISPOSE_ON_CLOSE
argument_list|)
expr_stmt|;
name|view
operator|.
name|setModal
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|makeCloseableOnEscape
argument_list|()
expr_stmt|;
name|centerView
argument_list|()
expr_stmt|;
name|view
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|refreshGeneratorAction
parameter_list|()
block|{
name|prepareGenerator
argument_list|()
expr_stmt|;
name|refreshSQLAction
argument_list|()
expr_stmt|;
block|}
comment|/**      * Updates a text area showing generated SQL.      */
specifier|public
name|void
name|refreshSQLAction
parameter_list|()
block|{
comment|// sync generator with defaults, make SQL, then sync the view...
name|generatorDefaults
operator|.
name|configureGenerator
argument_list|(
name|generator
argument_list|)
expr_stmt|;
name|createSQL
argument_list|()
expr_stmt|;
name|sqlBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
comment|/**      * Performs configured schema operations via DbGenerator.      */
specifier|public
name|void
name|generateSchemaAction
parameter_list|()
block|{
name|refreshGeneratorAction
argument_list|()
expr_stmt|;
comment|// sanity check...
if|if
condition|(
name|generator
operator|.
name|isEmpty
argument_list|(
literal|true
argument_list|)
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|getView
argument_list|()
argument_list|,
literal|"Nothing to generate."
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|DataSource
name|dataSource
init|=
name|connectionInfo
operator|.
name|makeDataSource
argument_list|(
name|getApplication
argument_list|()
operator|.
name|getClassLoadingService
argument_list|()
argument_list|)
decl_stmt|;
name|generator
operator|.
name|runGenerator
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
name|ValidationResult
name|failures
init|=
name|generator
operator|.
name|getFailures
argument_list|()
decl_stmt|;
if|if
condition|(
name|failures
operator|==
literal|null
operator|||
operator|!
name|failures
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
name|JOptionPane
operator|.
name|showMessageDialog
argument_list|(
name|getView
argument_list|()
argument_list|,
literal|"Schema Generation Complete."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|new
name|ValidationResultBrowser
argument_list|(
name|this
argument_list|)
operator|.
name|startupAction
argument_list|(
literal|"Schema Generation Complete"
argument_list|,
literal|"Schema generation finished. The following problem(s) were ignored."
argument_list|,
name|failures
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"Schema Generation Error"
argument_list|,
name|th
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Allows user to save generated SQL in a file.      */
specifier|public
name|void
name|storeSQLAction
parameter_list|()
block|{
name|JFileChooser
name|fc
init|=
operator|new
name|JFileChooser
argument_list|()
decl_stmt|;
name|fc
operator|.
name|setDialogType
argument_list|(
name|JFileChooser
operator|.
name|SAVE_DIALOG
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setDialogTitle
argument_list|(
literal|"Save SQL Script"
argument_list|)
expr_stmt|;
name|File
name|projectDir
init|=
name|Application
operator|.
name|getProject
argument_list|()
operator|.
name|getProjectDirectory
argument_list|()
decl_stmt|;
if|if
condition|(
name|projectDir
operator|!=
literal|null
condition|)
block|{
name|fc
operator|.
name|setCurrentDirectory
argument_list|(
name|projectDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fc
operator|.
name|showSaveDialog
argument_list|(
name|getView
argument_list|()
argument_list|)
operator|==
name|JFileChooser
operator|.
name|APPROVE_OPTION
condition|)
block|{
name|refreshGeneratorAction
argument_list|()
expr_stmt|;
try|try
block|{
name|File
name|file
init|=
name|fc
operator|.
name|getSelectedFile
argument_list|()
decl_stmt|;
name|FileWriter
name|fw
init|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|fw
argument_list|)
decl_stmt|;
name|pw
operator|.
name|print
argument_list|(
name|textForSQL
argument_list|)
expr_stmt|;
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|reportError
argument_list|(
literal|"Error Saving SQL"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|closeAction
parameter_list|()
block|{
name|view
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

