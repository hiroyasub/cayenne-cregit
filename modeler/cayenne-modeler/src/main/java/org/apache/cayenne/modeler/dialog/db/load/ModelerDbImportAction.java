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
operator|.
name|load
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
name|DataChannelDescriptorLoader
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
name|DataMapLoader
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
name|server
operator|.
name|DataSourceFactory
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
name|server
operator|.
name|DbAdapterFactory
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
name|xml
operator|.
name|DataChannelMetaData
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
name|merge
operator|.
name|token
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
name|di
operator|.
name|Inject
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
name|project
operator|.
name|ProjectSaver
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
name|DbImportConfiguration
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
name|DefaultDbImportAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
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
name|JOptionPane
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
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|ModelerDbImportAction
extends|extends
name|DefaultDbImportAction
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DIALOG_TITLE
init|=
literal|"Reverse Engineering Result"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataMap
name|targetMap
decl_stmt|;
specifier|private
name|DbLoadResultDialog
name|resultDialog
decl_stmt|;
specifier|private
name|boolean
name|isNothingChanged
decl_stmt|;
specifier|public
name|ModelerDbImportAction
parameter_list|(
annotation|@
name|Inject
name|Logger
name|logger
parameter_list|,
annotation|@
name|Inject
name|ProjectSaver
name|projectSaver
parameter_list|,
annotation|@
name|Inject
name|DataSourceFactory
name|dataSourceFactory
parameter_list|,
annotation|@
name|Inject
name|DbAdapterFactory
name|adapterFactory
parameter_list|,
annotation|@
name|Inject
name|DataMapLoader
name|mapLoader
parameter_list|,
annotation|@
name|Inject
name|MergerTokenFactoryProvider
name|mergerTokenFactoryProvider
parameter_list|,
annotation|@
name|Inject
name|DataChannelMetaData
name|metaData
parameter_list|,
annotation|@
name|Inject
name|DataChannelDescriptorLoader
name|dataChannelDescriptorLoader
parameter_list|)
block|{
name|super
argument_list|(
name|logger
argument_list|,
name|projectSaver
argument_list|,
name|dataSourceFactory
argument_list|,
name|adapterFactory
argument_list|,
name|mapLoader
argument_list|,
name|mergerTokenFactoryProvider
argument_list|,
name|dataChannelDescriptorLoader
argument_list|,
name|metaData
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|MergerToken
argument_list|>
name|log
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
parameter_list|)
block|{
name|resultDialog
operator|=
operator|new
name|DbLoadResultDialog
argument_list|(
name|DIALOG_TITLE
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: No changes to import."
argument_list|)
expr_stmt|;
name|isNothingChanged
operator|=
literal|true
expr_stmt|;
return|return
name|tokens
return|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"Detected changes: "
argument_list|)
expr_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
name|String
name|logString
init|=
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s %s"
argument_list|,
name|token
operator|.
name|getTokenName
argument_list|()
argument_list|,
name|token
operator|.
name|getTokenValue
argument_list|()
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|logString
argument_list|)
expr_stmt|;
name|resultDialog
operator|.
name|addRowToOutput
argument_list|(
name|logString
argument_list|)
expr_stmt|;
name|isNothingChanged
operator|=
literal|false
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|resultDialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|tokens
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|addMessageToLogs
parameter_list|(
name|String
name|message
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|messages
parameter_list|)
block|{
name|String
name|formattedMessage
init|=
name|String
operator|.
name|format
argument_list|(
literal|"    %-20s"
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|messages
operator|.
name|add
argument_list|(
name|formattedMessage
argument_list|)
expr_stmt|;
name|resultDialog
operator|.
name|addRowToOutput
argument_list|(
name|formattedMessage
argument_list|)
expr_stmt|;
name|isNothingChanged
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|logMessages
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|messages
parameter_list|)
block|{
name|super
operator|.
name|logMessages
argument_list|(
name|messages
argument_list|)
expr_stmt|;
if|if
condition|(
name|isNothingChanged
condition|)
block|{
name|JOptionPane
name|optionPane
init|=
operator|new
name|JOptionPane
argument_list|(
literal|"Detected changes: No changes to import."
argument_list|,
name|JOptionPane
operator|.
name|PLAIN_MESSAGE
argument_list|)
decl_stmt|;
name|JDialog
name|dialog
init|=
name|optionPane
operator|.
name|createDialog
argument_list|(
name|DIALOG_TITLE
argument_list|)
decl_stmt|;
name|dialog
operator|.
name|setModal
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|setAlwaysOnTop
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|resultDialog
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|resultDialog
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|DataMap
name|existingTargetMap
parameter_list|(
name|DbImportConfiguration
name|configuration
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|targetMap
return|;
block|}
block|}
end_class

end_unit

