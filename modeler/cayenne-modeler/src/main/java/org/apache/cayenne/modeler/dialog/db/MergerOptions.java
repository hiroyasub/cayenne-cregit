begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|JdbcAdapter
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
name|merge
operator|.
name|AbstractToDbToken
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
name|merge
operator|.
name|DbMerger
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
name|merge
operator|.
name|EntityMergeSupport
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
name|dbsync
operator|.
name|merge
operator|.
name|MergerContext
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
name|merge
operator|.
name|MergerToken
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
name|merge
operator|.
name|ModelMergeDelegate
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactoryProvider
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
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|db
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
name|dbsync
operator|.
name|reverse
operator|.
name|db
operator|.
name|DbLoaderConfiguration
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
name|db
operator|.
name|LoggingDbLoaderDelegate
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
name|filters
operator|.
name|FiltersConfig
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
name|filters
operator|.
name|PatternFilter
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
name|filters
operator|.
name|TableFilter
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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|DbRelationship
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
name|ObjAttribute
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
name|ObjEntity
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
name|ObjRelationship
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
name|event
operator|.
name|EntityEvent
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
name|event
operator|.
name|MapEvent
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
name|event
operator|.
name|AttributeDisplayEvent
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
name|event
operator|.
name|EntityDisplayEvent
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
name|event
operator|.
name|RelationshipDisplayEvent
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
name|project
operator|.
name|Project
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
name|resource
operator|.
name|Resource
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
name|*
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
name|java
operator|.
name|awt
operator|.
name|*
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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|MergerOptions
extends|extends
name|CayenneController
block|{
specifier|protected
name|MergerOptionsView
name|view
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
name|JdbcAdapter
name|adapter
decl_stmt|;
specifier|protected
name|String
name|textForSQL
decl_stmt|;
specifier|protected
name|MergerTokenSelectorController
name|tokens
decl_stmt|;
specifier|protected
name|String
name|defaultSchema
decl_stmt|;
specifier|private
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
decl_stmt|;
specifier|public
name|MergerOptions
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
parameter_list|,
name|String
name|defaultSchema
parameter_list|,
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|mergerTokenFactoryProvider
operator|=
name|mergerTokenFactoryProvider
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|tokens
operator|=
operator|new
name|MergerTokenSelectorController
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|view
operator|=
operator|new
name|MergerOptionsView
argument_list|(
name|tokens
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
name|defaultSchema
operator|=
name|defaultSchema
expr_stmt|;
comment|/*          * TODO:? this.generatorDefaults = (DBGeneratorDefaults) parent          * .getPreferenceDomainForProject() .getDetail("DbGenerator",          * DBGeneratorDefaults.class, true);          */
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
comment|// tables.updateTables(dataMap);
name|prepareMigrator
argument_list|()
expr_stmt|;
comment|// generatorDefaults.configureGenerator(generator);
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
comment|/*      * public DBGeneratorDefaults getGeneratorDefaults() { return generatorDefaults; }      */
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
literal|1
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
comment|/**      * check database and create the {@link List} of {@link MergerToken}s      */
specifier|protected
name|void
name|prepareMigrator
parameter_list|()
block|{
try|try
block|{
name|adapter
operator|=
operator|(
name|JdbcAdapter
operator|)
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
expr_stmt|;
name|MergerTokenFactory
name|mergerTokenFactory
init|=
name|mergerTokenFactoryProvider
operator|.
name|get
argument_list|(
name|adapter
argument_list|)
decl_stmt|;
name|tokens
operator|.
name|setMergerTokenFactory
argument_list|(
name|mergerTokenFactory
argument_list|)
expr_stmt|;
name|FiltersConfig
name|filters
init|=
name|FiltersConfig
operator|.
name|create
argument_list|(
literal|null
argument_list|,
name|defaultSchema
argument_list|,
name|TableFilter
operator|.
name|everything
argument_list|()
argument_list|,
name|PatternFilter
operator|.
name|INCLUDE_NOTHING
argument_list|)
decl_stmt|;
name|DbMerger
name|merger
init|=
name|DbMerger
operator|.
name|builder
argument_list|(
name|mergerTokenFactory
argument_list|)
operator|.
name|filters
argument_list|(
name|filters
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|DbLoaderConfiguration
name|config
init|=
operator|new
name|DbLoaderConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setFiltersConfig
argument_list|(
name|filters
argument_list|)
expr_stmt|;
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
name|DataMap
name|dbImport
decl_stmt|;
try|try
init|(
name|Connection
name|conn
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
init|;
init|)
block|{
name|dbImport
operator|=
operator|new
name|DbLoader
argument_list|(
name|conn
argument_list|,
name|adapter
argument_list|,
operator|new
name|LoggingDbLoaderDelegate
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbLoader
operator|.
name|class
argument_list|)
argument_list|)
argument_list|,
operator|new
name|EntityMergeSupport
argument_list|(
operator|new
name|DefaultObjectNameGenerator
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|load
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't doLoad dataMap from db."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|tokens
operator|.
name|setTokens
argument_list|(
name|merger
operator|.
name|createMergeTokens
argument_list|(
name|dataMap
argument_list|,
name|dbImport
argument_list|)
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
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|MergerToken
argument_list|>
name|it
init|=
name|tokens
operator|.
name|getSelectedTokens
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|batchTerminator
init|=
name|adapter
operator|.
name|getBatchTerminator
argument_list|()
decl_stmt|;
name|String
name|lineEnd
init|=
name|batchTerminator
operator|!=
literal|null
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
name|MergerToken
name|token
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|instanceof
name|AbstractToDbToken
condition|)
block|{
name|AbstractToDbToken
name|tdb
init|=
operator|(
name|AbstractToDbToken
operator|)
name|token
decl_stmt|;
for|for
control|(
name|String
name|sql
range|:
name|tdb
operator|.
name|createSql
argument_list|(
name|adapter
argument_list|)
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|lineEnd
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/*          * for (int i = 0; i< optionBindings.length; i++) {          * optionBindings[i].updateView(); }          */
name|sqlBinding
operator|.
name|updateView
argument_list|()
expr_stmt|;
block|}
comment|// ===============
comment|// Actions
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
comment|// prepareMigrator();
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
comment|// generatorDefaults.configureGenerator(generator);
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
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokensToMigrate
init|=
name|tokens
operator|.
name|getSelectedTokens
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokensToMigrate
operator|.
name|isEmpty
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
literal|"Nothing to migrate."
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|ProjectController
name|c
init|=
name|getProjectController
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|src
init|=
name|this
decl_stmt|;
specifier|final
name|DataChannelDescriptor
name|domain
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|getProjectController
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
specifier|final
name|DataNodeDescriptor
name|node
init|=
name|getProjectController
argument_list|()
operator|.
name|getCurrentDataNode
argument_list|()
decl_stmt|;
name|ModelMergeDelegate
name|delegate
init|=
operator|new
name|ModelMergeDelegate
argument_list|()
block|{
specifier|public
name|void
name|dbAttributeAdded
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentDbEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dbAttributeModified
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentDbEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dbAttributeRemoved
parameter_list|(
name|DbAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentDbEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireDbAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|c
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|dataMap
argument_list|,
name|node
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|c
operator|.
name|fireDbEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|fireDbEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|dataMap
argument_list|,
name|node
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentDbEntity
argument_list|()
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireDbRelationshipDisplayEvent
argument_list|(
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dbRelationshipRemoved
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentDbEntity
argument_list|()
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireDbRelationshipDisplayEvent
argument_list|(
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objAttributeAdded
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentObjEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objAttributeModified
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentObjEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objAttributeRemoved
parameter_list|(
name|ObjAttribute
name|att
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentObjEntity
argument_list|()
operator|==
name|att
operator|.
name|getEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireObjAttributeDisplayEvent
argument_list|(
operator|new
name|AttributeDisplayEvent
argument_list|(
name|src
argument_list|,
name|att
argument_list|,
name|att
operator|.
name|getEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|c
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|ADD
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|dataMap
argument_list|,
name|node
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|c
operator|.
name|fireObjEntityEvent
argument_list|(
operator|new
name|EntityEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|MapEvent
operator|.
name|REMOVE
argument_list|)
argument_list|)
expr_stmt|;
name|c
operator|.
name|fireObjEntityDisplayEvent
argument_list|(
operator|new
name|EntityDisplayEvent
argument_list|(
name|src
argument_list|,
name|ent
argument_list|,
name|dataMap
argument_list|,
name|node
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objRelationshipAdded
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentObjEntity
argument_list|()
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireObjRelationshipDisplayEvent
argument_list|(
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objRelationshipRemoved
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|getCurrentObjEntity
argument_list|()
operator|==
name|rel
operator|.
name|getSourceEntity
argument_list|()
condition|)
block|{
name|c
operator|.
name|fireObjRelationshipDisplayEvent
argument_list|(
operator|new
name|RelationshipDisplayEvent
argument_list|(
name|src
argument_list|,
name|rel
argument_list|,
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|dataMap
argument_list|,
name|domain
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
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
name|MergerContext
name|mergerContext
init|=
name|MergerContext
operator|.
name|builder
argument_list|(
name|dataMap
argument_list|)
operator|.
name|syntheticDataNode
argument_list|(
name|dataSource
argument_list|,
name|adapter
argument_list|)
operator|.
name|delegate
argument_list|(
name|delegate
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|boolean
name|modelChanged
init|=
literal|false
decl_stmt|;
for|for
control|(
name|MergerToken
name|tok
range|:
name|tokensToMigrate
control|)
block|{
name|int
name|numOfFailuresBefore
init|=
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|tok
operator|.
name|execute
argument_list|(
name|mergerContext
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|modelChanged
operator|&&
name|tok
operator|.
name|getDirection
argument_list|()
operator|.
name|equals
argument_list|(
name|MergeDirection
operator|.
name|TO_MODEL
argument_list|)
condition|)
block|{
name|modelChanged
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|numOfFailuresBefore
operator|==
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// looks like the token executed without failures
name|tokens
operator|.
name|removeToken
argument_list|(
name|tok
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|modelChanged
condition|)
block|{
comment|// mark the model as unsaved
name|Project
name|project
init|=
name|getApplication
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|project
operator|.
name|setModified
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ProjectController
name|projectController
init|=
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
decl_stmt|;
name|projectController
operator|.
name|setDirty
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|ValidationResult
name|failures
init|=
name|mergerContext
operator|.
name|getValidationResult
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
literal|"Migration Complete."
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
literal|"Migration Complete"
argument_list|,
literal|"Migration finished. The following problem(s) were ignored."
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
literal|"Migration Error"
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
name|Resource
name|projectDir
init|=
name|getApplication
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getConfigurationResource
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
operator|new
name|File
argument_list|(
name|projectDir
operator|.
name|getURL
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
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
specifier|private
name|ProjectController
name|getProjectController
parameter_list|()
block|{
return|return
name|getApplication
argument_list|()
operator|.
name|getFrameController
argument_list|()
operator|.
name|getProjectController
argument_list|()
return|;
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

